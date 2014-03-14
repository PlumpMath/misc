import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Board {
    private final int[][] blocks;
    private int N;

    private class NeighborsIterable implements Iterable<Board> {
        private List<Board> boards;

        public NeighborsIterable() {
            boards = new ArrayList<Board>(4);
            boolean found = false;
            int i = 0;
            int j = 0;
            do {
                if (j >= N) {
                    j = 0;
                    ++i;
                }
                if (i >= N)
                    break;
                found = blocks[i][j++] == 0;
            } while (!found);

            if (!found)
                throw new IllegalArgumentException("the board does not have an empty block.");

            --j; // to locate the empty block

            boolean top = true;
            boolean bottom = true;
            boolean left = true;
            boolean right = true;

            if (i == 0)
                top = false;
            if (i >= N - 1)
                bottom = false;
            if (j == 0)
                left = false;
            if (j >= N - 1)
                right = false;

            if (top) {
                addBoard(i, j, i - 1, j);
            }
            if (bottom) {
                addBoard(i, j, i + 1, j);
            }
            if (left) {
                addBoard(i, j, i, j - 1);
            }
            if (right) {
                addBoard(i, j, i, j + 1);
            }
        }

        private void addBoard(int eI, int eJ, int exchI, int exchJ) {
            int[][] copy = deepCopy(blocks, N);
            exchange(copy, eI, eJ, exchI, exchJ);
            boards.add(new Board(copy, false));
        }

        public Iterator<Board> iterator() {
            return boards.iterator();
        }
    }

    public Board(int[][] blocks) {
        this(blocks, true);
    }

    private Board(int[][] blocks, boolean deepCopy) {
        if (blocks.length != blocks[0].length)
            throw new IllegalArgumentException("array dimensions not equal");

        N = blocks.length;
        if (deepCopy)
            this.blocks = deepCopy(blocks, N);
        else
            this.blocks = blocks;
    }

    public int dimension() {
        return N;
    }

    public int hamming() {
        int mismatch = 0;
        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < N; ++j) {
                // the block is not the empty block and it is not at the right place
                if (blocks[i][j] != 0 && blocks[i][j] != N * i + j + 1)
                    ++mismatch;
            }
        }

        return mismatch;
    }

    public int manhattan() {
        int distances = 0;
        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < N; ++j) {
                int block = blocks[i][j];
                // the empty block or the block is at the right place
                if (block == 0 || block == N * i + j + 1)
                    continue;

                int desiredJ = block % N - 1;
                // block = N * desiredI + desiredJ + 1
                // but sometimes, block % N == 0, so it becomes block = N * x
                // under this condition, desiredJ will be -1
                if (desiredJ == -1)
                    desiredJ = N - 1;
                int desiredI = (block - desiredJ - 1) / N;
                int di = Math.abs(desiredI - i);
                int dj = Math.abs(desiredJ - j);
                distances += di + dj;
            }
        }

        return distances;
    }

    public boolean isGoal() {
        if (blocks[N - 1][N - 1] != 0)
            return false;

        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < N; ++j) {
                if (blocks[i][j] != 0 && blocks[i][j] != N * i + j + 1)
                    return false;
            }
        }

        return true;
    }

    public Board twin() {
        int[][] twin = deepCopy(blocks, N);

        if (twin[0][0] == 0 || twin[0][1] == 0)
            exchange(twin, 1, 0, 1, 1);
        else
            exchange(twin, 0, 0, 0, 1);

        return new Board(twin, false);
    }

    public boolean equals(Object y) {
        if (y == null)
            return false;
        if (y == this)
            return true;
        if (!(y instanceof Board))
            return false;

        Board that = (Board) y;

        if (this.N != that.N)
            return false;

        if (this.blocks == that.blocks)
            return true;

        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < N; ++j) {
                if (this.blocks[i][j] != that.blocks[i][j])
                    return false;
            }
        }

        return true;
    }

    public Iterable<Board> neighbors() {
        return new NeighborsIterable();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(N).append('\n');
        for (int i = 0; i < N; ++i) {
            sb.append(' ');
            for (int j = 0; j < N; ++j) {
                sb.append(blocks[i][j]);
                if (j != N - 1)
                    sb.append("  ");
                else
                    sb.append('\n');
            }
        }
        return sb.toString();
    }

    private static void exchange(int[][] blocks, int row1, int col1, int row2, int col2) {
        if (row1 < 0 || col1 < 0
                || row2 < 0 || col2 < 0
                || row1 >= blocks.length || col1 >= blocks[0].length
                || row2 >= blocks.length || col2 >= blocks[0].length)
            return;

        int temp = blocks[row1][col1];
        blocks[row1][col1] = blocks[row2][col2];
        blocks[row2][col2] = temp;
    }

    private static int[][] deepCopy(int[][] blocks, int N) {
        if (N < 0 || N > blocks.length || N > blocks[0].length)
            return null;

        int[][] copy = new int[N][N];
        for (int i = 0; i < N; ++i) {
            System.arraycopy(blocks[i], 0, copy[i], 0, N);
        }
        return copy;
    }

    public static void main(String[] args) {
        int[][] blocks = new int[][]{{5, 1, 8}, {2, 7, 3}, {4, 0, 6}};
        int[][] blocks2 = new int[][]{
                {1, 2, 3, 4, 5, 6, 7, 8, 9, 10},
                {11, 12, 13, 14, 15, 16, 17, 18, 19, 20},
                {21, 22, 23, 24, 25, 26, 27, 28, 29, 30},
                {31, 32, 33, 34, 35, 36, 37, 38, 39, 40},
                {41, 42, 43, 44, 45, 46, 47, 48, 49, 50},
                {51, 52, 53, 54, 55, 56, 57, 58, 59, 60},
                {61, 62, 63, 64, 65, 66, 67, 68, 69, 70},
                {71, 72, 73, 74, 75, 76, 77, 88, 78, 79},
                {81, 82, 83, 84, 85, 0, 86, 87, 89, 80},
                {91, 92, 93, 94, 95, 96, 97, 98, 99, 90}
        };
        exchange(blocks, 0, 0, 0, 1);
        Board b = new Board(blocks2);
        StdOut.println(b.manhattan());
        StdOut.println(b.dimension());
        StdOut.println(b.hamming());
    }
}