package net.mahdilamb.stats;

import java.util.function.ToDoubleFunction;

/**
 * Enumeration for histogram bin width estimator
 */
public enum BinWidthEstimator {

    /**
     * Calculate the histogram bin widths using the square root method
     */
    SQRT(StatUtils::sqrtBinWidthEstimator),
    /**
     * Calculate the histogram bin widths based on the Sturges method
     */
    STURGES(StatUtils::sturgesBinWidthEstimator),
    /**
     * Calculate the histogram bin widths based on the Rice method
     */
    RICE(StatUtils::riceBinWidthEstimator),
    /**
     * Calculate the histogram bin widths based on the Scott method
     */
    SCOTT(StatUtils::scottBinWidthEstimator),
    /**
     * Calculate the histogram bin widths using the Stone method
     */
    STONE(StatUtils::stoneBinWidthEstimator),
    /**
     * Calculate the histogram bin widths using the Freedman-Diaconis method
     */
    FREEDMAN_DIACONIS(StatUtils::freedmanDiaconisBinWidthEstimator),
    /**
     * Calculate the histogram using the auto method in NumPy. i.e. either the Sturges method or Freedman-Diaconis
     * method, depending on which is better
     */
    NUMPY_AUTO(StatUtils::numpyAutoBinWidthEstimator);
    private final ToDoubleFunction<double[]> estimator;

    BinWidthEstimator(ToDoubleFunction<double[]> arrayEstimator) {
        this.estimator = arrayEstimator;
    }

    /**
     * Estimate the histogram of an array using an estimator
     *
     * @param data the data input
     * @return the estimated best bin width
     */
    double estimate(double... data) {
        return estimator.applyAsDouble(data);
    }

}
