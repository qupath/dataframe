package net.mahdilamb.stats;

import net.mahdilamb.stats.utils.Statistic;
import net.mahdilamb.dataframe.utils.DualPivotQuickSort;
import net.mahdilamb.dataframe.utils.MergeSort;
import net.mahdilamb.stats.distributions.TDistributions;
import net.mahdilamb.stats.libs.Cephes;


import java.util.function.IntToDoubleFunction;

import static net.mahdilamb.stats.StatUtils.*;

/**
 * Utility class for working with correlations and rank-correlations
 */
public class Correlations {
    private Correlations() {

    }

    private static final double PRECISION_THRESHOLD = 1e-13;

    /**
     * Pearson's correlation of two continuous series of data. Method adapted from SciPy
     *
     * @param x    the x index-to-value getter
     * @param y    the y index-to-value getter
     * @param size the number of elements
     * @return the Pearson's correlation between x and y
     */
    public static double pearsonsCorrelation(IntToDoubleFunction x, IntToDoubleFunction y, int size) {
        if (size < 2) {
            throw new ArithmeticException("must be at least 2 values");
        }
        if (isConstant(x, size) || isConstant(y, size)) {
            System.err.println("x or y values are constant");
            return Double.NaN;
        }
        if (size == 2) {
            return Math.signum(x.applyAsDouble(1) - x.applyAsDouble(0)) * Math.signum(y.applyAsDouble(1) - y.applyAsDouble(0));
        }
        double xMean = mean(x, size);
        double yMean = mean(y, size);

        double normXM = 0;
        double normYM = 0;
        double[] xm = new double[size];
        double[] ym = new double[size];
        for (int i = 0; i < size; ++i) {
            double _x = (x.applyAsDouble(i) - xMean);
            normXM += _x * _x;
            double _y = (y.applyAsDouble(i) - yMean);
            normYM += _y * _y;
            xm[i] = _x;
            ym[i] = _y;
        }
        normXM = Math.sqrt(normXM);
        normYM = Math.sqrt(normYM);

        if (normXM < PRECISION_THRESHOLD * Math.abs(xMean) || normYM < PRECISION_THRESHOLD * Math.abs(normYM)) {
            System.err.println("Precision loss as all the values are close to the mean");
        }

        double r = 0;
        for (int i = 0; i < size; ++i) {
            double _x = xm[i] / normXM;
            double _y = ym[i] / normYM;
            r += _x * _y;
        }
        r = Math.max(min(r, 1.0), -1.0);
        return r;
    }

    /**
     * Pearson's correlation of two continuous series of data. Method adapted from SciPy
     *
     * @param x    the x index-to-value getter
     * @param y    the y index-to-value getter
     * @param size the number of elements
     * @return a named-tuple containing the correlation efficient ("coeff") and the p-value ("p-value")
     */
    public static Statistic pearsonsCorrelationP(IntToDoubleFunction x, IntToDoubleFunction y, int size) {
        if (size < 2) {
            throw new ArithmeticException("must be at least 2 values");
        }
        if (isConstant(x, size) || isConstant(y, size)) {
            System.err.println("x or y values are constant");
            return new Statistic("coeff", Double.NaN, Double.NaN);
        }
        if (size == 2) {
            return new Statistic("coeff", Math.signum(x.applyAsDouble(1) - x.applyAsDouble(0)) * Math.signum(y.applyAsDouble(1) - y.applyAsDouble(0)), 1);
        }
        double xMean = mean(x, size);
        double yMean = mean(y, size);

        double normXM = 0;
        double normYM = 0;
        double[] xm = new double[size];
        double[] ym = new double[size];
        for (int i = 0; i < size; ++i) {
            double _x = (x.applyAsDouble(i) - xMean);
            normXM += _x * _x;
            double _y = (y.applyAsDouble(i) - yMean);
            normYM += _y * _y;
            xm[i] = _x;
            ym[i] = _y;
        }
        normXM = Math.sqrt(normXM);
        normYM = Math.sqrt(normYM);
        double threshold = 1e-13;
        if (normXM < threshold * Math.abs(xMean) || normYM < threshold * Math.abs(normYM)) {
            System.err.println("Precision loss as all the values are close to the mean");
        }

        double r = 0;
        for (int i = 0; i < size; ++i) {
            double _x = xm[i] / normXM;
            double _y = ym[i] / normYM;
            r += _x * _y;
        }
        r = Math.max(min(r, 1.0), -1.0);
        double ab = size / 2. - 1;
        double prob = 2 * Cephes.btdtr(ab, ab, 0.5 * (1 - Math.abs((r))));
        return new Statistic("coeff", r, prob);
    }

