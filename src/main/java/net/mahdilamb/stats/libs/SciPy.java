package net.mahdilamb.stats.libs;

/*
Licence for including SciPy code

Copyright © 2001, 2002 Enthought, Inc.
All rights reserved.

Copyright © 2003-2019 SciPy Developers.
All rights reserved.
 */

/**
 * Utility class that includes SciPy methods transcribed for use in Java.
 * <p>
 * The method names have been kept the same as the source for them to be easy to find
 */
public final class SciPy {
    private SciPy() {
    }

    /**
     * Precision of double
     */
    public static final double MACHEP = Math.ulp(1d);

    /**
     * Calculate the log of the normal distribution function of a value
     *
     * @param a the value to calculate the log of the normal distribution function
     * @return the log ndtr of a value
     */
    public static double log_ndtr(double a) {
        return ndrt_c.log_ndtr(a);
    }

    /*
        Licence for including SciPy code

        Copyright © 2001, 2002 Enthought, Inc.
        All rights reserved.

        Copyright © 2003-2019 SciPy Developers.
        All rights reserved.
         */
  private   static final class ndrt_c {
        private ndrt_c() {

        }

        /*
         * double log_ndtr(double a)
         *
         * For a > -20, use the existing ndtr technique and take a log.
         * for a <= -20, we use the Taylor series approximation of erf to compute
         * the log CDF directly. The Taylor series consists of two parts which we will name "left"
         * and "right" accordingly.  The right part involves a summation which we compute until the
         * difference in terms falls below the machine-specific EPSILON.
         *
         * \Phi(z) &=&
         *   \frac{e^{-z^2/2}}{-z\sqrt{2\pi}}  * [1 +  \sum_{n=1}^{N-1}  (-1)^n \frac{(2n-1)!!}{(z^2)^n}]
         *   + O(z^{-2N+2})
         *   = [\mbox{LHS}] * [\mbox{RHS}] + \mbox{error}.
         *
         */

        /**
         * Calculate the log of the gaussian cumulative distribution function
         *
         * @param a the value to calculate the function of
         * @return the log of the gaussian cumulative distribution function of a
         */
        public static double log_ndtr(double a) {
            double log_LHS,        /* we compute the left hand side of the approx (LHS) in one shot */
                    last_total = 0,        /* variable used to check for convergence */
                    right_hand_side = 1,    /* includes first term from the RHS summation */
                    numerator = 1,        /* numerator for RHS summand */
                    denom_factor = 1,    /* use reciprocal for denominator to avoid division */
                    denom_cons = 1.0 / (a * a);    /* the precomputed division we use to adjust the denominator */
            long sign = 1, i = 0;

            if (a > 6) {
                return -Cephes.ndtr(-a);     /* log(1+x) \approx x */
            }
            if (a > -20) {
                return Math.log(Cephes.ndtr(a));
            }
            log_LHS = -0.5 * a * a - Math.log(-a) - 0.5 * Math.log(2 * Math.PI);

            while (Math.abs(last_total - right_hand_side) > MACHEP) {
                ++i;
                last_total = right_hand_side;
                sign = -sign;
                denom_factor *= denom_cons;
                numerator *= 2 * i - 1;
                right_hand_side += sign * numerator * denom_factor;
            }
            return log_LHS + Math.log(right_hand_side);
        }

    }
}
