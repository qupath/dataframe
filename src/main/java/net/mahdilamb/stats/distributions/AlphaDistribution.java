package net.mahdilamb.stats.distributions;

/**
 * An alpha distribution
 */
public final class AlphaDistribution implements ContinuousDistribution {
    /**
     * The alpha value of this distribution
     */
    public final double alpha;

    /**
     * Construct and alpha distribution with the given alpha
     *
     * @param alpha the alpha of the distribution
     */
    public AlphaDistribution(final double alpha) {
        this.alpha = alpha;
    }

    @Override
    public double PDF(double x) {
        return AlphaDistributions.PDF(x, alpha);
    }

    @Override
    public double logPDF(double x) {
        return AlphaDistributions.logPDF(x, alpha);
    }

    @Override
    public double CDF(double x) {
        return AlphaDistributions.CDF(x, alpha);
    }

    @Override
    public double PPF(double x) {
        return AlphaDistributions.PPF(x, alpha);
    }

    @Override
    public SummaryStatistics getStats() {
        return AlphaDistributions.stats;
    }
}
