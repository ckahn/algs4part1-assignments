/*************************************************************************
 * Represents a set of points in the unit square (all points have x- and 
 * y-coordinates between 0 and 1) using a 2d-tree to support efficient 
 * range search (finds all of the points contained in a query rectangle) 
 * and nearest neighbor search (finds a closest point to a query point). 
 *
 ************************************************************************/

public class KdTree {
    
    private Node root; 
    private RectHV qRect;

    private static class Node {
        
        private Point2D p;
        private Node lb, rt;
        private RectHV rect;
        private boolean compareByX;
        private int N;

        public Node(Point2D p, boolean compareByX, RectHV rect, int N) {
            this.p = p;
            this.compareByX = compareByX;
            this.rect = rect;
            this.N = N;        
        }
    }

    // construct an empty set of points 
    public KdTree() {
        root = null;
    }

    // is the set empty?     
    public boolean isEmpty() {
        return size() == 0;
    }

    // number of points in the set     
    public int size() {
        return size(root);
    }

    // return number of points in kd-tree rooted at x
    private int size(Node x) {
        if (x == null) return 0;
        else           return x.N;
    }

    // add the point to the set (if it is not already in the set)    
    public void insert(Point2D p) {
        root = insert(root, true, null, -2, p); // -2 because no parent
    }

    private Node insert(Node r, boolean compareByX, Node parent, 
                        int cmp, Point2D p) {
        if (r == null) {
            RectHV rect;
            if (parent == null)
                rect = new RectHV(0, 0, 1, 1);
            else if (parent.compareByX) {
                if (cmp < 0)
                    rect = new RectHV(parent.rect.xmin(), parent.rect.ymin(), 
                                      parent.p.x(), parent.rect.ymax());
                else
                    rect = new RectHV(parent.p.x(), parent.rect.ymin(), 
                                      parent.rect.xmax(), parent.rect.ymax());
            } else {
                if (cmp < 0)
                    rect = new RectHV(parent.rect.xmin(), parent.rect.ymin(), 
                                      parent.rect.xmax(), parent.p.y());
                else
                    rect = new RectHV(parent.rect.xmin(), parent.p.y(), 
                                      parent.rect.xmax(), parent.rect.ymax());
            }
            return new Node(p, compareByX, rect, 1);
        }
        if (r.p.equals(p))
            return r;
        if (compareByX) {
            int cmpX = Point2D.X_ORDER.compare(p, r.p);
            if (cmpX < 0)
                r.lb = insert(r.lb, false, r, cmpX, p);
            else
                r.rt = insert(r.rt, false, r, cmpX, p);
        } else {
            int cmpY = Point2D.Y_ORDER.compare(p, r.p);
            if (cmpY < 0)
                r.lb = insert(r.lb, true, r, cmpY, p);
            else
                r.rt = insert(r.rt, true, r, cmpY, p);
        }
        r.N = size(r.lb) + size(r.rt) + 1;
        return r;
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        Node r = root;
        while (r != null) {
            if (r.p.equals(p))
                return true;
            if (r.compareByX) {
                int cmpX = Point2D.X_ORDER.compare(p, r.p);
                if (cmpX < 0) r = r.lb;
                else          r = r.rt;
            } else {
                int cmpY = Point2D.Y_ORDER.compare(p, r.p);
                if (cmpY < 0) r = r.lb;
                else          r = r.rt;
            }
        }
        return false;
    }

    // draw all points to standard draw    
    public void draw() {
        Queue<Node> nodes = new Queue<Node>();
        nodes.enqueue(root);
        
        while (!nodes.isEmpty()) {
            Node n = nodes.dequeue();
            if (n == null) {
                continue;
            }
            StdDraw.setPenRadius();
            if (n.compareByX) {
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.line(n.p.x(), n.rect.ymin(), n.p.x(), n.rect.ymax());
            } else {
                StdDraw.setPenColor(StdDraw.BLUE);
                StdDraw.line(n.rect.xmin(), n.p.y(), n.rect.xmax(), n.p.y());
            }
            StdDraw.setPenRadius(0.01);
            StdDraw.setPenColor();
            StdDraw.point(n.p.x(), n.p.y());
            nodes.enqueue(n.lb);
            nodes.enqueue(n.rt);
        }
        
        StdDraw.setPenRadius();
        StdDraw.setPenColor();
        StdDraw.line(0,  0,  0,  1);
        StdDraw.line(0,  1,  1,  1);
        StdDraw.line(1,  1,  1,  0);
        StdDraw.line(1,  0,  0,  0);
    }

    // all points that are inside the rectangle
    public Iterable<Point2D> range(RectHV rect) {
        qRect = rect;
        Queue<Point2D> pointsInRect = new Queue<Point2D>();
        range(root, true, pointsInRect);
        return pointsInRect;
    }
    
    private void range(Node n, boolean compareByX, 
                       Queue<Point2D> pointsInRect) {
        if (n == null) 
            return;
        if (compareByX) {
            double xLine = n.p.x();
            if (qRect.xmax() < xLine)
                range(n.lb, false, pointsInRect);
            else if (qRect.xmin() > xLine)
                range(n.rt, false, pointsInRect); 
            else {
                if (qRect.contains(n.p))
                    pointsInRect.enqueue(n.p);
                range(n.lb, false, pointsInRect);
                range(n.rt, false, pointsInRect);
            }
        } else {
            double yLine = n.p.y();
            if (qRect.ymax() < yLine)
                range(n.lb, true, pointsInRect);
            else if (qRect.ymin() > yLine)
                range(n.rt, true, pointsInRect);
            else {
                if (qRect.contains(n.p))
                    pointsInRect.enqueue(n.p);
                range(n.lb, true, pointsInRect);
                range(n.rt, true, pointsInRect);
            }
        }
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (this.size() == 0) return null;
        return nearest(root, p, root.p);
    }
    
    private Point2D nearest(Node r, Point2D p, Point2D champion) {
        Point2D newChamp = champion;
        if (r == null)
            return newChamp;
        if (r.p.distanceSquaredTo(p) < newChamp.distanceSquaredTo(p))
            newChamp = r.p;
        if (r.compareByX) {
            if (p.x() < r.p.x()) {
                newChamp  = nearest(r.lb, p, newChamp);
                if (newChamp.distanceTo(p) > r.p.x()-p.x())
                    newChamp = nearest(r.rt, p, newChamp);
            }
            else {
                newChamp  = nearest(r.rt, p, newChamp);
                if (newChamp.distanceTo(p) > p.x()-r.p.x())
                    newChamp = nearest(r.lb, p, newChamp);
            }
        }
        else {
            if (p.y() < r.p.y()) {
                newChamp  = nearest(r.lb, p, newChamp);
                if (newChamp.distanceTo(p) > r.p.y()-p.y())
                    newChamp = nearest(r.rt, p, newChamp);
            } else {
                newChamp  = nearest(r.rt, p, newChamp);
                if (newChamp.distanceTo(p) > p.y()-r.p.y())
                    newChamp = nearest(r.lb, p, newChamp);
            }
        }
        return newChamp;
    }
    
    public static void main(String[] args) {
        KdTree kdt = new KdTree();
        kdt.insert(new Point2D(0.5, 0.5));
        kdt.insert(new Point2D(0.2, 0.2));
        kdt.draw();
    }
}
