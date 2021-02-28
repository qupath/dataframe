package net.mahdilamb.statistics;

import java.util.function.ToDoubleFunction;

/**
 * Stats object to enable caching of stats values and use more rigorous error checking (compared to
 * utility methods in {@link DensityHistogram}). Objects are assumed to be immutable.
 */
public interface Stats extends Iterable<Double> {
    /**
     * @return the number of elements in the stats series
     */
    default long getCount() {
        return StatUtils.count(this, Number::doubleValue);
    }

    /**
     * @return the sum of elements in the stats series
     */
    default double getSum() {
        return StatUtils.sum(this, Number::doubleValue);
    }

    /**
     * @return the mean of elements in the stats series
     */
    default double getMean() {
        return StatUtils.mean(this, Number::doubleValue);
    }

    /**
     * @return the minimum value in the stats series. If all elements are null, {@code NaN} is returned
     */
    default double getMin() {
        return StatUtils.min(this, Number::doubleValue);
    }

    /**
     * @return the maximum value in the stats series. If all elements are null, {@code NaN} is returned
     */
    default double getMax() {
        return StatUtils.max(this, Number::doubleValue);
    }

    /**
     * @return the range of the values in the stats series. If all elements are null, 0 is returned
     */
    default double getRange() {
        return StatUtils.range(this, Number::doubleValue);
    }

    /**
     * Create a stats series from an array of doubles
     *
     * @param data the array of doubles
     * @return a stats series
     */
    static Stats from(final double... data) {
        return new AbstractStats.OfDoubleArray(data);
    }

    /**
     * Create a stats series from an iterable of objects and a number extractor
     *
     * @param data      the data containing numeric values
     * @param converter the numeric extractor function
     * @param <T>       the type of the objects in the iterable
     * @return a stats object
     */
    static <T> Stats from(Iterable<? extends T> data, ToDoubleFunction<T> converter) {
        return new AbstractStats.OfNumericConvertible<>(data, converter);
    }

    /**
     * Create a stats series from an iterable of numeric objects
     *
     * @param data the data containing numeric values
     * @return a stats object
     */
    static Stats from(Iterable<? extends Number> data) {
        return new AbstractStats.OfNumericConvertible<>(data, Number::doubleValue);
    }
}
