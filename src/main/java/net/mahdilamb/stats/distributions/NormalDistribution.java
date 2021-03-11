package net.mahdilamb.stats.distributions;

import static net.mahdilamb.stats.StatUtils.count;
import static net.mahdilamb.stats.StatUtils.mean;
import static net.mahdilamb.stats.distributions.NormalDistributions.*;

/**
 * Normal distribution
 */
public final class NormalDistribution implements Distribution {
    /**
     * Summary statistics of a normal distribution
     */
    public static final SummaryStatistics stats = new SummaryStatistics(0, 1, 0, 0);
    /**
     * The mean and standard deviation of this distribution
     */
    public final double mean, standardDeviation;


    /**
     * Create a normal distribution from a given mean and standard deviation
     *
     * @param mean              mean of the data
     * @param standardDeviation the standard deviation of the data
     */
    public NormalDistribution(double mean, double standardDeviation) {
        this.mean = mean;
        this.standardDeviation = standardDeviation;
    }

    /**
     * Create a normal distribution from the given data
     *
     * @param data the data to build the distribution around
     */
    private NormalDistribution(double... data) {
        mean = mean(data);
        final double n = count(data);
        if (n <= 1) {
            throw new IllegalArgumentException("Cannot create a normal distribution with fewer than or equal to 1 element");
        }
        double sumMeanDiffSq = 0d;
        for (final double d : data) {
            final double meanDiff = d - mean;
            sumMeanDiffSq += (meanDiff * meanDiff);

        }
        standardDeviation = Math.sqrt(sumMeanDiffSq / (n - 1));
    }

    @Override
    public double PDF(double x) {
        return calculatePDF(x, mean, standardDeviation);
    }

    @Override
    public double logPDF(double x) {
        return calculateLogPDF(x, mean, standardDeviation);
    }

    @Override
    public double CDF(double x) {
        return calculateCDF(x, mean, standardDeviation);
    }

    @Override
    public double logCDF(double x) {
        return calculateLogCDF(x, mean, standardDeviation);
    }

    @Override
    public double PPF(double x) {
        return calculatePPF(x, mean, standardDeviation);
    }

    @Override
    public double SF(double x) {
        return calculateSF(x, mean, standardDeviation);
    }

    @Override
    public double logSF(double x) {
        return calculateLogSF(x, mean, standardDeviation);
    }


    @Override
    public SummaryStatistics getStats() {
        return stats;
    }

    /**
     * Create a normal distribution around data
     *
     * @param data the data to create the normal distribution around
     * @return a normal distribution
     */
    public static NormalDistribution fit(double... data) {
        return new NormalDistribution(data);
    }

    /**
     * Factory method to create a normal distribution given a mean and standard deviation
     *
     * @param mean  the mean
     * @param stdev the standard deviation
     * @return a normal distribution
     */
    public static NormalDistribution of(double mean, double stdev) {
        return new NormalDistribution(mean, stdev);
    }
}
