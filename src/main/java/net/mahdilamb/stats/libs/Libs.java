package net.mahdilamb.stats.libs;


public final class Libs {
    private Libs() {
    }

    public static boolean SILENT_EXCEPTIONS = true;

    private static final double two54 = 1.80143985094819840000e+16, /* 0x43500000, 0x00000000 */
            twom54 = 5.55111512312578270212e-17, /* 0x3C900000, 0x00000000 */
            huge = 1.0e+300,
            tiny = 1.0e-300;
    private static final long highMask = 0xFFFFFFFF00000000L,
            lowMask = 0x00000000FFFFFFFFL;

    /**
     * Get the left 32-bits of a double
     *
     * @param val the double
     * @return the left 32-bits of a double
     */
    static int getHighWord(double val) {
        return (int) ((Double.doubleToRawLongBits(val) & highMask) >> 32);
    }

    /**
     * Get the right 32-bits of a double
     *
     * @param val the double
     * @return the right 32-bits of a double
     */
    static int getLowWord(double val) {
        return (int) (Double.doubleToRawLongBits(val) & highMask);
    }

    /**
     * Set the left 32-bits of a double
     *
     * @param word     the value
     * @param highWord the left-32 bits
     * @return the original double with the high part changed
     */
    static double setHighWord(double word, int highWord) {
        final long lo = Double.doubleToRawLongBits(word) & lowMask;
        final long hi = (long) highWord << 32;
        return Double.longBitsToDouble(lo | hi);

    }

    /**
     * Copy the sign from one double to another
     *
     * @param x the sign to set
     * @param y the value to get the sign from
     * @return x with the sign of y
     */
    static double copySign(double x, double y) {
        int hx = getHighWord(x), hy = getHighWord(y);
        return setHighWord(x, (hx & 0x7fffffff) | (hy & 0x80000000));
    }


    /**
     * Load exponent function. Modified from <a href="https://android.googlesource.com/platform/bionic/+/ics-mr1-release/libc/bionic/ldexp.c">bionic</a>
     *
     * @param x the value
     * @param n the exponent
     * @return the loaded exponent
     */
    public static double ldexp(double x, int n) {
        if (n == 0 || Double.isNaN(x) || Double.isInfinite(x)) {
            return x;
        }
        long words = Double.doubleToRawLongBits(x);
        int hx = (int) ((words & highMask) >> 32), lx = (int) (words & lowMask), k;
        k = (hx & 0x7ff00000) >> 20;

        if (k == 0) {                /* 0 or subnormal x */
            if ((lx | (hx & 0x7fffffff)) == 0) return x; /* +-0 */
            x *= two54;
            hx = getHighWord(x);
            k = ((hx & 0x7ff00000) >> 20) - 54;
            if (n < -50000) return tiny * x;    /*underflow*/
        }
        if (k == 0x7ff) return x + x;        /* NaN or Inf */
        k = k + n;
        if (k > 0x7fe) return huge * copySign(huge, x); /* overflow  */
        if (k > 0)                /* normal result */ {
            x = setHighWord(x, (hx & 0x800fffff) | (k << 20));
            return x;
        }
        if (k <= -54) {
            if (n > 50000)    /* in case integer overflow in n+k */
                return huge * copySign(huge, x);    /*overflow*/
            else return tiny * copySign(tiny, x);    /*underflow*/
        }
        k += 54;                /* subnormal result */
        x = setHighWord(x, (hx & 0x800fffff) | (k << 20));
        return x * twom54;


    }

    /**
     * Return the fractional part of a double
     *
     * @param x the double
     * @return the fractional part
     */
    public static double fract(double x) {
        return x - ((long) x);
    }

    public static double max(double a, double b, double c) {
        return Math.max(Math.max(a, b), c);
    }

    /**
     * Modf function. Based on https://elixir.bootlin.com/musl/v1.1.9/source/src/math/modf.c (modified to work in Java
     * which doesn't have unsigned longs)
     *
     * @param x the value
     * @return the fractional part of the value
     */
    public static double modf(double x) {
        long mask;
        long i = Double.doubleToRawLongBits(x);
        int e = (int) (i >> 52 & 0x7ff) - 0x3ff;

        /* no fractional part */
        if (e >= 52) {
            if (e == 0x400 && i << 12 != 0) /* nan */
                return x;
            i &= 0x8000000000000000L;
            return Double.longBitsToDouble(i);
        }

        /* no integral part*/
        if (e < 0) {
            return x;
        }

        mask = 0xFFFFFFFFFFFFFL >> e;
        if ((i & mask) == 0) {
            i &= 0x8000000000000000L;
            return Double.longBitsToDouble(i);
        }
        i &= ~mask;
        return x - Double.longBitsToDouble(i);
    }

    /**
     * @param x the double
     * @return the integer part of a double
     */
    static long trunc(double x) {
        long i = Double.doubleToRawLongBits(x);
        int e = (int) ((i >>> 52 & 0x7ff) - 0x3ff);
        return (int) (((i & 0xfffffffffffffL) >> (52 - e)) + (1 << e));
    }

    public static double frexp(double x, int[] e) {
        if (x == 0. || x == -0.) {
            e[0] = 0;
            return x;
        }
        if (Double.isInfinite(x) || Double.isNaN(x)) {
            e[0] = -1;
            return x;
        }
        long i = Double.doubleToRawLongBits(x);
        int ee = (int) (i >>> 52 & 0x7ff);
        if (ee == 0) {
            if (x != 0) {
                x = frexp(x * 0x1p64, e);
                e[0] -= 64;
            } else e[0] = 0;
            return x;
        } else if (ee == 0x7ff) {
            return x;
        }
        e[0] = ee - 0x3fe;
        i &= 0x800fffffffffffffL;
        i |= 0x3fe0000000000000L;
        return Double.longBitsToDouble(i);

    }

}
