package net.mahdilamb.stats.utils;

/**
 * A statistic with a p-value
 */
public class Statistic {
    final String name;
    /**
     * The associated statistic and p-value
     */
    public final double statistic, pValue;

    public Statistic(String name, double statistic, double pValue) {
        this.name = name;
        this.statistic = statistic;
        this.pValue = pValue;
    }

    /**
     * @return the value of the statistic
     */
    public double get() {
        return statistic;
    }

    /**
     * @return the p-value of the statistic
     */
    public double getPValue() {
        return pValue;
    }

    @Override
    public String toString() {
        return String.format("%s statistic: %f, p-value: %f", name, statistic, pValue);
    }
}
