package net.mahdilamb.stats;

import net.mahdilamb.stats.utils.DensityBin;

import java.util.Iterator;

/**
 * A histogram where a density is associated with each of the bin edges
 */
public interface DensityHistogram extends Histogram {
    /**
     * @return the probability density function at each bin
     */
    double[] getDensity();

    /**
     * Get the density at a specific bin
     *
     * @param bin the bin
     * @return the density of a specific bin
     */
    default double getDensity(int bin) {
        return getDensity()[bin];
    }

    /**
     * @return an iterable over the density bins
     */
    default Iterable<DensityBin> densityBins() {
        return () -> new Iterator<>() {
            private int i = 0;

            @Override
            public boolean hasNext() {
                return i < getDensity().length;
            }

            @Override
            public DensityBin next() {
                return new DensityBin(getBinEdges()[i], getBinEdges()[++i], getDensity()[i - 1]);
            }
        };
    }
}
