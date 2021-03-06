package net.mahdilamb.stats;

import net.mahdilamb.stats.distributions.NormalDistributions;
import net.mahdilamb.stats.utils.FloydRivestSelection;
import net.mahdilamb.stats.utils.Statistic;

import java.util.Arrays;
import java.util.function.IntToDoubleFunction;

import static java.lang.Math.abs;
import static net.mahdilamb.stats.ArrayUtils.intRange;

/**
 * Utility class for calculating statistics. All statistics should be calculated without objects (where possible).
 * They should have signatures for the following:
 * <ul>
 * <li>#method(IntToDoubleFunction getter, int size). For other inputs that are not primitive double arrays.
 *  e.g. to get a sum from a Collection{@literal <}Double{@literal >}, you would use :
 *  <pre>
 *      List{@literal <}Double{@literal >} values = List.of(.2, 3., 4., 5., 6., 7.);
 *      double sum = sum(values::get, values.size());
 *  </pre>
 *
 * </li>
 * </ul>
 * <p>
 * They should output to a value of the highest precision (double or long).
 * <p>
 * Null values are ignored.
 * <p>
 * Methods includes:
 * <ul>
 *     <li>(double) {@link #sum}</li>
 *     <li>(double) {@link #mean}</li>
 *     <li>(double) {@link #variance}</li>
 *     <li>(double) {@link #standardDeviation}</li>
 *     <li>(double) {@link #moment}</li>
 *     <li>(double) {@link #skewness}</li>
 *     <li>(double) {@link #kurtosis}</li>
 *     <li>(double) {@link #pearsonsKurtosis}</li>
 *     <li>(double) {@link #count}</li>
 *     <li>(double) {@link #min}</li>
 *     <li>(double) {@link #max}</li>
 *     <li>(double) {@link #range}</li>
 *     <li>(double) {@link #quartile}</li>
 *     <li>(double) {@link #median}</li>
 *     <li>(double) {@link #quartileOne}</li>
 *     <li>(double) {@link #quartileThree}</li>
 *     <li>(double) {@link #interQuartileRange}</li>
 *     <li>(double) {@link #quantile}</li>
 *     <li>(double) {@link #percentile}</li>
 *     <li>(double) {@link #standardError}</li>
 * </ul>
 */
