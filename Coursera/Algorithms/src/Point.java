/*************************************************************************
 * Name:
 * Email:
 *
 * Compilation:  javac Point.java
 * Execution:
 * Dependencies: StdDraw.java
 *
 * Description: An immutable data type for points in the plane.
 *
 *************************************************************************/

import java.util.Comparator;

public class Point implements Comparable<Point> {

    public final Comparator<Point> SLOPE_ORDER = new BySlope();

    private final int x;                              // x coordinate
    private final int y;                              // y coordinate

    private class BySlope implements Comparator<Point> {
        public int compare(Point p1, Point p2) {
            Double s1 = p1.slopeTo(Point.this);
            Double s2 = p2.slopeTo(Point.this);

            return s1.compareTo(s2);
        }
    }

    // create the point (x, y)
    public Point(int x, int y) {
        /* DO NOT MODIFY */
        this.x = x;
        this.y = y;
    }

    // plot this point to standard drawing
    public void draw() {
        /* DO NOT MODIFY */
        StdDraw.point(x, y);
    }

    // draw line between this point and that point to standard drawing
    public void drawTo(Point that) {
        /* DO NOT MODIFY */
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    // slope between this point and that point
    public double slopeTo(Point that) {
        if (this.y == that.y) {
            if (this.x == that.x)
                return Double.NEGATIVE_INFINITY;
            else
                return 0;
        } else if (this.x == that.x) {
            return Double.POSITIVE_INFINITY;
        }

        return 1.0 * (that.y - this.y) / (that.x - this.x);
    }

    // is this point lexicographically smaller than that one?
    // comparing y-coordinates and breaking ties by x-coordinates
    public int compareTo(Point that) {
        if (this.y < that.y)
            return -1;
        if (this.y > that.y)
            return +1;
        if (this.x < that.x)
            return -1;
        if (this.x > that.x)
            return +1;
        return 0;
    }

    // return string representation of this point
    public String toString() {
        /* DO NOT MODIFY */
        return "(" + x + ", " + y + ")";
    }

    // unit test
    public static void main(String[] args) {
        Point p1 = new Point(1, 1);
        Point p2 = new Point(1, 1);
        Point p3 = new Point(1, 2);
        Point p4 = new Point(2, 1);

        StdOut.println(p1.slopeTo(p2));
        StdOut.println(p1.slopeTo(p3));
        StdOut.println(p1.slopeTo(p4));
        StdOut.println(p3.slopeTo(p4));
        StdOut.println(p4.slopeTo(p3));
        StdOut.println("---------");
        p1.draw();
        p1.drawTo(p2);
        p1.drawTo(p4);
        StdOut.println("---------");
        StdOut.println(p1.compareTo(p2));
        StdOut.println(p1.compareTo(p3));
        StdOut.println(p1.compareTo(p4));
    }
}