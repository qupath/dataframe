package net.mahdilamb.dataframe;


import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.function.IntPredicate;
import java.util.function.IntToDoubleFunction;

/**
 * A series of values
 */
public interface Series<T> extends Iterable<T> {
    /**
     * The default number of rows to display when printing to the console
     */
    int DEFAULT_DISPLAY_ROWS = 25;

    /**
     * Get the value at a specific index
     *
     * @param index the index
     * @return the value of interest. Note for numeric types, this will result in boxing of the primitive type
     */
    T get(int index);

    /**
     * @return the type of the series
     */
    DataType getType();

    /**
     * @return the size of the series
     */
    int size();

    /**
     * @param index the index
     * @return the associated ID at the given index
     */
    int getID(int index);

    /**
     * @return the name of the series
     */
    String getName();

    /**
     * @return a version of this series as a string series
     */
    StringSeries asString();

    /**
     * @return a version of this series as a double series
     */
    DoubleSeries asDouble();

    /**
     * @return a version of this series as a long series
     */
    LongSeries asLong();

    /**
     * @return this series as a boolean series
     */
    BooleanSeries asBoolean();

    /**
     * Create an array from this series
     *
     * @param output the output array
     * @return either the supplied array (if of the correct size) or another array containing the values of this series
     */
    default T[] toArray(T[] output) {
        if (output.length != size()) {
            output = Arrays.copyOf(output, size());
        }
        for (int i = 0; i < size(); ++i) {
            output[i] = get(i);
        }
        return output;
    }

    /**
     * @return the counts of each of the elements in the series
     */
    DataFrame valueCounts();

    /**
     * @return the frequency of each of the elements in the series
     */
    DataFrame frequencies();

    /**
     * Create a string series from an array of strings
     *
     * @param name   the name of the series
     * @param values the values of the series
     * @return a string series of the values
     */
    static StringSeries of(final String name, final String... values) {
        return new SeriesImpl.OfStringArray(name, values);
    }

    /**
     * Factory method to create a series from double data
     *
     * @param name the name of the series
     * @param data the data to use in the series
     * @return the default series wrapping the data
     */
    static DoubleSeries of(final String name, double... data) {
        return new SeriesImpl.OfDoubleArray(name, data);
    }

    /**
     * Factory method to create a series from long data
     *
     * @param name the name of the series
     * @param data the data to use in the series
     * @return the default series wrapping the data
     */
    static LongSeries of(final String name, long... data) {
        return new SeriesImpl.OfNonNaNLongArray(name, data);
    }

    /**
     * Factory method to create a series from a "collection" of objects.
     * <p>
     * This enables the generation of a series from a collection of objects without having to implement the interface
     *
     * @param name       the name of the series
     * @param dataGetter a function which gets a double element at a position in the series
     * @param size       the size of the series
     * @return a series from a collection of objects
     */
    static DoubleSeries of(final String name, IntToDoubleFunction dataGetter, int size) {
        return new SeriesImpl.OfFunctionalDouble(name, size, dataGetter);
    }

    /**
     * Get a subset of this series
     *
     * @param start the start index (inclusive)
     * @param end   the end index (exclusive)
     * @return a sliced view into this data series
     */
    Series<T> subset(int start, int end);

    /**
     * Get the first n elements
     *
     * @param n the number of elements
     * @return a sliced view into this data series
     */
    default Series<T> head(int n) {
        return subset(0, n);
    }

    /**
     * @return a sliced view of the first 5 elements in the series
     */
    default Series<T> head() {
        return head(5);
    }

    /**
     * Get the last n elements
     *
     * @param n the number of elements
     * @return a sliced view into this data series
     */
    default Series<T> tail(int n) {
        return subset(size() - n, size());
    }

    /**
     * @return a sliced view of the last 5 elements in the series
     */
    default Series<T> tail() {
        return tail(5);
    }

    /**
     * Get a subset of this series by doing a test on the indices of the series
     *
     * @param test the test on the indices
     * @return a sliced view of this series based on the test
     */
    Series<T> subset(IntPredicate test);

    /**
     * Sort the values in this series as either ascending or descending
     *
     * @param ascending whether to sort ascending
     * @return a view of this series, sorted
     */
    Series<T> sort(boolean ascending);

    /**
     * Sort the values in this series
     *
     * @return a view of this series, sorted
     */
    default Series<T> sort() {
        return sort(true);
    }

    /**
     * Output the series to the console
     *
     * @param maxRows the maximum number of rows to show
     * @return a string representation of this series
     */
    String toString(int maxRows);

    @Override
    default Iterator<T> iterator() {
        return new Iterator<>() {
            private int i = 0;

            @Override
            public boolean hasNext() {
                return i < size();
            }

            @Override
            public T next() {
                return get(i++);
            }
        };
    }

}
