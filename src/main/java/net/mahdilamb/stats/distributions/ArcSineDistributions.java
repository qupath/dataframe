package net.mahdilamb.stats.distributions;

/**
 * Utility methods for working with ArcSine distributions
 */
public final class ArcSineDistributions {
    private ArcSineDistributions() {

    }

    /**
     * First four central moments
     */
    public static final SummaryStatistics stats = new SummaryStatistics(.5, .125, 0, -1.5);

    /**
     * Calculate the PDF of the arcsine distribution at x
     *
     * @param x the position in the distribution
     * @return the PDF of the arcsine distribution at x
     */
    public static double PDF(double x) {
        return 1.0 / Math.PI / Math.sqrt(x * (1 - x));
    }

    /**
     * Calculate the CDF of the arcsine distribution at x
     *
     * @param x the position in the distribution
     * @return the CDF of the arcsine distribution at x
     */
    public static double CDF(double x) {
        return 2.0 / Math.PI * Math.asin(Math.sqrt(x));
    }

    /**
     * Calculate the PPF of the arcsine distribution at q
     *
     * @param q the position in the distribution
     * @return the PPF of the arcsine distribution at q
     */
    public static double PPF(double q) {
        return Math.pow(Math.sin(Math.PI / 2.0 * q), 2);
    }
}
