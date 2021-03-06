package net.mahdilamb.stats.distributions;

/**
 * A statistical distribution
 */
public interface Distribution {

    /**
     * Calculate the cumulative density function of a value
     *
     * @param x the value x
     * @return the cdf of x
     */
    double CDF(double x);

    /**
     * Calculate the log cumulative density function of a value
     *
     * @param x the value x
     * @return the log cdf of x
     */
    default double logCDF(double x) {
        return Math.log(CDF(x));
    }

    /**
     * Calculate the percent point function (inverse of CDF)
     *
     * @param x the x value
     * @return the percent point function at x
     */
    double PPF(double x);

    /**
     * Calculate the survival function of a value from the distribution
     *
     * @param x the value x
     * @return the survival function of x
     */
    default double SF(double x) {
        return 1 - CDF(x);
    }

    /**
     * Calculate the log of the survival function of a value from the distribution
     *
     * @param x the value x
     * @return the log of the survival function of x
     */
    default double logSF(double x) {
        return Math.log(SF(x));
    }

    /**
     * Calculate the inverse survival function of a value
     *
     * @param q the value q
     * @return the inverse survival function of a value
     */
    default double inverseSF(double q) {
        return PPF(1 - q);
    }

    /**
     * @return the median of the distribution
     */
    default double getMedian() {
        return PPF(0.5);
    }

    /**
     * @return the mean of the distribution
     */
    default double getMean() {
        return getStats().getMean();
    }

    /**
     * @return summary statistics (mean, variation, skewness, kurtosis)
     * of the distribution
     */
    SummaryStatistics getStats();
}
