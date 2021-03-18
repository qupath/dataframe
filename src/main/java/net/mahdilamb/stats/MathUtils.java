package net.mahdilamb.stats;

import static net.mahdilamb.stats.libs.Cephes.Gamma;

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
            dot = (lhs[i]* rhs[i]+ dot);
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
            dot =(lhs[i]* rhs[i]+ dot);
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

    /**
     * Perform summation using Kahan summation to improve accuracy in floating path math
     *
     * @param values the values
     * @return the
     */
    public static double kahanSum(double... values) {
        double accum = 0.0;
        double c = 0.0;
        for (double v : values) {
            double y = v - c;
            double t = accum + y;
            c = (t - accum) - y;
            accum = t;
        }
        return accum;
    }

    /**
     * Perform summation using the Kahan-Neumaier summation
     *
     * @param values the values to sum
     * @return the sum of the values
     */
    public static double neumaierSum(double... values) {
        double accum = values[0];
        double t, c = 0;
        int i;
        for (i = 1; i < values.length; i++) {
            t = accum + values[i];
            if (Math.abs(accum) >= Math.abs(values[i])) {
                c += (accum - t) + values[i];
            } else {
                c += (values[i] - t) + accum;
            }
            accum = t;
        }
        return accum + c;
    }

    /**
     * Perform summation using the "iterative Kahan–Babuška algorithm" proposed by Klein
     *
     * @param values the values to sum
     * @return the sum of the values
     */
    public static double kleinSum(double... values) {
        double s = 0, c, cc, cs = 0, ccs = 0, t;
        for (double value : values) {
            t = s + value;
            if (Math.abs(s) >= Math.abs(value)) {
                c = (s - t) + value;
            } else {
                c = (value - t) + s;
            }
            s = t;
            t = cs + c;
            if (Math.abs(cs) >= Math.abs(c)) {
                cc = (cs - t) + c;
            } else {
                cc = (c - t) + cs;
            }
            cs = t;
            ccs = ccs + cc;
        }
        return s + cs + ccs;
    }

    static int NUM_PARTIALS = 32;

    /**
     * fsum modified from <a href="https://github.com/nlmixrdevelopment/PreciseSums">PreciseSums</a>
     *
     * @param values the values to sum
     * @return this sum of the values
     */
    public static double fsum(double... values) {
        int m = NUM_PARTIALS;
        double[] p = new double[m];
        double x, y, t;
        double xsave, special_sum = 0.0, inf_sum = 0.0, sum;
        double hi, yr, lo = 0;
        int ix, i, j, n = 0;//, m = NUM_PARTIALS;
        //double *p = Calloc(NUM_PARTIALS, double);
        // for x in input
        for (ix = 0; ix < values.length; ix++) {
            x = values[ix];
            xsave = x;
            for (i = j = 0; j < n; j++) {
                y = p[j];
                if (Math.abs(x) < Math.abs(y)) {
                    t = x;
                    x = y;
                    y = t;
                }
                hi = x + y;
                yr = hi - x;
                lo = y - yr;
                if (lo != 0.0)
                    p[i++] = lo;
                x = hi;
            }

            n = i;
            if (x != 0.0) {
                if (!Double.isFinite(x)) {
                    /*
                    a nonfinite x could arise either as
                   a result of intermediate overflow, or
                   as a result of a nan or inf in the
                   summands */
                    if (Double.isFinite(xsave) || Double.isNaN(xsave)) {
                        System.err.println("intermediate overflow in fsum");
                        return kleinSum(values);
                    } else {
                        inf_sum += xsave;
                    }
                    special_sum += xsave;
                    /* reset partials */
                    n = 0;
                } else {
                    if (m > 0 && n >= m) {
                        //&& _fsum_realloc(&p, n, ps, &m)
                        // Doubles the size of array.
                        m += m;
                        p = new double[m];
                    } else if (m < 0 && n >= -m) {
                        System.err.println("The size of the saved partials is too small to calculate the sum.");
                        return kleinSum(values);
                    }
                    p[n++] = x;
                }
            }
        }
        if (special_sum != 0.0) {
            if (Double.isNaN(inf_sum)) {
                System.err.println("-inf + inf in fsum");
                return Double.NaN;
            }
            sum = special_sum;
            return sum;
        }

        hi = 0.0;
        if (n > 0) {
            hi = p[--n];
    /* sum_exact(ps, hi) from the top, stop when the sum becomes
       inexact. */
            while (n > 0) {
                x = hi;
                y = p[--n];
                if (Math.abs(y) >= Math.abs(x)) {
                    return kleinSum(values);
                    /* Rprintf("Partial Sums:\n"); */
                    /* for (i = 0; i < j; i++){ */
                    /*   Rprintf("p[%d] = %f\n",i,p[i]); */
                    /* } */
                    /* Rprintf("Assertion Error:\n"); */
                    /* Rprintf("fabs(y) >= fabs(x) or %f >= %f\n",fabs(y),fabs(x)); */
                    /* if (m > 0) Free(p); */
                    /* error("Error in parital sums."); */
                }
                hi = x + y;
                yr = hi - x;
                lo = y - yr;
                if (lo != 0.0)
                    break;
            }
    /* Make half-even rounding work across multiple partials.
       Needed so that sum([1e-16, 1, 1e16]) will round-up the last
       digit to two instead of down to zero (the 1e-16 makes the 1
       slightly closer to two).  With a potential 1 ULP rounding
       error fixed-up, math.fsum() can guarantee commutativity. */
            if (n > 0 && ((lo < 0.0 && p[n - 1] < 0.0) ||
                    (lo > 0.0 && p[n - 1] > 0.0))) {
                y = lo * 2.0;
                x = hi + y;
                yr = x - hi;
                if (y == yr)
                    hi = x;
            }
        }
        sum = hi;
        return sum;
    }

    /**
     * Splitter for Veltkamp and Dekker's two product
     */
    private static final double SPLITTER = 134217729;

    private static double[] twoProduct(double a, double b, double[] result) {
        double x = a * b;

        double c = SPLITTER * a;
        double abig = c - a;
        double ahi = c - abig;
        double alo = a - ahi;

        double d = SPLITTER * b;
        double bbig = d - b;
        double bhi = d - bbig;
        double blo = b - bhi;

        double err1 = x - (ahi * bhi);
        double err2 = err1 - (alo * bhi);
        double err3 = err2 - (ahi * blo);

        double y = alo * blo - err3;
        result[1] = y;
        result[0] = x;
        return result;
    }

    /**
     * Calculate the product of two numbers and the floating point error
     *
     * @param a value a
     * @param b value b
     * @return a two element array {result, error}
     */
    public static double[] twoProduct(double a, double b) {
        return twoProduct(a, b, new double[2]);
    }

    private static double[] twoProductFMA(double a, double b, double[] result) {
        double x = a * b;
        double y = Math.fma(a, b, -x);
        result[1] = y;
        result[0] = x;
        return result;
    }

    /**
     * Calculate the product of two numbers and the floating point error.
     * <p>
     * This is more efficient on CPUs that contain an FMA unit
     *
     * @param a value a
     * @param b value b
     * @return a two element array {result, error}
     */
    public static double[] twoProductFMA(double a, double b) {
        return twoProductFMA(a, b, new double[2]);
    }

    /**
     * Perform a compensated product of a series of values
     *
     * @param a the values
     * @return the compensated product
     */
    public static double compProd(double... a) {
        double p1 = a[0];
        double e1 = 0;
        for (int i = 1; i < a.length; ++i) {
            double x = p1 * a[i];

            double c = SPLITTER * p1;
            double abig = c - p1;
            double ahi = c - abig;
            double alo = p1 - ahi;

            double d = SPLITTER * a[i];
            double bbig = d - a[i];
            double bhi = d - bbig;
            double blo = a[i] - bhi;

            double err1 = x - (ahi * bhi);
            double err2 = err1 - (alo * bhi);
            double err3 = err2 - (ahi * blo);

            double y = alo * blo - err3;
            p1 = x;
            e1 = e1 * a[i] + y;
        }
        return p1 + e1;
    }

    /**
     * Perform a compensated product using the twoProductFMA (i.e. this is
     * better for CPUs with FMA unit)
     *
     * @param a the values
     * @return the product of a
     */
    public static double compProdFMA(double... a) {
        double p1 = a[0];
        double e1 = 0;
        for (int i = 1; i < a.length; ++i) {
            double x = p1 * a[i];
            double y = Math.fma(p1, a[i], -x);
            p1 = x;
            e1 = Math.fma(e1, a[i], y);
        }
        return p1 + e1;
    }

}

