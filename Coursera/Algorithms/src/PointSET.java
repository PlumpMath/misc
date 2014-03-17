import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PointSET {
    private SET<Point2D> points;

    public PointSET() {
        points = new SET<Point2D>();
    }

    public boolean isEmpty() {
        return points.isEmpty();
    }

    public int size() {
        return points.size();
    }

    public void insert(Point2D p) {
        points.add(p);
    }

    public boolean contains(Point2D p) {
        return points.contains(p);
    }

    public void draw() {
        Iterator<Point2D> iterator = points.iterator();
        while (iterator.hasNext())
            iterator.next().draw();
    }

    public Iterable<Point2D> range(RectHV rect) {
        List<Point2D> list = new ArrayList<Point2D>(points.size());
        Iterator<Point2D> iterator = points.iterator();
        while (iterator.hasNext()) {
            Point2D p = iterator.next();
            if (rect.contains(p))
                list.add(p);
        }

        return list;
    }

    public Point2D nearest(Point2D p) {
        if (isEmpty())
            return null;

        Iterator<Point2D> iterator = points.iterator();
        Point2D ret = iterator.next();
        double distance = p.distanceSquaredTo(ret);
        while (iterator.hasNext()) {
            Point2D temp = iterator.next();
            double d = p.distanceSquaredTo(temp);
            if (d < distance) {
                distance = d;
                ret = temp;
            }
        }

        return ret;
    }
}