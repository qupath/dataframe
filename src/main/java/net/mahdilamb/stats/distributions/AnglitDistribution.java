package net.mahdilamb.stats.distributions;

import static net.mahdilamb.stats.distributions.NormalDistributions.standardize;

/**
 * An Anglit distribution
 */
public final class AnglitDistribution implements Distribution {
    /**
     * The mean and standard deviation of this distribution
     */
    public final double mean, standardDeviation;

    /**
     * Construct an Anglit distribution around the given mean and standard deviation
     *
     * @param mean              the mean
     * @param standardDeviation the standard deviation
     */
    public AnglitDistribution(double mean, double standardDeviation) {
        this.mean = mean;
        this.standardDeviation = standardDeviation;
    }

    @Override
    public double PDF(double x) {
        return AnglitDistributions.PDF(standardize(x, mean, standardDeviation));

    }

    @Override
    public double CDF(double x) {
        return AnglitDistributions.CDF(standardize(x, mean, standardDeviation));
    }

    @Override
    public double PPF(double x) {
        return AnglitDistributions.PPF(standardize(x, mean, standardDeviation));
    }

    @Override
    public SummaryStatistics getStats() {
        return AnglitDistributions.stats;
    }
}
