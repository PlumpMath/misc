import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class KdTree {
    private Node root;
    private int size;

    private static class Node implements Comparable<Node> {
        private Point2D point;
        private int height;
        private Node left;
        private Node right;

        private Node(Point2D point) {
            this.point = point;
            height = 1;
            left = null;
            right = null;
        }

        public boolean isVertical() {
            return height % 2 != 0;
        }

        public int compareTo(Node that) {
            if (point.x() == that.point.x() && point.y() == that.point.y())
                return 0;

            if (that.isVertical()) {
                if (point.x() < that.point.x())
                    return -1;
                else
                    return +1;
            } else {
                if (point.y() < that.point.y())
                    return -1;
                else
                    return +1;
            }
        }
    }

    public KdTree() {
        root = null;
        size = 0;
    }

    public boolean isEmpty() {
        return root == null;
    }

    public int size() {
        return size;
    }

    public void insert(Point2D p) {
        if (root == null) {
            root = new Node(p);
            size++;
            return;
        }

        Node parent = root;
        Node child;
        Node n = new Node(p);
        boolean onLeft;
        for (;;) {
            if (n.compareTo(parent) < 0) {
                child = parent.left;
                onLeft = true;
            } else if (n.compareTo(parent) > 0) {
                child = parent.right;
                onLeft = false;
            } else {
                return;
            }

            if (child == null)
                break;

            parent = child;
        }

        size++;
        n.height = parent.height + 1;
        if (onLeft) {
            parent.left = n;
        } else {
            parent.right = n;
        }
    }

    public boolean contains(Point2D p) {
        if (isEmpty())
            return false;

        Node parent = root;
        Node child;
        Node n = new Node(p);
        for (;;) {
            if (n.compareTo(parent) < 0) {
                child = parent.left;
            } else if (n.compareTo(parent) > 0) {
                child = parent.right;
            } else {
                return true;
            }

            if (child == null)
                return false;

            parent = child;
        }
    }

    public void draw() {
        RectHV rect = new RectHV(0, 0, 1, 1);
        draw(root, rect);
    }

    private void draw(Node n, RectHV rect) {
        if (n == null)
            return;

        StdDraw.setPenColor(Color.black);
        n.point.draw();
        RectHV left, right;
        if (n.isVertical()) {
            StdDraw.setPenColor(Color.red);
            StdDraw.line(n.point.x(), rect.ymin(), n.point.x(), rect.ymax());

            left = new RectHV(rect.xmin(), rect.ymin(), n.point.x(), rect.ymax());
            right = new RectHV(n.point.x(), rect.ymin(), rect.xmax(), rect.ymax());
        } else {
            StdDraw.setPenColor(Color.blue);
            StdDraw.line(rect.xmin(), n.point.y(), rect.xmax(), n.point.y());

            left = new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), n.point.y());
            right = new RectHV(rect.xmin(), n.point.y(), rect.xmax(), rect.ymax());
        }

        if (n.left != null)
            draw(n.left, left);
        if (n.right != null)
            draw(n.right, right);
    }

    public Iterable<Point2D> range(RectHV rect) {
        List<Point2D> list = new ArrayList<Point2D>(size());
        range(root, rect, list, new RectHV(0, 0, 1, 1));
        return list;
    }

    private void range(Node n, RectHV rect, List<Point2D> list, RectHV srcRect) {
        if (n == null)
            return;

        if (rect.contains(n.point))
            list.add(n.point);

        RectHV left, right;
        if (n.isVertical()) {
            left = new RectHV(srcRect.xmin(), srcRect.ymin(), n.point.x(), srcRect.ymax());
            right = new RectHV(n.point.x(), srcRect.ymin(), srcRect.xmax(), srcRect.ymax());
            if (rect.intersects(left))
                range(n.left, rect, list, left);
            if (rect.intersects(right))
                range(n.right, rect, list, right);
        } else {
            left = new RectHV(srcRect.xmin(), srcRect.ymin(), srcRect.xmax(), n.point.y());
            right = new RectHV(srcRect.xmin(), n.point.y(), srcRect.xmax(), srcRect.ymax());
            if (rect.intersects(left))
                range(n.left, rect, list, left);
            if (rect.intersects(right))
                range(n.right, rect, list, right);
        }
    }

    public Point2D nearest(Point2D p) {
        if (isEmpty())
            return null;

        return nearest(root, p, new RectHV(0, 0, 1, 1), root.point);
    }

    private Point2D nearest(Node n, Point2D p, RectHV rect, Point2D current) {
        if (n == null)
            return current;

        Point2D nearest = n.point;
        double nearestSquareDistance = p.distanceSquaredTo(nearest);
        Point2D left, right;
        RectHV leftRect, rightRect;
        if (n.isVertical()) {
            leftRect = new RectHV(rect.xmin(), rect.ymin(), n.point.x(), rect.ymax());
            rightRect = new RectHV(n.point.x(), rect.ymin(), rect.xmax(), rect.ymax());
        } else {
            leftRect = new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), n.point.y());
            rightRect = new RectHV(rect.xmin(), n.point.y(), rect.xmax(), rect.ymax());
        }

        if (leftRect.contains(p)) {
            left = nearest(n.left, p, leftRect, nearest);
            if (!left.equals(nearest) && p.distanceSquaredTo(left) < nearestSquareDistance) {
                nearest = left;
                nearestSquareDistance = p.distanceSquaredTo(nearest);
            }

            if (rightRect.distanceSquaredTo(p) < nearestSquareDistance) {
                right = nearest(n.right, p, rightRect, nearest);
                if (!right.equals(nearest) && p.distanceSquaredTo(right) < nearestSquareDistance)
                    nearest = right;
            }
        } else {
            right = nearest(n.right, p, rightRect, nearest);
            if (!right.equals(nearest) && p.distanceSquaredTo(right) < nearestSquareDistance) {
                nearest = right;
                nearestSquareDistance = p.distanceSquaredTo(nearest);
            }

            if (leftRect.distanceSquaredTo(p) < nearestSquareDistance) {
                left = nearest(n.left, p, leftRect, nearest);
                if (!left.equals(nearest) && p.distanceSquaredTo(left) < nearestSquareDistance)
                    nearest = left;
            }
        }

        return nearest;
    }

    public static void main(String[] args) {
        String filename = args[0];
        In in = new In(filename);

        KdTree kdtree = new KdTree();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kdtree.insert(p);

        }

        StdOut.println(kdtree.size());
        StdOut.println(kdtree.isEmpty());
        StdOut.println(kdtree.contains(new Point2D(0.81, 0.3)));
    }
}
