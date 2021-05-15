package net.mahdilamb.stats;

import net.mahdilamb.stats.utils.Bin;
import net.mahdilamb.stats.utils.BinEdges;

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
    default BinEdges getBin(int bin) {
        return new BinEdges(getBinEdges()[bin], getBinEdges()[bin + 1]);
    }

    /**
     * @return the number of bins in the histogram
     */
    default int numBins() {
        return getCount().length;
    }

    /**
     * @return an iterable over the bins
     */
    default Iterable<Bin> bins() {
        return () -> new Iterator<Bin>() {
            private int i = 0;

            @Override
            public boolean hasNext() {
                return i < getCount().length;
            }

            @Override
            public Bin next() {
                return new Bin(getBinEdges()[i], getBinEdges()[++i], getCount()[i - 1]);
            }
        };
    }


}
