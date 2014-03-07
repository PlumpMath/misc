import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] items;
    private int size;

    private class RandomizedQueueIterator implements Iterator<Item> {
        private int current;
        private int[] sequences;

        public RandomizedQueueIterator() {
            current = 0;
            sequences = new int[size];
            for (int i = 0; i < size; ++i)
                sequences[i] = i;

            StdRandom.shuffle(sequences, 0, size - 1);
        }

        public boolean hasNext() {
            return current < size;
        }

        public Item next() {
            if (!hasNext())
                throw new NoSuchElementException("empty Q");

            return items[sequences[current++]];
        }

        public void remove() {
            throw new UnsupportedOperationException("not supported");
        }
    }

    public RandomizedQueue() {
        items = (Item[]) new Object[1];
        size = 0;
    }

    public boolean isEmpty() {
        return size <= 0;
    }

    public int size() {
        return size;
    }

    public void enqueue(Item item) {
        if (item == null)
            throw new NullPointerException("null item");

        if (size >= items.length)
            resize(2 * items.length);

        items[size++] = item;
    }

    public Item dequeue() {
        if (size <= 0)
            throw new NoSuchElementException("empty Q");

        int index = StdRandom.uniform(size);
        Item result = items[index];

        // make the items[index] null here if index is the last element
        items[index] = null;
        items[index] = items[--size];
        items[size] = null;

        if (size > 0 && size <= items.length / 4)
            resize(items.length / 2);

        return result;
    }

    public Item sample() {
        if (size <= 0)
            throw new NoSuchElementException("empty Q");

        return items[StdRandom.uniform(size)];
    }

    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    private void resize(int capacity) {
        Item[] copy = (Item[]) new Object[capacity];
        System.arraycopy(items, 0, copy, 0, size);
        items = copy;
    }

    public static void main(String[] args) {
        RandomizedQueue<Integer> q = new RandomizedQueue<Integer>();
        q.enqueue(1);
        q.enqueue(2);
        q.enqueue(3);
        q.enqueue(4);
        q.enqueue(5);

        Iterator<Integer> it = q.iterator();
        while (it.hasNext())
            StdOut.println(it.next());

        StdOut.println("--------------");
        StdOut.println(q.dequeue());
        StdOut.println(q.dequeue());
        StdOut.println(q.size());
        StdOut.println(q.sample());
        StdOut.println(q.size());
        StdOut.println("--------------");

        it = q.iterator();
        while (it.hasNext())
            StdOut.println(it.next());
    }
}
