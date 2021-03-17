package net.mahdilamb.stats.distributions;

/**
 * Continuous distribution
 */
public interface ContinuousDistribution extends Distribution {
    /**
     * Calculate the probability density function of a value
     *
     * @param x the value x
     * @return the pdf of x
     */
    double PDF(double x);

    /**
     * Calculate the log probability density function of a value
     *
     * @param x the value x
     * @return the log pdf of x
     */
    default double logPDF(double x) {
        return Math.log(PDF(x));
    }

}
