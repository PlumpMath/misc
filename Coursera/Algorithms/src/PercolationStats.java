public class PercolationStats {
    private double[] results;
    private double mean;
    private double stddev;

    public PercolationStats(int N, int T) {
        if (N <= 0 || T <= 0)
            throw new IllegalArgumentException("invalid args");

        int total = N * N;
        results = new double[T];

        for (int m = 0; m < T; ++m) {
            Percolation p = new Percolation(N);
            int opened = 0;
            while (!p.percolates()) {
                int i = StdRandom.uniform(1, N + 1);
                int j = StdRandom.uniform(1, N + 1);
                if (!p.isOpen(i, j)) {
                    p.open(i, j);
                    ++opened;
                }
            }
            results[m] = opened * 1.0 / total;
        }

        mean = StdStats.mean(results);
        stddev = StdStats.stddev(results);
    }

    public double mean() {
        return mean;
    }

    public double stddev() {
        return stddev;
    }

    public double confidenceLo() {
        return mean - 1.96 * stddev / Math.sqrt(results.length);
    }

    public double confidenceHi() {
        return mean + 1.96 * stddev / Math.sqrt(results.length);
    }

    public static void main(String[] args) {
        int N = Integer.parseInt(args[0]);
        int T = Integer.parseInt(args[1]);

        PercolationStats ps = new PercolationStats(N, T);
        StdOut.println("mean\t\t\t\t\t= " + ps.mean());
        StdOut.println("stddev\t\t\t\t\t= " + ps.stddev());
        StdOut.println("95% confidence interval = " + ps.confidenceLo() + ", " + ps.confidenceHi());
    }
}
