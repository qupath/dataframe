package net.mahdilamb.stats.utils;

/**
 * A histogram bin
 */
public class BinEdges {
    /**
     * The min and max of the bin
     */
    public final double min, max;

    /**
     * Create a bin
     *
     * @param min the minimum of the bin
     * @param max the maximum of the bin
     */
    public BinEdges(double min, double max) {
        this.min = min;
        this.max = max;
    }

    /**
     * @return the bin min
     */
    public final double getMin() {
        return min;
    }

    /**
     * @return the bin max
     */
    public final double getMax() {
        return max;
    }

    @Override
    public String toString() {
        return String.format("Bin {min: %f,max: %f}", min, max);
    }
}