public strictfp final class StatUtils {

    private StatUtils() {

    }

    static final String P_VALUE = "p-value";

    /**
     * Calculate the sum of the data using a functional operator.
     *
     * @param getter the lambda that gets and element at an index
     * @param size   the number of elements to sum
     * @return the sum of the data
     */
    public static double sum(IntToDoubleFunction getter, int size) {
        double accum = getter.applyAsDouble(0);
        for (int i = 1; i < size; ++i) {
            accum += getter.applyAsDouble(i);
        }
        return accum;
    }

    /**
     * Calculate the product of the data using a functional operator.
     *
     * @param getter the lambda that gets and element at an index
     * @param size   the number of elements to product
     * @return the product of the data
     */
    public static double product(IntToDoubleFunction getter, int size) {
        double accum = getter.applyAsDouble(0);
        for (int i = 1; i < size; ++i) {
            accum *= getter.applyAsDouble(i);
        }
        return accum;
    }

    /**
     * Calculate the mean of the data using a functional operator.
     *
     * @param getter the lambda that gets and element at an index
     * @param size   the number of elements to get the mean of
     * @return the mean of the data
     */
    public static double mean(IntToDoubleFunction getter, int size) {
        return sum(getter, size) / size;
    }

    /**
     * Calculate the minimum value in the data using a functional operator.
     *
     * @param getter the lambda that gets and element at an index
     * @param size   the number of elements to get the minimum of
     * @return the minimum value in the data
     * @see #percentile(double, double...) to calculate the minimum at a faster than linear time (but with higher
     * memory overhead), by using the 0th percentile
     */
    public static double min(IntToDoubleFunction getter, int size) {
        double min = getter.applyAsDouble(0);
        for (int i = 1; i < size; ++i) {
            min = Math.min(min, getter.applyAsDouble(i));
        }
        return min;
    }

    /**
     * Calculate the maximum value in the data using a functional operator.
     *
     * @param getter the lambda that gets and element at an index
     * @param size   the number of elements to get the maximum of
     * @return the maximum value in the data
     * @see #percentile(double, double...) to calculate the maximum at a faster than linear time (but with higher
     * memory overhead), by using the 100th percentile
     */
    public static double max(IntToDoubleFunction getter, int size) {
        double max = getter.applyAsDouble(0);
        for (int i = 1; i < size; ++i) {
            max = Math.max(max, getter.applyAsDouble(i));
        }
        return max;
    }

    /**
     * Calculate the range value in the data using a functional operator.
     *
     * @param getter the lambda that gets and element at an index
     * @param size   the number of elements to get the range of
     * @return the range of the data
     */
    public static double range(IntToDoubleFunction getter, int size) {
        double max = getter.applyAsDouble(0);
        double min = max;
        for (int i = 1; i < size; ++i) {
            double v = getter.applyAsDouble(i);
            max = Math.max(max, v);
            min = Math.min(min, v);
        }
        return max - min;
    }

    /**
     * Calculate the variance using a functional accessor
     *
     * @param getter the index-to-value getter
     * @param size   the size of the source
     * @return the variance of the data
     */
    public static double variance(IntToDoubleFunction getter, int size) {
        return variance(getter, size, 0);
    }

    public static double variance(IntToDoubleFunction getter, int size, int ddof) {
        final double mean = mean(getter, size);
        double sumMeanDiffSq = 0d;
        int n = size - ddof;
        for (int i = 0; i < size; ++i) {
            final double meanDiff = getter.applyAsDouble(i) - mean;
            sumMeanDiffSq += (meanDiff * meanDiff) / n;
        }
        return sumMeanDiffSq;
    }

    public static double standardDeviation(IntToDoubleFunction getter, int size, int ddof) {
        return Math.sqrt(variance(getter, size, ddof));
    }

    /**
     * Calculate the standard deviation using a functional accessor
     *
     * @param getter the index to double getter
     * @param size   the size of the source
     * @return the standard deviation of the values
     */
    public static double standardDeviation(IntToDoubleFunction getter, int size) {
        return Math.sqrt(variance(getter, size));
    }

    /**
     * Calculate the sum of the data
     *
     * @param data the data from which to calculate the sum
     * @return the sum of the data
     */
    public static double sum(final double[] data) {
        double accum = 0;
        for (final double d : data) {
            accum += d;
        }
        return accum;
    }

    /**
     * Calculate the product of the data
     *
     * @param data the data from which to calculate the sum
     * @return the product of the data
     */
    public static double product(final double... data) {
        double accum = 1;
        for (final double d : data) {
            accum *= d;
        }
        return accum;
    }

    /**
     * Return the number of elements in the data
     *
     * @param data the data
     * @return the count of the data
     */
    public static int count(double... data) {
        return data.length;
    }

    /**
     * Calculate the mean of the data
     *
     * @param data the data from which to calculate the mean
     * @return the mean of the data
     */
    public static double mean(final double... data) {
        double accum = 0;
        long count = 0;
        for (final double d : data) {
            ++count;
            accum += d;
        }
        return accum / count;
    }

    /**
     * Calculate the mean of non-nan data
     *
     * @param data the data from which to calculate the mean
     * @return the mean of the data
     */
    public static double NaNMean(final double... data) {
        double accum = 0;
        long count = 0;
        for (final double d : data) {
            if (Double.isNaN(d)) {
                continue;
            }
            ++count;
            accum += d;
        }
        return accum / count;
    }

    public static double NaNMean(IntToDoubleFunction data, int size) {
        double accum = 0;
        long count = 0;
        for (int i = 0; i < size; ++i) {
            final double d = data.applyAsDouble(i);
            if (Double.isNaN(d)) {
                continue;
            }
            ++count;
            accum += d;
        }
        return accum / count;
    }

    /**
     * Get the count of non-nan values
     *
     * @param data the data
     * @return the count of non-nan values
     */
    public static double NaNCount(final double... data) {
        long count = 0;
        for (final double d : data) {
            if (Double.isNaN(d)) {
                continue;
            }
            ++count;
        }
        return count;
    }

    public static double NaNCount(final IntToDoubleFunction data, int size) {
        long count = 0;
        for (int i = 0; i < size; ++i) {
            final double d = data.applyAsDouble(i);
            if (Double.isNaN(d)) {
                continue;
            }
            ++count;
        }
        return count;
    }

    /**
     * Calculate the minimum value of non-NaN data
     *
     * @param data the data
     * @return the minimum of the data
     */
    public static double NaNMin(final double... data) {
        double min = data[0];
        for (int i = 1; i < data.length; ++i) {
            final double d = data[i];
            if (Double.isNaN(d)) {
                continue;
            }
            min = Math.min(min, d);
        }
        return min;
    }

    /**
     * Calculate the maximum of the non-NaN data
     *
     * @param data the data
     * @return the maximum of the data
     */
    public static double NaNMax(final double... data) {
        double max = data[0];
        for (int i = 1; i < data.length; ++i) {
            final double d = data[i];
            if (Double.isNaN(d)) {
                continue;
            }
            max = Math.max(max, d);
        }
        return max;
    }

    /**
     * Get the variance of data when the mean is known
     *
     * @param mean the mean of the data
     * @param data the data itself
     * @return the variance of the data
     */
    public static double NaNVariance(final double mean, final double[] data) {
        if (data.length <= 1) {
            return Double.NaN;
        }
        int n = -1;

        double sumMeanDiffSq = 0d;
        for (final double d : data) {
            if (Double.isNaN(d)) {
                continue;
            }
            final double meanDiff = d - mean;
            sumMeanDiffSq += (meanDiff * meanDiff);
            n++;

        }
        return sumMeanDiffSq / (n);
    }

    public static double NaNVariance(final double mean, final IntToDoubleFunction data, int size) {
        if (size <= 1) {
            return Double.NaN;
        }
        int n = -1;

        double sumMeanDiffSq = 0d;
        for (int i = 0; i < size; ++i) {
            final double d = data.applyAsDouble(i);
            if (Double.isNaN(d)) {
                continue;
            }
            final double meanDiff = d - mean;
            sumMeanDiffSq += (meanDiff * meanDiff);
            n++;
        }
        return sumMeanDiffSq / (n);
    }

    /**
     * Calculate the variance of the data (ignores Nan)
     *
     * @param data the data
     * @return the variance of the data
     */
    public static double NaNVariance(final double... data) {
        return NaNVariance(NaNMean(data), data);
    }

    public static double NaNVariance(IntToDoubleFunction data, int size) {
        return NaNVariance(NaNMean(data, size), data, size);
    }

    /**
     * Calculate the standard deviation of the data (ignores Nan)
     *
     * @param data the data
     * @return the standard deviation of the data
     */
    public static double NaNStandardDeviation(final double... data) {
        return Math.sqrt(NaNVariance(data));
    }

    public static double NaNStandardDeviation(IntToDoubleFunction data, int size) {
        return Math.sqrt(NaNVariance(data, size));
    }

    /**
     * Return a copy of the data without NaN values
     *
     * @param data the data to filter
     * @return the filtered data
     */
    public static double[] NaNFilter(final double... data) {
        final double[] temp = new double[data.length];
        int i = 0;
        for (final double d : data) {
            if (Double.isNaN(d)) {
                continue;
            }
            temp[i++] = d;
        }
        final double[] out = new double[i];
        System.arraycopy(temp, 0, out, 0, i);
        return out;
    }

    /**
     * Calculate the percentile (0-100) of the sorted data using the given interpolation. Ignoring NaN values
     *
     * @param percentile    the percentile of interest (0-100)
     * @param interpolation the interpolation method
     * @param data          the data to calculate the percentile of
     * @return the given percentile of the data
     */
    static double NaNPercentileSorted(final double percentile, final Interpolation interpolation, final double... data) {
        return percentile(percentile, interpolation, NaNFilter(data));
    }

    /**
     * Calculate the percentile (0-100) of the sorted data using the given interpolation. Ignoring NaN values
     *
     * @param percentile the percentile of interest (0-100)
     * @param data       the data to calculate the percentile of
     * @return the given percentile of the data
     */
    static double NaNPercentileSorted(final double percentile, final double... data) {
        return NaNPercentile(percentile, Interpolation.LINEAR, data);
    }

    /**
     * Calculate the percentile (0-100) of the data using the given interpolation. Ignoring NaN values
     *
     * @param percentile    the percentile of interest (0-100)
     * @param interpolation the interpolation method
     * @param data          the data to calculate the percentile of
     * @return the given percentile of the data
     */
    public static double NaNPercentile(final double percentile, final Interpolation interpolation, final double... data) {
        final double[] sortedAndFilter = NaNFilter(data);
        Arrays.sort(sortedAndFilter);
        return percentileSorted(percentile, interpolation, sortedAndFilter);
    }

    /**
     * Calculate the percentile (0-100) of the data using the given interpolation. Ignoring NaN values
     *
     * @param percentile the percentile of interest (0-100)
     * @param data       the data to calculate the percentile of
     * @return the given percentile of the data
     */
    public static double NaNPercentile(final double percentile, final double... data) {
        return NaNPercentile(percentile, Interpolation.LINEAR, data);
    }

    /**
     * Calculate the quantile (0-1) of the data using the given interpolation. Ignoring NaN values
     *
     * @param quantile      the quantile of interest (0-1)
     * @param interpolation the interpolation method
     * @param data          the data to calculate the quantile of
     * @return the given quantile of the data
     */
    public static double NaNQuantile(final double quantile, final Interpolation interpolation, final double... data) {
        final double[] sortedAndFilter = NaNFilter(data);
        Arrays.sort(sortedAndFilter);
        return quantileSorted(quantile, interpolation, sortedAndFilter);
    }

    /**
     * Calculate the quantile (0-1) of the data using the given interpolation. Ignoring NaN values
     *
     * @param quantile the quantile of interest (0-1)
     * @param data     the data to calculate the quantile of
     * @return the given quantile of the data
     */
    public static double NaNQuantile(final double quantile, final double... data) {
        return NaNQuantile(quantile, Interpolation.LINEAR, data);
    }

    /**
     * Get the appropriate quantile in a functional style where the order has already been determined
     *
     * @param getter   the index-to-value getter
     * @param size     the size of the source
     * @param order    the sort order
     * @param quantile the quantile of interest (0-1)
     * @return the requested quantile of already sorted data
     */
    public static double quantile(IntToDoubleFunction getter, int size, int[] order, double quantile) {
        double pos = quantile * (size - 1);
        int p = (int) pos;
        if (pos != p) {
            return Interpolation.LINEAR.interpolate(pos - p, FloydRivestSelection.select(order, getter, 0, size - 1, p), FloydRivestSelection.select(order, getter, 0, size - 1, p + 1));
        } else {
            return FloydRivestSelection.select(order, getter, 0, size - 1, p);
        }
    }

    public static double quantile(IntToDoubleFunction getter, int size, double quantile) {
        return quantile(getter, size, intRange(size), quantile);
    }

    public static double interQuartileRange(IntToDoubleFunction getter, int size, int[] order) {
        double q1 = quantile(getter, size, order, .25);
        double q3 = quantile(getter, size, order, .75);
        return q3 - q1;
    }

    /**
     * Calculate the quantile (0-1) of the data using the given interpolation. Ignoring NaN values
     *
     * @param quantile      the quantile of interest (0-1)
     * @param interpolation the interpolation method
     * @param data          the data to calculate the quantile of
     * @return the given quantile of the data
     */
    static double NaNQuantileSorted(final double quantile, final Interpolation interpolation, final double... data) {
        return quantileSorted(quantile, interpolation, NaNFilter(data));
    }

    /**
     * Calculate the quantile (0-1) of the data using the given interpolation. Ignoring NaN values
     *
     * @param quantile the quantile of interest (0-1)
     * @param data     the data to calculate the quantile of
     * @return the given quantile of the data
     */
    static double NaNQuantileSorted(final double quantile, final double... data) {
        return NaNQuantileSorted(quantile, Interpolation.LINEAR, data);
    }

    /**
     * Calculate the minimum value in the data
     *
     * @param data the data
     * @return the minimum of the data
     */
    public static double min(final double... data) {
        Double min = null;
        for (final double d : data) {
            if (min == null || d < min) {
                min = d;
            }
        }
        if (min == null) {
            throw InsufficientDataException.createForArray();
        }
        return min;
    }

    /**
     * Calculate the maximum of the data
     *
     * @param data the data
     * @return the maximum of the data
     */
    public static double max(final double... data) {
        Double max = null;
        for (final double d : data) {
            if (max == null || d > max) {
                max = d;
            }
        }
        if (max == null) {
            throw InsufficientDataException.createForArray();
        }
        return max;
    }

    /**
     * Calculate the range of the data
     *
     * @param data the data
     * @return the range of the data
     */
    public static double range(final double first, final double... data) {
        double min = first;
        double max = first;
        for (final double d : data) {
            if (d < min) {
                min = d;
            }
            if (d > max) {
                max = d;
            }
        }
        return max - min;
    }

    /**
     * Calculate the variance of the data
     *
     * @param data the data
     * @return the variance of the data
     */
    public static double variance(final double... data) {
        final double mean = mean(data);
        final int n = count(data);
        double sumMeanDiffSq = 0d;
        for (final double d : data) {
            final double meanDiff = d - mean;
            sumMeanDiffSq += (meanDiff * meanDiff);

        }
        return sumMeanDiffSq / n;
    }

    /**
     * Calculate the standard deviation of the data
     *
     * @param data the data
     * @return the standard deviation of the data
     */
    public static double standardDeviation(final double... data) {
        final double var = variance(data);
        if (Double.isNaN(var)) {
            return var;
        }
        return Math.sqrt(var);
    }

    /**
     * Calculate the nth central moment of the data
     *
     * @param moment the moment
     * @param data   the data of interest
     * @return the nth central moment of the data
     */
    public static double moment(final int moment, final double... data) {
        if (moment < 0) {
            throw new ArithmeticException("Moment must be greater than 0");
        }
        if (moment <= 1) {
            return moment == 0 ? 1 : 0;
        }
        final int[] nList = new int[(moment + 1) / 2];
        nList[0] = moment;
        int currentN = moment;
        int nLen = 1;
        while (currentN > 2) {
            if (MathUtils.isOdd(currentN)) {
                currentN = (currentN - 1) / 2;
            } else {
                currentN /= 2;
            }
            nList[nLen++] = currentN;
        }
        final double mean = mean(data);
        final double[] a_zero_mean = new double[data.length];
        for (int g = 0; g < data.length; ++g) {
            a_zero_mean[g] = data[g] - mean;
        }
        final double[] s;
        s = a_zero_mean.clone();
        if (nList[nLen - 1] != 1) {
            for (int j = 0; j < s.length; ++j) {
                s[j] *= s[j];
            }
        }
        for (int j = nLen - 2; j >= 0; --j) {
            final int n = nList[j];
            for (int k = 0; k < s.length; ++k) {
                s[k] *= s[k];
                if (MathUtils.isOdd(n)) {
                    s[k] *= a_zero_mean[k];
                }
            }
        }
        return mean(s);
    }

    /**
     * Calculate the skewness of the data. This is the Fisher-Pearson skewness coefficient.
     *
     * @param data the data
     * @return the skewness of the data
     */
    public static double skewness(final double... data) {
        final int n = count(data);
        final double mean = mean(data);
        double accum2 = 0;
        double accum3 = 0;
        for (final double d : data) {
            final double meanDiff = (d - mean);
            double md2 = meanDiff * meanDiff;
            accum3 += md2 * meanDiff;
            accum2 += md2;
        }
        final double stdev = Math.sqrt(accum2 / n);
        return (accum3 / n) / (stdev * stdev * stdev);
    }

    /**
     * Calculate the kurtosis of the data. This is the Fisher's kurtosis
     *
     * @param data the data
     * @return the kurtosis of the data
     */
    public static double kurtosis(final double... data) {
        return pearsonsKurtosis(data) - 3;
    }

    /**
     * Calculate the kurtosis of the data using Pearson's method
     *
     * @param data the data
     * @return Pearson's kurtosis of the data
     */
    public static double pearsonsKurtosis(final double... data) {
        final int n = count(data);
        final double mean = mean(data);
        double accum2 = 0;
        double accum3 = 0;
        for (final double d : data) {
            final double meanDiff = (d - mean);
            double md2 = meanDiff * meanDiff;
            accum3 += md2 * md2;
            accum2 += md2;
        }
        final double var = accum2 / n;
        return (accum3 / n) / (var * var);
    }

    /**
     * Calculate the standard error of the mean of the data
     *
     * @param data the data
     * @return the standard error of the mean
     */
    public static double standardError(final double... data) {
        final double stdev = standardDeviation(data);
        if (count(data) == 0 || Double.isNaN(stdev) || stdev == 0) {
            return Double.NaN;
        }
        return stdev / Math.sqrt(count(data));
    }

    /**
     * Calculate the median on sorted data
     *
     * @param data the sorted data
     * @return the median of the data
     */
    static double medianSorted(final double... data) {
        if (count(data) <= 1) {
            return data[0];
        }
        final int n = count(data);
        final int mid = n >> 1;//bitwise divide by 2
        return MathUtils.isOdd(n) ? data[mid] : ((data[mid - 1] + data[mid]) * 0.5);
    }

    /**
     * Calculate the median on unsorted data
     *
     * @param data the unsorted data
     * @return the median of the data. Does not modify the input data
     */
    public static double median(final double... data) {
        final double[] sorted = data.clone();
        Arrays.sort(sorted);
        return medianSorted(sorted);
    }

    /**
     * Calculate the lower quartile of the sorted data
     *
     * @param data the data
     * @return the lower quartile of the sorted data
     */
    static double quartileOneSorted(final double... data) {
        if (data.length <= 1) {
            return data[0];
        }
        final int n = count(data);
        final int mid = n >> 1;//bitwise divide by 2
        final double[] sub = Arrays.copyOfRange(data, 0, mid);
        return medianSorted(sub);
    }

    /**
     * Calculate the lower quartile of the data
     *
     * @param data the data
     * @return the lower quartile of the data
     */
    public static double quartileOne(final double... data) {
        final double[] sorted = data.clone();
        Arrays.sort(sorted);
        return quartileOneSorted(sorted);
    }

    /**
     * Calculate the upper quartile of the data on sorted data
     *
     * @param data the sorted
     * @return the upper quartile of the data
     */
    static double quartileThreeSorted(final double... data) {
        if (data.length <= 1) {
            return data[0];
        }
        final int n = count(data);
        final int mid = n >> 1;//bitwise divide by 2
        final double[] sub = Arrays.copyOfRange(data, (n & 1) == 0 ? mid : mid + 1, n);
        return medianSorted(sub);
    }

    /**
     * Calculate the upper quartile of the data
     *
     * @param data the data
     * @return the upper quartile of the data
     */
    public static double quartileThree(final double... data) {
        final double[] sorted = data.clone();
        Arrays.sort(sorted);
        return quartileThreeSorted(sorted);
    }

    /**
     * Calculate the given quartile of the data
     *
     * @param quartile the quartile of interest  (1, 2, 3)
     * @param data     the data
     * @return the requested quartile of the data
     */
    public static double quartile(int quartile, double... data) {
        switch (quartile) {
            case 1:
                return quartileOne(data);
            case 2:
                return median(data);
            case 3:
                return quartileThree(data);
            default:
                throw new IllegalArgumentException("Quartile must be 1, 2 or 3");
        }
    }

    /**
     * Calculate the inter-quartile range of sorted data
     *
     * @param data the sorted data
     * @return the inter-quartile range of the data
     */
    static double interQuartileRangeSorted(final double... data) {
        return quartileThreeSorted(data) - quartileOneSorted(data);
    }

    /**
     * Calculate the inter-quartile range of the data
     *
     * @param data the data
     * @return the inter-quartile range of the data
     */
    public static double interQuartileRange(final double... data) {
        final double[] sorted = data.clone();
        Arrays.sort(sorted);
        return interQuartileRangeSorted(sorted);
    }

    /**
     * Calculate the quantile (0-1) of the sorted data using the given interpolation
     *
     * @param quantile      the quantile of interest (0-1)
     * @param interpolation the interpolation method
     * @param data          the data to calculate the quantile of
     * @return the given quantile of the data
     */
    static double quantileSorted(final double quantile, final Interpolation interpolation, final double... data) {
        if (quantile <= 0 || quantile >= 1) {
            if (quantile == 0) {
                return data[0];
            } else if (quantile == 1) {
                return data[data.length - 1];
            }
            throw new IllegalArgumentException("Quantile must be in range 0-1");
        }
        final double v = quantile * (data.length - 1);
        final int l = (int) v;
        if (l == v) {
            return data[l];
        }
        return interpolation.interpolate(v - l, data[l], data[Math.min(l + 1, data.length - 1)]);
    }

    /**
     * Calculate the quantile (0-1) of the sorted data using linear interpolation
     *
     * @param quantile the quantile of interest (0-1)
     * @param data     the data to calculate the quantile of
     * @return the given quantile of the data
     */
    static double quantileSorted(final double quantile, final double... data) {
        return quantileSorted(quantile, Interpolation.LINEAR, data);
    }

    /**
     * Calculate the quantile (0-1) of the data using the given interpolation
     *
     * @param quantile      the quantile of interest (0-1)
     * @param interpolation the interpolation method
     * @param data          the data to calculate the quantile of
     * @return the given quantile of the data
     */
    public static double quantile(final double quantile, final Interpolation interpolation, final double... data) {
        final double[] sorted = data.clone();
        Arrays.sort(sorted);
        return quantileSorted(quantile, interpolation, sorted);
    }

    /**
     * Calculate the quantile (0-1) of the data using linear interpolation
     *
     * @param quantile the quantile of interest (0-1)
     * @param data     the data to calculate the quantile of
     * @return the given quantile of the data
     */
    public static double quantile(final double quantile, final double... data) {
        return quantile(quantile, Interpolation.LINEAR, data);
    }

    /**
     * Calculate the percentile (0-100) of the sorted data using the given interpolation
     *
     * @param percentiles   the percentiles of interest [(0-100),...]
     * @param interpolation the interpolation method
     * @param data          the data to calculate the percentile of
     * @return the given percentile of the data
     */
    static double[] percentilesSorted(final double[] percentiles, final Interpolation interpolation, final double... data) {
        final double[] out = new double[percentiles.length];
        int i = 0;
        for (final double percentile : percentiles) {
            if (percentile <= 0 || percentile >= 100) {
                if (percentile == 0) {
                    out[i++] = data[0];
                    continue;
                } else if (percentile == 100) {
                    out[i++] = data[data.length - 1];
                    continue;
                }
                throw new IllegalArgumentException("percentile must be in range 0-100");
            }
            final double v = percentile * (data.length - 1) * 0.01;
            final int l = (int) v;
            if (l == v) {
                out[i++] = data[l];
                continue;
            }
            out[i++] = interpolation.interpolate(v - l, data[l], data[Math.min(l + 1, data.length - 1)]);
        }
        return out;
    }

    /**
     * Calculate the percentile (0-100) of the sorted data using the given interpolation
     *
     * @param percentile    the percentile of interest (0-100)
     * @param interpolation the interpolation method
     * @param data          the data to calculate the percentile of
     * @return the given percentile of the data
     */
    static double percentileSorted(final double percentile, final Interpolation interpolation, final double... data) {
        if (percentile <= 0 || percentile >= 100) {
            if (percentile == 0) {
                return data[0];
            } else if (percentile == 100) {
                return data[data.length - 1];
            }
            throw new IllegalArgumentException("percentile must be in range 0-100");
        }
        final double v = percentile * (data.length - 1) * 0.01;
        final int l = (int) v;
        if (l == v) {
            return data[l];
        }
        return interpolation.interpolate(v - l, data[l], data[Math.min(l + 1, data.length - 1)]);
    }

    /**
     * Calculate the percentile (0-100) of the sorted data using linear interpolation
     *
     * @param percentile the percentile of interest (0-100)
     * @param data       the data to calculate the percentile of
     * @return the given percentile of the data
     */
    static double percentileSorted(final double percentile, final double... data) {
        return percentileSorted(percentile, Interpolation.LINEAR, data);
    }

    /**
     * Calculate the percentile (0-100) of the data using the given interpolation
     *
     * @param percentile    the percentile of interest (0-100)
     * @param interpolation the interpolation method
     * @param data          the data to calculate the percentile of
     * @return the given percentile of the data
     */
    public static double percentile(final double percentile, final Interpolation interpolation, final double... data) {
        final double[] sorted = data.clone();
        Arrays.sort(sorted);
        return percentileSorted(percentile, interpolation, sorted);
    }

    /**
     * Calculate the percentile (0-100) of the data using the given interpolation
     *
     * @param percentiles   the percentile of interest [(0-100),...]
     * @param interpolation the interpolation method
     * @param data          the data to calculate the percentile of
     * @return the given percentile of the data
     */
    public static double[] percentiles(final double[] percentiles, final Interpolation interpolation, final double... data) {
        final double[] sorted = data.clone();
        Arrays.sort(sorted);
        return percentilesSorted(percentiles, interpolation, sorted);
    }

    /**
     * Calculate the percentile (0-100) of the data using linear interpolation
     *
     * @param percentile the percentile of interest (0-100)
     * @param data       the data to calculate the percentile of
     * @return the given percentile of the data
     */
    public static double percentile(final double percentile, final double... data) {
        return percentile(percentile, Interpolation.LINEAR, data);
    }

    /**
     * Increments existing bin counts
     *
     * @param bins the current bin counts
     * @param data the data to use in the bin counts
     * @return the new bin counts
     */
    static int[] binCount(int[] bins, final int... data) {
        for (int d : data) {
            bins[d]++;
        }
        return bins;
    }

    /**
     * Count the number of values of each number (values must be non-negative)
     *
     * @param data the data to get bin counts of
     * @return the bins
     */
    public static int[] binCount(final int... data) {
        return binCount(new int[max(data) + 1], data);
    }

    /**
     * Calculate the sum of int data
     *
     * @param data the data
     * @return the sum of the data
     */
    public static long sum(final int... data) {
        long accum = 0;
        for (final int d : data) {
            accum += d;
        }
        return accum;
    }

    /**
     * Calculate the maximum of the data
     *
     * @param data the data
     * @return the maximum of the data
     */
    public static int max(final int... data) {
        if (data.length <= 1) {
            return data[0];
        }
        Integer max = null;
        for (final int d : data) {
            if (max == null || d > max) {
                max = d;
            }
        }
        return max;
    }

    /**
     * Calculate the histogram bin edges from the number of bins and data
     *
     * @param numBins the number of bins
     * @param data    the data to get the bin edges of
     * @return the bin edges
     */
    public static double[] histogramBinEdges(int numBins, double... data) throws InsufficientDataException {
        double min = min(data);
        double max = max(data);
        return ArrayUtils.linearlySpaced(min, max, numBins);
    }

    /**
     * Calculate the histogram bin edges using a bin width estimator
     *
     * @param estimator the bin width estimator
     * @param data      the data to get the bin edges of
     * @return the bin edges
     */
    public static double[] histogramBinEdges(BinWidthEstimator estimator, double... data) throws InsufficientDataException {
        double min = min(data);
        double max = max(data);
        int nEqualBins = Math.max(1, (int) Math.ceil((max - min) / estimator.estimate(data)));
        return ArrayUtils.linearlySpaced(min, max, nEqualBins + 1);
    }

    /**
     * Calculate the histogram using a bin width estimator
     *
     * @param estimator the bin width estimator
     * @param data      the data to get the bin edges of
     * @return the bin edges
     */
    public static Histogram histogram(BinWidthEstimator estimator, double... data) throws InsufficientDataException {
        double min = min(data);
        double max = max(data);
        final int nEqualBins = Math.max(1, (int) Math.ceil((max - min) / estimator.estimate(data)));
        return histogram(ArrayUtils.linearlySpaced(min, max, nEqualBins + 1), data);
    }

    /**
     * Calculate a histogram using a specified number of bins
     *
     * @param numBins the number of bins
     * @param data    the data to get the histogram of
     * @return the histogram of the data
     */
    public static Histogram histogram(int numBins, double... data) {
        double min = min(data);
        double max = max(data);
        return histogram(ArrayUtils.linearlySpaced(min, max, numBins + 1), data);

    }

    /**
     * Check if an array of doubles is sorted
     *
     * @param values the values to check
     * @return whether the values are sorted
     */
    public static boolean isSorted(double... values) {
        if (values.length <= 1) {
            return true;
        }
        int n = values.length - 1;
        for (int i = 0; i < n; ++i) {
            if (Double.compare(values[i], values[i + 1]) > 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Generate a histogram. Code adapted from Numpy
     *
     * @param binEdges an array of bin edges
     * @param data     the data to generate the histogram over
     * @return a histogram
     */
    public static Histogram histogram(double[] binEdges, final double... data) {
        assert (binEdges.length > 1);
        if (!isSorted(binEdges)) {
            throw new UnsupportedOperationException("bin edges must be sorted");
        }
        return histogram0(binEdges, data);
    }

    private static Histogram histogram0(double[] binEdges, final double... data) {
        final int nEqualBins = binEdges.length - 1;
        final Histogram out = new HistogramImpl(new int[nEqualBins], binEdges);
        int n = count(data);
        final int blocks = n >> 16;
        final double norm = nEqualBins / (binEdges[nEqualBins] - binEdges[0]);
        for (int i = 0; i <= blocks; ++i) {
            final int rangeMin = i << 16;
            final int rangeMax = Math.min(n, ((i + 1) << 16) - 1);
            final double[] temp = new double[blocks == 0 ? n : 65536];
            System.arraycopy(data, rangeMin, temp, 0, rangeMax - rangeMin);

            final int[] indices = new int[temp.length];
            for (int a = 0; a < indices.length; ++a) {
                indices[a] = (int) ((temp[a] - binEdges[0]) * norm);
                if (indices[a] == nEqualBins) {
                    --indices[a];
                }
            }
            for (int j = 0; j < temp.length; ++j) {
                if (temp[j] < binEdges[indices[j]]) {
                    --indices[j];
                }
                if ((temp[j] >= binEdges[indices[j] + 1]) & (indices[j] != nEqualBins - 1)) {
                    ++indices[j];
                }
            }
            binCount(out.getCount(), indices);
        }
        return out;
    }

    private static void full(IntToDoubleFunction src, int srcPos,
                             double[] dest, int destPos,
                             int length) {
        for (int i = 0; i < length; ++i) {
            dest[destPos + i] = src.applyAsDouble(srcPos + i);
        }

    }

    /**
     * Convert a bin-counted histogram to a probability density histogram
     *
     * @param histogram the histogram
     * @return a density histogram based on the histogram
     */
    public static double[] toDensity(final Histogram histogram) {
        final double binWidth = (histogram.getBinEdges()[histogram.getCount().length] - histogram.getBinEdges()[0]) / histogram.getCount().length;
        final long n = sum(histogram.getCount());
        final double[] density = new double[histogram.getCount().length];
        for (int i = 0; i < density.length; ++i) {
            density[i] = ((double) histogram.getCount()[i]) / binWidth / n;
        }
        return density;
    }

    /**
     * Calculate the weighted average of data
     *
     * @param weights the weights of the data
     * @param data    the data
     * @return the weighted average of the data
     */
    public static double average(final double[] weights, final double... data) {
        double accum = 0;
        double sum = 0;
        for (int i = 0; i < data.length; ++i) {
            final double weight = i < weights.length ? weights[i] : 1;
            accum = Math.fma(data[i], weight, accum);
            sum += weight;
        }
        return accum / sum;
    }

    /**
     * Calculate the density histogram of data
     *
     * @param binWidthEstimator the estimator for the density histogram
     * @param data              the data
     * @return the density histogram of the data
     */
    public static DensityHistogram densityHistogram(BinWidthEstimator binWidthEstimator, double... data) {
        final HistogramImpl out = (HistogramImpl) histogram(binWidthEstimator, data);
        out.density = toDensity(out);
        return out;
    }

    /**
     * Calculate the density histogram of data
     *
     * @param numBins the number of bins
     * @param data    the data
     * @return the density histogram of the data
     */
    public static DensityHistogram densityHistogram(int numBins, double... data) {
        final HistogramImpl out = (HistogramImpl) histogram(numBins, data);
        out.density = toDensity(out);
        return out;
    }

    private static double jhat(double min, double max, int nbins, double pptX, double[] data) {
        final double hh = pptX / nbins;
        final int n = data.length;
        final int[] count = histogram(ArrayUtils.linearlySpaced(min, max, nbins + 1), data).getCount();
        final double[] p_k = new double[count.length];
        for (int i = 0; i < p_k.length; ++i) {
            p_k[i] = ((double) count[i]) / n;
        }
        return (2 - (n + 1) * MathUtils.dot(p_k, p_k)) / hh;
    }

    static double stoneBinWidthEstimator(final double... data) {
        final int n = count(data);
        Double min = null;
        Double max = null;
        for (final double d : data) {
            if (min == null || d < min) {
                min = d;
            }
            if (max == null || d > max) {
                max = d;
            }
        }
        if (max == null) {
            throw new ArithmeticException("No numeric values");
        }
        final double ptpX = max - min;
        if (n <= 1 || ptpX == 0) {
            return 0;
        }
        final int nBinsUpperBound = Math.max(100, (int) Math.sqrt(n));
        int nbins = 0;
        for (int i = 1; i < nBinsUpperBound + 1; ++i) {
            final double d = jhat(min, max, i, ptpX, data);
            if (i == 1 || d < nbins) {
                nbins = i;
            }
        }
        if (nbins == nBinsUpperBound) {
            System.err.println("Stone bin width estimator: the number of bins estimated may be suboptimal");
        }
        return ptpX / nbins;
    }

    static double sqrtBinWidthEstimator(final double[] data) {
        return StatUtils.range(data[0], data) / Math.sqrt(data.length);
    }

    static double sturgesBinWidthEstimator(final double[] data) {
        return StatUtils.range(data[0], data) / (MathUtils.log2(data.length) + 1.0);
    }

    static double riceBinWidthEstimator(final double[] data) {
        return StatUtils.range(data[0], data) / (2 * Math.cbrt(data.length));
    }

    static double scottBinWidthEstimator(final double[] data) {
        return Math.cbrt(24.0 * MathUtils.SQRT_PI / data.length) * StatUtils.standardDeviation(data);
    }

    static double doaneBinWidthEstimator(final double[] data) {
        if (data.length > 2) {
            //calculate std and mean
            final double mean = mean(data);
            final int n = count(data);
            double sumMeanDiffSq = 0d;
            Double min = null;
            Double max = null;
            for (final double d : data) {
                final double meanDiff = d - mean;
                sumMeanDiffSq += meanDiff * meanDiff;
                if (min == null || d < min) {
                    min = d;
                }
                if (max == null || d > max) {
                    max = d;
                }
            }
            final double sigma = Math.sqrt(sumMeanDiffSq / n);
            if (sigma > 0) {
                double accum = 0;
                for (final double d : data) {
                    final double md = (d - mean) / sigma;
                    accum += md * md * md;
                }
                double g1 = accum / n;
                final double sg1 = Math.sqrt(6.0 * (n - 2) / ((n + 1.0) * (n + 3)));
                return (max - min) / (1.0 + MathUtils.log2(count(data)) + MathUtils.log2(1.0 + abs(g1) / sg1));
            }
        }
        return 0.0;
    }

    static double numpyAutoBinWidthEstimator(final double... data) {
        final double fdBW = freedmanDiaconisBinWidthEstimator(data);
        final double sturgesBW = sturgesBinWidthEstimator(data);
        return fdBW == 0 ? sturgesBW : Math.min(sturgesBW, fdBW);
    }

    static double freedmanDiaconisBinWidthEstimator(final double... data) {
        final double[] q3q1 = percentiles(new double[]{75, 25}, Interpolation.LINEAR, data);
        return 2.0 * (q3q1[0] - q3q1[1]) * Math.pow(data.length, -1. / 3);
    }

    /**
     * @param values the values to check
     * @return whether any of the values are NaN
     */
    public static boolean containsNaN(double[] values) {
        for (final double v : values) {
            if (Double.isNaN(v)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param values the values to check
     * @return whether any of the values are non-finite
     */
    public static boolean containsNonFinite(double[] values) {
        for (final double v : values) {
            if (!Double.isFinite(v)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param values the index to value getter
     * @param size   the size of the source where the getter comes from
     * @return whether any of the values are NaN
     */
    public static boolean containsNaN(IntToDoubleFunction values, int size) {
        for (int i = 0; i < size; ++i) {
            final double v = values.applyAsDouble(i);
            if (Double.isNaN(v)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param values the index to value getter
     * @param size   the size of the source where the getter comes from
     * @return whether any of the values are non-finite
     */
    public static boolean containsNonFinite(IntToDoubleFunction values, int size) {
        for (int i = 0; i < size; ++i) {
            final double v = values.applyAsDouble(i);
            if (!Double.isFinite(v)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param values the values to check
     * @return whether all of the values are the same in the values
     */
    public static boolean isConstant(double[] values) {
        double v = values[0];
        for (int i = 1; i < values.length; ++i) {
            if (v != values[i]) {
                return false;
            }
        }
        return true;
    }

    /**
     * @param values the index to value getter
     * @param size   the size of the source where the getter comes from
     * @return whether all of the values are the same in the values
     */
    public static boolean isConstant(IntToDoubleFunction values, int size) {
        double v = values.applyAsDouble(0);
        for (int i = 1; i < size; ++i) {
            if (v != values.applyAsDouble(i)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Compare the skewness of these values with that of a normal distribution.
     * The method is adapted from SciPy
     *
     * @param values the values to check
     * @return a named-tuple containing the Z-statistic ("statistic") and p-value ("p-value")
     */
    public static Statistic skewTest(double[] values) {
        double b2 = skewness(values);
        int n = values.length;
        if (n < 8) {
            throw new ArithmeticException("Skew test is not valid with fewer than 8 samples");
        }
        double y = b2 * Math.sqrt(((n + 1) * (n + 3)) / (6.0 * (n - 2)));
        double beta2 = (3.0 * (n * n + 27 * n - 70) * (n + 1) * (n + 3) /
                ((n - 2.0) * (n + 5) * (n + 7) * (n + 9)));
        double W2 = -1 + Math.sqrt(2 * (beta2 - 1));
        double delta = 1 / Math.sqrt(0.5 * Math.log(W2));
        double alpha = Math.sqrt(2.0 / (W2 - 1));

        y = y == 0 ? 1 : y;

        double Z = delta * Math.log(y / alpha + Math.sqrt(Math.pow(y / alpha, 2) + 1));
        return new Statistic("statistic", Z, 2 * NormalDistributions.SF(Math.abs(Z)));
    }

}
