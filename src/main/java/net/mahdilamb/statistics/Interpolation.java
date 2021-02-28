package net.mahdilamb.statistics;


import net.mahdilamb.dataframe.functions.DoubleTernaryOperator;

/**
 * Enumeration for different types of interpolation
 */
public strictfp enum Interpolation {
    /**
     * Linearly interpolate between two points by the fractional amount
     */
    LINEAR((t, low, high) -> low + (high - low) * t),
    /**
     * Floor interpolate - always accept lowest
     */
    LOWER((t, low, high) -> low),
    /**
     * Ceil interpolate - always accept highest
     */
    HIGHER((t, low, high) -> high),
    /**
     * Round interpolate - always accept nearest
     */
    NEAREST((t, low, high) -> t < 0.5 ? low : high),
    /**
     * Mean interpolate - always accept mid point between two points
     */
    MIDPOINT((t, low, high) -> (low + high) * .5);
    private final DoubleTernaryOperator interpolator;

    Interpolation(DoubleTernaryOperator interpolator) {
        this.interpolator = interpolator;
    }

    /**
     * Calculate the interpolated value
     *
     * @param fraction the fraction (0-1) between low and high
     * @param low      the low value
     * @param high     the high value
     * @return the interpolated value
     */
    double interpolate(final double fraction, final double low, final double high) {
        return interpolator.applyAsDouble(fraction, low, high);
    }
}
