public class Solver {
    private Stack<Board> solutions;

    private class SearchNode implements Comparable<SearchNode> {
        private SearchNode parent;
        private Board board;
        private final int moved;

        public SearchNode(SearchNode parent, Board board, int moved) {
            this.parent = parent;
            this.board = board;
            this.moved = moved;
        }

        public int compareTo(SearchNode that) {
            int pThis = board.manhattan() + moved;
            int pThat = that.board.manhattan() + that.moved;
            if (pThis == pThat)
                return 0;
            else if (pThis < pThat)
                return -1;
            else
                return +1;
        }
    }

    public Solver(Board initial) {
        MinPQ<SearchNode> pq = new MinPQ<SearchNode>();
        pq.insert(new SearchNode(null, initial, 0));

        Board twin = initial.twin();
        MinPQ<SearchNode> twinPQ = new MinPQ<SearchNode>();
        twinPQ.insert(new SearchNode(null, twin, 0));

        while (true) {
            boolean pqEmpty = false;
            if (!pq.isEmpty()) {
                SearchNode current = pq.delMin();
                if (current.board.isGoal()) {
                    solutions = new Stack<Board>();
                    while (current != null) {
                        solutions.push(current.board);
                        current = current.parent;
                    }
                    break;
                }

                for (Board neighbor : current.board.neighbors()) {
                    if (current.parent == null || !current.parent.board.equals(neighbor))
                        pq.insert(new SearchNode(current, neighbor, current.moved + 1));
                }
            } else {
                pqEmpty = true;
            }

            boolean twinPQEmpty = false;
            if (!twinPQ.isEmpty()) {
                SearchNode current = twinPQ.delMin();
                if (current.board.isGoal()) {
                    break;
                }

                for (Board neighbor : current.board.neighbors()) {
                    if (current.parent == null || !current.parent.board.equals(neighbor))
                        twinPQ.insert(new SearchNode(current, neighbor, current.moved + 1));
                }
            } else {
                twinPQEmpty = true;
            }

            if (pqEmpty && twinPQEmpty) {
                break;
            }
        }
    }

    public boolean isSolvable() {
        return solutions != null;
    }

    public int moves() {
        if (solutions == null)
            return -1;
        return solutions.size() - 1; // remove the initial board
    }

    public Iterable<Board> solution() {
        return solutions;
    }

    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
