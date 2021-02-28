package net.mahdilamb.statistics;

import net.mahdilamb.utils.tuples.NamedDoubleTuple;
import net.mahdilamb.utils.tuples.NamedGenericTuple;
import net.mahdilamb.utils.tuples.Tuple;

import java.util.Iterator;

/**
 * A histogram with a count associated with each bin
 */
public interface Histogram {
    /**
     * @return an array of the counts
     */
    int[] getCount();

    /**
     * @return an array of the bin edges
     */
    double[] getBinEdges();

    /**
     * Get the count at a specified bin
     *
     * @param bin the bin
     * @return the count at the bin
     */
    default int getCount(int bin) {
        return getCount()[bin];
    }

    /**
     * Get a tuple representing a specific bin
     *
     * @param bin the bin
     * @return a tuple representing the bin
     */
    default NamedDoubleTuple getBin(int bin) {
        return Tuple.namedTuple(Tuple.of(getBinEdges()[bin], getBinEdges()[bin + 1]), "min", "max");
    }

    /**
     * @return the number of bins in the histogram
     */
    default int numBins() {
        return getCount().length;
    }

    default Iterable<NamedGenericTuple<?>> bins(){
        return () -> new Iterator<>() {
            private int i = 0;

            @Override
            public boolean hasNext() {
                return i < getCount().length;
            }

            @Override
            public NamedGenericTuple<?> next() {
                return Tuple.namedTuple(Tuple.of((Object) getBinEdges()[i], getBinEdges()[++i], getCount()[i - 1]), "min", "max", "count");
            }
        };
    }


}
