package net.mahdilamb.stats;


import net.mahdilamb.dataframe.functions.BiBooleanPredicate;

import java.util.Arrays;
import java.util.function.*;

/**
 * Utility methods for working with array
 */
public final strictfp class ArrayUtils {
    private ArrayUtils() {
    }

    /**
     * Reverse the order of a double array
     *
     * @param data the data to reverse the order
     * @return a copy of the data, reversed
     */
    public static double[] flip(final double[] data) {
        final double[] out = new double[data.length];
        for (int i = data.length - 1, j = 0; i >= 0; --i, ++j) {
            out[j] = data[i];
        }
        return out;
    }

    /**
     * Reverse the order of a double array
     *
     * @param data the data to reverse the order
     * @return the original data
     */
    public static double[] flipInPlace(final double[] data) {
        if (data.length == 1) {
            return data;
        }
        int mid = data.length >> 1;
        for (int i = 0, j = data.length - 1; i < mid; ++i, --j) {
            double tmp = data[i];
            data[i] = data[j];
            data[j] = tmp;
        }
        return data;
    }

    /**
     * Roll the data (i.e. shift the elements along)
     *
     * @param shift the amount to shift by
     * @param data  the data to shift
     * @return a copy of the data, rolled
     */
    public static double[] roll(int shift, final double[] data) {
        if (shift == 0) {
            return data.clone();
        }
        final double[] out = new double[data.length];
        if (shift > 0) {
            for (int j = 0; j < data.length; ++j) {
                int k = j - shift;
                out[j] = data[k < 0 ? data.length + k : k];
            }
        } else {
            for (int j = 0; j < data.length; ++j) {
                int k = j - shift;
                out[j] = data[k >= data.length ? k - data.length : k];
            }
        }
        return out;
    }

    /**
     * Apply a function to a subset of the array
     *
     * @param out       the output array
     * @param condition the condition to satisfy in order for the function to be applied
     * @param func      the function to apply to elements that satisfy the condition
     * @param data      the data to apply the function to
     * @return the output array
     * @throws ArrayIndexOutOfBoundsException if the length of out is less than the length of data
     */
    public static double[] where(double[] out, DoublePredicate condition, final DoubleUnaryOperator func, double[] data) {
        for (int i = 0; i < data.length; ++i) {
            if (condition.test(data[i])) {
                out[i] = func.applyAsDouble(data[i]);
            }
            out[i] = data[i];
        }
        return out;
    }

    /**
     * Apply a function to a subset of the array
     *
     * @param condition the condition to satisfy in order for the function to be applied
     * @param func      the function to apply to elements that satisfy the condition
     * @param data      the data to apply the function to
     * @return a copy of the input array, with a function applied to a subset of the data
     */
    public static double[] where(DoublePredicate condition, final DoubleUnaryOperator func, double[] data) {
        return where(new double[data.length], condition, func, data);
    }

    /**
     * Apply a function to a subset of the array
     *
     * @param out       the output array
     * @param condition the condition to satisfy in order for the function to be applied
     * @param func      the function to apply to elements that satisfy the condition
     * @param data      the data to apply the function to
     * @return the output array
     * @throws ArrayIndexOutOfBoundsException if the length of out is less than the length of data
     */
    public static long[] where(long[] out, LongPredicate condition, final LongUnaryOperator func, long[] data) {
        for (int i = 0; i < data.length; ++i) {
            if (condition.test(data[i])) {
                out[i] = func.applyAsLong(data[i]);
            }
            out[i] = data[i];
        }
        return out;
    }

    /**
     * Apply a function to a subset of the array
     *
     * @param condition the condition to satisfy in order for the function to be applied
     * @param func      the function to apply to elements that satisfy the condition
     * @param data      the data to apply the function to
     * @return a copy of the input array, with a function applied to a subset of the data
     */
    public static long[] where(LongPredicate condition, final LongUnaryOperator func, long[] data) {
        return where(new long[data.length], condition, func, data);
    }

    /**
     * Apply a function to a subset of the array
     *
     * @param out       the output array
     * @param condition the condition to satisfy in order for the function to be applied
     * @param func      the function to apply to elements that satisfy the condition
     * @param data      the data to apply the function to
     * @return the output array
     * @throws ArrayIndexOutOfBoundsException if the length of out is less than the length of data
     */
    public static int[] where(int[] out, IntPredicate condition, final IntUnaryOperator func, int[] data) {
        for (int i = 0; i < data.length; ++i) {
            if (condition.test(data[i])) {
                out[i] = func.applyAsInt(data[i]);
            }
            out[i] = data[i];
        }
        return out;
    }

    /**
     * Apply a function to a subset of the array
     *
     * @param condition the condition to satisfy in order for the function to be applied
     * @param func      the function to apply to elements that satisfy the condition
     * @param data      the data to apply the function to
     * @return a copy of the input array, with a function applied to a subset of the data
     */
    public static int[] where(IntPredicate condition, final IntUnaryOperator func, int[] data) {
        return where(new int[data.length], condition, func, data);
    }

    /**
     * Return whether any of the elements in the array are true
     *
     * @param data the data to check
     * @return whether any of the elements in the data are true
     */
    public static boolean any(boolean[] data) {
        for (final boolean d : data) {
            if (d) {
                return true;
            }
        }
        return false;
    }

    /**
     * Return whether all the elements in the array are true
     *
     * @param data the data to check
     * @return whether all of the elements in the data are true
     */
    public static boolean all(boolean[] data) {
        for (final boolean d : data) {
            if (!d) {
                return false;
            }
        }
        return true;
    }

    /**
     * Map a function to each element of a double array
     *
     * @param left the left handside operand in the function
     * @param func the function to apply
     * @param data the data to apply the function to
     * @return a copy of the data with the function applied
     */
    public static double[] map(double left, DoubleBinaryOperator func, final double[] data) {
        final double[] out = new double[data.length];
        for (int i = 0; i < data.length; ++i) {
            out[i] = func.applyAsDouble(left, data[i]);
        }
        return out;
    }

    /**
     * Test a condition on each element of a double array
     *
     * @param func the predicate function
     * @param data the data to apply the function to
     * @return a boolean array with the same legth of data
     */
    public static boolean[] mapToBool(DoublePredicate func, final double[] data) {
        final boolean[] out = new boolean[data.length];
        for (int i = 0; i < data.length; ++i) {
            out[i] = func.test(data[i]);
        }
        return out;
    }

    /**
     * Map a function to each element in the data
     *
     * @param func  the function to apply
     * @param right the right hand operand in the operation
     * @param data  the data to apply the function to
     * @return a new array with the result of applying the function
     */
    public static double[] map(DoubleBinaryOperator func, double right, final double[] data) {
        final double[] out = new double[data.length];
        for (int i = 0; i < data.length; ++i) {
            out[i] = func.applyAsDouble(data[i], right);
        }
        return out;
    }

    /**
     * Reduce a data series to a single value using a function
     *
     * @param func the reduction function
     * @param data the data to apply the function to
     * @return the result of reducing the data with a function
     */
    public static double reduce(DoubleBinaryOperator func, final double[] data) {
        double out = data[0];
        if (data.length == 1) {
            return out;
        }
        for (int i = 1; i < data.length; ++i) {
            out = func.applyAsDouble(out, data[i]);
        }
        return out;
    }

    /**
     * Reduce a data series to a single value using a function. The iteration stops if the reduction value reaches the
     * sentinel
     *
     * @param func     the reduction function
     * @param data     the data to apply the function to
     * @param sentinel the value to use as the early exit value (i.e. the loop ceases if the reduction value reaches
     *                 this)
     * @return the result of reducing the data with a function
     */
    public static double reduce(double sentinel, DoubleBinaryOperator func, final double[] data) {
        double out = data[0];
        if (data.length == 1) {
            return out;
        }
        for (int i = 1; i < data.length; ++i) {
            out = func.applyAsDouble(out, data[i]);
            if (out == sentinel) {
                return sentinel;
            }
        }
        return out;
    }

    /**
     * Reduce a data series to a single value using a function. The iteration stops if the reduction value reaches the
     * sentinel
     *
     * @param func     the reduction function
     * @param data     the data to apply the function to
     * @param sentinel the value to use as the early exit value (i.e. the loop ceases if the reduction value reaches
     *                 this)
     * @return the result of reducing the data with a function
     */
    public static boolean reduce(boolean sentinel, BiBooleanPredicate func, final boolean[] data) {
        boolean out = data[0];
        if (data.length == 1) {
            return out;
        }
        for (int i = 1; i < data.length; ++i) {
            out = func.test(out, data[i]);
            if (out == sentinel) {
                return sentinel;
            }
        }
        return out;
    }

    /**
     * Reduce a data series to a single value using a function
     *
     * @param func the reduction function
     * @param data the data to apply the function to
     * @return the result of reducing the data with a function
     */
    public static boolean reduce(BiBooleanPredicate func, final boolean[] data) {
        boolean out = data[0];
        if (data.length == 1) {
            return out;
        }
        for (int i = 1; i < data.length; ++i) {
            out = func.test(out, data[i]);
        }
        return out;
    }

    /**
     * Map a function to each element of the data and store the value in an output array
     *
     * @param out  the output array
     * @param data the data to apply the operation to
     * @param func the function to apply
     * @return the output array
     */
    public static double[] map(final double[] out, final double[] data, DoubleUnaryOperator func) {
        for (int i = 0; i < data.length; ++i) {
            out[i] = func.applyAsDouble(data[i]);
        }
        return out;
    }

    /**
     * Map a function to each element of the data and store the value in an output int array
     *
     * @param data the data to apply the operation to
     * @param func the function to apply
     * @return a new array of the same length as the data
     */
    public static int[] mapToInt(final double[] data, DoubleToIntFunction func) {
        final int[] out = new int[data.length];
        for (int i = 0; i < data.length; ++i) {
            out[i] = func.applyAsInt(data[i]);
        }
        return out;
    }

    /**
     * Map a function to each element of the data and store the value in an output long array
     *
     * @param data the data to apply the operation to
     * @param func the function to apply
     * @return a new array of the same length as the data
     */
    public static long[] mapToLong(final double[] data, DoubleToLongFunction func) {
        final long[] out = new long[data.length];
        for (int i = 0; i < data.length; ++i) {
            out[i] = func.applyAsLong(data[i]);
        }
        return out;
    }

    /**
     * Map a function to each element of the data and return a new array with the function
     * applied
     *
     * @param data the data to apply the operation to
     * @param func the function to apply
     * @return a new array of the same length as the data
     */
    public static double[] map(final double[] data, DoubleUnaryOperator func) {
        final double[] out = new double[data.length];
        return map(out, data, func);
    }

    /**
     * Create an array with the same value repeated
     *
     * @param value the value
     * @param n     the number of elements
     * @return an array filled with the value of size n
     */
    public static double[] full(final double value, int n) {
        final double[] out = new double[n];
        Arrays.fill(out, value);
        return out;
    }

    /**
     * Fill a 2D array with doubles
     *
     * @param out      the output array
     * @param supplier the supplier of the values
     * @return the output array with new values
     */
    public static double[][] full(double[][] out, DoubleSupplier supplier) {
        for (int i = 0; i < out.length; ++i) {
            for (int j = 0; j < out[i].length; ++j) {
                out[i][j] = supplier.getAsDouble();
            }
        }
        return out;
    }

    /**
     * Create an array of the same int repeated
     *
     * @param value the values to repeat
     * @param n     the number of times to repeat
     * @return an array with the same int repeated
     */
    public static int[] full(final int value, int n) {
        final int[] out = new int[n];
        Arrays.fill(out, value);
        return out;
    }

    /**
     * Fill an array with doubles
     *
     * @param out   the output array
     * @param value the supplier of the values
     * @return the output array with new values
     */
    public static double[] full(double[] out, DoubleSupplier value) {
        for (int i = 0; i < out.length; ++i) {
            out[i] = value.getAsDouble();
        }
        return out;
    }

    /**
     * Fill an array using a supplier
     *
     * @param supplier the supplier
     * @param n        the number of elements
     * @return an array of n elements, filled with the supplier
     */
    public static double[] full(DoubleSupplier supplier, int n) {
        return full(new double[n], supplier);
    }

    /**
     * Create an int array with the specified range
     *
     * @param start the starting point of the array
     * @param stop  the stopping point of the array
     * @param step  the size of the stop
     * @return an array with the specified range
     */
    public static int[] intRange(int start, int stop, int step) {
        final int[] out = new int[stop - start];
        for (int i = start; i < stop; i += step) {
            out[i] = i;
        }
        return out;
    }

    /**
     * Create an int array with the specified range, increasing by 1
     *
     * @param start the starting point of the array
     * @param stop  the stopping point of the array
     * @return an array with the specified range
     */
    public static int[] intRange(int start, int stop) {
        final int[] out = new int[stop - start];
        for (int i = start; i < stop; ++i) {
            out[i] = i;
        }
        return out;
    }

    /**
     * Create a range from 0 to the value
     *
     * @param value the value to end at
     * @return an integer array of the given range
     */
    public static int[] intRange(int value) {
        final int[] out = new int[value];
        for (int i = 0; i < value; ++i) {
            out[i] = i;
        }
        return out;
    }

    /**
     * Create an array with equal steps. There is no check for start {@literal <} stop.
     *
     * @param start the start point
     * @param stop  the end point
     * @param step  the size of the steps
     * @return a range from start to stop, with the given step sizel
     */
    public static double[] range(double start, double stop, double step) {
        assert start < stop;
        assert step != 0;
        final double[] out = new double[(int) (Math.ceil(stop - start) / step)];
        int j = 0;
        for (double i = 0; j < out.length; i += step) {
            out[j++] = i + start;
        }
        return out;
    }

    /**
     * Create an array containing a range
     *
     * @param start the start point
     * @param stop  the stop point
     * @return an array containing a linear series from start and including stop
     */
    public static double[] range(double start, double stop) {
        final double[] out = new double[(int) Math.ceil(stop - start)];
        for (int i = 0; i < out.length; ++i) {
            out[i] = i + start;
        }
        return out;
    }

    /**
     * Create an array of doubles starting form 0 and ending at the ceiling of the value
     *
     * @param value the requested end point (actual endpoint is ceiling of this value)
     * @return a array of doubles with a range of values from 0 to ceil(value)
     */
    public static double[] range(double value) {
        final double[] out = new double[(int) Math.ceil(value)];
        for (int i = 0; i < out.length; ++i) {
            out[i] = i;
        }
        return out;
    }

    /**
     * Create a linearly spaced array
     *
     * @param stopInclusive whether to include the stop value
     * @param start         the start point
     * @param stop          the stop point
     * @param num           the number of elements in the output array
     * @return a double array of length num
     */
    public static double[] linearlySpaced(boolean stopInclusive, double start, double stop, int num) {
        double step = (stop - start) / (stopInclusive ? (num - 1) : num);
        final double[] out = new double[num];
        for (int i = 0; i < num; ++i) {
            out[i] = Math.fma(i, step, start);
        }
        return out;
    }

    /**
     * Create a linearly spaced array, including the stop point
     *
     * @param start the start point
     * @param stop  the stop point
     * @param num   the number of elements in the output array
     * @return a double array of length num
     */
    public static double[] linearlySpaced(double start, double stop, int num) {
        return linearlySpaced(true, start, stop, num);
    }

    /**
     * Get a range of values that are logarithmically spaced
     *
     * @param base          the base for the logarithm
     * @param stopInclusive whether to include the stop value
     * @param start         the start exponent
     * @param stop          the end end exponent
     * @param num           the number of steps
     * @return an array containing a logarithmically spaced series of values
     */
    public static double[] logarithmicallySpaced(double base, boolean stopInclusive, double start, double stop, int num) {
        double step = (stop - start) / (stopInclusive ? (num - 1) : num);
        final double[] out = new double[num];
        for (int i = 0; i < num; ++i) {
            out[i] = Math.pow(base, Math.fma(i, step, start));
        }

        return out;
    }

    /**
     * Get a range of values that are logarithmically spaced, and includes the stop values
     *
     * @param base  the base for the logarithm
     * @param start the start exponent
     * @param stop  the end end exponent
     * @param num   the number of steps
     * @return an array containing a logarithmically spaced series of values
     */
    public static double[] logarithmicallySpaced(double base, double start, double stop, int num) {
        return logarithmicallySpaced(base, true, start, stop, num);
    }

    /**
     * Get a range of values (base 10) that are logarithmically spaced, and includes the stop values
     *
     * @param start the start exponent
     * @param stop  the end end exponent
     * @param num   the number of steps
     * @return an array containing a logarithmically spaced series of values
     */
    public static double[] logarithmicallySpaced(double start, double stop, int num) {
        return logarithmicallySpaced(10, start, stop, num);
    }

    /**
     * Clip each value in the data between min (if not {@code null}) and max ({@code null}})
     *
     * @param min  the minimum value (ignored if @code null})
     * @param max  the maximum value (ignored if @code null})
     * @param data the data to clip
     * @return a copy of the data, clipped within the specified range
     */
    public static double[] clip(final Double min, final Double max, double[] data) {
        final double[] out = new double[data.length];
        for (int i = 0; i < data.length; ++i) {
            if (min != null && data[i] < min) {
                out[i] = min;
                continue;
            }
            if (max != null && data[i] > max) {
                out[i] = max;
                continue;
            }
            out[i] = data[i];
        }
        return out;
    }

    /**
     * Find the index where a value can be inserted in a sorted array and the array
     * will remain sorted. Code adapted from CPython.
     *
     * @param a    the value to insert
     * @param lo   the low end of the range
     * @param hi   the high end of the range
     * @param data the sorted data
     * @return the index that a could be inserted and maintain sort in the range lo-hi
     */
    public static int bisectLeftRange(final double a, int lo, int hi, final double[] data) {
        if (lo < 0) {
            throw new IllegalArgumentException("lo must be non-negative");
        }
        while (lo < hi) {
            int mid = (lo + hi) >> 1;
            if (data[mid] < a) {
                lo = mid + 1;
            } else {
                hi = mid;
            }
        }
        return lo;
    }

    /**
     * Find the index where a value can be inserted in a sorted array and the array
     * will remain sorted. Code adapted from CPython.
     *
     * @param a    the value to insert
     * @param data the sorted data
     * @return the index that a could be inserted and maintain sort in the range lo-hi
     */
    public static int bisectLeft(final double a, final double[] data) {
        return bisectLeftRange(a, 0, data.length, data);
    }

    /**
     * Find the index where a value can be inserted in the given range of a sorted array and the array
     * will remain sorted. Code adapted from CPython.
     *
     * @param a    the value to insert
     * @param lo   the low end of the range
     * @param hi   the high end of the range
     * @param data the sorted data
     * @return the index that a could be inserted and maintain sort in the range lo-hi
     */
    public static int bisectRightRange(final double a, int lo, int hi, final double[] data) {
        if (lo < 0) {
            throw new IllegalArgumentException("lo must be non-negative");
        }
        while (lo < hi) {
            int mid = (lo + hi) >> 1;
            if (a < data[mid]) {
                hi = mid;

            } else {
                lo = mid + 1;
            }
        }
        return lo;
    }

    /**
     * Find the index where a value can be inserted in a sorted array and the array
     * will remain sorted. Code adapted from CPython.
     *
     * @param a    the value to insert
     * @param data the sorted data
     * @return the index that a could be inserted and maintain sort in the range lo-hi
     */
    public static int bisectRight(final double a, final double[] data) {
        return bisectRightRange(a, 0, data.length, data);
    }

    /**
     * Concatenate two double arrays to a 1 dimensional array
     *
     * @param data  the left hand side data
     * @param right the right hand side data
     * @return a new array containing the copied elements
     */
    public static double[] concatenate(final double[] data, double[] right) {
        final double[] out = Arrays.copyOf(data, data.length + right.length);
        System.arraycopy(right, 0, out, data.length, right.length);
        return out;
    }

    /**
     * Get a subset of boolean data depending on whether they are true in the boolean array
     *
     * @param indices the boolean array to use subset the data
     * @param data    the data to subset
     * @return a double area of the same length as indices
     */
    public static double[] subset(final boolean[] indices, final double[] data) {
        final double[] out = new double[indices.length];
        int j = 0;
        for (int i = 0; i < out.length; ++i) {
            if (indices[i]) {
                out[j++] = data[i];
            }
        }
        double[] clipped = new double[j];
        System.arraycopy(out, 0, clipped, 0, j);
        return clipped;
    }

    /**
     * Create a new array with a subset of the data based on the indices specified
     *
     * @param indices the indices of interest
     * @param data    the data to subset
     * @return a subset of the data. The output array will be the same length as the data
     */
    public static double[] subset(final int[] indices, final double[] data) {
        final double[] out = new double[indices.length];
        int j = 0;
        for (final int i : indices) {
            out[j++] = data[i];
        }
        return out;
    }

    /**
     * Create a new array with a subset of the data based on the indices specified
     *
     * @param indices the indices of interest
     * @param data    the data to subset
     * @return a subset of the data. The output array will be the same length as the data
     */
    public static int[] subset(boolean[] indices, int[] data) {
        final int[] out = new int[indices.length];
        int j = 0;
        for (int i = 0; i < out.length; ++i) {
            if (indices[i]) {
                out[j++] = data[i];
            }
        }
        int[] clipped = new int[j];
        System.arraycopy(out, 0, clipped, 0, j);
        return clipped;
    }

    /**
     * Apply a function to a subset of the data
     *
     * @param out      the output array
     * @param subset   the array containing the true/false values to decide whether the values are inclusive
     * @param function the function to apply if the index of the subset is true
     * @param data     the input data
     * @return the output data
     */
    static int[] where(int[] out, boolean[] subset, IntUnaryOperator function, int[] data) {
        for (int i = 0; i < out.length; ++i) {
            if (subset[i]) {
                out[i] = function.applyAsInt(data[i]);
            }
        }
        return out;
    }
}
