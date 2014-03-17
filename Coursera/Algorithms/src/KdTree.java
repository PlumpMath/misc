import java.util.HashSet;
import java.util.Set;

public class KdTree {
    private static final RectHV BOARD = new RectHV(0, 0, 1, 1);

    private Node root = null;
    private int size = 0;

    private enum SplitType {
        VERTICAL,
        HORIZONTAL
    }

    private static class Node {
        private Point2D point;
        private RectHV rect;
        private SplitType type;

        private Node left = null;
        private Node right = null;

        private Node(Point2D point, RectHV rect, SplitType type) {
            this.point = point;
            this.rect = rect;
            this.type = type;
        }
    }

    private static class NearestNodeHolder {
        private Node nearest;

        private NearestNodeHolder(Node nearest) {
            this.nearest = nearest;
        }
    }

    public boolean isEmpty() {
        return root == null;
    }

    public int size() {
        return size;
    }

    public void insert(Point2D p) {
        if (root == null) {
            root = new Node(p, BOARD, SplitType.VERTICAL);
            size++;
            return;
        }

        Node parent = root;
        Node child;
        boolean onLeft;
        while (true) {
            if (p.equals(parent.point)) {
                parent.point = p;
                return;
            }

            if (parent.type == SplitType.HORIZONTAL) {
                if (p.y() < parent.point.y()) {
                    child = parent.left;
                    onLeft = true;
                } else {
                    child = parent.right;
                    onLeft = false;
                }
            } else {
                if (p.x() < parent.point.x()) {
                    child = parent.left;
                    onLeft = true;
                } else {
                    child = parent.right;
                    onLeft = false;
                }
            }

            if (child == null)
                break;

            parent = child;
        }

        size++;

        RectHV rect;
        SplitType type;
        if (parent.type == SplitType.HORIZONTAL) {
            if (onLeft)
                rect = new RectHV(parent.rect.xmin(), parent.rect.ymin(), parent.rect.xmax(), parent.point.y());
            else
                rect = new RectHV(parent.rect.xmin(), parent.point.y(), parent.rect.xmax(), parent.rect.ymax());

            type = SplitType.VERTICAL;
        } else {
            if (onLeft)
                rect = new RectHV(parent.rect.xmin(), parent.rect.ymin(), parent.point.x(), parent.rect.ymax());
            else
                rect = new RectHV(parent.point.x(), parent.rect.ymin(), parent.rect.xmax(), parent.rect.ymax());

            type = SplitType.HORIZONTAL;
        }

        if (onLeft)
            parent.left = new Node(p, rect, type);
        else
            parent.right = new Node(p, rect, type);
    }

    public boolean contains(Point2D p) {
        if (isEmpty())
            return false;

        Node parent = root;
        Node child;
        while (true) {
            if (p.equals(parent.point))
                return true;

            if (parent.type == SplitType.HORIZONTAL) {
                if (p.y() < parent.point.y())
                    child = parent.left;
                else
                    child = parent.right;
            } else {
                if (p.x() < parent.point.x())
                    child = parent.left;
                else
                    child = parent.right;
            }

            if (child == null)
                return false;

            parent = child;
        }
    }

    public void draw() {
        draw(root);
    }

    private void draw(Node n) {
        if (n == null)
            return;

        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        n.point.draw();

        StdDraw.setPenRadius();
        if (n.type == SplitType.HORIZONTAL) {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(n.rect.xmin(), n.point.y(), n.rect.xmax(), n.point.y());
        } else {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(n.point.x(), n.rect.ymin(), n.point.x(), n.rect.ymax());
        }

        if (n.left != null)
            draw(n.left);
        if (n.right != null)
            draw(n.right);
    }

    public Iterable<Point2D> range(RectHV rect) {
        Set<Point2D> set = new HashSet<Point2D>();
        range(root, rect, set);
        return set;
    }

    private void range(Node n, RectHV rect, Set<Point2D> set) {
        if (n == null || !rect.intersects(n.rect))
            return;

        if (rect.contains(n.point))
            set.add(n.point);

        range(n.left, rect, set);
        range(n.right, rect, set);
    }

    public Point2D nearest(Point2D p) {
        if (isEmpty())
            return null;

        NearestNodeHolder holder = new NearestNodeHolder(root);
        nearest(root, p, holder);

        return holder.nearest.point;
    }

    private void nearest(Node n, Point2D p, NearestNodeHolder holder) {
        if (n == null)
            return;

        double currentSquareDistance = p.distanceSquaredTo(holder.nearest.point);

        if (currentSquareDistance < n.rect.distanceSquaredTo(p))
            return;

        if (currentSquareDistance > n.point.distanceSquaredTo(p))
            holder.nearest = n;


        if (n.type == SplitType.HORIZONTAL) {
            if (p.y() < n.point.y()) {
                nearest(n.left, p, holder);
                nearest(n.right, p, holder);
            } else {
                nearest(n.right, p, holder);
                nearest(n.left, p, holder);
            }
        } else {
            if (p.x() < n.point.x()) {
                nearest(n.left, p, holder);
                nearest(n.right, p, holder);
            } else {
                nearest(n.right, p, holder);
                nearest(n.left, p, holder);
            }
        }
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
