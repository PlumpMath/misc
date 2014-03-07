public class Brute {
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

        Point[] temp = new Point[4];

        for (int i = 0; i < count; ++i)
            for (int j = i + 1; j < count; ++j)
                for (int k = j + 1; k < count; ++k)
                    for (int l = k + 1; l < count; ++l) {
                        Point p1 = points[i];
                        Point p2 = points[j];
                        Point p3 = points[k];
                        Point p4 = points[l];

                        if (p1.compareTo(p2) == 0 || p1.compareTo(p3) == 0 || p1.compareTo(p4) == 0
                                || p2.compareTo(p3) == 0 || p2.compareTo(p4) == 0
                                || p3.compareTo(p4) == 0)
                            continue;

                        Double s12 = p1.slopeTo(p2);
                        Double s13 = p1.slopeTo(p3);
                        Double s14 = p1.slopeTo(p4);

                        if (s12.equals(s13) && s12.equals(s14)) {
                            temp[0] = p1;
                            temp[1] = p2;
                            temp[2] = p3;
                            temp[3] = p4;
                            Insertion.sort(temp);
                            StdOut.println(temp[0] + " -> " + temp[1] + " -> " + temp[2] + " -> " + temp[3]);
                            temp[0].drawTo(temp[3]);
                        }
                    }
    }
}
