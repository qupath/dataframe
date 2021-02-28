package net.mahdilamb.statistics.distributions;

import static net.mahdilamb.statistics.StatUtils.count;
import static net.mahdilamb.statistics.StatUtils.mean;
import static net.mahdilamb.statistics.distributions.NormalDistributions.*;

/**
 * Normal distribution
 */
public final class NormalDistribution implements Distribution {
    public static final SummaryStatistics stats = new SummaryStatistics(0, 1, 0, 0);
    private final double mean, stdev;

    /**
     * Create a normal distribution from a given mean and standard deviation
     *
     * @param mean  mean of the data
     * @param stdev the standard deviation of the data
     */
    public NormalDistribution(double mean, double stdev) {
        this.mean = mean;
        this.stdev = stdev;
    }

    /**
     * Create a normal distribution from the given data
     *
     * @param data the data to build the distribution around
     */
    public NormalDistribution(double... data) {
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
        stdev = Math.sqrt(sumMeanDiffSq / (n - 1));

    }

    @Override
    public double PDF(double x) {
        return calculatePDF(x, mean, stdev);
    }

    @Override
    public double logPDF(double x) {
        return calculateLogPDF(x, mean, stdev);
    }

    @Override
    public double CDF(double x) {
        return calculateCDF(x, mean, stdev);
    }

    @Override
    public double logCDF(double x) {
        return calculateLogCDF(x, mean, stdev);
    }

    @Override
    public double PPF(double x) {
        return calculatePPF(x, mean, stdev);
    }

    @Override
    public double SF(double x) {
        return calculateSF(x, mean, stdev);
    }

    @Override
    public double logSF(double x) {
        return calculateLogSF(x, mean, stdev);
    }

    @Override
    public SummaryStatistics getStats() {
        return stats;
    }
    //TODO      entropy   return 0.5*(Math.log(2*Math.pi)+1)


    /**
     * Create a normal distribution around data
     *
     * @param data the data to create the normal distribution around
     * @return a normal distribution
     */
    public static NormalDistribution from(double... data) {
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
