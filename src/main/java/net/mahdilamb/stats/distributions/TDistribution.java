package net.mahdilamb.stats.distributions;

/**
 * A continuous Student's t-distribution
 */
public final class TDistribution implements ContinuousDistribution {
    /**
     * The degrees of freedom of this t-distribution
     */
    public final int degreesOfFreedom;
    private SummaryStatistics stats;

    /**
     * Create a t-distribution with given degrees of freedom
     *
     * @param degreesOfFreedom the degrees of freedom
     */
    public TDistribution(int degreesOfFreedom) {
        this.degreesOfFreedom = degreesOfFreedom;
    }

    @Override
    public double PDF(double x) {
        return TDistributions.PDF(x, degreesOfFreedom);
    }

    @Override
    public double CDF(double x) {
        return TDistributions.CDF(x, degreesOfFreedom);
    }

    @Override
    public double PPF(double x) {
        return TDistributions.PPF(x, degreesOfFreedom);
    }

    @Override
    public double inverseSF(double q) {
        return TDistributions.inverseSF(q, degreesOfFreedom);
    }

    @Override
    public double SF(double x) {
        return TDistributions.SF(x, degreesOfFreedom);
    }

    @Override
    public double logPDF(double x) {
        return TDistributions.logPDF(x, degreesOfFreedom);
    }

    @Override
    public SummaryStatistics getStats() {
        if (stats == null) {
            stats = TDistributions.stats(degreesOfFreedom);
        }
        return stats;
    }
}