    /**
     * Assign ranks to data
     *
     * @param getter the index-to-value getter
     * @param size   the size of the source values
     * @param method the rank method
     * @return the ranks of the values
     */
    public static double[] rankData(IntToDoubleFunction getter, int size, RankMethod method) {
        final int[] order = ArrayUtils.intRange(size);
        final double[] ranks = new double[size];

        if (method == RankMethod.ORDINAL) {
            MergeSort.argSort(order, getter);
            for (int i = 0; i < order.length; ++i) {
                ranks[order[i]] = i + 1;
            }
        } else {
            DualPivotQuickSort.argSort(order, getter, true);
            int k = 0;
            int j = 0;
            //find runs in the sorted data
            while (j < size) {
                int start = j;
                double prev = getter.applyAsDouble(order[start]);
                ++j;
                while (j < size) {
                    double current = getter.applyAsDouble(order[j]);
                    if (current != prev) {
                        break;
                    }
                    ++j;
                }
                ++k;
                //use the appropriate method to deal with ties
                for (int i = start; i < j; ++i) {
                    ranks[order[i]] = method.ranker.computeRank(k, start, j);
                }
            }
        }
        return ranks;
    }

    /**
     * Get the ranks of the data using the average position to deal with ties
     *
     * @param getter the index to value getter
     * @param size   the size of the data source
     * @param method the name of the method used to deal with ties (i.e. "ordinal", "min", "max", "dense", "average")
     * @return the data ranks
     */
    public static double[] rankData(IntToDoubleFunction getter, int size, String method) {
        return rankData(getter, size, RankMethod.getRankMethod(method));
    }

    /**
     * Get the ranks of the data using the average position to deal with ties
     *
     * @param getter the index to value getter
     * @param size   the size of the data source
     * @return the data ranks
     */
    public static double[] rankData(IntToDoubleFunction getter, int size) {
        return rankData(getter, size, RankMethod.AVERAGE);
    }

    /**
     * Calculate the Pearson product-moment correlation coefficient
     *
     * @param x the x values
     * @param y the y values
     * @return the Pearson product-moment correlation coefficient
     */
    public static double correlationCoefficient(double[] x, double[] y) {
        double meanX = mean(x);
        double meanY = mean(y);
        double xm_ym = 0;
        double xm_xm = 0;
        double ym_ym = 0;
        for (int i = 0; i < x.length; ++i) {
            double xm = x[i] - meanX;
            double ym = y[i] - meanY;
            xm_ym += xm * ym;
            xm_xm += xm * xm;
            ym_ym += ym * ym;
        }
        return xm_ym / Math.sqrt(xm_xm * ym_ym);
    }

    /**
     * Calculate the Pearson product-moment correlation coefficient
     *
     * @param x    the index-to-x value getter
     * @param y    the index-to-y value getter
     * @param size the size of the source of the data
     * @return the Pearson product-moment correlation coefficient
     */
    public static double correlationCoefficient(IntToDoubleFunction x, IntToDoubleFunction y, int size) {
        double meanX = mean(x, size);
        double meanY = mean(y, size);
        double xm_ym = 0;
        double xm_xm = 0;
        double ym_ym = 0;
        for (int i = 0; i < size; ++i) {
            double xm = x.applyAsDouble(i) - meanX;
            double ym = y.applyAsDouble(i) - meanY;
            xm_ym += xm * ym;
            xm_xm += xm * xm;
            ym_ym += ym * ym;
        }
        return xm_ym / Math.sqrt(xm_xm * ym_ym);
    }

    /**
     * Calculate the Spearman's correlation coefficient of two continuous series of data
     *
     * @param x    the index-to-x value getter
     * @param y    the index-to-y value getter
     * @param size the size of the source of the data
     * @return the Spearman's correlation coefficient
     */
    public static double spearmansCorrelation(IntToDoubleFunction x, IntToDoubleFunction y, int size) {
        if (size <= 1) {
            return Double.NaN;
        }
        if (isConstant(x, size) || isConstant(y, size)) {
            System.err.println("x and/or y values are constant");
            return Double.NaN;
        }
        double[] xRanks = rankData(x, size);
        double[] yRanks = rankData(y, size);

        return correlationCoefficient(xRanks, yRanks);
    }

    /**
     * Calculate the Spearman's correlation coefficient of two continuous series of data
     *
     * @param x    the index-to-x value getter
     * @param y    the index-to-y value getter
     * @param size the size of the source of the data
     * @return a named double tuple containing the correlation coefficient ("coeff") and the p-value ("p-value")
     * comparing the correlation to a Student's t-distribution
     */
    public static Statistic spearmansCorrelationP(IntToDoubleFunction x, IntToDoubleFunction y, int size) {
        if (size <= 1) {
            return new Statistic("coeff", Double.NaN, Double.NaN);
        }
        if (isConstant(x, size) || isConstant(y, size)) {
            System.err.println("x and/or y values are constant");
            return new Statistic("coeff", Double.NaN, Double.NaN);
        }
        double[] xRanks = rankData(x, size);
        double[] yRanks = rankData(y, size);
        double c = correlationCoefficient(xRanks, yRanks);
        int dof = size - 2;
        return new Statistic("coeff", c, 2 * TDistributions.SF(Math.abs(c * Math.sqrt(Math.max(0, (dof / ((c + 1.0) * (1.0 - c)))))), dof));
    }
}
