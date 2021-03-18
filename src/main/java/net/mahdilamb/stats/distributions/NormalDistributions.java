package net.mahdilamb.stats.distributions;

import static net.mahdilamb.stats.libs.Cephes.ndtr;
import static net.mahdilamb.stats.libs.Cephes.ndtri;
import static net.mahdilamb.stats.libs.SciPy.log_ndtr;

/**
 * Utility class for working with normal distributions
 */
public final class NormalDistributions {
    private NormalDistributions() {

    }

    /**
     * Square root of 2*PI
     */
    public static final double PDF_C = Math.sqrt(2 * Math.PI);
    /**
     * LOG of the square root of 2*PI
     */
    public static final double PDF_LOG_C = Math.log(PDF_C);

    /**
     * Calculate the probability density function of a value from a standardized
     * normal distribution
     *
     * @param x the value
     * @return the PDF
     */
    public static double PDF(double x) {
        return Math.exp(-(x * x) * .5) / PDF_C;
    }

    /**
     * Calculate the log probability density function of a value from a standardized
     * normal distribution
     *
     * @param x the value
     * @return the log PDF
     */
    public static double logPDF(double x) {
        return (-(x * x) * .5) - PDF_LOG_C;
    }


    /**
     * Calculate the log cumulative density function of a value from a standardized
     * normal distribution
     *
     * @param x the value
     * @return the log CDF
     */
    public static double logCDF(double x) {
        return log_ndtr(x);
    }

    /**
     * Calculate the log cumulative density function of a value from the given normal distribution
     *
     * @param x      the value
     * @param mean   the mean of the distribution of interest
     * @param stddev the standard deviation of the distribution of interest
     * @return the log CDF
     */
    public static double logCDF(double x, double mean, double stddev) {
        return logCDF(standardize(x, mean, stddev));
    }

    /**
     * Calculate the probability density function of a value from the given normal distribution
     * normal distribution
     *
     * @param x      the value
     * @param mean   the mean of the distribution of interest
     * @param stddev the standard deviation of the distribution of interest
     * @return the PDF
     */
    public static double PDF(double x, double mean, double stddev) {
        return PDF(standardize(x, mean, stddev)) / stddev;
    }

    /**
     * Calculate the log probability density function of a value from the given normal distribution
     *
     * @param x     the value
     * @param mean  the mean of the distribution of interest
     * @param stdev the standard deviation of the distribution of interest
     * @return the log PDF
     */
    public static double logPDF(double x, double mean, double stdev) {
        return logPDF(standardize(x, mean, stdev)) - Math.log(stdev);
    }

    /**
     * Calculate the cumulative density function of a value from a standardized
     * normal distribution
     *
     * @param x the value to calculate the CDF of
     * @return the CDF
     */
    public static double CDF(double x) {
        return ndtr(x);
    }

    /**
     * Calculate the cumulative density function of a value from the given normal distribution
     *
     * @param x     the value
     * @param mean  the mean of the distribution of interest
     * @param stdev the standard deviation of the distribution of interest
     * @return the CDF
     */
    public static double CDF(double x, double mean, double stdev) {
        return CDF(standardize(x, mean, stdev));
    }

    /**
     * Calculate the percent point function of a value from a standardized
     * normal distribution
     *
     * @param x the value to calculate the PPF of
     * @return the PPF
     */
    public static double PPF(double x) {
        return ndtri(x);
    }

    /**
     * Calculate the percent point function of a value from the given normal distribution
     *
     * @param x     the value
     * @param mean  the mean of the distribution of interest
     * @param stdev the standard deviation of the distribution of interest
     * @return the PPF
     */
    public static double PPF(double x, double mean, double stdev) {
        return PPF(standardize(x, mean, stdev));
    }

    /**
     * Calculate the survival density function of a value from a standardized normal distribution
     *
     * @param x the value
     * @return the SF
     */
    public static double SF(double x) {
        return CDF(-x);
    }

    /**
     * Calculate the survival density function of a value from the given normal distribution
     *
     * @param x     the value
     * @param mean  the mean of the distribution of interest
     * @param stdev the standard deviation of the distribution of interest
     * @return the SF
     */
    public static double SF(double x, double mean, double stdev) {
        return SF(standardize(x, mean, stdev));
    }

    /**
     * Calculate the inverse survival density function of a value from a standardized normal distribution
     *
     * @param x the value
     * @return the inverse SF
     */
    public static double inverseSF(double x) {
        return -PPF(x);
    }

    /**
     * Calculate the inverse survival density function of a value from the given normal distribution
     *
     * @param x     the value
     * @param mean  the mean of the distribution of interest
     * @param stdev the standard deviation of the distribution of interest
     * @return the inverse SF
     */
    public static double inverseSF(double x, double mean, double stdev) {
        return inverseSF(standardize(x, mean, stdev));
    }

    /**
     * Calculate the log survival function of a value from a standardized normal distribution
     *
     * @param x the value
     * @return the log SF
     */
    public static double logSF(double x) {
        return logCDF(-x);
    }

    /**
     * Calculate the log survival function of a value from the given normal distribution
     *
     * @param x     the value
     * @param mean  the mean of the distribution of interest
     * @param stdev the standard deviation of the distribution of interest
     * @return the log SF
     */
    public static double logSF(double x, double mean, double stdev) {
        return logSF(standardize(x, mean, stdev));
    }

    /**
     * Rescale the data around a location
     *
     * @param x     the value to rescale
     * @param loc   the center
     * @param scale the scale
     * @return the value standardized
     */
    static double standardize(double x, double loc, double scale) {
        return (x - loc) / scale;
    }
}
