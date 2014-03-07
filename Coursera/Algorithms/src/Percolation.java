public class Percolation {
    private int size;
    private int total;
    private boolean[][] sites; // opened or not
    private WeightedQuickUnionUF noBottomUF;
    private WeightedQuickUnionUF withBottomUF;

    public Percolation(int N) {
        size = N;
        total = N * N;

        sites = new boolean[N][N];

        for (int i = 0; i < N; ++i)
            for (int j = 0; j < N; ++j)
                sites[i][j] = false;

        withBottomUF = new WeightedQuickUnionUF(total + 2); // two virtual sites
        noBottomUF = new WeightedQuickUnionUF(total + 1); // without bottom virtual site
    }

    public void open(int i, int j) {
        checkIndex(i, j);

        if (isOpen(i, j))
            return;

        sites[i - 1][j - 1] = true;

        // the four adjacent sites and top/bottom virtual sites
        boolean above = true;
        boolean right = true;
        boolean below = true;
        boolean left = true;
        boolean top = false;
        boolean bottom = false;

        if (i == 1) {
            top = true;
            above = false;
        }
        if (i == size) {
            bottom = true;
            below = false;
        }
        if (j == 1) {
            left = false;
        }
        if (j == size) {
            right = false;
        }

        // union with the top/bottom virtual sites
        if (top) {
            noBottomUF.union(map(i, j), total);
            withBottomUF.union(map(i, j), total);
        }
        if (bottom)
            withBottomUF.union(map(i, j), total + 1);

        // union with four adjacent sites
        if (above && isOpen(i - 1, j)) {
            noBottomUF.union(map(i, j), map(i - 1, j));
            withBottomUF.union(map(i, j), map(i - 1, j));
        }
        if (right && isOpen(i, j + 1)) {
            noBottomUF.union(map(i, j), map(i, j + 1));
            withBottomUF.union(map(i, j), map(i, j + 1));
        }
        if (below && isOpen(i + 1, j)) {
            noBottomUF.union(map(i, j), map(i + 1, j));
            withBottomUF.union(map(i, j), map(i + 1, j));
        }
        if (left && isOpen(i, j - 1)) {
            noBottomUF.union(map(i, j), map(i, j - 1));
            withBottomUF.union(map(i, j), map(i, j - 1));
        }
    }

    public boolean isOpen(int i, int j) {
        checkIndex(i, j);
        return sites[i - 1][j - 1];
    }

    public boolean isFull(int i, int j) {
        checkIndex(i, j);

        if (!isOpen(i, j))
            return false;

        // to check if a site is full, we just need to check if it is connected with the top virtual site
        // and, to avoid backwashing, we use the union without bottom virtual site to check
        return noBottomUF.connected(map(i, j), total);
    }

    public boolean percolates() {
        // check if the top virtual site and bottom virtual site are connected
        return withBottomUF.connected(total, total + 1);
    }

    private int map(int i, int j) {
        return (i - 1) * size + (j - 1);
    }

    private void checkIndex(int i, int j) {
        if (i > size || j > size
                || i < 1 || j < 1)
            throw new IndexOutOfBoundsException("invalid index");
    }
}
