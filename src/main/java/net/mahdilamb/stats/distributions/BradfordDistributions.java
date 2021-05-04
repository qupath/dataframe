package net.mahdilamb.stats.distributions;

/**
 * Static methods for working with Bradford distributions
 */
public final class BradfordDistributions {
    private BradfordDistributions() {

    }

    /**
     * @param x x
     * @param c c
     * @return the PDF at x
     */
    public static double PDF(double x, double c) {
        return c / (c * x + 1.0) / Math.log1p(c);
    }

    /**
     * @param x x
     * @param c c
     * @return the CDF at x
     */
    public static double CDF(double x, double c) {
        return Math.log1p(c * x) / Math.log1p(c);
    }

    /**
     * @param q q
     * @param c c
     * @return the PPF at x
     */
    public static double PPF(double q, double c) {
        return Math.expm1(q * Math.log1p(c)) / c;
    }

    /**
     * @param c c
     * @return the summary statistics for a Bradford distribution with the given parameters
     */
    public static SummaryStatistics stats(double c) {
        final double k = Math.log(1.0 + c);
        final double mu = (c - k) / (c * k);
        final double mu2 = ((c + 2.0) * k - 2.0 * c) / (2 * c * k * k);

        double g1 = Math.sqrt(2) * (12 * c * c - 9 * c * k * (c + 2) + 2 * k * k * (c * (c + 3) + 3));
        g1 /= Math.sqrt(c * (c * (k - 2) + 2 * k)) * (3 * c * (k - 2) + 6 * k);
        double g2 = (Math.pow(c, 3) * (k - 3) * (k * (3 * k - 16) + 24) + 12 * k * c * c * (k - 4) * (k - 3) +
                6 * c * k * k * (3 * k - 14) + 12 * Math.pow(k, 3));
        g2 /= Math.pow(3 * c * (c * (k - 2) + 2 * k), 2);
        return new SummaryStatistics(mu, mu2, g1, g2);
    }

}
