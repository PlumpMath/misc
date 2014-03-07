import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private Node head;
    private Node tail;
    private int size;

    private class Node {
        public Item getData() {
            return data;
        }

        public Node getPrev() {
            return prev;
        }

        public Node getNext() {
            return next;
        }

        private Item data;
        private Node prev;
        private Node next;
    }

    private class DequeIterator implements Iterator<Item> {
        private Node current;

        public DequeIterator(Node head) {
            current = head;
        }

        public boolean hasNext() {
            return current != null;
        }

        public Item next() {
            if (!hasNext())
                throw new NoSuchElementException("empty Q");

            Node tmp = current;
            current = current.next;
            return tmp.data;
        }

        public void remove() {
            throw new UnsupportedOperationException("not supported");
        }
    }

    public Deque() {
        head = null;
        tail = null;
        size = 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void addFirst(Item item) {
        if (item == null)
            throw new NullPointerException("null item");

        ++size;

        Node n = new Node();
        n.data = item;
        n.next = head;
        n.prev = null;

        if (head != null)
            head.prev = n;

        if (tail == null)
            tail = n;

        head = n;
    }

    public void addLast(Item item) {
        if (item == null)
            throw new NullPointerException("null item");

        ++size;

        Node n = new Node();
        n.data = item;
        n.next = null;
        n.prev = tail;

        if (tail != null)
            tail.next = n;

        if (head == null)
            head = n;

        tail = n;
    }

    public Item removeFirst() {
        if (size <= 0)
            throw new NoSuchElementException("empty Q");

        --size;

        Node tmp = head;

        if (size == 0) {
            head = null;
            tail = null;
            return tmp.data;
        }

        head = head.next;
        tmp.next = null;
        head.prev = null;

        return tmp.data;
    }

    public Item removeLast() {
        if (size <= 0)
            throw new NoSuchElementException("empty Q");

        --size;

        Node tmp = tail;

        if (size == 0) {
            head = null;
            tail = null;
            return tmp.data;
        }

        tail = tail.prev;
        tmp.prev = null;
        tail.next = null;

        return tmp.data;
    }

    public Iterator<Item> iterator() {
        return new DequeIterator(head);
    }

    public static void main(String[] args) {
        Deque<Integer> q = new Deque<Integer>();

        q.addFirst(3);
        q.addLast(4);
        q.addFirst(2);
        q.addLast(5);
        q.addFirst(1);

        Iterator<Integer> it = q.iterator();
        while (it.hasNext()) {
            StdOut.println(it.next());
        }
    }
}