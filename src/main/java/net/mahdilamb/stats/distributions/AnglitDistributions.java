package net.mahdilamb.stats.distributions;

/**
 * An Anglit distribution
 */
public final class AnglitDistributions {
    /**
     * The summary statistics of an Anglit distribution
     */
    public static final SummaryStatistics stats = new SummaryStatistics(0.0, Math.PI * Math.PI / 16 - 0.5, 0.0, -2 * (Math.pow(Math.PI, 4) - 96) / Math.pow((Math.PI * Math.PI - 8), 2));

    private AnglitDistributions() {
    }

    /**
     * @param x the point to get the PDF at
     * @return the probability density function of an Anglit distribution at x
     */
    public static double PDF(double x) {
        return Math.cos(2 * x);
    }

    /**
     * @param x the point to get the PDF at
     * @return the cumulative density function of an Anglit distribution at x
     */
    public static double CDF(double x) {
        double y = Math.sin(x + Math.PI / 4);
        return y * y;
    }

    /**
     * @param q the point to get the PDF at
     * @return the point probability function of an Anglit distribution at q
     */
    public static double PPF(double q) {
        return Math.asin(Math.sqrt(q)) - Math.PI / 4;
    }
}
