import java.util.TreeSet;

/*************************************************************************
 * Represents a set of points in the unit square (all points have x- and 
 * y-coordinates between 0 and 1) using a tree set for brute-force 
 * range search (finds all of the points contained in a query rectangle) 
 * and nearest neighbor search (finds a closest point to a query point). 
 * 
 ************************************************************************/

public class PointSET {
    
    private final TreeSet<Point2D> ts;
    private int size;
    
    // construct an empty set of points 
    public PointSET() {
        ts = new TreeSet<Point2D>();
        size = 0;
    }
    
    // is the set empty?     
    public boolean isEmpty() {
        return size == 0;
    }
    
    // number of points in the set     
    public int size() {
        return size;
    }
    
    // add the point to the set (if it is not already in the set)    
    public void insert(Point2D p) {
        if (!this.contains(p)) {
            ts.add(p);
            size++;
        }
    }
    
    // does the set contain point p?
    public boolean contains(Point2D p) {
        return ts.contains(p);
    }
    
    // draw all points to standard draw    
    public void draw() {
        for (Point2D p : ts)
            StdDraw.point(p.x(), p.y());
    }
    
    // all points that are inside the rectangle
    public Iterable<Point2D> range(RectHV rect) {
        TreeSet<Point2D> contained = new TreeSet<Point2D>(); 
        for (Point2D p : ts)
            if (rect.contains(p))
                contained.add(p);
        return contained;
    }
    
    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        Point2D nearest = null;
        double min = Double.MAX_VALUE;
        for (Point2D q : ts) {
            double distance = p.distanceTo(q);
            if (distance < min) {
                nearest = q;
                min = distance;
            }
        }
        return nearest;
    }
 }
