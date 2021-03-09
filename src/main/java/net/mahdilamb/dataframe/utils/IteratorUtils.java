package net.mahdilamb.dataframe.utils;

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

}
