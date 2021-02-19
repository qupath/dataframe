package net.mahdilamb.charts.dataframe.utils;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.IntBinaryOperator;
import java.util.function.IntConsumer;

public final class IteratorUtils {
    private IteratorUtils() {

    }

    /**
     * An iterator over an int array that shows only a max number of elements
     *
     * @param func        the function to apply to a non-skipped element. A -1 is used to show that an element has been skipped
     * @param numElements the number of elements to iterate over
     * @param maxShow     the maximum elements to show.  (ideally an odd number)
     */
    public static void skippedIterator(IntConsumer func, final int numElements, int maxShow) {
        if (numElements <= maxShow) {
            for (int i = 0; i < numElements; ++i) {
                func.accept(i);
            }
        } else {
            final int halfWay = (maxShow >>> 1);
            for (int i = 0; i < numElements; ++i) {
                if (i == halfWay) {
                    func.accept(-1);
                    i = numElements - halfWay - 1;
                    continue;
                }
                func.accept(i);

            }
        }
    }

    /**
     * A reduction operator for a skipped iterator.
     * <p>
     * The right hand operand in the operation function is the iterated value
     *
     * @param start       the start value for reduction
     * @param reduce      the reduction function (right hand operand is new value)
     * @param numElements the number of elements of the source to iterate
     * @param maxShow     the maximum number of values to iterator over
     * @return the reduced value
     */
    public static int skippedIterator(int start, IntBinaryOperator reduce, final int numElements, int maxShow) {
        if (numElements <= maxShow) {
            for (int i = 0; i < numElements; ++i) {
                start = reduce.applyAsInt(start, i);
            }
        } else {
            final int halfWay = (maxShow >>> 1);
            for (int i = 0; i < numElements; ++i) {
                if (i == halfWay) {
                    reduce.applyAsInt(start, -1);
                    i = numElements - halfWay - 1;
                    continue;
                }
                start = reduce.applyAsInt(start, i);

            }
        }
        return start;
    }

    /**
     * Iterate as python allows for slicing
     *
     * @param func             the consumer to use e.g. if using a List {@code List::get}
     * @param sourceIterLength the length of the source collection
     * @param start            the start index of the slice
     * @param stop             the stop index of the slice
     * @param step             the step of the slice
     */
    public static void slicedIterator(IntConsumer func, int sourceIterLength, int start, int stop, int step) {
        if (start == stop) {
            return;
        }
        if (step == 0) {
            throw new IllegalArgumentException("step cannot be 0");
        }
        start = start < 0 ? (sourceIterLength + start) : start;
        stop = stop < 0 ? (sourceIterLength + stop) : stop;
        if (step < 0) {
            if (start > stop) {
                throw new IllegalArgumentException("Step is facing the wrong direction");
            }
            do {
                func.accept(stop += step);
            } while (stop > start);

        } else {
            if (start > stop) {
                throw new IllegalArgumentException("Step is facing the wrong direction");
            }
            while (start < stop) {
                func.accept(start);
                start += step;
            }
        }
    }

    public static <T> void slice(Consumer<T> func, List<T> list, int start, int stop, int step) {
        slicedIterator(i -> func.accept(list.get(i)), list.size(), start, stop, step);
    }

    private static int parse(final String slice, int start, int end, int d) {
        int i = end - 1;
        if (slice.charAt(i) == ':') {
            if (slice.charAt(start) == ':' && i == start) {
                return d;
            }
            if (slice.charAt(i) == ':') {
                --end;
            }
        }
        int e = 1, j = 0;
        while (i >= start) {
            final char c = slice.charAt(i--);
            if (c == ':') {
                continue;
            }
            if (c == '-') {
                return -j;
            }
            j += e * (c - '0');
            e *= 10;
        }
        return j;
    }

    public static <T> void slice(Consumer<T> func, List<T> list, final String slice) {
        int i = 0, tokenStart = 0, j = 0;
        int start = 0, stop = list.size(), step = 1;
        while (i < slice.length()) {
            final char c = slice.charAt(i++);
            if (c == ':') {
                switch (j) {
                    case 0:
                        start = parse(slice, tokenStart, i, 0);
                        break;
                    case 1:
                        stop = parse(slice, tokenStart, i, list.size());
                        break;
                    case 2:
                        step = parse(slice, tokenStart, i, 1);
                        break;
                    default:
                        throw new IllegalArgumentException();
                }
                tokenStart = i;
                ++j;
            }
        }
        if (tokenStart < slice.length()) {
            switch (j) {
                case 0:
                    start = parse(slice, tokenStart, slice.length(), 0);
                    break;
                case 1:
                    stop = parse(slice, tokenStart, slice.length(), list.size());
                    break;
                case 2:
                    step = parse(slice, tokenStart, slice.length(), 1);
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
        slicedIterator(o -> func.accept(list.get(o)), list.size(), start, stop, step);
    }


}
