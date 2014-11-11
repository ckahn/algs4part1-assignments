/* 
 * Draws every (maximal) line segment that connects a subset of 4 or more,
 * points, given a set of N distinct points in the plane. Uses a faster,
 * sorting-based solution.
 */

import java.util.Arrays;

public class Fast {
    
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
        
        // sort points by natural order and create duplicate array
        Arrays.sort(pts);
        Point[] ptsBySlope = Arrays.copyOf(pts, pts.length);
        
        // draw line through any 4 collinear points
        StdDraw.setPenRadius(0.001);
        for (int i = 0; i < pts.length-2; i++) {
            
            Arrays.sort(ptsBySlope, pts[i].SLOPE_ORDER);
            
            for (int j = i+1; j < pts.length; j++) {
                
                // get slope from i to j, and sort accordingly
                double slope = pts[i].slopeTo(pts[j]);
                
                // find a point with i->j slope
                int first = Arrays.binarySearch(ptsBySlope, pts[j], 
                        pts[i].SLOPE_ORDER);
                                
                // find first point with slope
                while (first > 0 
                        && pts[i].slopeTo(ptsBySlope[first-1]) == slope)
                    first--;
                
                // find last point with slope
                int last = first;
                while (last < ptsBySlope.length-1 
                        && pts[i].slopeTo(ptsBySlope[last+1]) == slope)
                    last++;
                                
                // sort subsection of array with same-slope points
                Arrays.sort(ptsBySlope, first, last+1);
                                
                // the line has not already been drawn, draw it
                if (last-first > 1 && ptsBySlope[first] == pts[j]) {
                    System.out.print(pts[i].toString() + " -> ");
                    while (first < last) {
                        System.out.print(ptsBySlope[first++].toString() + " -> ");
                    }
                    System.out.println(ptsBySlope[last].toString());
                    pts[i].drawTo(ptsBySlope[last]);
                } 
            }
        }
        
        // display to screen all at once
        StdDraw.show(0);

        // reset the pen radius
        StdDraw.setPenRadius();
    }
}
