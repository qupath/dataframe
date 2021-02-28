package net.mahdilamb.statistics.distributions;

import static net.mahdilamb.statistics.libs.Cephes.ndtr;
import static net.mahdilamb.statistics.libs.Cephes.ndtri;
import static net.mahdilamb.statistics.libs.SciPy.log_ndtr;

public final class NormalDistributions {
    private NormalDistributions() {

    }

    public static final double PDF_C = Math.sqrt(2 * Math.PI);
    public static final double PDF_LOG_C = Math.log(PDF_C);

    /**
     * Calculate the probability density function of a value from a standardized
     * normal distribution
     *
     * @param x the value
     * @return the PDF
     */
    public static double calculatePDF(double x) {
        return Math.exp(-(x * x) * .5) / PDF_C;
    }

    /**
     * Calculate the log probability density function of a value from a standardized
     * normal distribution
     *
     * @param x the value
     * @return the log PDF
     */
    public static double calculateLogPDF(double x) {
        return (-(x * x) * .5) - PDF_LOG_C;
    }


    /**
     * Calculate the log cumulative density function of a value from a standardized
     * normal distribution
     *
     * @param x the value
     * @return the log CDF
     */
    public static double calculateLogCDF(double x) {
        return log_ndtr(x);
    }

    /**
     * Calculate the log cumulative density function of a value from the given normal distribution
     *
     * @param x     the value
     * @param mean  the mean of the distribution of interest
     * @param stdev the standard deviation of the distribution of interest
     * @return the log CDF
     */
    public static double calculateLogCDF(double x, double mean, double stdev) {
        return calculateLogCDF(standardize(x, mean, stdev));
    }

    /**
     * Calculate the probability density function of a value from the given normal distribution
     * normal distribution
     *
     * @param x     the value
     * @param mean  the mean of the distribution of interest
     * @param stdev the standard deviation of the distribution of interest
     * @return the PDF
     */
    public static double calculatePDF(double x, double mean, double stdev) {
        return calculatePDF(standardize(x, mean, stdev)) / stdev;
    }

    /**
     * Calculate the log probability density function of a value from the given normal distribution
     *
     * @param x     the value
     * @param mean  the mean of the distribution of interest
     * @param stdev the standard deviation of the distribution of interest
     * @return the log PDF
     */
    public static double calculateLogPDF(double x, double mean, double stdev) {
        return calculateLogPDF(standardize(x, mean, stdev)) - Math.log(stdev);
    }

    /**
     * Calculate the cumulative density function of a value from a standardized
     * normal distribution
     *
     * @param x the value to calculate the CDF of
     * @return the CDF
     */
    public static double calculateCDF(double x) {
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
    public static double calculateCDF(double x, double mean, double stdev) {
        return calculateCDF(standardize(x, mean, stdev));
    }

    /**
     * Calculate the percent point function of a value from a standardized
     * normal distribution
     *
     * @param x the value to calculate the PPF of
     * @return the PPF
     */
    public static double calculatePPF(double x) {
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
    public static double calculatePPF(double x, double mean, double stdev) {
        return calculatePPF(standardize(x, mean, stdev));
    }

    /**
     * Calculate the survival density function of a value from a standardized normal distribution
     *
     * @param x the value
     * @return the SF
     */
    public static double calculateSF(double x) {
        return calculateCDF(-x);
    }

    /**
     * Calculate the survival density function of a value from the given normal distribution
     *
     * @param x     the value
     * @param mean  the mean of the distribution of interest
     * @param stdev the standard deviation of the distribution of interest
     * @return the SF
     */
    public static double calculateSF(double x, double mean, double stdev) {
        return calculateSF(standardize(x, mean, stdev));
    }

    /**
     * Calculate the inverse survival density function of a value from a standardized normal distribution
     *
     * @param x the value
     * @return the inverse SF
     */
    public static double calculateInverseSF(double x) {
        return -calculatePPF(x);
    }

    /**
     * Calculate the inverse survival density function of a value from the given normal distribution
     *
     * @param x     the value
     * @param mean  the mean of the distribution of interest
     * @param stdev the standard deviation of the distribution of interest
     * @return the inverse SF
     */
    public static double calculateInverseSF(double x, double mean, double stdev) {
        return calculateInverseSF(standardize(x, mean, stdev));
    }

    /**
     * Calculate the log survival function of a value from a standardized normal distribution
     *
     * @param x the value
     * @return the log SF
     */
    public static double calculateLogSF(double x) {
        return calculateLogCDF(-x);
    }

    /**
     * Calculate the log survival function of a value from the given normal distribution
     *
     * @param x     the value
     * @param mean  the mean of the distribution of interest
     * @param stdev the standard deviation of the distribution of interest
     * @return the log SF
     */
    public static double calculateLogSF(double x, double mean, double stdev) {
        return calculateLogSF(standardize(x, mean, stdev));
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
