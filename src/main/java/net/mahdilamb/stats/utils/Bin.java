package net.mahdilamb.stats.utils;

public class Bin extends BinEdges {
    /**
     * The number of elements in the bin
     */
    public final int count;

    /**
     * Create a bin
     *
     * @param min   the min of the bin
     * @param max   the max of the bin
     * @param count the number in the bin
     */
    public Bin(double min, double max, int count) {
        super(min, max);
        this.count = count;
    }

    /**
     * @return the count in the bin
     */
    public int getCount() {
        return count;
    }

    @Override
    public String toString() {
        return String.format("Bin {min: %f,max: %f, count %d}", min, max, count);
    }
}
