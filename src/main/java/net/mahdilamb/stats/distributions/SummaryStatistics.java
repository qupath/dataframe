package net.mahdilamb.stats.distributions;


/**
 * Summary statistics of a distribution
 */
public final class SummaryStatistics {
    /**
     * Precision for output string
     */
    public static int PRECISION = 2;
    private final double mean, variation, skewness, kurtosis;

    SummaryStatistics(final double mean, final double variation, final double skewness, final double kurtosis) {
        this.mean = mean;
        this.variation = variation;
        this.skewness = skewness;
        this.kurtosis = kurtosis;
    }

    /**
     * @return the mean
     */
    public final double getMean() {
        return mean;
    }

    /**
     * @return the variation
     */
    public final double getVariation() {
        return variation;
    }

    /**
     * @return Fisher's skewness
     */
    public final double getSkewness() {
        return skewness;
    }

    /**
     * @return Fisher's kurtosis
     */
    public final double getKurtosis() {
        return kurtosis;
    }

    public final String toString() {
        return String.format("Statistics {mean: %s, variation: %s, skewness: %s, kurtosis: %s}", getMean(), getVariation(), getSkewness(), getKurtosis());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SummaryStatistics)) return false;
        final SummaryStatistics that = (SummaryStatistics) o;
        return Double.compare(that.mean, mean) == 0 && Double.compare(that.variation, variation) == 0 && Double.compare(that.skewness, skewness) == 0 && Double.compare(that.kurtosis, kurtosis) == 0;
    }

    @Override
    public int hashCode() {
        int result = 31 + Double.hashCode(mean);
        result = 31 * result + Double.hashCode(variation);
        result = 31 * result + Double.hashCode(skewness);
        result = 31 * result + Double.hashCode(kurtosis);
        return result;
    }
}
