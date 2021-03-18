package net.mahdilamb.stats.distributions;

import net.mahdilamb.stats.libs.Cephes;

/**
 * Utility class for alpha distributions
 */
public final class AlphaDistributions {
    /**
     * The summary statistics for an alpha distribution
     */
    public static final SummaryStatistics stats = new SummaryStatistics(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.NaN, Double.NaN);

    private AlphaDistributions() {

    }

    /**
     * @param x the value get the PDF at
     * @param a the alpha of the distribution
     * @return the probability density function of an alpha distribution
     */
    public static double PDF(double x, double a) {
        return 1.0 / (x * x) / NormalDistributions.CDF(a) * NormalDistributions.PDF(a - 1.0 / x);
    }

    /**
     * @param x the value get the log PDF at
     * @param a the alpha of the distribution
     * @return the log probability density function of an alpha distribution
     */
    public static double logPDF(double x, double a) {
        return -2 * Math.log(x) + NormalDistributions.logPDF(a - 1.0 / x) - Math.log(NormalDistributions.CDF(a));
    }

    /**
     * @param x the value get the CDF at
     * @param a the alpha of the distribution
     * @return the cumulative density function of an alpha distribution
     */
    public static double CDF(double x, double a) {
        return NormalDistributions.CDF(a - 1.0 / x) / NormalDistributions.CDF(a);
    }

    /**
     * @param q the value get the PPF at
     * @param a the alpha of the distribution
     * @return the point probability  function of an alpha distribution
     */
    public static double PPF(double q, double a) {
        return 1.0 / (a - Cephes.ndtri(q * NormalDistributions.CDF(a)));
    }

}
