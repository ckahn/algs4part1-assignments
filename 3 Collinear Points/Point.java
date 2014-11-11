/*************************************************************************
 * Dependencies: StdDraw.java
 *
 * Description: An immutable data type for points in the plane.
 *
 *************************************************************************/

import java.util.Comparator;

public class Point implements Comparable<Point> {

    // compare points by slope
    public final Comparator<Point> SLOPE_ORDER = new BySlope();

    private final int x;    // x coordinate
    private final int y;    // y coordinate

    // create the point (x, y)
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // plot this point to standard drawing
    public void draw() {
        StdDraw.point(x, y);
    }

    // draw line between invoking point and end point to standard drawing
    public void drawTo(Point end) {
        StdDraw.line(this.x, this.y, end.x, end.y);
    }

    // slope between invoking point and end point
    public double slopeTo(Point end) {
        if (end == null)
            throw new java.lang.NullPointerException("Point object is null");
        if (this.equals(end)) 
            return Double.NEGATIVE_INFINITY;
        if (end.x - this.x == 0) 
            return Double.POSITIVE_INFINITY;
        return (double) (end.y - this.y) / (double) (end.x - this.x);
    }

    // is invoking point lexicographically smaller than second one?
    // comparing y-coordinates and breaking ties by x-coordinates
    public int compareTo(Point second) {
        if (this.y < second.y)
            return -1;
        if (this.y == second.y && this.x < second.x)
            return -1;
        if (this.y > second.y)
            return 1;
        if (this.y == second.y && this.x > second.x)
            return 1;
        return 0;
    }

    // return string representation of this point
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    // unit test
    public static void main(String[] args) {
        Point orig = new Point(0, 0);
        
        for (int x = -3; x < 2; x++) {
            for (int y = -1; y < 2; y++) {
                Point p = new Point(x, y);
                System.out.println("The slope from " + orig.toString() 
                        + " to " + p.toString() + " is " + orig.slopeTo(p));
                
                String compared = "";
                if (orig.compareTo(p) == -1)
                    compared = "less than ";
                else if (orig.compareTo(p) == 1)
                    compared = "greater than ";
                else
                    compared = "equal to ";
                System.out.println(orig.toString() + " is " + compared 
                        + p.toString());
                
                System.out.println("-----");
            }
        }
    }
    
    private class BySlope implements Comparator<Point> {
        
        @Override
        public int compare(Point q1, Point q2) {
            if (q1 == null || q2 ==  null) 
                throw new java.lang.NullPointerException("Point object"
                        + "is null");
            if (Point.this.slopeTo(q1) < Point.this.slopeTo(q2)) 
                return -1;
            if (Point.this.slopeTo(q1) > Point.this.slopeTo(q2))
                return 1;
            return 0;
        }
        
    }
}
