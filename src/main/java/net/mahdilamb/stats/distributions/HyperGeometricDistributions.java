package net.mahdilamb.stats.distributions;

import net.mahdilamb.stats.libs.Cephes;

/**
 * Utility class for working with hypergeometric distributions
 */
public final class HyperGeometricDistributions {
    private HyperGeometricDistributions() {

    }

    /**
     * Calculate the log PMF in a hyper geometric distribution
     *
     * @param k k
     * @param M M
     * @param n n
     * @param N n
     * @return the associated log PMF
     */
    public static double logPMF(double k, double M, double n, double N) {
        double bad = M - n;
        return (Cephes.betaln(n + 1, 1) + Cephes.betaln(bad + 1, 1) + Cephes.betaln(M - N + 1, N + 1) -
                Cephes.betaln(k + 1, n - k + 1) - Cephes.betaln(N - k + 1, bad - N + k + 1) -
                Cephes.betaln(M + 1, 1));
    }

    /**
     * Calculate the PMF in a hyper geometric distribution
     *
     * @param k k
     * @param M M
     * @param n n
     * @param N n
     * @return the associated PMF
     */
    public static double PMF(double k, double M, double n, double N) {
        return Math.exp(logPMF(k, M, n, N));
    }

    /**
     * @param M M
     * @param n n
     * @param N n
     * @return the summary statistics of a hyper geometric function as defined by the parameters
     */
    public static SummaryStatistics stats(double M, double n, double N) {
        double m = M - n;
        double p = n / M;
        double mu = N * p;

        double var = m * n * N * (M - N) * 1.0 / (M * M * (M - 1));
        double g1 = (m - n) * (M - 2 * N) / (M - 2.0) * Math.sqrt((M - 1.0) / (m * n * N * (M - N)));

        double g2 = M * (M + 1) - 6. * N * (M - N) - 6. * n * m;
        g2 *= (M - 1) * M * M;
        g2 += 6. * n * N * (M - N) * m * (5. * M - 6);
        g2 /= n * N * (M - N) * m * (M - 2.) * (M - 3.);
        return new SummaryStatistics(mu, var, g1, g2);
    }
}
