package net.mahdilamb.stats;

import java.util.function.DoubleUnaryOperator;
import java.util.function.IntToDoubleFunction;

/**
 * Utility class for producing regressions
 */
public final class Regressions {
    private Regressions() {
    }

    /**
     * Return a linear regression evaluator using the Ordinary Least Squares method
     *
     * @param x    the index-to-x values
     * @param y    the index-to-y values
     * @param size the size of the source of the values
     * @return a linear regression evaluator
     */
    public static DoubleUnaryOperator linearRegression(IntToDoubleFunction x, IntToDoubleFunction y, int size) {
        double a = 0, b = 0, c = 0, d = 0;
        for (int n = 0; n < size; n++) {
            double _x = x.applyAsDouble(n);
            double _y = y.applyAsDouble(n);
            a += _x;
            b += _y;
            c += _x * _x;
            d += _x * _y;
        }
        final double run = ((size * c) - (a * a));
        final double rise = ((size * d) - (a * b));
        final double gradient = run == 0 ? 0 : (rise / run);
        final double intercept = ((b / size) - ((gradient * a) / size));
        return xi -> intercept + xi * gradient;
    }

    /**
     * Get an x-to-y evaluator over an exponential regression fit to data. Adapted from https://github.com/Tom-Alexander/regression-js/blob/master/src/regression.js
     *
     * @param xData the index-to-x data
     * @param yData the index-to-y data
     * @param size  the size of the input data
     * @return an exponential evaluator over x and y
     */
    public static DoubleUnaryOperator exponentialRegression(IntToDoubleFunction xData, IntToDoubleFunction yData, int size) {
        double b = 0, c = 0, d = 0, e = 0, f = 0;
        for (int n = 0; n < size; n++) {
            double _x = xData.applyAsDouble(n);
            double _y = yData.applyAsDouble(n);
            b += _y;
            c += _x * _x * _y;
            d += _y * Math.log(_y);
            e += _x * _y * Math.log(_y);
            f += _x * _y;
        }

        final double denominator = ((b * c) - (f * f));
        final double A = Math.exp(((c * d) - (f * e)) / denominator);
        final double B = ((b * e) - (f * d)) / denominator;
        return x -> A * Math.exp(B * x);
    }

    /**
     * Get an x-to-y evaluator over an power regression fit to data. Adapted from https://github.com/Tom-Alexander/regression-js/blob/master/src/regression.js
     *
     * @param xData the index-to-x data
     * @param yData the index-to-y data
     * @param size  the size of the input data
     * @return an power evaluator over x and y
     */
    public static DoubleUnaryOperator powerRegression(IntToDoubleFunction xData, IntToDoubleFunction yData, int size) {
        double sum_0 = 0, sum_1 = 0, sum_2 = 0, sum_3 = 0;
        for (int n = 0; n < size; n++) {
            double _x = xData.applyAsDouble(n);
            double _y = yData.applyAsDouble(n);
            sum_0 += Math.log(_x);
            sum_1 += Math.log(_y) * Math.log(_x);
            sum_2 += Math.log(_y);
            sum_3 += (Math.pow(Math.log(_x), 2));
        }

        final double coeffB = ((size * sum_1) - (sum_0 * sum_2)) / ((size * sum_3) - (sum_0 * sum_0));
        final double coeffA = Math.exp(((sum_2 - (coeffB * sum_0)) / size));
        return x -> coeffA * Math.pow(x, coeffB);
    }
}
