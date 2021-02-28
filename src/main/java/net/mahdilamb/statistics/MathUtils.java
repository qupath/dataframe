package net.mahdilamb.statistics;

/**
 * Utility class for mathematics functions
 */
public final strictfp class MathUtils {
    private MathUtils() {

    }

    /**
     * The denominator to be used in log2
     */
    public static final double LOG2_DENOM = Math.log(2);
    /**
     * Constant value (square root of pi)
     */
    public static final double SQRT_PI = Math.sqrt(Math.PI);

    /**
     * Calculate the inverse hyperbolic cosine of a value.
     * <p>
     * Values must be greater than 1
     *
     * @param x the value
     * @return the inverse hyperbolic cosine of the value
     */
    public static double acosh(double x) {
        if (x < 1.0) {
            throw new ArithmeticException("acosh only available for real numbers greater than 1");
        }
        return Math.log(x + Math.sqrt(Math.fma(x, x, -1)));
    }

    /**
     * Calculate the inverse hyperbolic sine of a value.
     *
     * @param x the value
     * @return the inverse hyperbolic sine of the value
     */
    public static double asinh(double x) {
        if (x == 0.0) {
            return x;
        }
        return Math.log(x + Math.sqrt(Math.fma(x, x, 1)));

    }

    /**
     * Calculate the inverse hyperbolic tangent of a value.
     * <p>
     * Values must be between -1 and 1
     *
     * @param x the value
     * @return the inverse hyperbolic tangent of the value
     */
    public static double atanh(double x) {
        if (x > 1.0 || x < -1.0) {
            throw new ArithmeticException("atanh not available for real numbers less than 1 and greater than -1");
        }
        return 0.5 * Math.log((1.0 + x) / (1.0 - x));
    }

    /**
     * Return a copy of the data with each element rounded
     *
     * @param data the input data
     * @return the data with each element rounded
     */
    public static double[] round(final double... data) {
        final double[] out = new double[data.length];
        for (int i = 0; i < data.length; ++i) {
            out[i] = Math.round(data[i]);
        }
        return out;
    }

    /**
     * Calculate a copy of the data with the cumulative product of the data
     *
     * @param data the input data
     * @return a new array with the cumulative product (of the same size as the data)
     */
    public static double[] cumulativeProduct(final double... data) {
        final double[] out = new double[data.length];
        double cum = 0;
        for (int i = 0; i < data.length; ++i) {
            cum *= data[i];
            out[i] = cum;
        }
        return out;
    }

    /**
     * Calculate a copy of the data with the cumulative sum of the data
     *
     * @param data the input data
     * @return a new array with the cumulative sum (of the same size as the data)
     */
    public static double[] cumulativeSum(final double... data) {
        final double[] out = new double[data.length];
        double cum = 0;
        for (int i = 0; i < data.length; ++i) {
            cum += data[i];
            out[i] = cum;
        }
        return out;
    }

    /**
     * Calculate a copy of the data with each element being the difference of adjacent elements
     *
     * @param data the input data
     * @return a new array with the difference between each element. The output data will be of length 1 less than the input data
     */
    public static double[] difference(final double... data) {
        final double[] out = new double[data.length - 1];
        for (int i = 0; i < out.length; ++i) {
            out[i] = data[i + 1] - data[i];
        }
        return out;
    }

    /**
     * Calculate the log2 of a value
     *
     * @param x the value to calculate the log2 of
     * @return the log2 of x
     */
    public static double log2(final double x) {
        return Math.log(x) / LOG2_DENOM;
    }

    /**
     * Calculate the dot product between two same sized vectors
     *
     * @param lhs the left-handside vector
     * @param rhs the right-handside vector
     * @return the dot product between a and b
     */
    public static double dot(final double[] lhs, final double[] rhs) {
        if (lhs.length != rhs.length) {
            throw new IllegalArgumentException("Both vectors must be the same size");
        }
        double dot = 0;
        for (int i = 0; i < lhs.length; ++i) {
            dot = Math.fma(lhs[i], rhs[i], dot);
        }
        return dot;
    }

    /**
     * Calculate the dot product between two same sized vectors
     *
     * @param lhs the left-handside vector
     * @param rhs the right-handside vector
     * @return the dot product between a and b
     */
    public static long dot(final int[] lhs, final int[] rhs) {
        if (lhs.length != rhs.length) {
            throw new IllegalArgumentException("Both vectors must be the same size");
        }
        double dot = 0;
        for (int i = 0; i < lhs.length; ++i) {
            dot = Math.fma(lhs[i], rhs[i], dot);
        }
        return (long) dot;
    }

    /**
     * Calculate the magnitude of a vector
     *
     * @param vec the vector
     * @return the magnitude of the vector
     */
    public static double length(final double[] vec) {
        return dot(vec, vec);
    }

    /**
     * Bitwise method to test if a value is odd
     *
     * @param v the value
     * @return whether the value is odd
     */
    public static boolean isOdd(int v) {
        return (v & 1) != 0;
    }

    /**
     * Bitwise method to test if a value is even
     *
     * @param v the value
     * @return whether the value is even
     */
    public static boolean isEven(int v) {
        return (v & 1) == 0;
    }

}

