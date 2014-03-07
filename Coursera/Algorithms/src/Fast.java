import java.util.Arrays;

public class Fast {
    public static void main(String[] args) {
        In in = new In(args[0]);
        int count = in.readInt();

        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);

        Point[] points = new Point[count];
        for (int i = 0; i < count; ++i) {
            points[i] = new Point(in.readInt(), in.readInt());
            points[i].draw();
        }

        Point[] aux2 = new Point[count];

        for (int i = 0; i < count - 3; ++i) {
            Point p = points[i];
            Arrays.sort(points, i + 1, count, p.SLOPE_ORDER);

            boolean exist = false;
            int j = i + 1;
            while (j < count - 1) {
                Double s = points[j].slopeTo(p);
                int k = j + 1;
                while (s.compareTo(points[k].slopeTo(p)) == 0) {
                    ++k;
                    if (k >= count)
                        break;
                }
                if (k - j < 3) {
                    j = k;
                    continue;
                }

                for (int t = 0; t < i; ++t) {
                    if (s.compareTo(p.slopeTo(points[t])) == 0) {
                        exist = true;
                        break;
                    }
                }
                if (exist) {
                    j = k;
                    continue;
                }

                aux2[0] = p;
                System.arraycopy(points, j, aux2, 1, k - j);

                Arrays.sort(aux2, 0, k - j + 1);
                aux2[0].drawTo(aux2[k - j]);
                for (int l = 0; l < k - j + 1 - 1; ++l) {
                    StdOut.print(aux2[l] + " -> ");
                }
                StdOut.println(aux2[k - j]);

                j = k;
            }
        }
    }
}
