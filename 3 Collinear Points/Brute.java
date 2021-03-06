/* 
 * Draws every (maximal) line segment that connects a subset of 4 or more,
 * points, given a set of N distinct points in the plane. Uses brute force.
 */
import java.util.Arrays;

public class Brute {
    
    public static void main(String[] args) {
        
        Point[] pts;

        // rescale coordinates and turn on animation mode
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        StdDraw.setPenRadius(0.01);

        // read in the input
        String filename = args[0];
        In in = new In(filename);
        pts = new Point[in.readInt()];
        for (int i = 0; i < pts.length; i++) {
            int x = in.readInt();
            int y = in.readInt();
            pts[i] = new Point(x, y);
            pts[i].draw();
        }
        
        // draw line through any 4 collinear points
        Arrays.sort(pts);
        StdDraw.setPenRadius(0.001);
        for (int i = 0; i < pts.length; i++)
            for (int j = i+1; j < pts.length; j++)
                for (int k = j+1; k < pts.length; k++)
                    for (int l = k+1; l < pts.length; l++)
                        if (areInLine(pts[i], pts[j], pts[k], pts[l])) {
                                pts[i].drawTo(pts[l]);
                                System.out.print(pts[i].toString() + " -> ");
                                System.out.print(pts[j].toString() + " -> ");
                                System.out.print(pts[k].toString() + " -> ");
                                System.out.println(pts[l].toString());                        }

        // display to screen all at once
        StdDraw.show(0);

        // reset the pen radius
        StdDraw.setPenRadius();
    }

    private static boolean areInLine(Point p, Point q, Point r, Point s) {
        if (p.slopeTo(q) == q.slopeTo(r) && p.slopeTo(q) == r.slopeTo(s))
            return true;
        return false;
    }
}
