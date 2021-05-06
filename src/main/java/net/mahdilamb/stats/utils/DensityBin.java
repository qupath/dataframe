package net.mahdilamb.stats.utils;

public class DensityBin extends BinEdges {
    /**
     * The density of elements in the bin
     */
    public final double density;

    /**
     * Create a bin
     *
     * @param min     the min of the bin
     * @param max     the max of the bin
     * @param density the number in the bin
     */
    public DensityBin(double min, double max, double density) {
        super(min, max);
        this.density = density;
    }

    /**
     * @return the density in the bin
     */
    public double getDensity() {
        return density;
    }

    @Override
    public String toString() {
        return String.format("Bin {min: %f,max: %f, density %f}", min, max, density);
    }
}
