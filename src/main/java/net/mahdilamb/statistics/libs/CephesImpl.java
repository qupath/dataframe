package net.mahdilamb.statistics.libs;

import net.mahdilamb.statistics.distributions.NormalDistributions;
import net.mahdilamb.statistics.libs.exceptions.*;

import static java.lang.Math.*;
import static net.mahdilamb.statistics.MathUtils.asinh;
import static net.mahdilamb.statistics.libs.CephesImpl.beta_c.beta;
import static net.mahdilamb.statistics.libs.CephesImpl.beta_c.lbeta;
import static net.mahdilamb.statistics.libs.CephesImpl.chbevl_c.chbevl;
import static net.mahdilamb.statistics.libs.CephesImpl.ellpe_c.ellpe;
import static net.mahdilamb.statistics.libs.CephesImpl.ellpk_c.ellpk;
import static net.mahdilamb.statistics.libs.CephesImpl.gamma_c.lgam;
import static net.mahdilamb.statistics.libs.CephesImpl.gamma_c.lgam_sgn;
import static net.mahdilamb.statistics.libs.CephesImpl.gammasgn_c.gammasgn;
import static net.mahdilamb.statistics.libs.CephesImpl.i0_c.i0;
import static net.mahdilamb.statistics.libs.CephesImpl.igami_c.igamci;
import static net.mahdilamb.statistics.libs.CephesImpl.j0_c.j0;
import static net.mahdilamb.statistics.libs.CephesImpl.j0_c.y0;
import static net.mahdilamb.statistics.libs.CephesImpl.j1_c.j1;
import static net.mahdilamb.statistics.libs.CephesImpl.j1_c.y1;
import static net.mahdilamb.statistics.libs.CephesImpl.jv_c.jv;
import static net.mahdilamb.statistics.libs.CephesImpl.ndtri_c.ndtri;
import static net.mahdilamb.statistics.libs.CephesImpl.polevl_c.p1evl;
import static net.mahdilamb.statistics.libs.CephesImpl.polevl_c.polevl;
import static net.mahdilamb.statistics.libs.CephesImpl.sinpi_c.sinpi;
import static net.mahdilamb.statistics.libs.CephesImpl.unity_c.cosm1;
import static net.mahdilamb.statistics.libs.CephesImpl.unity_c.log1pmx;
import static net.mahdilamb.statistics.libs.CephesImpl.yn_c.yn;
import static net.mahdilamb.statistics.libs.CephesImpl.zeta_c.zeta;
import static net.mahdilamb.statistics.libs.Libs.*;
import static net.mahdilamb.statistics.libs.SciPy.MACHEP;

/**
 * This "class" contains the actual implementations of Cephes units. The code has been modified from the SciPy
 * repository.
 */
/*
 * Cephes Math Library Release 2.0:  April, 1987
 * Copyright 1985, 1987 by Stephen L. Moshier
 * Direct inquiries to 30 Frost Street, Cambridge, MA 02140
 */
strictfp final class CephesImpl {
    private CephesImpl() {

    }

    /*                                                     chbevl.c
     *
     *     Evaluate Chebyshev series
     *
     *
     *
     * SYNOPSIS:
     *
     * int N;
     * double x, y, coef[N], chebevl();
     *
     * y = chbevl( x, coef, N );
     *
     *
     *
     * DESCRIPTION:
     *
     * Evaluates the series
     *
     *        N-1
     *         - '
     *  y  =   >   coef[i] T (x/2)
     *         -            i
     *        i=0
     *
     * of Chebyshev polynomials Ti at argument x/2.
     *
     * Coefficients are stored in reverse order, i.e. the zero
     * order term is last in the array.  Note N is the number of
     * coefficients, not the order.
     *
     * If coefficients are for the interval a to b, x must
     * have been transformed to x -> 2(2x - b - a)/(b-a) before
     * entering the routine.  This maps x from (a, b) to (-1, 1),
     * over which the Chebyshev polynomials are defined.
     *
     * If the coefficients are for the inverted interval, in
     * which (a, b) is mapped to (1/b, 1/a), the transformation
     * required is x -> 2(2ab/x - b - a)/(b-a).  If b is infinity,
     * this becomes x -> 4a/x - 1.
     *
     *
     *
     * SPEED:
     *
     * Taking advantage of the recurrence properties of the
     * Chebyshev polynomials, the routine requires one more
     * addition per loop than evaluating a nested polynomial of
     * the same degree.
     *
     */
    static final class chbevl_c {
        private chbevl_c() {

        }

        /*
         * Cephes Math Library Release 2.0:  April, 1987
         * Copyright 1985, 1987 by Stephen L. Moshier
         * Direct inquiries to 30 Frost Street, Cambridge, MA 02140
         */

        /**
         * Evaluate Chebyshev series
         *
         * @param x     the value
         * @param array the series
         * @param n     the length of the series
         * @return the solution to the Chebyshev series
         */
        public static double chbevl(double x, double[] array, int n) {
            double b0 = array[0], b1 = 0, b2 = 0;
            int p = 0;

            for (int i = n - 1; i > 0; --i) {
                b2 = b1;
                b1 = b0;
                b0 = x * b1 - b2 + array[++p];
            }
            return (0.5 * (b0 - b2));
        }
    }

    /*
     * Cephes Math Library Release 2.0:  April, 1987
     * Copyright 1984, 1987 by Stephen L. Moshier
     * Direct inquiries to 30 Frost Street, Cambridge, MA 02140
     */
    static final class tandg_c {
        private tandg_c() {
        }

        static final double PI180 = 1.74532925199432957692E-2;
        static final double lossth = 1.0e14;


        static double tandg(double x) {
            return (tancot(x, 0));
        }


        static double cotdg(double x) {
            return (tancot(x, 1));
        }


        static double tancot(double xx, int cotflg) {
            double x;
            int sign;

            /* make argument positive but save the sign */
            if (xx < 0) {
                x = -xx;
                sign = -1;
            } else {
                x = xx;
                sign = 1;
            }

            if (x > lossth) {
                // sf_error("tandg", SF_ERROR_NO_RESULT, NULL);
                return 0.0;
            }

            /* modulo 180 */
            x = x - 180.0 * Math.floor(x / 180.0);
            if (cotflg != 0) {
                if (x <= 90.0) {
                    x = 90.0 - x;
                } else {
                    x = x - 90.0;
                    sign *= -1;
                }
            } else {
                if (x > 90.0) {
                    x = 180.0 - x;
                    sign *= -1;
                }
            }
            if (x == 0.0) {
                return 0.0;
            } else if (x == 45.0) {
                return sign * 1.0;
            } else if (x == 90.0) {
                //  sf_error((cotflg ? "cotdg" : "tandg"), SF_ERROR_SINGULAR, NULL);
                return Double.POSITIVE_INFINITY;
            }
            /* x is now transformed into [0, 90) */
            return sign * Math.tan(x * PI180);
        }
    }
    /*                                                     ellpe.c
     *
     *     Complete elliptic integral of the second kind
     *
     *
     *
     * SYNOPSIS:
     *
     * double m, y, ellpe();
     *
     * y = ellpe( m );
     *
     *
     *
     * DESCRIPTION:
     *
     * Approximates the integral
     *
     *
     *            pi/2
     *             -
     *            | |                 2
     * E(m)  =    |    sqrt( 1 - m sin t ) dt
     *          | |
     *           -
     *            0
     *
     * Where m = 1 - m1, using the approximation
     *
     *      P(x)  -  x log x Q(x).
     *
     * Though there are no singularities, the argument m1 is used
     * internally rather than m for compatibility with ellpk().
     *
     * E(1) = 1; E(0) = pi/2.
     *
     *
     * ACCURACY:
     *
     *                      Relative error:
     * arithmetic   domain     # trials      peak         rms
     *    IEEE       0, 1       10000       2.1e-16     7.3e-17
     *
     *
     * ERROR MESSAGES:
     *
     *   message         condition      value returned
     * ellpe domain      x<0, x>1            0.0
     *
     */

    /*                                                     ellpe.c         */

    /* Elliptic integral of second kind */

    /*
     * Cephes Math Library, Release 2.1:  February, 1989
     * Copyright 1984, 1987, 1989 by Stephen L. Moshier
     * Direct inquiries to 30 Frost Street, Cambridge, MA 02140
     *
     * Feb, 2002:  altered by Travis Oliphant
     * so that it is called with argument m
     * (which gets immediately converted to m1 = 1-m)
     */
    static final class ellpe_c {
        private ellpe_c() {

        }

        private final static double P[] = {
                1.53552577301013293365E-4,
                2.50888492163602060990E-3,
                8.68786816565889628429E-3,
                1.07350949056076193403E-2,
                7.77395492516787092951E-3,
                7.58395289413514708519E-3,
                1.15688436810574127319E-2,
                2.18317996015557253103E-2,
                5.68051945617860553470E-2,
                4.43147180560990850618E-1,
                1.00000000000000000299E0
        };

        private final static double Q[] = {
                3.27954898576485872656E-5,
                1.00962792679356715133E-3,
                6.50609489976927491433E-3,
                1.68862163993311317300E-2,
                2.61769742454493659583E-2,
                3.34833904888224918614E-2,
                4.27180926518931511717E-2,
                5.85936634471101055642E-2,
                9.37499997197644278445E-2,
                2.49999999999888314361E-1
        };

        static double ellpe(double x) throws DomainException {
            x = 1.0 - x;
            if (x <= 0.0) {
                if (x == 0.0)
                    return (1.0);
                return (DomainException.raiseException("ellpe", Double.NaN));
            }
            if (x > 1.0) {
                return ellpe(1.0 - 1 / x) * sqrt(x);
            }
            return (polevl(x, P, 10) - log(x) * (x * polevl(x, Q, 9)));
        }

    }

    /*
     * Cephes Math Library Release 2.1:  January, 1989
     * Copyright 1984, 1987, 1989 by Stephen L. Moshier
     * Direct inquiries to 30 Frost Street, Cambridge, MA 02140
     */
    static final class dawsn_c {
        private dawsn_c() {
        }

        private static final double[] AN = {
                1.13681498971755972054E-11,
                8.49262267667473811108E-10,
                1.94434204175553054283E-8,
                9.53151741254484363489E-7,
                3.07828309874913200438E-6,
                3.52513368520288738649E-4,
                -8.50149846724410912031E-4,
                4.22618223005546594270E-2,
                -9.17480371773452345351E-2,
                9.99999999999999994612E-1,
        };

        private static final double[] AD = {
                2.40372073066762605484E-11,
                1.48864681368493396752E-9,
                5.21265281010541664570E-8,
                1.27258478273186970203E-6,
                2.32490249820789513991E-5,
                3.25524741826057911661E-4,
                3.48805814657162590916E-3,
                2.79448531198828973716E-2,
                1.58874241960120565368E-1,
                5.74918629489320327824E-1,
                1.00000000000000000539E0,
        };

        /* interval 3.25 to 6.25 */
        private static final double[] BN = {
                5.08955156417900903354E-1,
                -2.44754418142697847934E-1,
                9.41512335303534411857E-2,
                -2.18711255142039025206E-2,
                3.66207612329569181322E-3,
                -4.23209114460388756528E-4,
                3.59641304793896631888E-5,
                -2.14640351719968974225E-6,
                9.10010780076391431042E-8,
                -2.40274520828250956942E-9,
                3.59233385440928410398E-11,
        };

        private static final double[] BD = {
                /*  1.00000000000000000000E0, */
                -6.31839869873368190192E-1,
                2.36706788228248691528E-1,
                -5.31806367003223277662E-2,
                8.48041718586295374409E-3,
                -9.47996768486665330168E-4,
                7.81025592944552338085E-5,
                -4.55875153252442634831E-6,
                1.89100358111421846170E-7,
                -4.91324691331920606875E-9,
                7.18466403235734541950E-11,
        };

        /* 6.25 to infinity */
        private static final double[] CN = {
                -5.90592860534773254987E-1,
                6.29235242724368800674E-1,
                -1.72858975380388136411E-1,
                1.64837047825189632310E-2,
                -4.86827613020462700845E-4,
        };

        private static final double[] CD = {
                /* 1.00000000000000000000E0, */
                -2.69820057197544900361E0,
                1.73270799045947845857E0,
                -3.93708582281939493482E-1,
                3.44278924041233391079E-2,
                -9.73655226040941223894E-4,
        };

        public static double dawsn(double xx) {
            double x, y;
            int sign;


            sign = 1;
            if (xx < 0.0) {
                sign = -1;
                xx = -xx;
            }

            if (xx < 3.25) {
                x = xx * xx;
                y = xx * polevl(x, AN, 9) / polevl(x, AD, 10);
                return (sign * y);
            }


            x = 1.0 / (xx * xx);

            if (xx < 6.25) {
                y = 1.0 / xx + x * polevl(x, BN, 10) / (p1evl(x, BD, 10) * xx);
                return (sign * 0.5 * y);
            }


            if (xx > 1.0e9)
                return ((sign * 0.5) / xx);

            /* 6.25 to infinity */
            y = 1.0 / xx + x * polevl(x, CN, 4) / (p1evl(x, CD, 5) * xx);
            return (sign * 0.5 * y);
        }

    }

    static final class spence_c {
        private spence_c() {

        }

        static double spence(double x) {
            double w, y, z;
            int flag;

            if (x < 0.0) {
                //  sf_error("spence", SF_ERROR_DOMAIN, NULL);
                return (Double.NaN);
            }

            if (x == 1.0)
                return (0.0);

            if (x == 0.0)
                return (Cephes.NPY_PI * Cephes.NPY_PI / 6.0);

            flag = 0;

            if (x > 2.0) {
                x = 1.0 / x;
                flag |= 2;
            }

            if (x > 1.5) {
                w = (1.0 / x) - 1.0;
                flag |= 2;
            } else if (x < 0.5) {
                w = -x;
                flag |= 1;
            } else
                w = x - 1.0;

            final double[] A = {
                    4.65128586073990045278E-5,
                    7.31589045238094711071E-3,
                    1.33847639578309018650E-1,
                    8.79691311754530315341E-1,
                    2.71149851196553469920E0,
                    4.25697156008121755724E0,
                    3.29771340985225106936E0,
                    1.00000000000000000126E0,
            };
            final double[] B = {
                    6.90990488912553276999E-4,
                    2.54043763932544379113E-2,
                    2.82974860602568089943E-1,
                    1.41172597751831069617E0,
                    3.63800533345137075418E0,
                    5.03278880143316990390E0,
                    3.54771340985225096217E0,
                    9.99999999999999998740E-1,

            };
            y = -w * polevl(w,
                    A
                    , 7) / polevl(w,
                    B
                    , 7);

            if ((flag & 1) != 0)
                y = (Cephes.NPY_PI * Cephes.NPY_PI) / 6.0 - Math.log(x) * Math.log(1.0 - x) - y;

            if ((flag & 2) != 0) {
                z = Math.log(x);
                y = -0.5 * z * z - y;
            }

            return (y);
        }


    }
    /*
     *     Gamma function
     *
     *
     *
     * SYNOPSIS:
     *
     * double x, y, Gamma();
     *
     * y = Gamma( x );
     *
     *
     *
     * DESCRIPTION:
     *
     * Returns Gamma function of the argument.  The result is
     * correctly signed.
     *
     * Arguments |x| <= 34 are reduced by recurrence and the function
     * approximated by a rational function of degree 6/7 in the
     * interval (2,3).  Large arguments are handled by Stirling's
     * formula. Large negative arguments are made positive using
     * a reflection formula.
     *
     *
     * ACCURACY:
     *
     *                      Relative error:
     * arithmetic   domain     # trials      peak         rms
     *    IEEE    -170,-33      20000       2.3e-15     3.3e-16
     *    IEEE     -33,  33     20000       9.4e-16     2.2e-16
     *    IEEE      33, 171.6   20000       2.3e-15     3.2e-16
     *
     * Error for arguments outside the test range will be larger
     * owing to error amplification by the exponential function.
     *
     */

    /*                                                     lgam()
     *
     *     Natural logarithm of Gamma function
     *
     *
     *
     * SYNOPSIS:
     *
     * double x, y, lgam();
     *
     * y = lgam( x );
     *
     *
     *
     * DESCRIPTION:
     *
     * Returns the base e (2.718...) logarithm of the absolute
     * value of the Gamma function of the argument.
     *
     * For arguments greater than 13, the logarithm of the Gamma
     * function is approximated by the logarithmic version of
     * Stirling's formula using a polynomial approximation of
     * degree 4. Arguments between -33 and +33 are reduced by
     * recurrence to the interval [2,3] of a rational approximation.
     * The cosecant reflection formula is employed for arguments
     * less than -33.
     *
     * Arguments greater than MAXLGM return NPY_INFINITY and an error
     * message.  MAXLGM = 2.556348e305 for IEEE arithmetic.
     *
     *
     *
     * ACCURACY:
     *
     *
     * arithmetic      domain        # trials     peak         rms
     *    IEEE    0, 3                 28000     5.4e-16     1.1e-16
     *    IEEE    2.718, 2.556e305     40000     3.5e-16     8.3e-17
     * The error criterion was relative when the function magnitude
     * was greater than one but absolute when it was less than one.
     *
     * The following test used the relative error criterion, though
     * at certain points the relative error could be much higher than
     * indicated.
     *    IEEE    -200, -4             10000     4.8e-16     1.3e-16
     *
     */

    /*
     * Cephes Math Library Release 2.2:  July, 1992
     * Copyright 1984, 1987, 1989, 1992 by Stephen L. Moshier
     * Direct inquiries to 30 Frost Street, Cambridge, MA 02140
     */

    static final class gamma_c {
        private gamma_c() {

        }

        final static double[] P = {
                1.60119522476751861407E-4,
                1.19135147006586384913E-3,
                1.04213797561761569935E-2,
                4.76367800457137231464E-2,
                2.07448227648435975150E-1,
                4.94214826801497100753E-1,
                9.99999999999999996796E-1
        };

        final static double[] Q = {
                -2.31581873324120129819E-5,
                5.39605580493303397842E-4,
                -4.45641913851797240494E-3,
                1.18139785222060435552E-2,
                3.58236398605498653373E-2,
                -2.34591795718243348568E-1,
                7.14304917030273074085E-2,
                1.00000000000000000320E0
        };

        final static double MAXGAM = 171.624376956302725;
        final static double LOGPI = 1.14472988584940017414;

        /* Stirling's formula for the Gamma function */
        final static double[] STIR = {
                7.87311395793093628397E-4,
                -2.29549961613378126380E-4,
                -2.68132617805781232825E-3,
                3.47222221605458667310E-3,
                8.33333333333482257126E-2,
        };

        final static double MAXSTIR = 143.01608;
        final static double SQTPI = 2.50662827463100050242E0;


        /* Gamma function computed by Stirling's formula.
         * The polynomial STIR is valid for 33 <= x <= 172.
         */
        public static double stirf(double x) {
            double y, w, v;

            if (x >= MAXGAM) {
                return Double.POSITIVE_INFINITY;
            }
            w = 1.0 / x;
            w = 1.0 + w * polevl(w, STIR, 4);
            y = exp(x);
            if (x > MAXSTIR) {        /* Avoid overflow in pow() */
                v = pow(x, 0.5 * x - 0.25);
                y = v * (v / y);
            } else {
                y = pow(x, x - 0.5) / y;
            }
            y = SQTPI * y * w;
            return y;
        }


        public static double Gamma(double x) throws OverflowException {
            double p, q, z;
            int i;
            int sgngam = 1;

            if (!Double.isFinite(x)) {
                return x;
            }
            q = abs(x);

            if (q > 33.0) {
                if (x < 0.0) {
                    p = floor(q);
                    if (p == q) {
                        return OverflowException.raiseException("Gamma", Double.POSITIVE_INFINITY);
                    }
                    i = (int) p;
                    if ((i & 1) == 0)
                        sgngam = -1;
                    z = q - p;
                    if (z > 0.5) {
                        p += 1.0;
                        z = q - p;
                    }
                    z = q * sin(Cephes.NPY_PI * z);
                    if (z == 0.0) {
                        return sgngam * Double.POSITIVE_INFINITY;
                    }
                    z = abs(z);
                    z = Cephes.NPY_PI / (z * stirf(q));
                } else {
                    z = stirf(x);
                }
                return (sgngam * z);
            }

            z = 1.0;
            while (x >= 3.0) {
                x -= 1.0;
                z *= x;
            }

            while (x < 0.0) {
                if (x > -1.E-9) {
                    return x == 0 ? OverflowException.raiseException("Gamma", Double.POSITIVE_INFINITY) : (z / ((1.0 + 0.5772156649015329 * x) * x));
                }
                z /= x;
                x += 1.0;
            }

            while (x < 2.0) {
                if (x < 1.e-9) {
                    return x == 0 ? OverflowException.raiseException("Gamma", Double.POSITIVE_INFINITY) : (z / ((1.0 + 0.5772156649015329 * x) * x));
                }
                z /= x;
                x += 1.0;
            }

            if (x == 2.0) {
                return (z);
            }
            x -= 2.0;
            p = polevl(x, P, 6);
            q = polevl(x, Q, 7);
            return (z * p / q);
        }


        /* A[]: Stirling's formula expansion of log Gamma
         * B[], C[]: log Gamma function between 2 and 3
         */
        static double[] A = {
                8.11614167470508450300E-4,
                -5.95061904284301438324E-4,
                7.93650340457716943945E-4,
                -2.77777777730099687205E-3,
                8.33333333333331927722E-2
        };

        static double[] B = {
                -1.37825152569120859100E3,
                -3.88016315134637840924E4,
                -3.31612992738871184744E5,
                -1.16237097492762307383E6,
                -1.72173700820839662146E6,
                -8.53555664245765465627E5
        };

        static double[] C = {
                /* 1.00000000000000000000E0, */
                -3.51815701436523470549E2,
                -1.70642106651881159223E4,
                -2.20528590553854454839E5,
                -1.13933444367982507207E6,
                -2.53252307177582951285E6,
                -2.01889141433532773231E6
        };

        /* log( sqrt( 2*pi ) ) */
        static final double LS2PI = 0.91893853320467274178;

        static final double MAXLGM = 2.556348e305;

        /* Logarithm of Gamma function */
        static double lgam(double x) {
            return lgam_sgn(x, new int[1]);
        }

        static double lgam_sgn(double x, int[] sign) throws SingularException {
            double p, q, u, w, z;
            int i;

            sign[0] = 1;

            if (!Double.isFinite(x))
                return x;

            if (x < -34.0) {
                q = -x;
                w = lgam_sgn(q, sign);
                p = floor(q);
                if (p == q) {
                    return SingularException.raiseException("lgam", Double.POSITIVE_INFINITY);
                }
                i = (int) p;
                if ((i & 1) == 0)
                    sign[0] = -1;
                else {
                    sign[0] = 1;
                }
                z = q - p;
                if (z > 0.5) {
                    p += 1.0;
                    z = p - q;
                }
                z = q * sin(Cephes.NPY_PI * z);
                if (z == 0.0)
                    return SingularException.raiseException("lgam", Double.POSITIVE_INFINITY);
                /*     z = log(NPY_PI) - log( z ) - w; */
                z = LOGPI - log(z) - w;
                return (z);
            }

            if (x < 13.0) {
                z = 1.0;
                p = 0.0;
                u = x;
                while (u >= 3.0) {
                    p -= 1.0;
                    u = x + p;
                    z *= u;
                }
                while (u < 2.0) {
                    if (u == 0.0) {
                        return SingularException.raiseException("lgam", Double.POSITIVE_INFINITY);
                    }
                    z /= u;
                    p += 1.0;
                    u = x + p;
                }
                if (z < 0.0) {
                    sign[0] = -1;
                    z = -z;
                } else {
                    sign[0] = 1;

                }
                if (u == 2.0)
                    return (log(z));
                p -= 2.0;
                x = x + p;
                p = x * polevl(x, B, 5) / p1evl(x, C, 6);
                return (log(z) + p);
            }

            if (x > MAXLGM) {
                return sign[0] * Double.POSITIVE_INFINITY;
            }

            q = (x - 0.5) * log(x) - x + LS2PI;
            if (x > 1.0e8) {
                return (q);
            }
            p = 1.0 / (x * x);
            if (x >= 1000.0) {
                q += ((7.9365079365079365079365e-4 * p
                        - 2.7777777777777777777778e-3) * p
                        + 0.0833333333333333333333) / x;
            } else {
                q += polevl(p, A, 4) / x;
            }
            return (q);
        }

    }
    /*                                                     igam.c
     *
     *     Incomplete Gamma integral
     *
     *
     *
     * SYNOPSIS:
     *
     * double a, x, y, igam();
     *
     * y = igam( a, x );
     *
     * DESCRIPTION:
     *
     * The function is defined by
     *
     *                           x
     *                            -
     *                   1       | |  -t  a-1
     *  igam(a,x)  =   -----     |   e   t   dt.
     *                  -      | |
     *                 | (a)    -
     *                           0
     *
     *
     * In this implementation both arguments must be positive.
     * The integral is evaluated by either a power series or
     * continued fraction expansion, depending on the relative
     * values of a and x.
     *
     * ACCURACY:
     *
     *                      Relative error:
     * arithmetic   domain     # trials      peak         rms
     *    IEEE      0,30       200000       3.6e-14     2.9e-15
     *    IEEE      0,100      300000       9.9e-14     1.5e-14
     */
    /*							igamc()
     *
     *	Complemented incomplete Gamma integral
     *
     *
     *
     * SYNOPSIS:
     *
     * double a, x, y, igamc();
     *
     * y = igamc( a, x );
     *
     * DESCRIPTION:
     *
     * The function is defined by
     *
     *
     *  igamc(a,x)   =   1 - igam(a,x)
     *
     *                            inf.
     *                              -
     *                     1       | |  -t  a-1
     *               =   -----     |   e   t   dt.
     *                    -      | |
     *                   | (a)    -
     *                             x
     *
     *
     * In this implementation both arguments must be positive.
     * The integral is evaluated by either a power series or
     * continued fraction expansion, depending on the relative
     * values of a and x.
     *
     * ACCURACY:
     *
     * Tested at random a, x.
     *                a         x                      Relative error:
     * arithmetic   domain   domain     # trials      peak         rms
     *    IEEE     0.5,100   0,100      200000       1.9e-14     1.7e-15
     *    IEEE     0.01,0.5  0,100      200000       1.4e-13     1.6e-15
     */

    /*
     * Cephes Math Library Release 2.0:  April, 1987
     * Copyright 1985, 1987 by Stephen L. Moshier
     * Direct inquiries to 30 Frost Street, Cambridge, MA 02140
     */

    /* Sources
     * [1] "The Digital Library of Mathematical Functions", dlmf.nist.gov
     * [2] Maddock et. al., "Incomplete Gamma Functions",
     *     https://www.boost.org/doc/libs/1_61_0/libs/math/doc/html/math_toolkit/sf_gamma/igamma.html
     */

    /* Scipy changes:
     * - 05-01-2016: added asymptotic expansion for igam to improve the
     *   a ~ x regime.
     * - 06-19-2016: additional series expansion added for igamc to
     *   improve accuracy at small arguments.
     * - 06-24-2016: better choice of domain for the asymptotic series;
     *   improvements in accuracy for the asymptotic series when a and x
     *   are very close.
     */

    static final class igam_c {
        private igam_c() {

        }

        static final int MAXITER = 2000;
        static final int IGAM = 1;
        static final int IGAMC = 0;
        static final int SMALL = 20;
        static final int LARGE = 200;
        static final double SMALLRATIO = 0.3;
        static final double LARGERATIO = 4.5;

        static double big = 4.503599627370496e15;
        static double biginv = 2.22044604925031308085e-16;

        static final int K = 25;
        static final int N = 25;

        static final double[][] d
                =

                {
                        {
                                -3.3333333333333333e-1, 8.3333333333333333e-2, -1.4814814814814815e-2, 1.1574074074074074e-3, 3.527336860670194e-4, -1.7875514403292181e-4, 3.9192631785224378e-5, -2.1854485106799922e-6, -1.85406221071516e-6, 8.296711340953086e-7, -1.7665952736826079e-7, 6.7078535434014986e-9, 1.0261809784240308e-8, -4.3820360184533532e-9, 9.1476995822367902e-10, -2.551419399494625e-11, -5.8307721325504251e-11, 2.4361948020667416e-11, -5.0276692801141756e-12, 1.1004392031956135e-13, 3.3717632624009854e-13, -1.3923887224181621e-13, 2.8534893807047443e-14, -5.1391118342425726e-16, -1.9752288294349443e-15
                        },
                        {
                                -1.8518518518518519e-3, -3.4722222222222222e-3, 2.6455026455026455e-3, -9.9022633744855967e-4, 2.0576131687242798e-4, -4.0187757201646091e-7, -1.8098550334489978e-5, 7.6491609160811101e-6, -1.6120900894563446e-6, 4.6471278028074343e-9, 1.378633446915721e-7, -5.752545603517705e-8, 1.1951628599778147e-8, -1.7543241719747648e-11, -1.0091543710600413e-9, 4.1627929918425826e-10, -8.5639070264929806e-11, 6.0672151016047586e-14, 7.1624989648114854e-12, -2.9331866437714371e-12, 5.9966963656836887e-13, -2.1671786527323314e-16, -4.9783399723692616e-14, 2.0291628823713425e-14, -4.13125571381061e-15
                        },
                        {
                                4.1335978835978836e-3, -2.6813271604938272e-3, 7.7160493827160494e-4, 2.0093878600823045e-6, -1.0736653226365161e-4, 5.2923448829120125e-5, -1.2760635188618728e-5, 3.4235787340961381e-8, 1.3721957309062933e-6, -6.298992138380055e-7, 1.4280614206064242e-7, -2.0477098421990866e-10, -1.4092529910867521e-8, 6.228974084922022e-9, -1.3670488396617113e-9, 9.4283561590146782e-13, 1.2872252400089318e-10, -5.5645956134363321e-11, 1.1975935546366981e-11, -4.1689782251838635e-15, -1.0940640427884594e-12, 4.6622399463901357e-13, -9.905105763906906e-14, 1.8931876768373515e-17, 8.8592218725911273e-15
                        },
                        {
                                6.4943415637860082e-4, 2.2947209362139918e-4, -4.6918949439525571e-4, 2.6772063206283885e-4, -7.5618016718839764e-5, -2.3965051138672967e-7, 1.1082654115347302e-5, -5.6749528269915966e-6, 1.4230900732435884e-6, -2.7861080291528142e-11, -1.6958404091930277e-7, 8.0994649053880824e-8, -1.9111168485973654e-8, 2.3928620439808118e-12, 2.0620131815488798e-9, -9.4604966618551322e-10, 2.1541049775774908e-10, -1.388823336813903e-14, -2.1894761681963939e-11, 9.7909989511716851e-12, -2.1782191880180962e-12, 6.2088195734079014e-17, 2.126978363279737e-13, -9.3446887915174333e-14, 2.0453671226782849e-14
                        },
                        {
                                -8.618882909167117e-4, 7.8403922172006663e-4, -2.9907248030319018e-4, -1.4638452578843418e-6, 6.6414982154651222e-5, -3.9683650471794347e-5, 1.1375726970678419e-5, 2.5074972262375328e-10, -1.6954149536558306e-6, 8.9075075322053097e-7, -2.2929348340008049e-7, 2.956794137544049e-11, 2.8865829742708784e-8, -1.4189739437803219e-8, 3.4463580499464897e-9, -2.3024517174528067e-13, -3.9409233028046405e-10, 1.8602338968504502e-10, -4.356323005056618e-11, 1.2786001016296231e-15, 4.6792750266579195e-12, -2.1492464706134829e-12, 4.9088156148096522e-13, -6.3385914848915603e-18, -5.0453320690800944e-14
                        },
                        {
                                -3.3679855336635815e-4, -6.9728137583658578e-5, 2.7727532449593921e-4, -1.9932570516188848e-4, 6.7977804779372078e-5, 1.419062920643967e-7, -1.3594048189768693e-5, 8.0184702563342015e-6, -2.2914811765080952e-6, -3.252473551298454e-10, 3.4652846491085265e-7, -1.8447187191171343e-7, 4.8240967037894181e-8, -1.7989466721743515e-14, -6.3061945000135234e-9, 3.1624176287745679e-9, -7.8409242536974293e-10, 5.1926791652540407e-15, 9.3589442423067836e-11, -4.5134262161632782e-11, 1.0799129993116827e-11, -3.661886712685252e-17, -1.210902069055155e-12, 5.6807435849905643e-13, -1.3249659916340829e-13
                        },
                        {
                                5.3130793646399222e-4, -5.9216643735369388e-4, 2.7087820967180448e-4, 7.9023532326603279e-7, -8.1539693675619688e-5, 5.6116827531062497e-5, -1.8329116582843376e-5, -3.0796134506033048e-9, 3.4651553688036091e-6, -2.0291327396058604e-6, 5.7887928631490037e-7, 2.338630673826657e-13, -8.8286007463304835e-8, 4.7435958880408128e-8, -1.2545415020710382e-8, 8.6496488580102925e-14, 1.6846058979264063e-9, -8.5754928235775947e-10, 2.1598224929232125e-10, -7.6132305204761539e-16, -2.6639822008536144e-11, 1.3065700536611057e-11, -3.1799163902367977e-12, 4.7109761213674315e-18, 3.6902800842763467e-13
                        },
                        {
                                3.4436760689237767e-4, 5.1717909082605922e-5, -3.3493161081142236e-4, 2.812695154763237e-4, -1.0976582244684731e-4, -1.2741009095484485e-7, 2.7744451511563644e-5, -1.8263488805711333e-5, 5.7876949497350524e-6, 4.9387589339362704e-10, -1.0595367014026043e-6, 6.1667143761104075e-7, -1.7562973359060462e-7, -1.2974473287015439e-12, 2.695423606288966e-8, -1.4578352908731271e-8, 3.887645959386175e-9, -3.8810022510194121e-17, -5.3279941738772867e-10, 2.7437977643314845e-10, -6.9957960920705679e-11, 2.5899863874868481e-17, 8.8566890996696381e-12, -4.403168815871311e-12, 1.0865561947091654e-12
                        },
                        {
                                -6.5262391859530942e-4, 8.3949872067208728e-4, -4.3829709854172101e-4, -6.969091458420552e-7, 1.6644846642067548e-4, -1.2783517679769219e-4, 4.6299532636913043e-5, 4.5579098679227077e-9, -1.0595271125805195e-5, 6.7833429048651666e-6, -2.1075476666258804e-6, -1.7213731432817145e-11, 3.7735877416110979e-7, -2.1867506700122867e-7, 6.2202288040189269e-8, 6.5977038267330006e-16, -9.5903864974256858e-9, 5.2132144922808078e-9, -1.3991589583935709e-9, 5.382058999060575e-16, 1.9484714275467745e-10, -1.0127287556389682e-10, 2.6077347197254926e-11, -5.0904186999932993e-18, -3.3721464474854592e-12
                        },
                        {
                                -5.9676129019274625e-4, -7.2048954160200106e-5, 6.7823088376673284e-4, -6.4014752602627585e-4, 2.7750107634328704e-4, 1.8197008380465151e-7, -8.4795071170685032e-5, 6.105192082501531e-5, -2.1073920183404862e-5, -8.8585890141255994e-10, 4.5284535953805377e-6, -2.8427815022504408e-6, 8.7082341778646412e-7, 3.6886101871706965e-12, -1.5344695190702061e-7, 8.862466778790695e-8, -2.5184812301826817e-8, -1.0225912098215092e-14, 3.8969470758154777e-9, -2.1267304792235635e-9, 5.7370135528051385e-10, -1.887749850169741e-19, -8.0931538694657866e-11, 4.2382723283449199e-11, -1.1002224534207726e-11
                        },
                        {
                                1.3324454494800656e-3, -1.9144384985654775e-3, 1.1089369134596637e-3, 9.932404122642299e-7, -5.0874501293093199e-4, 4.2735056665392884e-4, -1.6858853767910799e-4, -8.1301893922784998e-9, 4.5284402370562147e-5, -3.127053674781734e-5, 1.044986828530338e-5, 4.8435226265680926e-11, -2.1482565873456258e-6, 1.329369701097492e-6, -4.0295693092101029e-7, -1.7567877666323291e-13, 7.0145043163668257e-8, -4.040787734999483e-8, 1.1474026743371963e-8, 3.9642746853563325e-18, -1.7804938269892714e-9, 9.7480262548731646e-10, -2.6405338676507616e-10, 5.794875163403742e-18, 3.7647749553543836e-11
                        },
                        {
                                1.579727660730835e-3, 1.6251626278391582e-4, -2.0633421035543276e-3, 2.1389686185689098e-3, -1.0108559391263003e-3, -3.9912705529919201e-7, 3.6235025084764691e-4, -2.8143901463712154e-4, 1.0449513336495887e-4, 2.1211418491830297e-9, -2.5779417251947842e-5, 1.7281818956040463e-5, -5.6413773872904282e-6, -1.1024320105776174e-11, 1.1223224418895175e-6, -6.8693396379526735e-7, 2.0653236975414887e-7, 4.6714772409838506e-14, -3.5609886164949055e-8, 2.0470855345905963e-8, -5.8091738633283358e-9, -1.332821287582869e-16, 9.0354604391335133e-10, -4.9598782517330834e-10, 1.3481607129399749e-10
                        },
                        {
                                -4.0725121195140166e-3, 6.4033628338080698e-3, -4.0410161081676618e-3, -2.183732802866233e-6, 2.1740441801254639e-3, -1.9700440518418892e-3, 8.3595469747962458e-4, 1.9445447567109655e-8, -2.5779387120421696e-4, 1.9009987368139304e-4, -6.7696499937438965e-5, -1.4440629666426572e-10, 1.5712512518742269e-5, -1.0304008744776893e-5, 3.304517767401387e-6, 7.9829760242325709e-13, -6.4097794149313004e-7, 3.8894624761300056e-7, -1.1618347644948869e-7, -2.816808630596451e-15, 1.9878012911297093e-8, -1.1407719956357511e-8, 3.2355857064185555e-9, 4.1759468293455945e-20, -5.0423112718105824e-10
                        },
                        {
                                -5.9475779383993003e-3, -5.4016476789260452e-4, 8.7910413550767898e-3, -9.8576315587856125e-3, 5.0134695031021538e-3, 1.2807521786221875e-6, -2.0626019342754683e-3, 1.7109128573523058e-3, -6.7695312714133799e-4, -6.9011545676562133e-9, 1.8855128143995902e-4, -1.3395215663491969e-4, 4.6263183033528039e-5, 4.0034230613321351e-11, -1.0255652921494033e-5, 6.612086372797651e-6, -2.0913022027253008e-6, -2.0951775649603837e-13, 3.9756029041993247e-7, -2.3956211978815887e-7, 7.1182883382145864e-8, 8.925574873053455e-16, -1.2101547235064676e-8, 6.9350618248334386e-9, -1.9661464453856102e-9
                        },
                        {
                                1.7402027787522711e-2, -2.9527880945699121e-2, 2.0045875571402799e-2, 7.0289515966903407e-6, -1.2375421071343148e-2, 1.1976293444235254e-2, -5.4156038466518525e-3, -6.3290893396418616e-8, 1.8855118129005065e-3, -1.473473274825001e-3, 5.5515810097708387e-4, 5.2406834412550662e-10, -1.4357913535784836e-4, 9.9181293224943297e-5, -3.3460834749478311e-5, -3.5755837291098993e-12, 7.1560851960630076e-6, -4.5516802628155526e-6, 1.4236576649271475e-6, 1.8803149082089664e-14, -2.6623403898929211e-7, 1.5950642189595716e-7, -4.7187514673841102e-8, -6.5107872958755177e-17, 7.9795091026746235e-9
                        },
                        {
                                3.0249124160905891e-2, 2.4817436002649977e-3, -4.9939134373457022e-2, 5.9915643009307869e-2, -3.2483207601623391e-2, -5.7212968652103441e-6, 1.5085251778569354e-2, -1.3261324005088445e-2, 5.5515262632426148e-3, 3.0263182257030016e-8, -1.7229548406756723e-3, 1.2893570099929637e-3, -4.6845138348319876e-4, -1.830259937893045e-10, 1.1449739014822654e-4, -7.7378565221244477e-5, 2.5625836246985201e-5, 1.0766165333192814e-12, -5.3246809282422621e-6, 3.349634863064464e-6, -1.0381253128684018e-6, -5.608909920621128e-15, 1.9150821930676591e-7, -1.1418365800203486e-7, 3.3654425209171788e-8
                        },
                        {
                                -9.9051020880159045e-2, 1.7954011706123486e-1, -1.2989606383463778e-1, -3.1478872752284357e-5, 9.0510635276848131e-2, -9.2828824411184397e-2, 4.4412112839877808e-2, 2.7779236316835888e-7, -1.7229543805449697e-2, 1.4182925050891573e-2, -5.6214161633747336e-3, -2.39598509186381e-9, 1.6029634366079908e-3, -1.1606784674435773e-3, 4.1001337768153873e-4, 1.8365800754090661e-11, -9.5844256563655903e-5, 6.3643062337764708e-5, -2.076250624489065e-5, -1.1806020912804483e-13, 4.2131808239120649e-6, -2.6262241337012467e-6, 8.0770620494930662e-7, 6.0125912123632725e-16, -1.4729737374018841e-7
                        },
                        {
                                -1.9994542198219728e-1, -1.5056113040026424e-2, 3.6470239469348489e-1, -4.6435192311733545e-1, 2.6640934719197893e-1, 3.4038266027147191e-5, -1.3784338709329624e-1, 1.276467178337056e-1, -5.6213828755200985e-2, -1.753150885483011e-7, 1.9235592956768113e-2, -1.5088821281095315e-2, 5.7401854451350123e-3, 1.0622382710310225e-9, -1.5335082692563998e-3, 1.0819320643228214e-3, -3.7372510193945659e-4, -6.6170909729031985e-12, 8.4263617380909628e-5, -5.5150706827483479e-5, 1.7769536448348069e-5, 3.8827923210205533e-14, -3.53513697488768e-6, 2.1865832130045269e-6, -6.6812849447625594e-7
                        },
                        {
                                7.2438608504029431e-1, -1.3918010932653375, 1.0654143352413968, 1.876173868950258e-4, -8.2705501176152696e-1, 8.9352433347828414e-1, -4.4971003995291339e-1, -1.6107401567546652e-6, 1.9235590165271091e-1, -1.6597702160042609e-1, 6.8882222681814333e-2, 1.3910091724608687e-8, -2.146911561508663e-2, 1.6228980898865892e-2, -5.9796016172584256e-3, -1.1287469112826745e-10, 1.5167451119784857e-3, -1.0478634293553899e-3, 3.5539072889126421e-4, 8.1704322111801517e-13, -7.7773013442452395e-5, 5.0291413897007722e-5, -1.6035083867000518e-5, 1.2469354315487605e-14, 3.1369106244517615e-6
                        },
                        {
                                1.6668949727276811, 1.165462765994632e-1, -3.3288393225018906, 4.4692325482864037, -2.6977693045875807, -2.600667859891061e-4, 1.5389017615694539, -1.4937962361134612, 6.8881964633233148e-1, 1.3077482004552385e-6, -2.5762963325596288e-1, 2.1097676102125449e-1, -8.3714408359219882e-2, -7.7920428881354753e-9, 2.4267923064833599e-2, -1.7813678334552311e-2, 6.3970330388900056e-3, 4.9430807090480523e-11, -1.5554602758465635e-3, 1.0561196919903214e-3, -3.5277184460472902e-4, 9.3002334645022459e-14, 7.5285855026557172e-5, -4.8186515569156351e-5, 1.5227271505597605e-5
                        },
                        {
                                -6.6188298861372935, 1.3397985455142589e+1, -1.0789350606845146e+1, -1.4352254537875018e-3, 9.2333694596189809, -1.0456552819547769e+1, 5.5105526029033471, 1.2024439690716742e-5, -2.5762961164755816, 2.3207442745387179, -1.0045728797216284, -1.0207833290021914e-7, 3.3975092171169466e-1, -2.6720517450757468e-1, 1.0235252851562706e-1, 8.4329730484871625e-10, -2.7998284958442595e-2, 2.0066274144976813e-2, -7.0554368915086242e-3, 1.9402238183698188e-12, 1.6562888105449611e-3, -1.1082898580743683e-3, 3.654545161310169e-4, -5.1290032026971794e-11, -7.6340103696869031e-5
                        },
                        {
                                -1.7112706061976095e+1, -1.1208044642899116, 3.7131966511885444e+1, -5.2298271025348962e+1, 3.3058589696624618e+1, 2.4791298976200222e-3, -2.061089403411526e+1, 2.088672775145582e+1, -1.0045703956517752e+1, -1.2238783449063012e-5, 4.0770134274221141, -3.473667358470195, 1.4329352617312006, 7.1359914411879712e-8, -4.4797257159115612e-1, 3.4112666080644461e-1, -1.2699786326594923e-1, -2.8953677269081528e-10, 3.3125776278259863e-2, -2.3274087021036101e-2, 8.0399993503648882e-3, -1.177805216235265e-9, -1.8321624891071668e-3, 1.2108282933588665e-3, -3.9479941246822517e-4
                        },
                        {
                                7.389033153567425e+1, -1.5680141270402273e+2, 1.322177542759164e+2, 1.3692876877324546e-2, -1.2366496885920151e+2, 1.4620689391062729e+2, -8.0365587724865346e+1, -1.1259851148881298e-4, 4.0770132196179938e+1, -3.8210340013273034e+1, 1.719522294277362e+1, 9.3519707955168356e-7, -6.2716159907747034, 5.1168999071852637, -2.0319658112299095, -4.9507215582761543e-9, 5.9626397294332597e-1, -4.4220765337238094e-1, 1.6079998700166273e-1, -2.4733786203223402e-8, -4.0307574759979762e-2, 2.7849050747097869e-2, -9.4751858992054221e-3, 6.419922235909132e-6, 2.1250180774699461e-3
                        },
                        {
                                2.1216837098382522e+2, 1.3107863022633868e+1, -4.9698285932871748e+2, 7.3121595266969204e+2, -4.8213821720890847e+2, -2.8817248692894889e-2, 3.2616720302947102e+2, -3.4389340280087117e+2, 1.7195193870816232e+2, 1.4038077378096158e-4, -7.52594195897599e+1, 6.651969984520934e+1, -2.8447519748152462e+1, -7.613702615875391e-7, 9.5402237105304373, -7.5175301113311376, 2.8943997568871961, -4.6612194999538201e-7, -8.0615149598794088e-1, 5.8483006570631029e-1, -2.0845408972964956e-1, 1.4765818959305817e-4, 5.1000433863753019e-2, -3.3066252141883665e-2, 1.5109265210467774e-2
                        },
                        {
                                -9.8959643098322368e+2, 2.1925555360905233e+3, -1.9283586782723356e+3, -1.5925738122215253e-1, 1.9569985945919857e+3, -2.4072514765081556e+3, 1.3756149959336496e+3, 1.2920735237496668e-3, -7.525941715948055e+2, 7.3171668742208716e+2, -3.4137023466220065e+2, -9.9857390260608043e-6, 1.3356313181291573e+2, -1.1276295161252794e+2, 4.6310396098204458e+1, -7.9237387133614756e-6, -1.4510726927018646e+1, 1.1111771248100563e+1, -4.1690817945270892, 3.1008219800117808e-3, 1.1220095449981468, -7.6052379926149916e-1, 3.6262236505085254e-1, 2.216867741940747e-1, 4.8683443692930507e-1
                        }
                };


        public static double igam(double a, double x) throws DomainException {
            double absxma_a;

            if (x < 0 || a < 0) {
                return DomainException.raiseException("gammainc", Double.NaN);
            } else if (a == 0) {
                if (x > 0) {
                    return 1;
                } else {
                    return Double.NaN;
                }
            } else if (x == 0) {
                /* Zero integration limit */
                return 0;
            } else if (Double.isInfinite(a)) {
                if (Double.isInfinite(x)) {
                    return Double.NaN;
                }
                return 0;
            } else if (Double.isInfinite(x)) {
                return 1;
            }

            /* Asymptotic regime where a ~ x; see [2]. */
            absxma_a = abs(x - a) / a;
            if ((a > SMALL) && (a < LARGE) && (absxma_a < SMALLRATIO)) {
                return asymptotic_series(a, x, IGAM);
            } else if ((a > LARGE) && (absxma_a < LARGERATIO / sqrt(a))) {
                return asymptotic_series(a, x, IGAM);
            }

            if ((x > 1.0) && (x > a)) {
                return (1.0 - igamc(a, x));
            }

            return igam_series(a, x);
        }


        public static double igamc(double a, double x) throws DomainException {
            double absxma_a;

            if (x < 0 || a < 0) {
                return DomainException.raiseException("gammaicnc", Double.NaN);
            } else if (a == 0) {
                if (x > 0) {
                    return 0;
                } else {
                    return Double.NaN;
                }
            } else if (x == 0) {
                return 1;
            } else if (Double.isInfinite(a)) {
                if (Double.isInfinite(x)) {
                    return Double.NaN;
                }
                return 1;
            } else if (Double.isInfinite(x)) {
                return 0;
            }

            /* Asymptotic regime where a ~ x; see [2]. */
            absxma_a = abs(x - a) / a;
            if ((a > SMALL) && (a < LARGE) && (absxma_a < SMALLRATIO)) {
                return asymptotic_series(a, x, IGAMC);
            } else if ((a > LARGE) && (absxma_a < LARGERATIO / sqrt(a))) {
                return asymptotic_series(a, x, IGAMC);
            }

            /* Everywhere else; see [2]. */
            if (x > 1.1) {
                if (x < a) {
                    return 1.0 - igam_series(a, x);
                } else {
                    return igamc_continued_fraction(a, x);
                }
            } else if (x <= 0.5) {
                if (-0.4 / log(x) < a) {
                    return 1.0 - igam_series(a, x);
                } else {
                    return igamc_series(a, x);
                }
            } else {
                if (x * 1.1 < a) {
                    return 1.0 - igam_series(a, x);
                } else {
                    return igamc_series(a, x);
                }
            }
        }


        /* Compute
         *
         * x^a * exp(-x) / gamma(a)
         *
         * corrected from (15) and (16) in [2] by replacing exp(x - a) with
         * exp(a - x).
         */
        public static double igam_fac(double a, double x) throws UnderflowException {
            double ax, fac, res, num;

            if (abs(a - x) > 0.4 * abs(a)) {
                ax = a * log(x) - x - lgam(a);
                if (ax < -Cephes.MAXLOG) {
                    return UnderflowException.raiseException("igam", 0.0);
                }
                return exp(ax);
            }

            fac = a + Boost.lanczos_g - 0.5;
            res = sqrt(fac / exp(1)) / Boost.lanczos_sum_expg_scaled(a);

            if ((a < 200) && (x < 200)) {
                res *= exp(a - x) * pow(x / fac, a);
            } else {
                num = x - a - Boost.lanczos_g + 0.5;
                res *= exp(a * log1pmx(num / fac) + x * (0.5 - Boost.lanczos_g) / fac);
            }

            return res;
        }


        /* Compute igamc using DLMF 8.9.2. */
        public static double igamc_continued_fraction(double a, double x) {
            int i;
            double ans, ax, c, yc, r, t, y, z;
            double pk, pkm1, pkm2, qk, qkm1, qkm2;

            ax = igam_fac(a, x);
            if (ax == 0.0) {
                return 0.0;
            }

            /* continued fraction */
            y = 1.0 - a;
            z = x + y + 1.0;
            c = 0.0;
            pkm2 = 1.0;
            qkm2 = x;
            pkm1 = x + 1.0;
            qkm1 = z * x;
            ans = pkm1 / qkm1;

            for (i = 0; i < MAXITER; i++) {
                c += 1.0;
                y += 1.0;
                z += 2.0;
                yc = y * c;
                pk = pkm1 * z - pkm2 * yc;
                qk = qkm1 * z - qkm2 * yc;
                if (qk != 0) {
                    r = pk / qk;
                    t = abs((ans - r) / r);
                    ans = r;
                } else
                    t = 1.0;
                pkm2 = pkm1;
                pkm1 = pk;
                qkm2 = qkm1;
                qkm1 = qk;
                if (abs(pk) > big) {
                    pkm2 *= biginv;
                    pkm1 *= biginv;
                    qkm2 *= biginv;
                    qkm1 *= biginv;
                }
                if (t <= MACHEP) {
                    break;
                }
            }

            return (ans * ax);
        }


        /* Compute igam using DLMF 8.11.4. */
        public static double igam_series(double a, double x) {
            int i;
            double ans, ax, c, r;

            ax = igam_fac(a, x);
            if (ax == 0.0) {
                return 0.0;
            }

            /* power series */
            r = a;
            c = 1.0;
            ans = 1.0;

            for (i = 0; i < MAXITER; i++) {
                r += 1.0;
                c *= x / r;
                ans += c;

                if (c <= MACHEP * ans) {
                    break;
                }
            }

            return (ans * ax / a);
        }


        /* Compute igamc using DLMF 8.7.3. This is related to the series in
         * igam_series but extra care is taken to avoid cancellation.
         */
        public static double igamc_series(double a, double x) {
            int n;
            double fac = 1;
            double sum = 0;
            double term, logx;

            for (n = 1; n < MAXITER; n++) {
                fac *= -x / n;
                term = fac / (a + n);
                sum += term;
                if (abs(term) <= MACHEP * abs(sum)) {
                    break;
                }
            }

            logx = log(x);
            term = -CephesImpl.unity_c.expm1(a * logx - CephesImpl.unity_c.lgam1p(a));
            return term - exp(a * logx - lgam(a)) * sum;
        }


        /* Compute igam/igamc using DLMF 8.12.3/8.12.4. */
        public static double asymptotic_series(double a, double x, int func) {
            int k, n, sgn;
            int maxpow = 0;
            double lambda = x / a;
            double sigma = (x - a) / a;
            double eta, res, ck, ckterm, term, absterm;
            double absoldterm = Double.POSITIVE_INFINITY;
            double[] etapow = new double[N];
            etapow[0] = 1;
            double sum = 0;
            double afac = 1;

            if (func == IGAM) {
                sgn = -1;
            } else {
                sgn = 1;
            }

            if (lambda > 1) {
                eta = sqrt(-2 * log1pmx(sigma));
            } else if (lambda < 1) {
                eta = -sqrt(-2 * log1pmx(sigma));
            } else {
                eta = 0;
            }
            res = 0.5 * CephesImpl.ndtr_c.erfc(sgn * eta * sqrt(a / 2));

            for (k = 0; k < K; k++) {
                ck = d[k][0];
                for (n = 1; n < N; n++) {
                    if (n > maxpow) {
                        etapow[n] = eta * etapow[n - 1];
                        maxpow += 1;
                    }
                    ckterm = d[k][n] * etapow[n];
                    ck += ckterm;
                    if (abs(ckterm) < MACHEP * abs(ck)) {
                        break;
                    }
                }
                term = ck * afac;
                absterm = abs(term);
                if (absterm > absoldterm) {
                    break;
                }
                sum += term;
                if (absterm < MACHEP * abs(sum)) {
                    break;
                }
                absoldterm = absterm;
                afac /= a;
            }
            res += sgn * exp(-0.5 * a * eta * eta) * sum / sqrt(2 * PI * a);

            return res;
        }

    }
    /*                                                     ndtri.c
     *
     *     Inverse of Normal distribution function
     *
     *
     *
     * SYNOPSIS:
     *
     * double x, y, ndtri();
     *
     * x = ndtri( y );
     *
     *
     *
     * DESCRIPTION:
     *
     * Returns the argument, x, for which the area under the
     * Gaussian probability density function (integrated from
     * minus infinity to x) is equal to y.
     *
     *
     * For small arguments 0 < y < exp(-2), the program computes
     * z = sqrt( -2.0 * log(y) );  then the approximation is
     * x = z - log(z)/z  - (1/z) P(1/z) / Q(1/z).
     * There are two rational functions P/Q, one for 0 < y < exp(-32)
     * and the other for y up to exp(-2).  For larger arguments,
     * w = y - 0.5, and  x/sqrt(2pi) = w + w**3 R(w**2)/S(w**2)).
     *
     *
     * ACCURACY:
     *
     *                      Relative error:
     * arithmetic   domain        # trials      peak         rms
     *    IEEE     0.125, 1        20000       7.2e-16     1.3e-16
     *    IEEE     3e-308, 0.135   50000       4.6e-16     9.8e-17
     *
     *
     * ERROR MESSAGES:
     *
     *   message         condition    value returned
     * ndtri domain       x < 0        NPY_NAN
     * ndtri domain       x > 1        NPY_NAN
     *
     */


    /*
     * Cephes Math Library Release 2.1:  January, 1989
     * Copyright 1984, 1987, 1989 by Stephen L. Moshier
     * Direct inquiries to 30 Frost Street, Cambridge, MA 02140
     */

    static final class ndtri_c {
        private ndtri_c() {

        }

        /**
         * Inverse of Normal distribution function
         *
         * @param y0 the value to apply the inverse of normal distribution function
         * @return the inverse of normal distribution function at y0
         */
        public static double ndtri(double y0) throws DomainException {
            double x, y, z, y2, x0, x1;
            int code;

            if (y0 == 0.0) {
                return Double.NEGATIVE_INFINITY;
            }
            if (y0 == 1.0) {
                return Double.POSITIVE_INFINITY;
            }
            if (y0 < 0.0 || y0 > 1.0) {
                return DomainException.raiseException("ndtri", Double.NaN);
            }
            code = 1;
            y = y0;
            if (y > (1.0 - 0.13533528323661269189)) {    /* 0.135... = exp(-2) */
                y = 1.0 - y;
                code = 0;
            }
            /* approximation for 0 <= |y - 0.5| <= 3/8 */
            final double[] P0 = {
                    -5.99633501014107895267E1,
                    9.80010754185999661536E1,
                    -5.66762857469070293439E1,
                    1.39312609387279679503E1,
                    -1.23916583867381258016E0,
            };

            final double[] Q0 = {
                    /* 1.00000000000000000000E0, */
                    1.95448858338141759834E0,
                    4.67627912898881538453E0,
                    8.63602421390890590575E1,
                    -2.25462687854119370527E2,
                    2.00260212380060660359E2,
                    -8.20372256168333339912E1,
                    1.59056225126211695515E1,
                    -1.18331621121330003142E0,
            };

            /* Approximation for interval z = sqrt(-2 log y ) between 2 and 8
             * i.e., y between exp(-2) = .135 and exp(-32) = 1.27e-14.
             */
            final double[] P1 = {
                    4.05544892305962419923E0,
                    3.15251094599893866154E1,
                    5.71628192246421288162E1,
                    4.40805073893200834700E1,
                    1.46849561928858024014E1,
                    2.18663306850790267539E0,
                    -1.40256079171354495875E-1,
                    -3.50424626827848203418E-2,
                    -8.57456785154685413611E-4,
            };

            final double[] Q1 = {
                    /*  1.00000000000000000000E0, */
                    1.57799883256466749731E1,
                    4.53907635128879210584E1,
                    4.13172038254672030440E1,
                    1.50425385692907503408E1,
                    2.50464946208309415979E0,
                    -1.42182922854787788574E-1,
                    -3.80806407691578277194E-2,
                    -9.33259480895457427372E-4,
            };

            /* Approximation for interval z = sqrt(-2 log y ) between 8 and 64
             * i.e., y between exp(-32) = 1.27e-14 and exp(-2048) = 3.67e-890.
             */

            final double[] P2 = {
                    3.23774891776946035970E0,
                    6.91522889068984211695E0,
                    3.93881025292474443415E0,
                    1.33303460815807542389E0,
                    2.01485389549179081538E-1,
                    1.23716634817820021358E-2,
                    3.01581553508235416007E-4,
                    2.65806974686737550832E-6,
                    6.23974539184983293730E-9,
            };

            final double[] Q2 = {
                    /*  1.00000000000000000000E0, */
                    6.02427039364742014255E0,
                    3.67983563856160859403E0,
                    1.37702099489081330271E0,
                    2.16236993594496635890E-1,
                    1.34204006088543189037E-2,
                    3.28014464682127739104E-4,
                    2.89247864745380683936E-6,
                    6.79019408009981274425E-9,
                    0
            };
            if (y > 0.13533528323661269189) {
                y = y - 0.5;
                y2 = y * y;
                x = y + y * (y2 * polevl(y2, P0, 4) / p1evl(y2, Q0, 8));
                x = x * NormalDistributions.PDF_C;
                return x;
            }

            x = sqrt(-2.0 * log(y));
            x0 = x - log(x) / x;

            z = 1.0 / x;
            if (x < 8.0) {    /* y > exp(-32) = 1.2664165549e-14 */
                x1 = z * polevl(z, P1, 8) / p1evl(z, Q1, 8);
            } else {
                x1 = z * polevl(z, P2, 8) / p1evl(z, Q2, 8);
            }
            x = x0 - x1;
            if (code != 0) {
                x = -x;
            }
            return (x);
        }
    }

    static final class i1_c {
        private i1_c() {

        }
        /*                                                     i1.c
         *
         *     Modified Bessel function of order one
         *
         *
         *
         * SYNOPSIS:
         *
         * double x, y, i1();
         *
         * y = i1( x );
         *
         *
         *
         * DESCRIPTION:
         *
         * Returns modified Bessel function of order one of the
         * argument.
         *
         * The function is defined as i1(x) = -i j1( ix ).
         *
         * The range is partitioned into the two intervals [0,8] and
         * (8, infinity).  Chebyshev polynomial expansions are employed
         * in each interval.
         *
         *
         *
         * ACCURACY:
         *
         *                      Relative error:
         * arithmetic   domain     # trials      peak         rms
         *    IEEE      0, 30       30000       1.9e-15     2.1e-16
         *
         *
         */


        /* Chebyshev coefficients for exp(-x) I1(x) / x
         * in the interval [0,8].
         *
         * lim(x->0){ exp(-x) I1(x) / x } = 1/2.
         */

        private static final double[] A = {
                2.77791411276104639959E-18,
                -2.11142121435816608115E-17,
                1.55363195773620046921E-16,
                -1.10559694773538630805E-15,
                7.60068429473540693410E-15,
                -5.04218550472791168711E-14,
                3.22379336594557470981E-13,
                -1.98397439776494371520E-12,
                1.17361862988909016308E-11,
                -6.66348972350202774223E-11,
                3.62559028155211703701E-10,
                -1.88724975172282928790E-9,
                9.38153738649577178388E-9,
                -4.44505912879632808065E-8,
                2.00329475355213526229E-7,
                -8.56872026469545474066E-7,
                3.47025130813767847674E-6,
                -1.32731636560394358279E-5,
                4.78156510755005422638E-5,
                -1.61760815825896745588E-4,
                5.12285956168575772895E-4,
                -1.51357245063125314899E-3,
                4.15642294431288815669E-3,
                -1.05640848946261981558E-2,
                2.47264490306265168283E-2,
                -5.29459812080949914269E-2,
                1.02643658689847095384E-1,
                -1.76416518357834055153E-1,
                2.52587186443633654823E-1
        };

        /* Chebyshev coefficients for exp(-x) sqrt(x) I1(x)
         * in the inverted interval [8,infinity].
         *
         * lim(x->inf){ exp(-x) sqrt(x) I1(x) } = 1/sqrt(2pi).
         */
        private static final double[] B = {
                7.51729631084210481353E-18,
                4.41434832307170791151E-18,
                -4.65030536848935832153E-17,
                -3.20952592199342395980E-17,
                2.96262899764595013876E-16,
                3.30820231092092828324E-16,
                -1.88035477551078244854E-15,
                -3.81440307243700780478E-15,
                1.04202769841288027642E-14,
                4.27244001671195135429E-14,
                -2.10154184277266431302E-14,
                -4.08355111109219731823E-13,
                -7.19855177624590851209E-13,
                2.03562854414708950722E-12,
                1.41258074366137813316E-11,
                3.25260358301548823856E-11,
                -1.89749581235054123450E-11,
                -5.58974346219658380687E-10,
                -3.83538038596423702205E-9,
                -2.63146884688951950684E-8,
                -2.51223623787020892529E-7,
                -3.88256480887769039346E-6,
                -1.10588938762623716291E-4,
                -9.76109749136146840777E-3,
                7.78576235018280120474E-1
        };

        public static double i1(double x) {
            double y, z;

            z = abs(x);
            if (z <= 8.0) {
                y = (z / 2.0) - 2.0;
                z = chbevl(y, A, 29) * z * exp(z);
            } else {
                z = exp(z) * chbevl(32.0 / z - 2.0, B, 25) / sqrt(z);
            }
            if (x < 0.0)
                z = -z;
            return (z);
        }

        /*							i1e.c
         *
         *	Modified Bessel function of order one,
         *	exponentially scaled
         *
         *
         *
         * SYNOPSIS:
         *
         * double x, y, i1e();
         *
         * y = i1e( x );
         *
         *
         *
         * DESCRIPTION:
         *
         * Returns exponentially scaled modified Bessel function
         * of order one of the argument.
         *
         * The function is defined as i1(x) = -i exp(-|x|) j1( ix ).
         *
         *
         *
         * ACCURACY:
         *
         *                      Relative error:
         * arithmetic   domain     # trials      peak         rms
         *    IEEE      0, 30       30000       2.0e-15     2.0e-16
         * See i1().
         *
         */

        public static double i1e(double x) {
            double y, z;

            z = abs(x);
            if (z <= 8.0) {
                y = (z / 2.0) - 2.0;
                z = chbevl(y, A, 29) * z;
            } else {
                z = chbevl(32.0 / z - 2.0, B, 25) / sqrt(z);
            }
            if (x < 0.0)
                z = -z;
            return (z);
        }

    }

    static final class i0_c {
        private i0_c() {

        }

        /* Chebyshev coefficients for exp(-x) I0(x)
         * in the interval [0,8].
         *
         * lim(x->0){ exp(-x) I0(x) } = 1.
         */
        static final double[] A = {
                -4.41534164647933937950E-18,
                3.33079451882223809783E-17,
                -2.43127984654795469359E-16,
                1.71539128555513303061E-15,
                -1.16853328779934516808E-14,
                7.67618549860493561688E-14,
                -4.85644678311192946090E-13,
                2.95505266312963983461E-12,
                -1.72682629144155570723E-11,
                9.67580903537323691224E-11,
                -5.18979560163526290666E-10,
                2.65982372468238665035E-9,
                -1.30002500998624804212E-8,
                6.04699502254191894932E-8,
                -2.67079385394061173391E-7,
                1.11738753912010371815E-6,
                -4.41673835845875056359E-6,
                1.64484480707288970893E-5,
                -5.75419501008210370398E-5,
                1.88502885095841655729E-4,
                -5.76375574538582365885E-4,
                1.63947561694133579842E-3,
                -4.32430999505057594430E-3,
                1.05464603945949983183E-2,
                -2.37374148058994688156E-2,
                4.93052842396707084878E-2,
                -9.49010970480476444210E-2,
                1.71620901522208775349E-1,
                -3.04682672343198398683E-1,
                6.76795274409476084995E-1
        };

        /* Chebyshev coefficients for exp(-x) sqrt(x) I0(x)
         * in the inverted interval [8,infinity].
         *
         * lim(x->inf){ exp(-x) sqrt(x) I0(x) } = 1/sqrt(2pi).
         */
        static final double[] B = {
                -7.23318048787475395456E-18,
                -4.83050448594418207126E-18,
                4.46562142029675999901E-17,
                3.46122286769746109310E-17,
                -2.82762398051658348494E-16,
                -3.42548561967721913462E-16,
                1.77256013305652638360E-15,
                3.81168066935262242075E-15,
                -9.55484669882830764870E-15,
                -4.15056934728722208663E-14,
                1.54008621752140982691E-14,
                3.85277838274214270114E-13,
                7.18012445138366623367E-13,
                -1.79417853150680611778E-12,
                -1.32158118404477131188E-11,
                -3.14991652796324136454E-11,
                1.18891471078464383424E-11,
                4.94060238822496958910E-10,
                3.39623202570838634515E-9,
                2.26666899049817806459E-8,
                2.04891858946906374183E-7,
                2.89137052083475648297E-6,
                6.88975834691682398426E-5,
                3.36911647825569408990E-3,
                8.04490411014108831608E-1
        };

        public static double i0(double x) {
            double y;

            if (x < 0)
                x = -x;
            if (x <= 8.0) {
                y = (x / 2.0) - 2.0;
                return (exp(x) * chbevl(y, A, 30));
            }

            return (exp(x) * chbevl(32.0 / x - 2.0, B, 25) / sqrt(x));

        }


        public static double i0e(double x) {
            double y;

            if (x < 0)
                x = -x;
            if (x <= 8.0) {
                y = (x / 2.0) - 2.0;
                return (chbevl(y, A, 30));
            }

            return (chbevl(32.0 / x - 2.0, B, 25) / sqrt(x));

        }

    }

    static final class k1_c {
        private k1_c() {

        }

        /* Chebyshev coefficients for x(K1(x) - log(x/2) I1(x))
         * in the interval [0,2].
         *
         * lim(x->0){ x(K1(x) - log(x/2) I1(x)) } = 1.
         */

        private static final double[] A = {
                -7.02386347938628759343E-18,
                -2.42744985051936593393E-15,
                -6.66690169419932900609E-13,
                -1.41148839263352776110E-10,
                -2.21338763073472585583E-8,
                -2.43340614156596823496E-6,
                -1.73028895751305206302E-4,
                -6.97572385963986435018E-3,
                -1.22611180822657148235E-1,
                -3.53155960776544875667E-1,
                1.52530022733894777053E0
        };

        /* Chebyshev coefficients for exp(x) sqrt(x) K1(x)
         * in the interval [2,infinity].
         *
         * lim(x->inf){ exp(x) sqrt(x) K1(x) } = sqrt(pi/2).
         */
        private static final double[] B = {
                -5.75674448366501715755E-18,
                1.79405087314755922667E-17,
                -5.68946255844285935196E-17,
                1.83809354436663880070E-16,
                -6.05704724837331885336E-16,
                2.03870316562433424052E-15,
                -7.01983709041831346144E-15,
                2.47715442448130437068E-14,
                -8.97670518232499435011E-14,
                3.34841966607842919884E-13,
                -1.28917396095102890680E-12,
                5.13963967348173025100E-12,
                -2.12996783842756842877E-11,
                9.21831518760500529508E-11,
                -4.19035475934189648750E-10,
                2.01504975519703286596E-9,
                -1.03457624656780970260E-8,
                5.74108412545004946722E-8,
                -3.50196060308781257119E-7,
                2.40648494783721712015E-6,
                -1.93619797416608296024E-5,
                1.95215518471351631108E-4,
                -2.85781685962277938680E-3,
                1.03923736576817238437E-1,
                2.72062619048444266945E0
        };

        /*                                                     k1.c
         *
         *     Modified Bessel function, third kind, order one
         *
         *
         *
         * SYNOPSIS:
         *
         * double x, y, k1();
         *
         * y = k1( x );
         *
         *
         *
         * DESCRIPTION:
         *
         * Computes the modified Bessel function of the third kind
         * of order one of the argument.
         *
         * The range is partitioned into the two intervals [0,2] and
         * (2, infinity).  Chebyshev polynomial expansions are employed
         * in each interval.
         *
         *
         *
         * ACCURACY:
         *
         *                      Relative error:
         * arithmetic   domain     # trials      peak         rms
         *    IEEE      0, 30       30000       1.2e-15     1.6e-16
         *
         * ERROR MESSAGES:
         *
         *   message         condition      value returned
         * k1 domain          x <= 0          NPY_INFINITY
         *
         */
        public static double k1(double x) {
            double y, z;

            if (x == 0.0) {
                return Double.POSITIVE_INFINITY;
            } else if (x < 0.0) {
                return Double.NaN;
            }
            z = 0.5 * x;

            if (x <= 2.0) {
                y = x * x - 2.0;
                y = log(z) * i1_c.i1(x) + chbevl(y, A, 11) / x;
                return (y);
            }

            return (exp(-x) * chbevl(8.0 / x - 2.0, B, 25) / sqrt(x));
        }

        /*							k1e.c
         *
         *	Modified Bessel function, third kind, order one,
         *	exponentially scaled
         *
         *
         *
         * SYNOPSIS:
         *
         * double x, y, k1e();
         *
         * y = k1e( x );
         *
         *
         *
         * DESCRIPTION:
         *
         * Returns exponentially scaled modified Bessel function
         * of the third kind of order one of the argument:
         *
         *      k1e(x) = exp(x) * k1(x).
         *
         *
         *
         * ACCURACY:
         *
         *                      Relative error:
         * arithmetic   domain     # trials      peak         rms
         *    IEEE      0, 30       30000       7.8e-16     1.2e-16
         * See k1().
         *
         */
        public static double k1e(double x) {
            double y;

            if (x == 0.0) {
                return Double.POSITIVE_INFINITY;
            } else if (x < 0.0) {
                return Double.NaN;
            }

            if (x <= 2.0) {
                y = x * x - 2.0;
                y = log(0.5 * x) * i1_c.i1(x) + chbevl(y, A, 11) / x;
                return (y * exp(x));
            }

            return chbevl(8.0 / x - 2.0, B, 25) / sqrt(x);
        }
    }
    /*                                                     ndtr.c
     *
     *     Normal distribution function
     *
     *
     *
     * SYNOPSIS:
     *
     * double x, y, ndtr();
     *
     * y = ndtr( x );
     *
     *
     *
     * DESCRIPTION:
     *
     * Returns the area under the Gaussian probability density
     * function, integrated from minus infinity to x:
     *
     *                            x
     *                             -
     *                   1        | |          2
     *    ndtr(x)  = ---------    |    exp( - t /2 ) dt
     *               sqrt(2pi)  | |
     *                           -
     *                          -inf.
     *
     *             =  ( 1 + erf(z) ) / 2
     *             =  erfc(z) / 2
     *
     * where z = x/sqrt(2). Computation is via the functions
     * erf and erfc.
     *
     *
     * ACCURACY:
     *
     *                      Relative error:
     * arithmetic   domain     # trials      peak         rms
     *    IEEE     -13,0        30000       3.4e-14     6.7e-15
     *
     *
     * ERROR MESSAGES:
     *
     *   message         condition         value returned
     * erfc underflow    x > 37.519379347       0.0
     *
     */
    /*							erf.c
     *
     *	Error function
     *
     *
     *
     * SYNOPSIS:
     *
     * double x, y, erf();
     *
     * y = erf( x );
     *
     *
     *
     * DESCRIPTION:
     *
     * The integral is
     *
     *                           x
     *                            -
     *                 2         | |          2
     *   erf(x)  =  --------     |    exp( - t  ) dt.
     *              sqrt(pi)   | |
     *                          -
     *                           0
     *
     * For 0 <= |x| < 1, erf(x) = x * P4(x**2)/Q5(x**2); otherwise
     * erf(x) = 1 - erfc(x).
     *
     *
     *
     * ACCURACY:
     *
     *                      Relative error:
     * arithmetic   domain     # trials      peak         rms
     *    IEEE      0,1         30000       3.7e-16     1.0e-16
     *
     */
    /*							erfc.c
     *
     *	Complementary error function
     *
     *
     *
     * SYNOPSIS:
     *
     * double x, y, erfc();
     *
     * y = erfc( x );
     *
     *
     *
     * DESCRIPTION:
     *
     *
     *  1 - erf(x) =
     *
     *                           inf.
     *                             -
     *                  2         | |          2
     *   erfc(x)  =  --------     |    exp( - t  ) dt
     *               sqrt(pi)   | |
     *                           -
     *                            x
     *
     *
     * For small x, erfc(x) = 1 - erf(x); otherwise rational
     * approximations are computed.
     *
     *
     *
     * ACCURACY:
     *
     *                      Relative error:
     * arithmetic   domain     # trials      peak         rms
     *    IEEE      0,26.6417   30000       5.7e-14     1.5e-14
     */


    /*
     * Cephes Math Library Release 2.2:  June, 1992
     * Copyright 1984, 1987, 1988, 1992 by Stephen L. Moshier
     * Direct inquiries to 30 Frost Street, Cambridge, MA 02140
     */
    static final class ndtr_c {
        private ndtr_c() {

        }

        /**
         * Normal distribution function
         *
         * @param a the value to calculate x of
         * @return the normal distribution function of x
         */
        public static double ndtr(double a) throws DomainException {
            double x, y, z;

            if (Double.isNaN(a)) {
                return DomainException.raiseException("ndtr", Double.NaN);
            }
            x = a * Cephes.NPY_SQRT1_2;
            z = abs(x);

            if (z < Cephes.NPY_SQRT1_2) {
                y = 0.5 + 0.5 * erf(x);
            } else {
                y = 0.5 * erfc(z);

                if (x > 0) {
                    y = 1.0 - y;
                }
            }

            return y;
        }
        /*							erfc.c
         *
         *	Complementary error function
         *
         *
         *
         * SYNOPSIS:
         *
         * double x, y, erfc();
         *
         * y = erfc( x );
         *
         *
         *
         * DESCRIPTION:
         *
         *
         *  1 - erf(x) =
         *
         *                           inf.
         *                             -
         *                  2         | |          2
         *   erfc(x)  =  --------     |    exp( - t  ) dt
         *               sqrt(pi)   | |
         *                           -
         *                            x
         *
         *
         * For small x, erfc(x) = 1 - erf(x); otherwise rational
         * approximations are computed.
         *
         *
         *
         * ACCURACY:
         *
         *                      Relative error:
         * arithmetic   domain     # trials      peak         rms
         *    IEEE      0,26.6417   30000       5.7e-14     1.5e-14
         */

        /**
         * @param a the value to calculate the complementary error function
         * @return the complementary error function of a
         */
        public static double erfc(double a) throws DomainException, UnderflowException {
            final double[] P = {
                    2.46196981473530512524E-10,
                    5.64189564831068821977E-1,
                    7.46321056442269912687E0,
                    4.86371970985681366614E1,
                    1.96520832956077098242E2,
                    5.26445194995477358631E2,
                    9.34528527171957607540E2,
                    1.02755188689515710272E3,
                    5.57535335369399327526E2
            };
            final double[] R = {
                    5.64189583547755073984E-1,
                    1.27536670759978104416E0,
                    5.01905042251180477414E0,
                    6.16021097993053585195E0,
                    7.40974269950448939160E0,
                    2.97886665372100240670E0
            };
            final double[] Q = {
                    /* 1.00000000000000000000E0, */
                    1.32281951154744992508E1,
                    8.67072140885989742329E1,
                    3.54937778887819891062E2,
                    9.75708501743205489753E2,
                    1.82390916687909736289E3,
                    2.24633760818710981792E3,
                    1.65666309194161350182E3,
                    5.57535340817727675546E2
            };

            final double[] S = {
                    /* 1.00000000000000000000E0, */
                    2.26052863220117276590E0,
                    9.39603524938001434673E0,
                    1.20489539808096656605E1,
                    1.70814450747565897222E1,
                    9.60896809063285878198E0,
                    3.36907645100081516050E0
            };

            double p, q, x, y, z;

            if (Double.isNaN(a)) {
                return DomainException.raiseException("erfc", Double.NaN);
            }
            x = a < 0 ? -a : a;

            if (x < 1.0) {
                return (1.0 - erf(a));
            }

            z = -a * a;
            if (z < -Cephes.MAXLOG) {
                UnderflowException.raiseException("erfc");
                return a < 0 ? 2 : 0;
            }

            z = exp(z);

            if (x < 8.0) {
                p = polevl(x, P, 8);
                q = p1evl(x, Q, 8);
            } else {
                p = polevl(x, R, 5);
                q = p1evl(x, S, 6);
            }
            y = (z * p) / q;

            if (a < 0) {
                y = 2.0 - y;
            }
            if (y == 0.0) {
                UnderflowException.raiseException("erfc");
                return a < 0 ? 2 : 0;
            }

            return y;
        }
        /*							erf.c
         *
         *	Error function
         *
         *
         *
         * SYNOPSIS:
         *
         * double x, y, erf();
         *
         * y = erf( x );
         *
         *
         *
         * DESCRIPTION:
         *
         * The integral is
         *
         *                           x
         *                            -
         *                 2         | |          2
         *   erf(x)  =  --------     |    exp( - t  ) dt.
         *              sqrt(pi)   | |
         *                          -
         *                           0
         *
         * For 0 <= |x| < 1, erf(x) = x * P4(x**2)/Q5(x**2); otherwise
         * erf(x) = 1 - erfc(x).
         *
         *
         *
         * ACCURACY:
         *
         *                      Relative error:
         * arithmetic   domain     # trials      peak         rms
         *    IEEE      0,1         30000       3.7e-16     1.0e-16
         *
         */

        /**
         * @param x the value x
         * @return the error function of x
         */
        public static double erf(double x) throws DomainException {
            final double[] T = {
                    9.60497373987051638749E0,
                    9.00260197203842689217E1,
                    2.23200534594684319226E3,
                    7.00332514112805075473E3,
                    5.55923013010394962768E4
            };
            final double[] U = {
                    /* 1.00000000000000000000E0, */
                    3.35617141647503099647E1,
                    5.21357949780152679795E2,
                    4.59432382970980127987E3,
                    2.26290000613890934246E4,
                    4.92673942608635921086E4
            };
            double y, z;

            if (Double.isNaN(x)) {
                return DomainException.raiseException("erf", Double.NaN);
            }

            if (x < 0.0) {
                return -erf(-x);
            }

            if (abs(x) > 1.0) {
                return (1.0 - erfc(x));
            }
            z = x * x;

            y = x * polevl(z, T, 4) / p1evl(z, U, 5);
            return y;

        }


    }

    static final class polevl_c {
        private polevl_c() {
        }
        /*                                                     polevl.c
         *                                                     p1evl.c
         *
         *     Evaluate polynomial
         *
         *
         *
         * SYNOPSIS:
         *
         * int N;
         * double x, y, coef[N+1], polevl[];
         *
         * y = polevl( x, coef, N );
         *
         *
         *
         * DESCRIPTION:
         *
         * Evaluates polynomial of degree N:
         *
         *                     2          N
         * y  =  C  + C x + C x  +...+ C x
         *        0    1     2          N
         *
         * Coefficients are stored in reverse order:
         *
         * coef[0] = C  , ..., coef[N] = C  .
         *            N                   0
         *
         *  The function p1evl() assumes that coef[N] = 1.0 and is
         * omitted from the array.  Its calling arguments are
         * otherwise the same as polevl().
         *
         *
         * SPEED:
         *
         * In the interest of speed, there are no checks for out
         * of bounds arithmetic.  This routine is used by most of
         * the functions in the library.  Depending on available
         * equipment features, the user may wish to rewrite the
         * program in microcode or assembly language.
         *
         */

        /**
         * Evaluate the polynomial
         *
         * @param x    the value to calculate the polynomial of
         * @param coef the coefficients
         * @param N    degree
         * @return the polynomial of x
         */
        public static double polevl(double x, double[] coef, int N) {
            int i = 0;
            double ans = coef[0];
            while (i < N) {
                ans = ans * x + coef[++i];
            }
            return ans;
        }
        /*                                                     p1evl() */
        /*                                          N
         * Evaluate polynomial when coefficient of x  is 1.0.
         * Otherwise same as polevl.
         */

        /**
         * Evaluate the polynomial (assumes coeff[N] = 1)
         *
         * @param x    the value to calculate the polynomial of
         * @param coef the coefficients
         * @param N    degree
         * @return the polynomial of x
         */
        public static double p1evl(double x, double[] coef, int N) {
            int i = 0;
            double ans = x + coef[0];
            final int m = N - 1;
            while (i < m) {
                ans = ans * x + coef[++i];
            }

            return ans;
        }
    }

    /* Scipy changes:
     * - 06-10-2016: added lgam1p
     */
    static final class unity_c {
        private unity_c() {

        }

        /* log1p(x) = log(1 + x)  */

        /* Coefficients for log(1+x) = x - x**2/2 + x**3 P(x)/Q(x)
         * 1/sqrt(2) <= x < sqrt(2)
         * Theoretical peak relative error = 2.32e-20
         */
        final static double[] LP = {
                4.5270000862445199635215E-5,
                4.9854102823193375972212E-1,
                6.5787325942061044846969E0,
                2.9911919328553073277375E1,
                6.0949667980987787057556E1,
                5.7112963590585538103336E1,
                2.0039553499201281259648E1,
        };

        final static double[] LQ = {
                /* 1.0000000000000000000000E0, */
                1.5062909083469192043167E1,
                8.3047565967967209469434E1,
                2.2176239823732856465394E2,
                3.0909872225312059774938E2,
                2.1642788614495947685003E2,
                6.0118660497603843919306E1,
        };

        public static double log1p(double x) {
            double z;

            z = 1.0 + x;
            if ((z < Cephes.NPY_SQRT1_2) || (z > Cephes.NPY_SQRT2))
                return (log(z));
            z = x * x;
            z = -0.5 * z + x * (z * polevl(x, LP, 6) / p1evl(x, LQ, 6));
            return (x + z);
        }


        /* log(1 + x) - x */
        public static double log1pmx(double x) {
            if (abs(x) < 0.5) {
                int n;
                double xfac = x;
                double term;
                double res = 0;

                for (n = 2; n < igam_c.MAXITER; n++) {
                    xfac *= -x;
                    term = xfac / n;
                    res += term;
                    if (abs(term) < MACHEP * abs(res)) {
                        break;
                    }
                }
                return res;
            } else {
                return log1p(x) - x;
            }
        }


        /* expm1(x) = exp(x) - 1  */

        /*  e^x =  1 + 2x P(x^2)/( Q(x^2) - P(x^2) )
         * -0.5 <= x <= 0.5
         */

        static double[] EP = {
                1.2617719307481059087798E-4,
                3.0299440770744196129956E-2,
                9.9999999999999999991025E-1,
        };

        static double[] EQ = {
                3.0019850513866445504159E-6,
                2.5244834034968410419224E-3,
                2.2726554820815502876593E-1,
                2.0000000000000000000897E0,
        };

        public static double expm1(double x) {
            double r, xx;

            if (!Double.isInfinite(x)) {
                if (Double.isNaN(x)) {
                    return x;
                } else if (x > 0) {
                    return x;
                } else {
                    return -1.0;
                }

            }
            if ((x < -0.5) || (x > 0.5))
                return (exp(x) - 1.0);
            xx = x * x;
            r = x * polevl(xx, EP, 2);
            r = r / (polevl(xx, EQ, 3) - r);
            return (r + r);
        }



        /* cosm1(x) = cos(x) - 1  */

        final static double[] coscof =

                {
                        4.7377507964246204691685E-14,
                        -1.1470284843425359765671E-11,
                        2.0876754287081521758361E-9,
                        -2.7557319214999787979814E-7,
                        2.4801587301570552304991E-5,
                        -1.3888888888888872993737E-3,
                        4.1666666666666666609054E-2,
                };

        public static double cosm1(double x) {
            double xx;

            if ((x < -Cephes.NPY_PI_4) || (x > Cephes.NPY_PI_4))
                return (cos(x) - 1.0);
            xx = x * x;
            xx = -0.5 * xx + xx * xx * polevl(xx, coscof, 6);
            return xx;
        }


        /* Compute lgam(x + 1) around x = 0 using its Taylor series. */
        public static double lgam1p_taylor(double x) {
            int n;
            double xfac, coeff, res;

            if (x == 0) {
                return 0;
            }
            res = -Cephes.NPY_EULER * x;
            xfac = -x;
            for (n = 2; n < 42; n++) {
                xfac *= -x;
                coeff = zeta(n, 1) * xfac / n;
                res += coeff;
                if (abs(coeff) < MACHEP * abs(res)) {
                    break;
                }
            }

            return res;
        }


        /* Compute lgam(x + 1). */
        public static double lgam1p(double x) {
           /* if (abs(x) <= 0.5) {
                return lgam1p_taylor(x);
            } else if (abs(x - 1) < 0.5) {
                return log(x) + lgam1p_taylor(x - 1);
            } else {
                return lgam(x + 1);
            }*/
            return 0;
        }

    }

    /*                                                     zetac.c
     *
     *     Riemann zeta function
     *
     *
     *
     * SYNOPSIS:
     *
     * double x, y, zetac();
     *
     * y = zetac( x );
     *
     *
     *
     * DESCRIPTION:
     *
     *
     *
     *                inf.
     *                 -    -x
     *   zetac(x)  =   >   k   ,   x > 1,
     *                 -
     *                k=2
     *
     * is related to the Riemann zeta function by
     *
     *     Riemann zeta(x) = zetac(x) + 1.
     *
     * Extension of the function definition for x < 1 is implemented.
     * Zero is returned for x > log2(NPY_INFINITY).
     *
     * ACCURACY:
     *
     * Tabulated values have full machine accuracy.
     *
     *                      Relative error:
     * arithmetic   domain     # trials      peak         rms
     *    IEEE      1,50        10000       9.8e-16            1.3e-16
     *
     *
     */

    /*
     * Cephes Math Library Release 2.1:  January, 1989
     * Copyright 1984, 1987, 1989 by Stephen L. Moshier
     * Direct inquiries to 30 Frost Street, Cambridge, MA 02140
     */

    static final class zetac_c {
        private zetac_c() {

        }

        /* Riemann zeta(x) - 1
         * for integer arguments between 0 and 30.
         */
        static final double[] azetac = {
                -1.50000000000000000000E0,
                0.0,  /* Not used; zetac(1.0) is infinity. */
                6.44934066848226436472E-1,
                2.02056903159594285400E-1,
                8.23232337111381915160E-2,
                3.69277551433699263314E-2,
                1.73430619844491397145E-2,
                8.34927738192282683980E-3,
                4.07735619794433937869E-3,
                2.00839282608221441785E-3,
                9.94575127818085337146E-4,
                4.94188604119464558702E-4,
                2.46086553308048298638E-4,
                1.22713347578489146752E-4,
                6.12481350587048292585E-5,
                3.05882363070204935517E-5,
                1.52822594086518717326E-5,
                7.63719763789976227360E-6,
                3.81729326499983985646E-6,
                1.90821271655393892566E-6,
                9.53962033872796113152E-7,
                4.76932986787806463117E-7,
                2.38450502727732990004E-7,
                1.19219925965311073068E-7,
                5.96081890512594796124E-8,
                2.98035035146522801861E-8,
                1.49015548283650412347E-8,
                7.45071178983542949198E-9,
                3.72533402478845705482E-9,
                1.86265972351304900640E-9,
                9.31327432419668182872E-10
        };


        static final int MAXL2 = 127;

        static final double SQRT_2_PI = 0.79788456080286535587989;

        /*
         * Riemann zeta function, minus one
         */

        /**
         * Calculate the Riemann zeta function of a value, minus one
         *
         * @param x the value to get the function of
         * @return the Riemann zeta function
         */
        public static double zetac(double x) {
            if (Double.isNaN(x)) {
                return x;
            } else if (x == Double.NEGATIVE_INFINITY) {
                return Double.NaN;
            } else if (x < 0.0 && x > -0.01) {
                return zetac_smallneg(x);
            } else if (x < 0.0) {
                return zeta_reflection(-x) - 1;
            } else {
                return zetac_positive(x);
            }
        }

        /*
         * Riemann zeta function
         */

        /**
         * Calculate the Riemann zeta function of a value
         *
         * @param x the value
         * @return the Riemann zeta function of the value
         */
        public static double riemann_zeta(double x) {
            if (Double.isNaN(x)) {
                return x;
            } else if (x == Double.NEGATIVE_INFINITY) {
                return Double.NaN;
            } else if (x < 0.0 && x > -0.01) {
                return 1 + zetac_smallneg(x);
            } else if (x < 0.0) {
                return zeta_reflection(-x);
            } else {
                return 1 + zetac_positive(x);
            }
        }


        /*
         * Compute zetac for positive arguments
         */
        static double zetac_positive(double x) {
            int i;
            double a, b, s, w;

            if (x == 1.0) {
                return Double.POSITIVE_INFINITY;
            }

            if (x >= MAXL2) {
                /* because first term is 2**-x */
                return 0.0;
            }

            /* Tabulated values for integer argument */
            w = floor(x);
            if (w == x) {
                i = (int) x;
                if (i < 31) {
                    return (azetac[i]);

                }
            }

            if (x < 1.0) {
                w = 1.0 - x;
                a = polevl(x,
                        /* (1-x) (zeta(x) - 1), 0 <= x <= 1 */
                        new double[]{
                                -3.28717474506562731748E-1,
                                1.55162528742623950834E1,
                                -2.48762831680821954401E2,
                                1.01050368053237678329E3,
                                1.26726061410235149405E4,
                                -1.11578094770515181334E5,
                        }, 5) / (w * p1evl(x,
                        new double[]{
                                /* 1.00000000000000000000E0, */
                                1.95107674914060531512E1,
                                3.17710311750646984099E2,
                                3.03835500874445748734E3,
                                2.03665876435770579345E4,
                                7.43853965136767874343E4,
                        }, 5));
                return a;
            }

            if (x <= 10.0) {
                b = pow(2.0, x) * (x - 1.0);
                w = 1.0 / x;
                s = (x * polevl(w,
                        /* 2**x (1 - 1/x) (zeta(x) - 1) = P(1/x)/Q(1/x), 1 <= x <= 10 */

                        new double[]{
                                5.85746514569725319540E11,
                                2.57534127756102572888E11,
                                4.87781159567948256438E10,
                                5.15399538023885770696E9,
                                3.41646073514754094281E8,
                                1.60837006880656492731E7,
                                5.92785467342109522998E5,
                                1.51129169964938823117E4,
                                2.01822444485997955865E2,

                        }

                        , 8)) / (b * p1evl(w,
                        new double[]{

                                /*  1.00000000000000000000E0, */
                                3.90497676373371157516E11,
                                5.22858235368272161797E10,
                                5.64451517271280543351E9,
                                3.39006746015350418834E8,
                                1.79410371500126453702E7,
                                5.66666825131384797029E5,
                                1.60382976810944131506E4,
                                1.96436237223387314144E2,

                        }, 8));
                return s;
            }

            if (x <= 50.0) {
                b = pow(2.0, -x);
                w = polevl(x,
                        /* log(zeta(x) - 1 - 2**-x), 10 <= x <= 50 */
                        new double[]{
                                8.70728567484590192539E6,
                                1.76506865670346462757E8,
                                2.60889506707483264896E10,
                                5.29806374009894791647E11,
                                2.26888156119238241487E13,
                                3.31884402932705083599E14,
                                5.13778997975868230192E15,
                                -1.98123688133907171455E15,
                                -9.92763810039983572356E16,
                                7.82905376180870586444E16,
                                9.26786275768927717187E16,


                        }, 10) / p1evl(x,
                        new double[]{
                                /* 1.00000000000000000000E0, */
                                -7.92625410563741062861E6,
                                -1.60529969932920229676E8,
                                -2.37669260975543221788E10,
                                -4.80319584350455169857E11,
                                -2.07820961754173320170E13,
                                -2.96075404507272223680E14,
                                -4.86299103694609136686E15,
                                5.34589509675789930199E15,
                                5.71464111092297631292E16,
                                -1.79915597658676556828E16,
                        }, 10);
                w = exp(w) + b;
                return w;
            }

            /* Basic sum of inverse powers */
            s = 0.0;
            a = 1.0;
            do {
                a += 2.0;
                b = pow(a, -x);
                s += b;
            }
            while (b / s > MACHEP);

            b = pow(2.0, -x);
            s = (s + b) / (1.0 - b);
            return s;
        }

        /*
         * Compute zetac for small negative x. We can't use the reflection
         * formula because to double precision 1 - x = 1 and zetac(1) = inf.
         */

        /**
         * Calculate the zeta function for small negative values
         *
         * @param x the value
         * @return the zeta function for a small negative value
         */
        public static double zetac_smallneg(double x) {
            return polevl(x,
                    new double[]{

                            -1.0000000009110164892,
                            -1.0000000057646759799,
                            -9.9999983138417361078e-1,
                            -1.0000013011460139596,
                            -1.000001940896320456,
                            -9.9987929950057116496e-1,
                            -1.000785194477042408,
                            -1.0031782279542924256,
                            -9.1893853320467274178e-1,
                            -1.5,

                    }, 9);
        }

        /*
         * Compute zetac using the reflection formula (see DLMF 25.4.2) plus
         * the Lanczos approximation for Gamma to avoid overflow.
         */

        /**
         * Compute the zeta function using reflection
         *
         * @param x the value to get the zeta function of
         * @return the zeta function, using reflection, of a value
         */
        public static double zeta_reflection(double x) {
            double base, large_term, small_term, hx, x_shift;

            hx = x / 2;
            if (hx == floor(hx)) {
                /* Hit a zero of the sine factor */
                return 0;
            }

            /* Reduce the argument to sine */
            x_shift = x % 4;
            small_term = -SQRT_2_PI * sin(0.5 * Cephes.NPY_PI * x_shift);
            small_term *= Boost.lanczos_sum_expg_scaled(x + 1) * zeta(x + 1, 1);

            /* Group large terms together to prevent overflow */
            base = (x + Boost.lanczos_g + 0.5) / (2 * Cephes.NPY_PI * Cephes.NPY_E);
            large_term = pow(base, x + 0.5);
            if (Double.isFinite(large_term)) {
                return large_term * small_term;
            }
            /*
             * We overflowed, but we might be able to stave off overflow by
             * factoring in the small term earlier. To do this we compute
             *
             * (sqrt(large_term) * small_term) * sqrt(large_term)
             *
             * Since we only call this method for negative x bounded away from
             * zero, the small term can only be as small sine on that region;
             * i.e. about machine epsilon. This means that if the above still
             * overflows, then there was truly no avoiding it.
             */
            large_term = pow(base, 0.5 * x + 0.25);
            return (large_term * small_term) * large_term;
        }

    }

    static final class zeta_c {
        private zeta_c() {

        }




        /*                                                     zeta.c
         *
         *     Riemann zeta function of two arguments
         *
         *
         *
         * SYNOPSIS:
         *
         * double x, q, y, zeta();
         *
         * y = zeta( x, q );
         *
         *
         *
         * DESCRIPTION:
         *
         *
         *
         *                 inf.
         *                  -        -x
         *   zeta(x,q)  =   >   (k+q)
         *                  -
         *                 k=0
         *
         * where x > 1 and q is not a negative integer or zero.
         * The Euler-Maclaurin summation formula is used to obtain
         * the expansion
         *
         *                n
         *                -       -x
         * zeta(x,q)  =   >  (k+q)
         *                -
         *               k=1
         *
         *           1-x                 inf.  B   x(x+1)...(x+2j)
         *      (n+q)           1         -     2j
         *  +  ---------  -  -------  +   >    --------------------
         *        x-1              x      -                   x+2j+1
         *                   2(n+q)      j=1       (2j)! (n+q)
         *
         * where the B2j are Bernoulli numbers.  Note that (see zetac.c)
         * zeta(x,1) = zetac(x) + 1.
         *
         *
         *
         * ACCURACY:
         *
         *
         *
         * REFERENCE:
         *
         * Gradshteyn, I. S., and I. M. Ryzhik, Tables of Integrals,
         * Series, and Products, p. 1073; Academic Press, 1980.
         *
         */

        /*
         * Cephes Math Library Release 2.0:  April, 1987
         * Copyright 1984, 1987 by Stephen L. Moshier
         * Direct inquiries to 30 Frost Street, Cambridge, MA 02140
         */

        public static double zeta(double x, double q) throws DomainException, SingularException {
            int i;
            double a, b, k, s, t, w;
            /* Expansion coefficients
             * for Euler-Maclaurin summation formula
             * (2k)! / B2k
             * where B2k are Bernoulli numbers
             */
            final double[] A = {
                    12.0,
                    -720.0,
                    30240.0,
                    -1209600.0,
                    47900160.0,
                    -1.8924375803183791606e9,    /*1.307674368e12/691 */
                    7.47242496e10,
                    -2.950130727918164224e12,    /*1.067062284288e16/3617 */
                    1.1646782814350067249e14,    /*5.109094217170944e18/43867 */
                    -4.5979787224074726105e15,    /*8.028576626982912e20/174611 */
                    1.8152105401943546773e17,    /*1.5511210043330985984e23/854513 */
                    -7.1661652561756670113e18    /*1.6938241367317436694528e27/236364091 */
            };

            if (x == 1.0)
                return Double.POSITIVE_INFINITY;

            if (x < 1.0) {
                return DomainException.raiseException("zeta", Double.NaN);
            }

            if (q <= 0.0) {
                if (q == floor(q)) {
                    SingularException.raiseException("zeta");
                    return Double.POSITIVE_INFINITY;
                }
                if (x != floor(x)) {
                    return DomainException.raiseException("zeta", Double.NaN);    /* because q^-x not defined */
                }
            }

            /* Asymptotic expansion
             * https://dlmf.nist.gov/25.11#E43
             */
            if (q > 1e8) {
                return (1 / (x - 1) + 1 / (2 * q)) * pow(q, 1 - x);
            }

            /* Euler-Maclaurin summation formula */

            /* Permit negative q but continue sum until n+q > +9 .
             * This case should be handled by a reflection formula.
             * If q<0 and x is an integer, there is a relation to
             * the polyGamma function.
             */
            s = pow(q, -x);
            a = q;
            i = 0;
            b = 0.0;
            while ((i < 9) || (a <= 9.0)) {
                i += 1;
                a += 1.0;
                b = pow(a, -x);
                s += b;
                if (abs(b / s) < MACHEP)
                    return s;
            }


            w = a;
            s += b * w / (x - 1.0);
            s -= 0.5 * b;
            a = 1.0;
            k = 0.0;
            for (i = 0; i < 12; i++) {
                a *= x + k;
                b /= w;
                t = a * b / A[i];
                s = s + t;
                t = abs(t / s);
                if (t < MACHEP) {
                    return s;
                }
                k += 1.0;
                a *= x + k;
                b /= w;
                k += 1.0;
            }
            return s;
        }

    }

    /*
     * Implement sin(pi * x) and cos(pi * x) for real x. Since the periods
     * of these functions are integral (and thus representable in double
     * precision), it's possible to compute them with greater accuracy
     * than sin(x) and cos(x).
     */
    static final class sinpi_c {
        sinpi_c() {

        }

        /* Compute sin(pi * x). */
        static double sinpi(double x) {
            double s = 1.0;
            double r;

            if (x < 0.0) {
                x = -x;
                s = -1.0;
            }

            r = x % 2.0;
            if (r < 0.5) {
                return s * sin(Cephes.M_PI * r);
            } else if (r > 1.5) {
                return s * sin(Cephes.M_PI * (r - 2.0));
            } else {
                return -s * sin(Cephes.M_PI * (r - 1.0));
            }
        }


        /* Compute cos(pi * x) */
        static double cospi(double x) {
            double r;

            if (x < 0.0) {
                x = -x;
            }

            r = x % 2.0;
            if (r == 0.5) {
                // We don't want to return -0.0
                return 0.0;
            }
            if (r < 1.0) {
                return -sin(Cephes.M_PI * (r - 0.5));
            } else {
                return sin(Cephes.M_PI * (r - 1.5));
            }
        }
    }

    static final class tukey_c {
        private tukey_c() {
        }

        static final double SMALLVAL = 1e-4;
        static final double EPS = 1.0e-14;
        static final int MAXCOUNT = 60;

        static double tukeylambdacdf(double x, double lmbda) {
            double pmin, pmid, pmax, plow, phigh, xeval;
            int count;

            if (Double.isNaN(x) || Double.isNaN(lmbda)) {
                return Double.NaN;
            }

            xeval = 1.0 / lmbda;
            if (lmbda > 0.0) {
                if (x < (-xeval))
                    return 0.0;
                if (x > xeval)
                    return 1.0;
            }

            if ((-SMALLVAL < lmbda) && (lmbda < SMALLVAL)) {
                if (x >= 0)
                    return 1.0 / (1.0 + exp(-x));
                else
                    return exp(x) / (1.0 + exp(x));
            }

            pmin = 0.0;
            pmid = 0.5;
            pmax = 1.0;
            plow = pmin;
            phigh = pmax;
            count = 0;

            while ((count < MAXCOUNT) && (abs(pmid - plow) > EPS)) {
                xeval = (pow(pmid, lmbda) - pow(1.0 - pmid, lmbda)) / lmbda;
                if (xeval == x)
                    return pmid;
                if (xeval > x) {
                    phigh = pmid;
                    pmid = (pmid + plow) / 2.0;
                } else {
                    plow = pmid;
                    pmid = (pmid + phigh) / 2.0;
                }
                count++;
            }
            return pmid;
        }

    }

    static final class k0_c {
        private k0_c() {

        }

        /* Chebyshev coefficients for K0(x) + log(x/2) I0(x)
         * in the interval [0,2].  The odd order coefficients are all
         * zero; only the even order coefficients are listed.
         *
         * lim(x->0){ K0(x) + log(x/2) I0(x) } = -EUL.
         */

        static final double[] A = {
                1.37446543561352307156E-16,
                4.25981614279661018399E-14,
                1.03496952576338420167E-11,
                1.90451637722020886025E-9,
                2.53479107902614945675E-7,
                2.28621210311945178607E-5,
                1.26461541144692592338E-3,
                3.59799365153615016266E-2,
                3.44289899924628486886E-1,
                -5.35327393233902768720E-1
        };

        /* Chebyshev coefficients for exp(x) sqrt(x) K0(x)
         * in the inverted interval [2,infinity].
         *
         * lim(x->inf){ exp(x) sqrt(x) K0(x) } = sqrt(pi/2).
         */
        static final double[] B = {
                5.30043377268626276149E-18,
                -1.64758043015242134646E-17,
                5.21039150503902756861E-17,
                -1.67823109680541210385E-16,
                5.51205597852431940784E-16,
                -1.84859337734377901440E-15,
                6.34007647740507060557E-15,
                -2.22751332699166985548E-14,
                8.03289077536357521100E-14,
                -2.98009692317273043925E-13,
                1.14034058820847496303E-12,
                -4.51459788337394416547E-12,
                1.85594911495471785253E-11,
                -7.95748924447710747776E-11,
                3.57739728140030116597E-10,
                -1.69753450938905987466E-9,
                8.57403401741422608519E-9,
                -4.66048989768794782956E-8,
                2.76681363944501510342E-7,
                -1.83175552271911948767E-6,
                1.39498137188764993662E-5,
                -1.28495495816278026384E-4,
                1.56988388573005337491E-3,
                -3.14481013119645005427E-2,
                2.44030308206595545468E0
        };

        static double k0(double x) {
            double y, z;

            if (x == 0.0) {
                //  sf_error("k0", SF_ERROR_SINGULAR, NULL);
                return Double.POSITIVE_INFINITY;
            } else if (x < 0.0) {
                //  sf_error("k0", SF_ERROR_DOMAIN, NULL);
                return Double.NaN;
            }

            if (x <= 2.0) {
                y = x * x - 2.0;
                y = chbevl(y, A, 10) - log(0.5 * x) * i0(x);
                return (y);
            }
            z = 8.0 / x - 2.0;
            y = exp(-x) * chbevl(z, B, 25) / sqrt(x);
            return (y);
        }


        static double k0e(double x) {
            double y;

            if (x == 0.0) {
                //  sf_error("k0e", SF_ERROR_SINGULAR, NULL);
                return Double.POSITIVE_INFINITY;
            } else if (x < 0.0) {
                //sf_error("k0e", SF_ERROR_DOMAIN, NULL);
                return Double.NaN;
            }

            if (x <= 2.0) {
                y = x * x - 2.0;
                y = chbevl(y, A, 10) - log(0.5 * x) * i0(x);
                return (y * exp(x));
            }

            y = chbevl(8.0 / x - 2.0, B, 25) / sqrt(x);
            return (y);
        }

    }

    static final class chdtr_c {
        private chdtr_c() {

        }

        static double chdtrc(double df, double x) {

            if (x < 0.0)
                return 1.0;        /* modified by T. Oliphant */
            return (CephesImpl.igam_c.igamc(df / 2.0, x / 2.0));
        }


        static double chdtr(double df, double x) throws DomainException {

            if ((x < 0.0)) {        /* || (df < 1.0) ) */
                return (DomainException.raiseException("chdtr", Double.NaN));
            }
            return (CephesImpl.igam_c.igam(df / 2.0, x / 2.0));
        }


        static double chdtri(double df, double y) throws DomainException {
            double x;

            if ((y < 0.0) || (y > 1.0)) {    /* || (df < 1.0) ) */
                return (DomainException.raiseException("chdtri", Double.NaN));
            }

            x = igamci(0.5 * df, y);
            return (2.0 * x);
        }

    }

    /*
     * (C) Copyright John Maddock 2006.
     * Use, modification and distribution are subject to the
     * Boost Software License, Version 1.0. (See accompanying file
     *  LICENSE_1_0.txt or copy at https://www.boost.org/LICENSE_1_0.txt)
     */
    static final class igami_c {
        private igami_c() {

        }


        static double find_inverse_s(double p, double q) {
            /*
             * Computation of the Incomplete Gamma Function Ratios and their Inverse
             * ARMIDO R. DIDONATO and ALFRED H. MORRIS, JR.
             * ACM Transactions on Mathematical Software, Vol. 12, No. 4,
             * December 1986, Pages 377-393.
             *
             * See equation 32.
             */
            double s, t;
            final double[] a = {0.213623493715853, 4.28342155967104,
                    11.6616720288968, 3.31125922108741};
            final double[] b = {0.3611708101884203e-1, 1.27364489782223,
                    6.40691597760039, 6.61053765625462, 1};

            if (p < 0.5) {
                t = sqrt(-2 * log(p));
            } else {
                t = sqrt(-2 * log(q));
            }
            s = t - polevl(t, a, 3) / polevl(t, b, 4);
            if (p < 0.5)
                s = -s;
            return s;
        }


        static double didonato_SN(double a, double x, int N, double tolerance) {
            /*
             * Computation of the Incomplete Gamma Function Ratios and their Inverse
             * ARMIDO R. DIDONATO and ALFRED H. MORRIS, JR.
             * ACM Transactions on Mathematical Software, Vol. 12, No. 4,
             * December 1986, Pages 377-393.
             *
             * See equation 34.
             */
            double sum = 1.0;

            if (N >= 1) {
                int i;
                double partial = x / (a + 1);

                sum += partial;
                for (i = 2; i <= N; ++i) {
                    partial *= x / (a + i);
                    sum += partial;
                    if (partial < tolerance) {
                        break;
                    }
                }
            }
            return sum;
        }


        static double find_inverse_gamma(double a, double p, double q) {
            /*
             * In order to understand what's going on here, you will
             * need to refer to:
             *
             * Computation of the Incomplete Gamma Function Ratios and their Inverse
             * ARMIDO R. DIDONATO and ALFRED H. MORRIS, JR.
             * ACM Transactions on Mathematical Software, Vol. 12, No. 4,
             * December 1986, Pages 377-393.
             */
            double result;

            if (a == 1) {
                if (q > 0.9) {
                    result = -CephesImpl.unity_c.log1p(-p);
                } else {
                    result = -log(q);
                }
            } else if (a < 1) {
                double g = CephesImpl.gamma_c.Gamma(a);
                double b = q * g;

                if ((b > 0.6) || ((b >= 0.45) && (a >= 0.3))) {
                    /* DiDonato & Morris Eq 21:
                     *
                     * There is a slight variation from DiDonato and Morris here:
                     * the first form given here is unstable when p is close to 1,
                     * making it impossible to compute the inverse of Q(a,x) for small
                     * q. Fortunately the second form works perfectly well in this case.
                     */
                    double u;
                    if ((b * q > 1e-8) && (q > 1e-5)) {
                        u = pow(p * g * a, 1 / a);
                    } else {
                        u = exp((-q / a) - Cephes.NPY_EULER);
                    }
                    result = u / (1 - (u / (a + 1)));
                } else if ((a < 0.3) && (b >= 0.35)) {
                    /* DiDonato & Morris Eq 22: */
                    double t = exp(-Cephes.NPY_EULER - b);
                    double u = t * exp(t);
                    result = t * exp(u);
                } else if ((b > 0.15) || (a >= 0.3)) {
                    /* DiDonato & Morris Eq 23: */
                    double y = -log(b);
                    double u = y - (1 - a) * log(y);
                    result = y - (1 - a) * log(u) - log(1 + (1 - a) / (1 + u));
                } else if (b > 0.1) {
                    /* DiDonato & Morris Eq 24: */
                    double y = -log(b);
                    double u = y - (1 - a) * log(y);
                    result = y - (1 - a) * log(u)
                            - log((u * u + 2 * (3 - a) * u + (2 - a) * (3 - a))
                            / (u * u + (5 - a) * u + 2));
                } else {
                    /* DiDonato & Morris Eq 25: */
                    double y = -log(b);
                    double c1 = (a - 1) * log(y);
                    double c1_2 = c1 * c1;
                    double c1_3 = c1_2 * c1;
                    double c1_4 = c1_2 * c1_2;
                    double a_2 = a * a;
                    double a_3 = a_2 * a;

                    double c2 = (a - 1) * (1 + c1);
                    double c3 = (a - 1) * (-(c1_2 / 2)
                            + (a - 2) * c1
                            + (3 * a - 5) / 2);
                    double c4 = (a - 1) * ((c1_3 / 3) - (3 * a - 5) * c1_2 / 2
                            + (a_2 - 6 * a + 7) * c1
                            + (11 * a_2 - 46 * a + 47) / 6);
                    double c5 = (a - 1) * (-(c1_4 / 4)
                            + (11 * a - 17) * c1_3 / 6
                            + (-3 * a_2 + 13 * a - 13) * c1_2
                            + (2 * a_3 - 25 * a_2 + 72 * a - 61) * c1 / 2
                            + (25 * a_3 - 195 * a_2 + 477 * a - 379) / 12);

                    double y_2 = y * y;
                    double y_3 = y_2 * y;
                    double y_4 = y_2 * y_2;
                    result = y + c1 + (c2 / y) + (c3 / y_2) + (c4 / y_3) + (c5 / y_4);
                }
            } else {
                /* DiDonato and Morris Eq 31: */
                double s = find_inverse_s(p, q);

                double s_2 = s * s;
                double s_3 = s_2 * s;
                double s_4 = s_2 * s_2;
                double s_5 = s_4 * s;
                double ra = sqrt(a);

                double w = a + s * ra + (s_2 - 1) / 3;
                w += (s_3 - 7 * s) / (36 * ra);
                w -= (3 * s_4 + 7 * s_2 - 16) / (810 * a);
                w += (9 * s_5 + 256 * s_3 - 433 * s) / (38880 * a * ra);

                if ((a >= 500) && (Math.abs(1 - w / a) < 1e-6)) {
                    result = w;
                } else if (p > 0.5) {
                    if (w < 3 * a) {
                        result = w;
                    } else {
                        double D = Math.max(2, a * (a - 1));
                        double lg = lgam(a);
                        double lb = log(q) + lg;
                        if (lb < -D * 2.3) {
                            /* DiDonato and Morris Eq 25: */
                            double y = -lb;
                            double c1 = (a - 1) * log(y);
                            double c1_2 = c1 * c1;
                            double c1_3 = c1_2 * c1;
                            double c1_4 = c1_2 * c1_2;
                            double a_2 = a * a;
                            double a_3 = a_2 * a;

                            double c2 = (a - 1) * (1 + c1);
                            double c3 = (a - 1) * (-(c1_2 / 2)
                                    + (a - 2) * c1
                                    + (3 * a - 5) / 2);
                            double c4 = (a - 1) * ((c1_3 / 3)
                                    - (3 * a - 5) * c1_2 / 2
                                    + (a_2 - 6 * a + 7) * c1
                                    + (11 * a_2 - 46 * a + 47) / 6);
                            double c5 = (a - 1) * (-(c1_4 / 4)
                                    + (11 * a - 17) * c1_3 / 6
                                    + (-3 * a_2 + 13 * a - 13) * c1_2
                                    + (2 * a_3 - 25 * a_2 + 72 * a - 61) * c1 / 2
                                    + (25 * a_3 - 195 * a_2 + 477 * a - 379) / 12);

                            double y_2 = y * y;
                            double y_3 = y_2 * y;
                            double y_4 = y_2 * y_2;
                            result = y + c1 + (c2 / y) + (c3 / y_2) + (c4 / y_3) + (c5 / y_4);
                        } else {
                            /* DiDonato and Morris Eq 33: */
                            double u = -lb + (a - 1) * log(w) - log(1 + (1 - a) / (1 + w));
                            result = -lb + (a - 1) * log(u) - log(1 + (1 - a) / (1 + u));
                        }
                    }
                } else {
                    double z = w;
                    double ap1 = a + 1;
                    double ap2 = a + 2;
                    if (w < 0.15 * ap1) {
                        /* DiDonato and Morris Eq 35: */
                        double v = log(p) + lgam(ap1);
                        z = exp((v + w) / a);
                        s = CephesImpl.unity_c.log1p(z / ap1 * (1 + z / ap2));
                        z = exp((v + z - s) / a);
                        s = CephesImpl.unity_c.log1p(z / ap1 * (1 + z / ap2));
                        z = exp((v + z - s) / a);
                        s = CephesImpl.unity_c.log1p(z / ap1 * (1 + z / ap2 * (1 + z / (a + 3))));
                        z = exp((v + z - s) / a);
                    }

                    if ((z <= 0.01 * ap1) || (z > 0.7 * ap1)) {
                        result = z;
                    } else {
                        /* DiDonato and Morris Eq 36: */
                        double ls = log(didonato_SN(a, z, 100, 1e-4));
                        double v = log(p) + lgam(ap1);
                        z = exp((v + z - ls) / a);
                        result = z * (1 - (a * log(z) - z - v + ls) / (a - z));
                    }
                }
            }
            return result;
        }


        static double igami(double a, double p) throws DomainException {
            int i;
            double x, fac, f_fp, fpp_fp;

            if (Double.isNaN(a) || Double.isNaN(p)) {
                return Double.NaN;
            } else if ((a < 0) || (p < 0) || (p > 1)) {
                DomainException.raiseException("gammaincinv");
            } else if (p == 0.0) {
                return 0.0;
            } else if (p == 1.0) {
                return Double.POSITIVE_INFINITY;
            } else if (p > 0.9) {
                return igamci(a, 1 - p);
            }

            x = find_inverse_gamma(a, p, 1 - p);
            /* Halley's method */
            for (i = 0; i < 3; i++) {
                fac = CephesImpl.igam_c.igam_fac(a, x);
                if (fac == 0.0) {
                    return x;
                }
                f_fp = (CephesImpl.igam_c.igam(a, x) - p) * x / fac;
                /* The ratio of the first and second derivatives simplifies */
                fpp_fp = -1.0 + (a - 1) / x;
                if (Double.isInfinite(fpp_fp)) {
                    /* Resort to Newton's method in the case of overflow */
                    x = x - f_fp;
                } else {
                    x = x - f_fp / (1.0 - 0.5 * f_fp * fpp_fp);
                }
            }

            return x;
        }


        static double igamci(double a, double q) throws DomainException {
            int i;
            double x, fac, f_fp, fpp_fp;

            if (Double.isNaN(a) || Double.isNaN(q)) {
                return Double.NaN;
            } else if ((a < 0.0) || (q < 0.0) || (q > 1.0)) {
                DomainException.raiseException("gammainccinv");
            } else if (q == 0.0) {
                return Double.POSITIVE_INFINITY;
            } else if (q == 1.0) {
                return 0.0;
            } else if (q > 0.9) {
                return igami(a, 1 - q);
            }

            x = find_inverse_gamma(a, 1 - q, q);
            for (i = 0; i < 3; i++) {
                fac = CephesImpl.igam_c.igam_fac(a, x);
                if (fac == 0.0) {
                    return x;
                }
                f_fp = (CephesImpl.igam_c.igamc(a, x) - q) * x / (-fac);
                fpp_fp = -1.0 + (a - 1) / x;
                if (Double.isInfinite(fpp_fp)) {
                    x = x - f_fp;
                } else {
                    x = x - f_fp / (1.0 - 0.5 * f_fp * fpp_fp);
                }
            }

            return x;
        }

    }

    static final class erfinv_c {
        private erfinv_c() {
        }

        /*
         * Inverse of the error function.
         *
         * Computes the inverse of the error function on the restricted domain
         * -1 < y < 1. This restriction ensures the existence of a unique result
         * such that erf(erfinv(y)) = y.
         */
        static double erfinv(double y) throws DomainException {
            final double domain_lb = -1;
            final double domain_ub = 1;

            if ((domain_lb < y) && (y < domain_ub)) {
                return ndtri(0.5 * (y + 1)) * Cephes.NPY_SQRT1_2;
            } else if (y == domain_lb) {
                return Double.NEGATIVE_INFINITY;
            } else if (y == domain_ub) {
                return Double.POSITIVE_INFINITY;
            } else if (Double.isNaN(y)) {
                return DomainException.raiseException("erfinv", y);
            } else {
                return DomainException.raiseException("erfinv", Double.NaN);
            }
        }

        /*
         * Inverse of the complementary error function.
         *
         * Computes the inverse of the complimentary error function on the restricted
         * domain 0 < y < 2. This restriction ensures the existence of a unique result
         * such that erfc(erfcinv(y)) = y.
         */
        static double erfcinv(double y) throws DomainException {
            final double domain_lb = 0;
            final double domain_ub = 2;

            if ((domain_lb < y) && (y < domain_ub)) {
                return -ndtri(0.5 * y) * Cephes.NPY_SQRT1_2;
            } else if (y == domain_lb) {
                return Double.POSITIVE_INFINITY;
            } else if (y == domain_ub) {
                return Double.NEGATIVE_INFINITY;
            } else if (Double.isNaN(y)) {
                return DomainException.raiseException("erfcinv", y);
            } else {
                return DomainException.raiseException("erfcinv", Double.NaN);
            }
        }
    }

    static final class j0_c {
        private j0_c() {

        }

        static double[] PP = {
                7.96936729297347051624E-4,
                8.28352392107440799803E-2,
                1.23953371646414299388E0,
                5.44725003058768775090E0,
                8.74716500199817011941E0,
                5.30324038235394892183E0,
                9.99999999999999997821E-1,
        };

        static double[] PQ = {
                9.24408810558863637013E-4,
                8.56288474354474431428E-2,
                1.25352743901058953537E0,
                5.47097740330417105182E0,
                8.76190883237069594232E0,
                5.30605288235394617618E0,
                1.00000000000000000218E0,
        };

        static double[] QP = {
                -1.13663838898469149931E-2,
                -1.28252718670509318512E0,
                -1.95539544257735972385E1,
                -9.32060152123768231369E1,
                -1.77681167980488050595E2,
                -1.47077505154951170175E2,
                -5.14105326766599330220E1,
                -6.05014350600728481186E0,
        };

        static double[] QQ = {
                /*  1.00000000000000000000E0, */
                6.43178256118178023184E1,
                8.56430025976980587198E2,
                3.88240183605401609683E3,
                7.24046774195652478189E3,
                5.93072701187316984827E3,
                2.06209331660327847417E3,
                2.42005740240291393179E2,
        };

        static double[] YP = {
                1.55924367855235737965E4,
                -1.46639295903971606143E7,
                5.43526477051876500413E9,
                -9.82136065717911466409E11,
                8.75906394395366999549E13,
                -3.46628303384729719441E15,
                4.42733268572569800351E16,
                -1.84950800436986690637E16,
        };

        static double[] YQ = {
                /* 1.00000000000000000000E0, */
                1.04128353664259848412E3,
                6.26107330137134956842E5,
                2.68919633393814121987E8,
                8.64002487103935000337E10,
                2.02979612750105546709E13,
                3.17157752842975028269E15,
                2.50596256172653059228E17,
        };

        /*  5.783185962946784521175995758455807035071 */
        static double DR1 = 5.78318596294678452118E0;

        /* 30.47126234366208639907816317502275584842 */
        static double DR2 = 3.04712623436620863991E1;

        static double[] RP = {
                -4.79443220978201773821E9,
                1.95617491946556577543E12,
                -2.49248344360967716204E14,
                9.70862251047306323952E15,
        };

        static double[] RQ = {
                /* 1.00000000000000000000E0, */
                4.99563147152651017219E2,
                1.73785401676374683123E5,
                4.84409658339962045305E7,
                1.11855537045356834862E10,
                2.11277520115489217587E12,
                3.10518229857422583814E14,
                3.18121955943204943306E16,
                1.71086294081043136091E18,
        };


        static double j0(double x) {
            double w, z, p, q, xn;

            if (x < 0)
                x = -x;

            if (x <= 5.0) {
                z = x * x;
                if (x < 1.0e-5)
                    return (1.0 - z / 4.0);

                p = (z - DR1) * (z - DR2);
                p = p * polevl(z, RP, 3) / p1evl(z, RQ, 8);
                return (p);
            }

            w = 5.0 / x;
            q = 25.0 / (x * x);
            p = polevl(q, PP, 6) / polevl(q, PQ, 6);
            q = polevl(q, QP, 7) / p1evl(q, QQ, 7);
            xn = x - Cephes.NPY_PI_4;
            p = p * cos(xn) - w * q * sin(xn);
            return (p * Cephes.SQ2OPI / sqrt(x));
        }

        /*                                                     y0() 2  */
        /* Bessel function of second kind, order zero  */

        /* Rational approximation coefficients YP[], YQ[] are used here.
         * The function computed is  y0(x)  -  2 * log(x) * j0(x) / NPY_PI,
         * whose value at x = 0 is  2 * ( log(0.5) + EUL ) / NPY_PI
         * = 0.073804295108687225.
         */

        static double y0(double x) {
            double w, z, p, q, xn;

            if (x <= 5.0) {
                if (x == 0.0) {
                    return SingularException.raiseException("y1", Double.NEGATIVE_INFINITY);
                } else if (x < 0.0) {
                    return DomainException.raiseException("y0", Double.NaN);
                }
                z = x * x;
                w = polevl(z, YP, 7) / p1evl(z, YQ, 7);
                w += Cephes.NPY_2_PI * log(x) * j0(x);
                return (w);
            }

            w = 5.0 / x;
            z = 25.0 / (x * x);
            p = polevl(z, PP, 6) / polevl(z, PQ, 6);
            q = polevl(z, QP, 7) / p1evl(z, QQ, 7);
            xn = x - Cephes.NPY_PI_4;
            p = p * sin(xn) + w * q * cos(xn);
            return (p * Cephes.SQ2OPI / sqrt(x));
        }

    }

    static final class yn_c {
        private yn_c() {
        }

        static double yn(int n, double x) {
            double an, anm1, anm2, r;
            int k, sign;

            if (n < 0) {
                n = -n;
                if ((n & 1) == 0)    /* -1**n */
                    sign = 1;
                else
                    sign = -1;
            } else
                sign = 1;


            if (n == 0)
                return (sign * y0(x));
            if (n == 1)
                return (sign * y1(x));

            /* test for overflow */
            if (x == 0.0) {
                return SingularException.raiseException("yn", -Double.NEGATIVE_INFINITY * sign);
            } else if (x < 0.0) {
                return DomainException.raiseException("yn", Double.NaN);
            }

            /* forward recurrence on n */

            anm2 = y0(x);
            anm1 = y1(x);
            k = 1;
            r = 2 * k;
            do {
                an = r * anm1 / x - anm2;
                anm2 = anm1;
                anm1 = an;
                r += 2.0;
                ++k;
            }
            while (k < n);

            return (sign * an);
        }

    }

    static final class j1_c {
        private j1_c() {

        }

        final static double[] RP = {
                -8.99971225705559398224E8,
                4.52228297998194034323E11,
                -7.27494245221818276015E13,
                3.68295732863852883286E15,
        };

        final static double[] RQ = {
                /* 1.00000000000000000000E0, */
                6.20836478118054335476E2,
                2.56987256757748830383E5,
                8.35146791431949253037E7,
                2.21511595479792499675E10,
                4.74914122079991414898E12,
                7.84369607876235854894E14,
                8.95222336184627338078E16,
                5.32278620332680085395E18,
        };

        final static double[] PP = {
                7.62125616208173112003E-4,
                7.31397056940917570436E-2,
                1.12719608129684925192E0,
                5.11207951146807644818E0,
                8.42404590141772420927E0,
                5.21451598682361504063E0,
                1.00000000000000000254E0,
        };

        final static double[] PQ = {
                5.71323128072548699714E-4,
                6.88455908754495404082E-2,
                1.10514232634061696926E0,
                5.07386386128601488557E0,
                8.39985554327604159757E0,
                5.20982848682361821619E0,
                9.99999999999999997461E-1,
        };

        final static double[] QP = {
                5.10862594750176621635E-2,
                4.98213872951233449420E0,
                7.58238284132545283818E1,
                3.66779609360150777800E2,
                7.10856304998926107277E2,
                5.97489612400613639965E2,
                2.11688757100572135698E2,
                2.52070205858023719784E1,
        };

        final static double[] QQ = {
                /* 1.00000000000000000000E0, */
                7.42373277035675149943E1,
                1.05644886038262816351E3,
                4.98641058337653607651E3,
                9.56231892404756170795E3,
                7.99704160447350683650E3,
                2.82619278517639096600E3,
                3.36093607810698293419E2,
        };

        final static double[] YP = {
                1.26320474790178026440E9,
                -6.47355876379160291031E11,
                1.14509511541823727583E14,
                -8.12770255501325109621E15,
                2.02439475713594898196E17,
                -7.78877196265950026825E17,
        };

        final static double[] YQ = {
                /* 1.00000000000000000000E0, */
                5.94301592346128195359E2,
                2.35564092943068577943E5,
                7.34811944459721705660E7,
                1.87601316108706159478E10,
                3.88231277496238566008E12,
                6.20557727146953693363E14,
                6.87141087355300489866E16,
                3.97270608116560655612E18,
        };


        static double Z1 = 1.46819706421238932572E1;
        static double Z2 = 4.92184563216946036703E1;


        static double j1(double x) {
            double w, z, p, q, xn;

            w = x;
            if (x < 0)
                return -j1(-x);

            if (w <= 5.0) {
                z = x * x;
                w = polevl(z, RP, 3) / p1evl(z, RQ, 8);
                w = w * x * (z - Z1) * (z - Z2);
                return (w);
            }

            w = 5.0 / x;
            z = w * w;
            p = polevl(z, PP, 6) / polevl(z, PQ, 6);
            q = polevl(z, QP, 7) / p1evl(z, QQ, 7);
            xn = x - Cephes.THPIO4;
            p = p * cos(xn) - w * q * sin(xn);
            return (p * Cephes.SQ2OPI / sqrt(x));
        }


        static double y1(double x) {
            double w, z, p, q, xn;

            if (x <= 5.0) {
                if (x == 0.0) {
                    return SingularException.raiseException("y1", Double.NEGATIVE_INFINITY);
                } else if (x <= 0.0) {
                    return DomainException.raiseException("y1", Double.NaN);
                }
                z = x * x;
                w = x * (polevl(z, YP, 5) / p1evl(z, YQ, 8));
                w += Cephes.NPY_2_PI * (j1(x) * log(x) - 1.0 / x);
                return (w);
            }

            w = 5.0 / x;
            z = w * w;
            p = polevl(z, PP, 6) / polevl(z, PQ, 6);
            q = polevl(z, QP, 7) / p1evl(z, QQ, 7);
            xn = x - Cephes.THPIO4;
            p = p * sin(xn) + w * q * cos(xn);
            return (p * Cephes.SQ2OPI / sqrt(x));
        }


    }

    static final class gdtr_c {
        private gdtr_c() {
        }

        static double gdtr(double a, double b, double x) throws DomainException {

            if (x < 0.0) {
                return DomainException.raiseException("gdtr", Double.NaN);
            }
            return (CephesImpl.igam_c.igam(b, a * x));
        }


        static double gdtrc(double a, double b, double x) throws DomainException {

            if (x < 0.0) {
                return DomainException.raiseException("gdtrc", Double.NaN);
            }
            return (CephesImpl.igam_c.igamc(b, a * x));
        }


        static double gdtri(double a, double b, double y) throws DomainException {

            if ((y < 0.0) || (y > 1.0) || (a <= 0.0) || (b < 0.0)) {
                return DomainException.raiseException("gdtri", Double.NaN);
            }

            return (igamci(b, 1.0 - y) / a);
        }

    }

    static final class gammasgn_c {
        private gammasgn_c() {

        }

        static double gammasgn(double x) {
            double fx;

            if (Double.isNaN(x)) {
                return x;
            }
            if (x > 0) {
                return 1.0;
            } else {
                fx = floor(x);
                if (x - fx == 0.0) {
                    return 0.0;
                } else if (((int) fx % 2) != 0) {
                    return -1.0;
                } else {
                    return 1.0;
                }
            }
        }

    }

    static final class poch_c {
        private poch_c() {
        }

        static boolean is_nonpos_int(double x) {
            return x <= 0 && x == ceil(x) && Math.abs(x) < 1e13;
        }

        static double poch(double a, double m) {
            double r;

            r = 1.0;

            /*
             * 1. Reduce magnitude of `m` to |m| < 1 by using recurrence relations.
             *
             * This may end up in over/underflow, but then the function itself either
             * diverges or goes to zero. In case the remainder goes to the opposite
             * direction, we end up returning 0*INF = NAN, which is OK.
             */

            /* Recurse down */
            while (m >= 1.0) {
                if (a + m == 1) {
                    break;
                }
                m -= 1.0;
                r *= (a + m);
                if (!Double.isFinite(r) || r == 0) {
                    break;
                }
            }

            /* Recurse up */
            while (m <= -1.0) {
                if (a + m == 0) {
                    break;
                }
                r /= (a + m);
                m += 1.0;
                if (!Double.isFinite(r) || r == 0) {
                    break;
                }
            }

            /*
             * 2. Evaluate function with reduced `m`
             *
             * Now either `m` is not big, or the `r` product has over/underflown.
             * If so, the function itself does similarly.
             */

            if (m == 0) {
                /* Easy case */
                return r;
            } else if (a > 1e4 && Math.abs(m) <= 1) {
                /* Avoid loss of precision */
                return r * pow(a, m) * (
                        1
                                + m * (m - 1) / (2 * a)
                                + m * (m - 1) * (m - 2) * (3 * m - 1) / (24 * a * a)
                                + m * m * (m - 1) * (m - 1) * (m - 2) * (m - 3) / (48 * a * a * a)
                );
            }

            /* Check for infinity */
            if (is_nonpos_int(a + m) && !is_nonpos_int(a) && a + m != m) {
                return Double.POSITIVE_INFINITY;
            }

            /* Check for zero */
            if (!is_nonpos_int(a + m) && is_nonpos_int(a)) {
                return 0;
            }

            return r * exp(lgam(a + m) - lgam(a)) * gammasgn(a + m) * gammasgn(a);
        }

    }

    static final class rgamma_c {
        private rgamma_c() {

        }

        final static double[] R = {
                3.13173458231230000000E-17,
                -6.70718606477908000000E-16,
                2.20039078172259550000E-15,
                2.47691630348254132600E-13,
                -6.60074100411295197440E-12,
                5.13850186324226978840E-11,
                1.08965386454418662084E-9,
                -3.33964630686836942556E-8,
                2.68975996440595483619E-7,
                2.96001177518801696639E-6,
                -8.04814124978471142852E-5,
                4.16609138709688864714E-4,
                5.06579864028608725080E-3,
                -6.41925436109158228810E-2,
                -4.98558728684003594785E-3,
                1.27546015610523951063E-1
        };

        static double rgamma(double x) {
            double w, y, z;
            int sign;

            if (x > 34.84425627277176174) {
                return exp(-lgam(x));
            }
            if (x < -34.034) {
                w = -x;
                z = sinpi(w);
                if (z == 0.0) {
                    return 0.0;
                }
                if (z < 0.0) {
                    sign = 1;
                    z = -z;
                } else {
                    sign = -1;
                }

                y = log(w * z) - log(Cephes.NPY_PI) + lgam(w);
                if (y < -Cephes.MAXLOG) {
                    return UnderflowException.raiseException("rgamma", sign * 0.0);
                }
                if (y > Cephes.MAXLOG) {
                    return OverflowException.raiseException("rgamma", sign * Double.POSITIVE_INFINITY);
                }
                return (sign * exp(y));
            }
            z = 1.0;
            w = x;

            while (w > 1.0) {        /* Downward recurrence */
                w -= 1.0;
                z *= w;
            }
            while (w < 0.0) {        /* Upward recurrence */
                z /= w;
                w += 1.0;
            }
            if (w == 0.0)        /* Nonpositive integer */
                return (0.0);
            if (w == 1.0)        /* Other integer */
                return (1.0 / z);

            y = w * (1.0 + chbevl(4.0 * w - 2.0, R, 16)) / z;
            return (y);
        }

    }
    /*                                                     ellpk.c
     *
     *     Complete elliptic integral of the first kind
     *
     *
     *
     * SYNOPSIS:
     *
     * double m1, y, ellpk();
     *
     * y = ellpk( m1 );
     *
     *
     *
     * DESCRIPTION:
     *
     * Approximates the integral
     *
     *
     *
     *            pi/2
     *             -
     *            | |
     *            |           dt
     * K(m)  =    |    ------------------
     *            |                   2
     *          | |    sqrt( 1 - m sin t )
     *           -
     *            0
     *
     * where m = 1 - m1, using the approximation
     *
     *     P(x)  -  log x Q(x).
     *
     * The argument m1 is used internally rather than m so that the logarithmic
     * singularity at m = 1 will be shifted to the origin; this
     * preserves maximum accuracy.
     *
     * K(0) = pi/2.
     *
     * ACCURACY:
     *
     *                      Relative error:
     * arithmetic   domain     # trials      peak         rms
     *    IEEE       0,1        30000       2.5e-16     6.8e-17
     *
     * ERROR MESSAGES:
     *
     *   message         condition      value returned
     * ellpk domain       x<0, x>1           0.0
     *
     */

    /*                                                     ellpk.c */


    /*
     * Cephes Math Library, Release 2.0:  April, 1987
     * Copyright 1984, 1987 by Stephen L. Moshier
     * Direct inquiries to 30 Frost Street, Cambridge, MA 02140
     */
    static final class ellpk_c {
        private ellpk_c() {
        }

        final static double[] P = {
                1.37982864606273237150E-4,
                2.28025724005875567385E-3,
                7.97404013220415179367E-3,
                9.85821379021226008714E-3,
                6.87489687449949877925E-3,
                6.18901033637687613229E-3,
                8.79078273952743772254E-3,
                1.49380448916805252718E-2,
                3.08851465246711995998E-2,
                9.65735902811690126535E-2,
                1.38629436111989062502E0
        };

        final static double[] Q = {
                2.94078955048598507511E-5,
                9.14184723865917226571E-4,
                5.94058303753167793257E-3,
                1.54850516649762399335E-2,
                2.39089602715924892727E-2,
                3.01204715227604046988E-2,
                3.73774314173823228969E-2,
                4.88280347570998239232E-2,
                7.03124996963957469739E-2,
                1.24999999999870820058E-1,
                4.99999999999999999821E-1
        };

        static double C1 = 1.3862943611198906188E0;    /* log(4) */


        static double ellpk(double x) throws DomainException, SingularException {

            if (x < 0.0) {
                return DomainException.raiseException("ellpk", Double.NaN);
            }

            if (x > 1.0) {
                if (Double.isInfinite(x)) {
                    return 0.0;
                }
                return ellpk(1. / x) / sqrt(x);
            }

            if (x > MACHEP) {
                return (polevl(x, P, 10) - log(x) * polevl(x, Q, 10));
            } else {
                if (x == 0.0) {
                    return SingularException.raiseException("ellpk", Double.POSITIVE_INFINITY);
                } else {
                    return (C1 - 0.5 * log(x));
                }
            }
        }

    }

    static final class sindg_c {
        private sindg_c() {
        }

        static double[] sincof = {
                1.58962301572218447952E-10,
                -2.50507477628503540135E-8,
                2.75573136213856773549E-6,
                -1.98412698295895384658E-4,
                8.33333333332211858862E-3,
                -1.66666666666666307295E-1
        };

        static double[] coscof = {
                1.13678171382044553091E-11,
                -2.08758833757683644217E-9,
                2.75573155429816611547E-7,
                -2.48015872936186303776E-5,
                1.38888888888806666760E-3,
                -4.16666666666666348141E-2,
                4.99999999999999999798E-1
        };

        static double PI180 = 1.74532925199432957692E-2;    /* pi/180 */
        static double lossth = 1.0e14;

        static double sindg(double x) {
            double y, z, zz;
            int j, sign;

            /* make argument positive but save the sign */
            sign = 1;
            if (x < 0) {
                x = -x;
                sign = -1;
            }

            if (x > lossth) {
                return NoResultException.raiseException("sindg", 0.);
            }

            y = floor(x / 45.0);    /* integer part of x/NPY_PI_4 */

            /* strip high bits of integer part to prevent integer overflow */
            z = ldexp(y, -4);
            z = floor(z);        /* integer part of y/8 */
            z = y - ldexp(z, 4);    /* y - 16 * (y/16) */

            j = (int) z;            /* convert to integer for tests on the phase angle */
            /* map zeros to origin */
            if ((j & 1) != 0) {
                j += 1;
                y += 1.0;
            }
            j = j & 07;            /* octant modulo 360 degrees */
            /* reflect in x axis */
            if (j > 3) {
                sign = -sign;
                j -= 4;
            }

            z = x - y * 45.0;        /* x mod 45 degrees */
            z *= PI180;            /* multiply by pi/180 to convert to radians */
            zz = z * z;

            if ((j == 1) || (j == 2)) {
                y = 1.0 - zz * polevl(zz, coscof, 6);
            } else {
                y = z + z * (zz * polevl(zz, sincof, 5));
            }

            if (sign < 0)
                y = -y;

            return (y);
        }


        static double cosdg(double x) {
            double y, z, zz;
            int j, sign;

            /* make argument positive */
            sign = 1;
            if (x < 0)
                x = -x;

            if (x > lossth) {
                //   sf_error("cosdg", SF_ERROR_NO_RESULT, NULL);
                return (0.0);
            }

            y = floor(x / 45.0);
            z = ldexp(y, -4);
            z = floor(z);        /* integer part of y/8 */
            z = y - ldexp(z, 4);    /* y - 16 * (y/16) */

            /* integer and fractional part modulo one octant */
            j = (int) z;
            if ((j & 1) != 0) {        /* map zeros to origin */
                j += 1;
                y += 1.0;
            }
            j = j & 07;
            if (j > 3) {
                j -= 4;
                sign = -sign;
            }

            if (j > 1)
                sign = -sign;

            z = x - y * 45.0;        /* x mod 45 degrees */
            z *= PI180;            /* multiply by pi/180 to convert to radians */

            zz = z * z;

            if ((j == 1) || (j == 2)) {
                y = z + z * (zz * polevl(zz, sincof, 5));
            } else {
                y = 1.0 - zz * polevl(zz, coscof, 6);
            }

            if (sign < 0)
                y = -y;

            return (y);
        }


        /* Degrees, minutes, seconds to radians: */

        /* 1 arc second, in radians = 4.848136811095359935899141023579479759563533023727e-6 */
        static double P64800 =
                4.848136811095359935899141023579479759563533023727e-6;

        static double radian(double d, double m, double s) {
            return (((d * 60.0 + m) * 60.0 + s) * P64800);
        }

    }

    static final class psi_c {
        private psi_c() {

        }

        static double[] A = {
                8.33333333333333333333E-2,
                -2.10927960927960927961E-2,
                7.57575757575757575758E-3,
                -4.16666666666666666667E-3,
                3.96825396825396825397E-3,
                -8.33333333333333333333E-3,
                8.33333333333333333333E-2
        };


        static double digamma_imp_1_2(double x) {
            /*
             * Rational approximation on [1, 2] taken from Boost.
             *
             * Now for the approximation, we use the form:
             *
             * digamma(x) = (x - root) * (Y + R(x-1))
             *
             * Where root is the location of the positive root of digamma,
             * Y is a constant, and R is optimised for low absolute error
             * compared to Y.
             *
             * Maximum Deviation Found:               1.466e-18
             * At double precision, max error found:  2.452e-17
             */
            double r, g;

            final float Y = 0.99558162689208984f;

            final double root1 = 1569415565.0 / 1073741824.0;
            final double root2 = (381566830.0 / 1073741824.0) / 1073741824.0;
            final double root3 = 0.9016312093258695918615325266959189453125e-19;

            final double[] P = {
                    -0.0020713321167745952,
                    -0.045251321448739056,
                    -0.28919126444774784,
                    -0.65031853770896507,
                    -0.32555031186804491,
                    0.25479851061131551
            };
            final double[] Q = {
                    -0.55789841321675513e-6,
                    0.0021284987017821144,
                    0.054151797245674225,
                    0.43593529692665969,
                    1.4606242909763515,
                    2.0767117023730469,
                    1.0
            };
            g = x - root1;
            g -= root2;
            g -= root3;
            r = polevl(x - 1.0, P, 5) / polevl(x - 1.0, Q, 6);

            return g * Y + g * r;
        }


        static double psi_asy(double x) {
            double y, z;

            if (x < 1.0e17) {
                z = 1.0 / (x * x);
                y = z * polevl(z, A, 6);
            } else {
                y = 0.0;
            }

            return log(x) - (0.5 / x) - y;
        }


        static double psi(double x) {
            double y = 0.0;
            double r;
            int i, n;

            if (Double.isInfinite(x)) {
                return x;
            } else if (x == Double.POSITIVE_INFINITY) {
                return x;
            } else if (x == Double.NEGATIVE_INFINITY) {
                return Double.NaN;
            } else if (x == 0) {
                return SingularException.raiseException("psi", Libs.copySign(Double.POSITIVE_INFINITY, -x));
            } else if (x < 0.0) {
                /* argument reduction before evaluating tan(pi * x) */
                r = fract(x);
                if (r == 0.0) {
                    return SingularException.raiseException("psi", Double.NaN);
                }
                y = -Cephes.NPY_PI / tan(Cephes.NPY_PI * r);
                x = 1.0 - x;
            }

            /* check for positive integer up to 10 */
            if ((x <= 10.0) && (x == floor(x))) {
                n = (int) x;
                for (i = 1; i < n; i++) {
                    y += 1.0 / i;
                }
                y -= Cephes.NPY_EULER;
                return y;
            }

            /* use the recurrence relation to move x into [1, 2] */
            if (x < 1.0) {
                y -= 1.0 / x;
                x += 1.0;
            } else if (x < 10.0) {
                while (x > 2.0) {
                    x -= 1.0;
                    y += 1.0 / x;
                }
            }
            if ((1.0 <= x) && (x <= 2.0)) {
                y += digamma_imp_1_2(x);
                return y;
            }

            /* x is large, use the asymptotic series */
            y += psi_asy(x);
            return y;
        }

    }

    static final class pdtr_c {
        private pdtr_c() {

        }

        static double pdtrc(double k, double m) {
            double v;

            if (k < 0.0 || m < 0.0) {
                return DomainException.raiseException("pdtrc", Double.NaN);
            }
            if (m == 0.0) {
                return 0.0;
            }
            v = floor(k) + 1;
            return (CephesImpl.igam_c.igam(v, m));
        }


        static double pdtr(double k, double m) {
            double v;

            if (k < 0 || m < 0) {
                return DomainException.raiseException("pdtr", Double.NaN);
            }
            if (m == 0.0) {
                return 1.0;
            }
            v = floor(k) + 1;
            return (CephesImpl.igam_c.igamc(v, m));
        }


        static double pdtri(int k, double y) {
            double v;

            if ((k < 0) || (y < 0.0) || (y >= 1.0)) {
                return DomainException.raiseException("pdtri", Double.NaN);
            }
            v = k + 1;
            v = igamci(v, y);
            return (v);
        }

    }

    static final class owens_t_c {
        private owens_t_c() {
        }

        static final int[] SELECT_METHOD = {
                0, 0, 1, 12, 12, 12, 12, 12, 12, 12, 12, 15, 15, 15, 8,
                0, 1, 1, 2, 2, 4, 4, 13, 13, 14, 14, 15, 15, 15, 8,
                1, 1, 2, 2, 2, 4, 4, 14, 14, 14, 14, 15, 15, 15, 9,
                1, 1, 2, 4, 4, 4, 4, 6, 6, 15, 15, 15, 15, 15, 9,
                1, 2, 2, 4, 4, 5, 5, 7, 7, 16, 16, 16, 11, 11, 10,
                1, 2, 4, 4, 4, 5, 5, 7, 7, 16, 16, 16, 11, 11, 11,
                1, 2, 3, 3, 5, 5, 7, 7, 16, 16, 16, 16, 16, 11, 11,
                1, 2, 3, 3, 5, 5, 17, 17, 17, 17, 16, 16, 16, 11, 11
        };

        static final double[] HRANGE = {0.02, 0.06, 0.09, 0.125, 0.26, 0.4, 0.6, 1.6,
                1.7, 2.33, 2.4, 3.36, 3.4, 4.8};

        static final double[] ARANGE = {0.025, 0.09, 0.15, 0.36, 0.5, 0.9, 0.99999};

        static final double[] ORD = {2, 3, 4, 5, 7, 10, 12, 18, 10, 20, 30, 0, 4, 7,
                8, 20, 0, 0};

        static final int METHODS[] = {1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 3, 4, 4, 4, 4,
                5, 6};

        static final double[] C = {
                0.99999999999999999999999729978162447266851932041876728736094298092917625009873,
                -0.99999999999999999999467056379678391810626533251885323416799874878563998732905968,
                0.99999999999999999824849349313270659391127814689133077036298754586814091034842536,
                -0.9999999999999997703859616213643405880166422891953033591551179153879839440241685,
                0.99999999999998394883415238173334565554173013941245103172035286759201504179038147,
                -0.9999999999993063616095509371081203145247992197457263066869044528823599399470977,
                0.9999999999797336340409464429599229870590160411238245275855903767652432017766116267,
                -0.999999999574958412069046680119051639753412378037565521359444170241346845522403274,
                0.9999999933226234193375324943920160947158239076786103108097456617750134812033362048,
                -0.9999999188923242461073033481053037468263536806742737922476636768006622772762168467,
                0.9999992195143483674402853783549420883055129680082932629160081128947764415749728967,
                -0.999993935137206712830997921913316971472227199741857386575097250553105958772041501,
                0.99996135597690552745362392866517133091672395614263398912807169603795088421057688716,
                -0.99979556366513946026406788969630293820987757758641211293079784585126692672425362469,
                0.999092789629617100153486251423850590051366661947344315423226082520411961968929483,
                -0.996593837411918202119308620432614600338157335862888580671450938858935084316004769854,
                0.98910017138386127038463510314625339359073956513420458166238478926511821146316469589567,
                -0.970078558040693314521331982203762771512160168582494513347846407314584943870399016019,
                0.92911438683263187495758525500033707204091967947532160289872782771388170647150321633673,
                -0.8542058695956156057286980736842905011429254735181323743367879525470479126968822863,
                0.73796526033030091233118357742803709382964420335559408722681794195743240930748630755,
                -0.58523469882837394570128599003785154144164680587615878645171632791404210655891158,
                0.415997776145676306165661663581868460503874205343014196580122174949645271353372263,
                -0.2588210875241943574388730510317252236407805082485246378222935376279663808416534365,
                0.1375535825163892648504646951500265585055789019410617565727090346559210218472356689,
                -0.0607952766325955730493900985022020434830339794955745989150270485056436844239206648,
                0.0216337683299871528059836483840390514275488679530797294557060229266785853764115,
                -0.00593405693455186729876995814181203900550014220428843483927218267309209471516256,
                0.0011743414818332946510474576182739210553333860106811865963485870668929503649964142,
                -1.489155613350368934073453260689881330166342484405529981510694514036264969925132E-4,
                9.072354320794357587710929507988814669454281514268844884841547607134260303118208E-6
        };

        static final double[] PTS = {
                0.35082039676451715489E-02, 0.31279042338030753740E-01,
                0.85266826283219451090E-01, 0.16245071730812277011E+00,
                0.25851196049125434828E+00, 0.36807553840697533536E+00,
                0.48501092905604697475E+00, 0.60277514152618576821E+00,
                0.71477884217753226516E+00, 0.81475510988760098605E+00,
                0.89711029755948965867E+00, 0.95723808085944261843E+00,
                0.99178832974629703586E+00
        };

        static final double[] WTS = {
                0.18831438115323502887E-01, 0.18567086243977649478E-01,
                0.18042093461223385584E-01, 0.17263829606398753364E-01,
                0.16243219975989856730E-01, 0.14994592034116704829E-01,
                0.13535474469662088392E-01, 0.11886351605820165233E-01,
                0.10070377242777431897E-01, 0.81130545742299586629E-02,
                0.60419009528470238773E-02, 0.38862217010742057883E-02,
                0.16793031084546090448E-02
        };


        static int get_method(double h, double a) {
            int ihint, iaint, i;

            ihint = 14;
            iaint = 7;

            for (i = 0; i < 14; i++) {
                if (h <= HRANGE[i]) {
                    ihint = i;
                    break;
                }
            }

            for (i = 0; i < 7; i++) {
                if (a <= ARANGE[i]) {
                    iaint = i;
                    break;
                }
            }
            return SELECT_METHOD[iaint * 15 + ihint];
        }


        static double owens_t_norm1(double x) {
            return CephesImpl.ndtr_c.erf(x / sqrt(2)) / 2;
        }


        static double owens_t_norm2(double x) {
            return CephesImpl.ndtr_c.erfc(x / sqrt(2)) / 2;
        }


        static double owensT1(double h, double a, double m) {
            int j = 1;
            int jj = 1;

            double hs = -0.5 * h * h;
            double dhs = exp(hs);
            double as = a * a;
            double aj = a / (2 * Cephes.NPY_PI);
            double dj = CephesImpl.unity_c.expm1(hs);
            double gj = hs * dhs;

            double val = atan(a) / (2 * Cephes.NPY_PI);

            while (true) {
                val += dj * aj / jj;

                if (m <= j) {
                    break;
                }
                j++;
                jj += 2;
                aj *= as;
                dj = gj - dj;
                gj *= hs / j;
            }

            return val;
        }


        static double owensT2(double h, double a, double ah, double m) {
            int i = 1;
            int maxi = (int) (2 * m + 1);
            double hs = h * h;
            double as = -a * a;
            double y = 1.0 / hs;
            double val = 0.0;
            double vi = a * exp(-0.5 * ah * ah) / sqrt(2 * Cephes.NPY_PI);
            double z = (CephesImpl.ndtr_c.ndtr(ah) - 0.5) / h;

            while (true) {
                val += z;
                if (maxi <= i) {
                    break;
                }
                z = y * (vi - i * z);
                vi *= as;
                i += 2;
            }
            val *= exp(-0.5 * hs) / sqrt(2 * Cephes.NPY_PI);

            return val;
        }


        static double owensT3(double h, double a, double ah) {
            double aa, hh, y, vi, zi, result;
            int i;

            aa = a * a;
            hh = h * h;
            y = 1 / hh;

            vi = a * exp(-ah * ah / 2) / sqrt(2 * Cephes.NPY_PI);
            zi = owens_t_norm1(ah) / h;
            result = 0;

            for (i = 0; i <= 30; i++) {
                result += zi * C[i];
                zi = y * ((2 * i + 1) * zi - vi);
                vi *= aa;
            }

            result *= exp(-hh / 2) / sqrt(2 * Cephes.NPY_PI);

            return result;
        }


        static double owensT4(double h, double a, double m) {
            double maxi, hh, naa, ai, yi, result;
            int i;

            maxi = 2 * m + 1;
            hh = h * h;
            naa = -a * a;

            i = 1;
            ai = a * exp(-hh * (1 - naa) / 2) / (2 * Cephes.NPY_PI);
            yi = 1;
            result = 0;

            while (true) {
                result += ai * yi;

                if (maxi <= i) {
                    break;
                }

                i += 2;
                yi = (1 - hh * yi) / i;
                ai *= naa;
            }

            return result;
        }


        static double owensT5(double h, double a) {
            double result, r, aa, nhh;
            int i;

            result = 0;
            r = 0;
            aa = a * a;
            nhh = -0.5 * h * h;

            for (i = 1; i < 14; i++) {
                r = 1 + aa * PTS[i - 1];
                result += WTS[i - 1] * exp(nhh * r) / r;
            }

            result *= a;

            return result;
        }


        static double owensT6(double h, double a) {
            double normh, y, r, result;

            normh = owens_t_norm2(h);
            y = 1 - a;
            r = atan2(y, (1 + a));
            result = normh * (1 - normh) / 2;

            if (r != 0) {
                result -= r * exp(-y * h * h / (2 * r)) / (2 * Cephes.NPY_PI);
            }

            return result;
        }


        static double owens_t_dispatch(double h, double a, double ah) {
            int index, meth_code;
            double m, result;

            if (h == 0) {
                return atan(a) / (2 * Cephes.NPY_PI);
            }
            if (a == 0) {
                return 0;
            }
            if (a == 1) {
                return owens_t_norm2(-h) * owens_t_norm2(h) / 2;
            }

            index = get_method(h, a);
            m = ORD[index];
            meth_code = METHODS[index];

            switch (meth_code) {
                case 1:
                    result = owensT1(h, a, m);
                    break;
                case 2:
                    result = owensT2(h, a, ah, m);
                    break;
                case 3:
                    result = owensT3(h, a, ah);
                    break;
                case 4:
                    result = owensT4(h, a, m);
                    break;
                case 5:
                    result = owensT5(h, a);
                    break;
                case 6:
                    result = owensT6(h, a);
                    break;
                default:
                    result = Double.NaN;
            }

            return result;
        }


        static double owens_t(double h, double a) {
            double result, fabs_a, fabs_ah, normh, normah;

            if (Double.isNaN(h) || Double.isNaN(a)) {
                return Double.NaN;
            }

            /* exploit that T(-h,a) == T(h,a) */
            h = Math.abs(h);

            /*
             * Use equation (2) in the paper to remap the arguments such that
             * h >= 0 and 0 <= a <= 1 for the call of the actual computation
             * routine.
             */
            fabs_a = Math.abs(a);
            fabs_ah = fabs_a * h;

            if (fabs_a == Double.POSITIVE_INFINITY) {
                /* See page 13 in the paper */
                result = owens_t_norm2(h);
            } else if (h == Double.POSITIVE_INFINITY) {
                result = 0;
            } else if (fabs_a <= 1) {
                result = owens_t_dispatch(h, fabs_a, fabs_ah);
            } else {
                if (fabs_ah <= 0.67) {
                    normh = owens_t_norm1(h);
                    normah = owens_t_norm1(fabs_ah);
                    result = 0.25 - normh * normah -
                            owens_t_dispatch(fabs_ah, (1 / fabs_a), h);
                } else {
                    normh = owens_t_norm2(h);
                    normah = owens_t_norm2(fabs_ah);
                    result = (normh + normah) / 2 - normh * normah -
                            owens_t_dispatch(fabs_ah, (1 / fabs_a), h);
                }
            }

            if (a < 0) {
                /* exploit that T(h,-a) == -T(h,a) */
                return -result;
            }

            return result;
        }

    }

    static final class kn_c {
        private kn_c() {

        }

        static final double EUL = 5.772156649015328606065e-1;
        static final int MAXFAC = 31;

        static double kn(int nn, double x) {
            double k, kf, nk1f, nkf, zn, t, s, z0, z;
            double ans, fn, pn, pk, zmn, tlg, tox;
            int i, n;

            if (nn < 0)
                n = -nn;
            else
                n = nn;

            if (n > MAXFAC) {
                return OverflowException.raiseException("kn", Double.POSITIVE_INFINITY);
            }

            if (x <= 0.0) {
                if (x < 0.0) {
                    return DomainException.raiseException("kn", Double.NaN);
                } else {
                    return SingularException.raiseException("kn", Double.POSITIVE_INFINITY);
                }
            }


            if (x > 9.55) {

                /* Asymptotic expansion for Kn(x) */
                /* Converges to 1.4e-17 for x > 18.4 */


                if (x > Cephes.MAXLOG) {
                    return UnderflowException.raiseException("kn", 0.);
                }
                k = n;
                pn = 4.0 * k * k;
                pk = 1.0;
                z0 = 8.0 * x;
                fn = 1.0;
                t = 1.0;
                s = t;
                nkf = Double.POSITIVE_INFINITY;
                i = 0;
                do {
                    z = pn - pk * pk;
                    t = t * z / (fn * z0);
                    nk1f = Math.abs(t);
                    if ((i >= n) && (nk1f > nkf)) {
                        return exp(-x) * sqrt(Cephes.NPY_PI / (2.0 * x)) * s;
                    }
                    nkf = nk1f;
                    s += t;
                    fn += 1.0;
                    pk += 2.0;
                    i += 1;
                }
                while (Math.abs(t / s) > MACHEP);
            }

            ans = 0.0;
            z0 = 0.25 * x * x;
            fn = 1.0;
            pn = 0.0;
            zmn = 1.0;
            tox = 2.0 / x;

            if (n > 0) {
                /* compute factorial of n and psi(n) */
                pn = -EUL;
                k = 1.0;
                for (i = 1; i < n; i++) {
                    pn += 1.0 / k;
                    k += 1.0;
                    fn *= k;
                }

                zmn = tox;

                if (n == 1) {
                    ans = 1.0 / x;
                } else {
                    nk1f = fn / n;
                    kf = 1.0;
                    s = nk1f;
                    z = -z0;
                    zn = 1.0;
                    for (i = 1; i < n; i++) {
                        nk1f = nk1f / (n - i);
                        kf = kf * i;
                        zn *= z;
                        t = nk1f * zn / kf;
                        s += t;
                        if ((Double.MAX_VALUE - Math.abs(t)) < Math.abs(s))
                            return Double.POSITIVE_INFINITY;
                        if ((tox > 1.0) && ((Double.MAX_VALUE / tox) < zmn))
                            return Double.POSITIVE_INFINITY;
                        zmn *= tox;
                    }
                    s *= 0.5;
                    t = Math.abs(s);
                    if ((zmn > 1.0) && ((Double.MAX_VALUE / zmn) < t))
                        return Double.POSITIVE_INFINITY;
                    if ((t > 1.0) && ((Double.MAX_VALUE / t) < zmn))
                        return Double.POSITIVE_INFINITY;
                    ans = s * zmn;
                }
            }


            tlg = 2.0 * log(0.5 * x);
            pk = -EUL;
            if (n == 0) {
                pn = pk;
                t = 1.0;
            } else {
                pn = pn + 1.0 / n;
                t = 1.0 / fn;
            }
            s = (pk + pn - tlg) * t;
            k = 1.0;
            do {
                t *= z0 / (k * (k + n));
                pk += 1.0 / k;
                pn += 1.0 / (k + n);
                s += (pk + pn - tlg) * t;
                k += 1.0;
            }
            while (Math.abs(t / s) > MACHEP);

            s = 0.5 * s / zmn;
            if ((n & 1) != 0)
                s = -s;
            ans += s;

            return (ans);
        }
    }
    /*                                                     beta.c
     *
     *     Beta function
     *
     *
     *
     * SYNOPSIS:
     *
     * double a, b, y, beta();
     *
     * y = beta( a, b );
     *
     *
     *
     * DESCRIPTION:
     *
     *                   -     -
     *                  | (a) | (b)
     * beta( a, b )  =  -----------.
     *                     -
     *                    | (a+b)
     *
     * For large arguments the logarithm of the function is
     * evaluated using lgam(), then exponentiated.
     *
     *
     *
     * ACCURACY:
     *
     *                      Relative error:
     * arithmetic   domain     # trials      peak         rms
     *    IEEE       0,30       30000       8.1e-14     1.1e-14
     *
     * ERROR MESSAGES:
     *
     *   message         condition          value returned
     * beta overflow    log(beta) > MAXLOG       0.0
     *                  a or b <0 integer        0.0
     *
     */


    /*
     * Cephes Math Library Release 2.0:  April, 1987
     * Copyright 1984, 1987 by Stephen L. Moshier
     * Direct inquiries to 30 Frost Street, Cambridge, MA 02140
     */

    static final class beta_c {
        private beta_c() {

        }

        static final double MAXGAM = 171.624376956302725;


        static final double ASYMP_FACTOR = 1e6;

        static double beta(double a, double b) throws OverflowException {
            double y;
            int[] sign = {1};

            if (a <= 0.0) {
                if (a == floor(a)) {
                    if (a == (int) a) {
                        return beta_negint((int) a, b);
                    } else {
                        return OverflowException.raiseException("beta", (sign[0] * Double.POSITIVE_INFINITY));
                    }
                }
            }

            if (b <= 0.0) {
                if (b == floor(b)) {
                    if (b == (int) b) {
                        return beta_negint((int) b, a);
                    } else {
                        return OverflowException.raiseException("beta", (sign[0] * Double.POSITIVE_INFINITY));
                    }
                }
            }

            if (Math.abs(a) < Math.abs(b)) {
                y = a;
                a = b;
                b = y;
            }

            if (Math.abs(a) > ASYMP_FACTOR * Math.abs(b) && a > ASYMP_FACTOR) {
                /* Avoid loss of precision in lgam(a + b) - lgam(a) */
                y = lbeta_asymp(a, b, sign);
                return sign[0] * exp(y);
            }

            y = a + b;
            if (Math.abs(y) > MAXGAM || Math.abs(a) > MAXGAM || Math.abs(b) > MAXGAM) {
                int[] sgngam = new int[1];
                y = lgam_sgn(y, sgngam);
                sign[0] *= sgngam[0];        /* keep track of the sign */
                y = lgam_sgn(b, sgngam) - y;
                sign[0] *= sgngam[0];
                y = lgam_sgn(a, sgngam) + y;
                sign[0] *= sgngam[0];
                if (y > Cephes.MAXLOG) {
                    return OverflowException.raiseException("beta", (sign[0] * Double.POSITIVE_INFINITY));
                }
                return (sign[0] * exp(y));
            }

            y = CephesImpl.gamma_c.Gamma(y);
            a = CephesImpl.gamma_c.Gamma(a);
            b = CephesImpl.gamma_c.Gamma(b);
            if (y == 0.0)
                return OverflowException.raiseException("beta", (sign[0] * Double.POSITIVE_INFINITY));

            if (Math.abs(Math.abs(a) - Math.abs(y)) > Math.abs(Math.abs(b) - Math.abs(y))) {
                y = b / y;
                y *= a;
            } else {
                y = a / y;
                y *= b;
            }

            return (y);

        }


        /* Natural log of |beta|. */

        static double lbeta(double a, double b) throws OverflowException {
            double y;
            int[] sign = {1};


            if (a <= 0.0) {
                if (a == floor(a)) {
                    if (a == (int) a) {
                        return lbeta_negint((int) a, b);
                    } else {
                        return (sign[0] * Double.POSITIVE_INFINITY);
                    }
                }
            }

            if (b <= 0.0) {
                if (b == floor(b)) {
                    if (b == (int) b) {
                        return lbeta_negint((int) b, a);
                    } else {
                        return (sign[0] * Double.POSITIVE_INFINITY);
                    }
                }
            }

            if (Math.abs(a) < Math.abs(b)) {
                y = a;
                a = b;
                b = y;
            }

            if (Math.abs(a) > ASYMP_FACTOR * Math.abs(b) && a > ASYMP_FACTOR) {
                /* Avoid loss of precision in lgam(a + b) - lgam(a) */
                y = lbeta_asymp(a, b, sign);
                return y;
            }

            y = a + b;
            if (Math.abs(y) > MAXGAM || Math.abs(a) > MAXGAM || Math.abs(b) > MAXGAM) {
                int[] sgngam = new int[1];
                y = lgam_sgn(y, sgngam);
                sign[0] *= sgngam[0];        /* keep track of the sign */
                y = lgam_sgn(b, sgngam) - y;
                sign[0] *= sgngam[0];        /* keep track of the sign */
                y = lgam_sgn(a, sgngam) + y;
                sign[0] *= sgngam[0];        /* keep track of the sign */
                return (y);
            }

            y = CephesImpl.gamma_c.Gamma(y);
            a = CephesImpl.gamma_c.Gamma(a);
            b = CephesImpl.gamma_c.Gamma(b);
            if (y == 0.0) {
                return OverflowException.raiseException("lbeta", sign[0] * Double.POSITIVE_INFINITY);
            }

            if (Math.abs(Math.abs(a) - Math.abs(y)) > Math.abs(Math.abs(b) - Math.abs(y))) {
                y = b / y;
                y *= a;
            } else {
                y = a / y;
                y *= b;
            }

            if (y < 0) {
                y = -y;
            }

            return (log(y));
        }

        /*
         * Asymptotic expansion for  ln(|B(a, b)|) for a > ASYMP_FACTOR*max(|b|, 1).
         */
        static double lbeta_asymp(double a, double b, int[] sgn) {
            double r = lgam_sgn(b, sgn);
            r -= b * log(a);

            r += b * (1 - b) / (2 * a);
            r += b * (1 - b) * (1 - 2 * b) / (12 * a * a);
            r += -b * b * (1 - b) * (1 - b) / (12 * a * a * a);

            return r;
        }


        /*
         * Special case for a negative integer argument
         */

        static double beta_negint(int a, double b) {
            int sgn;
            if (b == (int) b && 1 - a - b > 0) {
                sgn = ((int) b % 2 == 0) ? 1 : -1;
                return sgn * beta(1 - a - b, b);
            } else {
                return OverflowException.raiseException("lbeta", Double.POSITIVE_INFINITY);
            }
        }

        static double lbeta_negint(int a, double b) {
            double r;
            if (b == (int) b && 1 - a - b > 0) {
                r = lbeta(1 - a - b, b);
                return r;
            } else {
                return OverflowException.raiseException("lbeta", Double.POSITIVE_INFINITY);
            }
        }

    }
    /*                                                     incbet.c
     *
     *     Incomplete beta integral
     *
     *
     * SYNOPSIS:
     *
     * double a, b, x, y, incbet();
     *
     * y = incbet( a, b, x );
     *
     *
     * DESCRIPTION:
     *
     * Returns incomplete beta integral of the arguments, evaluated
     * from zero to x.  The function is defined as
     *
     *                  x
     *     -            -
     *    | (a+b)      | |  a-1     b-1
     *  -----------    |   t   (1-t)   dt.
     *   -     -     | |
     *  | (a) | (b)   -
     *                 0
     *
     * The domain of definition is 0 <= x <= 1.  In this
     * implementation a and b are restricted to positive values.
     * The integral from x to 1 may be obtained by the symmetry
     * relation
     *
     *    1 - incbet( a, b, x )  =  incbet( b, a, 1-x ).
     *
     * The integral is evaluated by a continued fraction expansion
     * or, when b*x is small, by a power series.
     *
     * ACCURACY:
     *
     * Tested at uniformly distributed random points (a,b,x) with a and b
     * in "domain" and x between 0 and 1.
     *                                        Relative error
     * arithmetic   domain     # trials      peak         rms
     *    IEEE      0,5         10000       6.9e-15     4.5e-16
     *    IEEE      0,85       250000       2.2e-13     1.7e-14
     *    IEEE      0,1000      30000       5.3e-12     6.3e-13
     *    IEEE      0,10000    250000       9.3e-11     7.1e-12
     *    IEEE      0,100000    10000       8.7e-10     4.8e-11
     * Outputs smaller than the IEEE gradual underflow threshold
     * were excluded from these statistics.
     *
     * ERROR MESSAGES:
     *   message         condition      value returned
     * incbet domain      x<0, x>1          0.0
     * incbet underflow                     0.0
     */


    /*
     * Cephes Math Library, Release 2.3:  March, 1995
     * Copyright 1984, 1995 by Stephen L. Moshier
     */

    static final class incbet_c {
        private incbet_c() {

        }

        final static double MAXGAM = 171.624376956302725;


        final static double big = 4.503599627370496e15;
        final static double biginv = 2.22044604925031308085e-16;


        static double incbet(double aa, double bb, double xx) {
            double a, b, t, x, xc, w, y;
            int flag;

            if (aa <= 0.0 || bb <= 0.0)
                return DomainException.raiseException("incbet", Double.NaN);

            if ((xx <= 0.0) || (xx >= 1.0)) {
                if (xx == 0.0)
                    return (0.0);
                if (xx == 1.0)
                    return (1.0);
                return DomainException.raiseException("incbet", Double.NaN);
            }

            flag = 0;
            if ((bb * xx) <= 1.0 && xx <= 0.95) {
                t = pseries(aa, bb, xx);
                return t;
            }

            w = 1.0 - xx;

            /* Reverse a and b if x is greater than the mean. */
            if (xx > (aa / (aa + bb))) {
                flag = 1;
                a = bb;
                b = aa;
                xc = xx;
                x = w;
            } else {
                a = aa;
                b = bb;
                xc = w;
                x = xx;
            }

            if (flag == 1 && (b * x) <= 1.0 && x <= 0.95) {
                t = pseries(a, b, x);
                return t <= MACHEP ? 1. - MACHEP : 1. - t;
            }

            /* Choose expansion for better convergence. */
            y = x * (a + b - 2.0) - (a - 1.0);
            if (y < 0.0)
                w = incbcf(a, b, x);
            else
                w = incbd(a, b, x) / xc;

            /* Multiply w by the factor
             * a      b   _             _     _
             * x  (1-x)   | (a+b) / ( a | (a) | (b) ) .   */

            y = a * log(x);
            t = b * log(xc);
            if ((a + b) < MAXGAM && Math.abs(y) < Cephes.MAXLOG && Math.abs(t) < Cephes.MAXLOG) {
                t = pow(xc, b);
                t *= pow(x, a);
                t /= a;
                t *= w;
                t *= 1.0 / beta(a, b);
                return flag == 1 ? (t <= MACHEP ? 1. - MACHEP : 1. - t) : t;
            }
            /* Resort to logarithms.  */
            y += t - lbeta(a, b);
            y += log(w / a);
            if (y < Cephes.MINLOG)
                t = 0.0;
            else
                t = exp(y);

            return flag == 1 ? (t <= MACHEP ? 1. - MACHEP : 1. - t) : t;
        }

        /* Continued fraction expansion #1
         * for incomplete beta integral
         */

        static double incbcf(double a, double b, double x) {
            double xk, pk, pkm1, pkm2, qk, qkm1, qkm2;
            double k1, k2, k3, k4, k5, k6, k7, k8;
            double r, t, ans, thresh;
            int n;

            k1 = a;
            k2 = a + b;
            k3 = a;
            k4 = a + 1.0;
            k5 = 1.0;
            k6 = b - 1.0;
            k7 = k4;
            k8 = a + 2.0;

            pkm2 = 0.0;
            qkm2 = 1.0;
            pkm1 = 1.0;
            qkm1 = 1.0;
            ans = 1.0;
            r = 1.0;
            n = 0;
            thresh = 3.0 * MACHEP;
            do {

                xk = -(x * k1 * k2) / (k3 * k4);
                pk = pkm1 + pkm2 * xk;
                qk = qkm1 + qkm2 * xk;
                pkm2 = pkm1;
                pkm1 = pk;
                qkm2 = qkm1;
                qkm1 = qk;

                xk = (x * k5 * k6) / (k7 * k8);
                pk = pkm1 + pkm2 * xk;
                qk = qkm1 + qkm2 * xk;
                pkm2 = pkm1;
                pkm1 = pk;
                qkm2 = qkm1;
                qkm1 = qk;

                if (qk != 0)
                    r = pk / qk;
                if (r != 0) {
                    t = Math.abs((ans - r) / r);
                    ans = r;
                } else
                    t = 1.0;

                if (t < thresh)
                    return (ans);

                k1 += 1.0;
                k2 += 1.0;
                k3 += 2.0;
                k4 += 2.0;
                k5 += 1.0;
                k6 -= 1.0;
                k7 += 2.0;
                k8 += 2.0;

                if ((Math.abs(qk) + Math.abs(pk)) > big) {
                    pkm2 *= biginv;
                    pkm1 *= biginv;
                    qkm2 *= biginv;
                    qkm1 *= biginv;
                }
                if (Math.abs(qk) < biginv || Math.abs(pk) < biginv) {
                    pkm2 *= big;
                    pkm1 *= big;
                    qkm2 *= big;
                    qkm1 *= big;
                }
            }
            while (++n < 300);

            return (ans);
        }


        /* Continued fraction expansion #2
         * for incomplete beta integral
         */

        static double incbd(double a, double b, double x) {
            double xk, pk, pkm1, pkm2, qk, qkm1, qkm2;
            double k1, k2, k3, k4, k5, k6, k7, k8;
            double r, t, ans, z, thresh;
            int n;

            k1 = a;
            k2 = b - 1.0;
            k3 = a;
            k4 = a + 1.0;
            k5 = 1.0;
            k6 = a + b;
            k7 = a + 1.0;

            k8 = a + 2.0;

            pkm2 = 0.0;
            qkm2 = 1.0;
            pkm1 = 1.0;
            qkm1 = 1.0;
            z = x / (1.0 - x);
            ans = 1.0;
            r = 1.0;
            n = 0;
            thresh = 3.0 * MACHEP;
            do {

                xk = -(z * k1 * k2) / (k3 * k4);
                pk = pkm1 + pkm2 * xk;
                qk = qkm1 + qkm2 * xk;
                pkm2 = pkm1;
                pkm1 = pk;
                qkm2 = qkm1;
                qkm1 = qk;

                xk = (z * k5 * k6) / (k7 * k8);
                pk = pkm1 + pkm2 * xk;
                qk = qkm1 + qkm2 * xk;
                pkm2 = pkm1;
                pkm1 = pk;
                qkm2 = qkm1;
                qkm1 = qk;

                if (qk != 0)
                    r = pk / qk;
                if (r != 0) {
                    t = Math.abs((ans - r) / r);
                    ans = r;
                } else
                    t = 1.0;

                if (t < thresh)
                    return (ans);

                k1 += 1.0;
                k2 -= 1.0;
                k3 += 2.0;
                k4 += 2.0;
                k5 += 1.0;
                k6 += 1.0;
                k7 += 2.0;
                k8 += 2.0;

                if ((Math.abs(qk) + Math.abs(pk)) > big) {
                    pkm2 *= biginv;
                    pkm1 *= biginv;
                    qkm2 *= biginv;
                    qkm1 *= biginv;
                }
                if ((Math.abs(qk) < biginv) || (Math.abs(pk) < biginv)) {
                    pkm2 *= big;
                    pkm1 *= big;
                    qkm2 *= big;
                    qkm1 *= big;
                }
            }
            while (++n < 300);
            return (ans);
        }

        /* Power series for incomplete beta integral.
         * Use when b*x is small and x not too close to 1.  */

        static double pseries(double a, double b, double x) {
            double s, t, u, v, n, t1, z, ai;

            ai = 1.0 / a;
            u = (1.0 - b) * x;
            v = u / (a + 1.0);
            t1 = v;
            t = u;
            n = 2.0;
            s = 0.0;
            z = MACHEP * ai;
            while (Math.abs(v) > z) {
                u = (n - b) * x / n;
                t *= u;
                v = t / (a + n);
                s += v;
                n += 1.0;
            }
            s += t1;
            s += ai;

            u = a * log(x);
            if ((a + b) < MAXGAM && Math.abs(u) < Cephes.MAXLOG) {
                t = 1.0 / beta(a, b);
                s = s * t * pow(x, a);
            } else {
                t = -lbeta(a, b) + u + log(s);
                if (t < Cephes.MINLOG)
                    s = 0.0;
                else
                    s = exp(t);
            }
            return (s);
        }

    }
    /*                                                     incbi()
     *
     *      Inverse of incomplete beta integral
     *
     *
     *
     * SYNOPSIS:
     *
     * double a, b, x, y, incbi();
     *
     * x = incbi( a, b, y );
     *
     *
     *
     * DESCRIPTION:
     *
     * Given y, the function finds x such that
     *
     *  incbet( a, b, x ) = y .
     *
     * The routine performs interval halving or Newton iterations to find the
     * root of incbet(a,b,x) - y = 0.
     *
     *
     * ACCURACY:
     *
     *                      Relative error:
     *                x     a,b
     * arithmetic   domain  domain  # trials    peak       rms
     *    IEEE      0,1    .5,10000   50000    5.8e-12   1.3e-13
     *    IEEE      0,1   .25,100    100000    1.8e-13   3.9e-15
     *    IEEE      0,1     0,5       50000    1.1e-12   5.5e-15
     *    VAX       0,1    .5,100     25000    3.5e-14   1.1e-15
     * With a and b constrained to half-integer or integer values:
     *    IEEE      0,1    .5,10000   50000    5.8e-12   1.1e-13
     *    IEEE      0,1    .5,100    100000    1.7e-14   7.9e-16
     * With a = .5, b constrained to half-integer or integer values:
     *    IEEE      0,1    .5,10000   10000    8.3e-11   1.0e-11
     */


    /*
     * Cephes Math Library Release 2.4:  March,1996
     * Copyright 1984, 1996 by Stephen L. Moshier
     */

    static final class incbi_c {
        private incbi_c() {

        }


        static double incbi(double aa, double bb, double yy0) {
            double a, b, y0, d = 0, y, x, x0, x1, lgm, yp = 0, dithresh, yl, yh, xt = 0;
            int rflg, nflg = 0;


            if (yy0 <= 0)
                return (0.0);
            if (yy0 >= 1.0)
                return (1.0);
            x0 = 0.0;
            yl = 0.0;
            x1 = 1.0;
            yh = 1.0;

            if (aa <= 1.0 || bb <= 1.0) {
                dithresh = 1.0e-6;
                rflg = 0;
                a = aa;
                b = bb;
                y0 = yy0;
                x = a / (a + b);
                y = Cephes.incbet(a, b, x);
                return ihalve(x, x0, x1, rflg, y, a, b, yp, dithresh, nflg, yl, yh, y0, d, xt, aa, bb, yy0);
            } else {
                dithresh = 1.0e-4;
            }
            /* approximation to inverse function */

            yp = -ndtri(yy0);

            if (yy0 > 0.5) {
                rflg = 1;
                a = bb;
                b = aa;
                y0 = 1.0 - yy0;
                yp = -yp;
            } else {
                rflg = 0;
                a = aa;
                b = bb;
                y0 = yy0;
            }

            lgm = (yp * yp - 3.0) / 6.0;
            x = 2.0 / (1.0 / (2.0 * a - 1.0) + 1.0 / (2.0 * b - 1.0));
            d = yp * sqrt(x + lgm) / x
                    - (1.0 / (2.0 * b - 1.0) - 1.0 / (2.0 * a - 1.0))
                    * (lgm + 5.0 / 6.0 - 2.0 / (3.0 * x));
            d = 2.0 * d;
            if (d < Cephes.MINLOG) {
                x = 1.0;
                return under(rflg);
            }
            x = a / (a + b * exp(d));
            y = Cephes.incbet(a, b, x);
            yp = (y - y0) / y0;
            if (Math.abs(yp) < 0.2)
                return newt(nflg, rflg, x, a, b, y, yl, x0, yh, x1, y0, d, xt, aa, bb, yy0, yp);

            return ihalve(x, x0, x1, rflg, y, a, b, yp, dithresh, nflg, yl, yh, y0, d, xt, aa, bb, yy0);

        }

        /* Resort to interval halving if not close enough. */
        private static double ihalve(double x, double x0, double x1, int rflg, double y, double a, double b, double yp, double dithresh, int nflg, double yl, double yh, double y0, double d, double xt, double aa, double bb, double yy0) {

            int dir = 0;
            double di = 0.5;
            for (int i = 0; i < 100; i++) {
                if (i != 0) {
                    x = x0 + di * (x1 - x0);
                    if (x == 1.0)
                        x = 1.0 - MACHEP;
                    if (x == 0.0) {
                        di = 0.5;
                        x = x0 + di * (x1 - x0);
                        if (x == 0.0)
                            return under(rflg);
                    }
                    y = Cephes.incbet(a, b, x);
                    yp = (x1 - x0) / (x1 + x0);
                    if (Math.abs(yp) < dithresh)
                        return newt(nflg, rflg, x, a, b, y, yl, x0, yh, x1, y0, d, xt, aa, bb, yy0, yp);
                    yp = (y - y0) / y0;
                    if (Math.abs(yp) < dithresh)
                        return newt(nflg, rflg, x, a, b, y, yl, x0, yh, x1, y0, d, xt, aa, bb, yy0, yp);
                }
                if (y < y0) {
                    x0 = x;
                    yl = y;
                    if (dir < 0) {
                        dir = 0;
                        di = 0.5;
                    } else if (dir > 3)
                        di = 1.0 - (1.0 - di) * (1.0 - di);
                    else if (dir > 1)
                        di = 0.5 * di + 0.5;
                    else
                        di = (y0 - y) / (yh - yl);
                    dir += 1;
                    if (x0 > 0.75) {
                        if (rflg == 1) {
                            rflg = 0;
                            a = aa;
                            b = bb;
                            y0 = yy0;
                        } else {
                            rflg = 1;
                            a = bb;
                            b = aa;
                            y0 = 1.0 - yy0;
                        }
                        x = 1.0 - x;
                        y = Cephes.incbet(a, b, x);
                        x0 = 0.0;
                        yl = 0.0;
                        x1 = 1.0;
                        yh = 1.0;
                        return ihalve(x, x0, x1, rflg, y, a, b, yp, dithresh, nflg, yl, yh, y0, d, xt, aa, bb, yy0);
                    }
                } else {
                    x1 = x;
                    if (rflg == 1 && x1 < MACHEP) {
                        x = 0.0;
                        return done(rflg, x);
                    }
                    yh = y;
                    if (dir > 0) {
                        dir = 0;
                        di = 0.5;
                    } else if (dir < -3)
                        di = di * di;
                    else if (dir < -1)
                        di = 0.5 * di;
                    else
                        di = (y - y0) / (yh - yl);
                    dir -= 1;
                }
            }
            LossException.raiseException("incbi");
            if (x0 >= 1.0) {
                x = 1.0 - MACHEP;
                return done(rflg, x);
            }
            if (x <= 0.0) {
                return under(rflg);
            }
            return newt(nflg, rflg, x, a, b, y, yl, x0, yh, x1, y0, d, xt, aa, bb, yy0, yp);

        }

        private static double under(int rflg) {
            UnderflowException.raiseException("incbi");
            return done(rflg, 0.0);
        }

        private static double newt(int nflg, int rflg, double x, double a, double b, double y, double yl, double x0, double yh, double x1,
                                   double y0, double d, double xt, double aa, double bb, double yy0, double yp) {

            if (nflg != 0)
                return done(rflg, x);
            nflg = 1;
            double lgm = lgam(a + b) - lgam(a) - lgam(b);

            for (int i = 0; i < 8; i++) {
                /* Compute the function at this point. */
                if (i != 0)
                    y = Cephes.incbet(a, b, x);
                if (y < yl) {
                    x = x0;
                    y = yl;
                } else if (y > yh) {
                    x = x1;
                    y = yh;
                } else if (y < y0) {
                    x0 = x;
                    yl = y;
                } else {
                    x1 = x;
                    yh = y;
                }
                if (x == 1.0 || x == 0.0)
                    break;
                /* Compute the derivative of the function at this point. */
                d = (a - 1.0) * log(x) + (b - 1.0) * log(1.0 - x) + lgm;
                if (d < Cephes.MINLOG)
                    return done(rflg, x);
                if (d > Cephes.MAXLOG)
                    break;
                d = exp(d);
                /* Compute the step to the next approximation of x. */
                d = (y - y0) / d;
                xt = x - d;
                if (xt <= x0) {
                    y = (x - x0) / (x1 - x0);
                    xt = x0 + 0.5 * y * (x - x0);
                    if (xt <= 0.0)
                        break;
                }
                if (xt >= x1) {
                    y = (x1 - x) / (x1 - x0);
                    xt = x1 - 0.5 * y * (x1 - x);
                    if (xt >= 1.0)
                        break;
                }
                x = xt;
                if (Math.abs(d / x) < 128.0 * MACHEP)
                    return done(rflg, x);
            }
            /* Did not converge.  */
            double dithresh = 256.0 * MACHEP;
            return ihalve(x, x0, x1, rflg, y, a, b, yp, dithresh, nflg, yl, yh, y0, d, xt, aa, bb, yy0);

        }

        private static double done(int rflg, double x) {
            if (rflg != 0) {
                if (x <= MACHEP)
                    x = 1.0 - MACHEP;
                else
                    x = 1.0 - x;
            }
            return (x);
        }


    }
    /*                                                     airy.c
     *
     *     Airy function
     *
     *
     *
     * SYNOPSIS:
     *
     * double x, ai, aip, bi, bip;
     * int airy();
     *
     * airy( x, _&ai, _&aip, _&bi, _&bip );
     *
     *
     *
     * DESCRIPTION:
     *
     * Solution of the differential equation
     *
     *     y"(x) = xy.
     *
     * The function returns the two independent solutions Ai, Bi
     * and their first derivatives Ai'(x), Bi'(x).
     *
     * Evaluation is by power series summation for small x,
     * by rational minimax approximations for large x.
     *
     *
     *
     * ACCURACY:
     * Error criterion is absolute when function <= 1, relative
     * when function > 1, except * denotes relative error criterion.
     * For large negative x, the absolute error increases as x^1.5.
     * For large positive x, the relative error increases as x^1.5.
     *
     * Arithmetic  domain   function  # trials      peak         rms
     * IEEE        -10, 0     Ai        10000       1.6e-15     2.7e-16
     * IEEE          0, 10    Ai        10000       2.3e-14*    1.8e-15*
     * IEEE        -10, 0     Ai'       10000       4.6e-15     7.6e-16
     * IEEE          0, 10    Ai'       10000       1.8e-14*    1.5e-15*
     * IEEE        -10, 10    Bi        30000       4.2e-15     5.3e-16
     * IEEE        -10, 10    Bi'       30000       4.9e-15     7.3e-16
     *
     */
    /*							airy.c */

    /*
     * Cephes Math Library Release 2.8:  June, 2000
     * Copyright 1984, 1987, 1989, 2000 by Stephen L. Moshier
     */
    static final class airy_c {
        private airy_c() {

        }

        final static double c1 = 0.35502805388781723926;
        final static double c2 = 0.258819403792806798405;
        final static double sqrt3 = 1.732050807568877293527;
        final static double sqpii = 5.64189583547756286948E-1;


        final static double MAXAIRY = 103.892; //25.77


        static double AN[] = {
                3.46538101525629032477E-1,
                1.20075952739645805542E1,
                7.62796053615234516538E1,
                1.68089224934630576269E2,
                1.59756391350164413639E2,
                7.05360906840444183113E1,
                1.40264691163389668864E1,
                9.99999999999999995305E-1,
        };

        static double AD[] = {
                5.67594532638770212846E-1,
                1.47562562584847203173E1,
                8.45138970141474626562E1,
                1.77318088145400459522E2,
                1.64234692871529701831E2,
                7.14778400825575695274E1,
                1.40959135607834029598E1,
                1.00000000000000000470E0,
        };

        static double APN[] = {
                6.13759184814035759225E-1,
                1.47454670787755323881E1,
                8.20584123476060982430E1,
                1.71184781360976385540E2,
                1.59317847137141783523E2,
                6.99778599330103016170E1,
                1.39470856980481566958E1,
                1.00000000000000000550E0,
        };

        static double APD[] = {
                3.34203677749736953049E-1,
                1.11810297306158156705E1,
                7.11727352147859965283E1,
                1.58778084372838313640E2,
                1.53206427475809220834E2,
                6.86752304592780337944E1,
                1.38498634758259442477E1,
                9.99999999999999994502E-1,
        };

        static double BN16[] = {
                -2.53240795869364152689E-1,
                5.75285167332467384228E-1,
                -3.29907036873225371650E-1,
                6.44404068948199951727E-2,
                -3.82519546641336734394E-3,
        };

        static double BD16[] = {
                /* 1.00000000000000000000E0, */
                -7.15685095054035237902E0,
                1.06039580715664694291E1,
                -5.23246636471251500874E0,
                9.57395864378383833152E-1,
                -5.50828147163549611107E-2,
        };

        static double BPPN[] = {
                4.65461162774651610328E-1,
                -1.08992173800493920734E0,
                6.38800117371827987759E-1,
                -1.26844349553102907034E-1,
                7.62487844342109852105E-3,
        };

        static double BPPD[] = {
                /* 1.00000000000000000000E0, */
                -8.70622787633159124240E0,
                1.38993162704553213172E1,
                -7.14116144616431159572E0,
                1.34008595960680518666E0,
                -7.84273211323341930448E-2,
        };

        static double AFN[] = {
                -1.31696323418331795333E-1,
                -6.26456544431912369773E-1,
                -6.93158036036933542233E-1,
                -2.79779981545119124951E-1,
                -4.91900132609500318020E-2,
                -4.06265923594885404393E-3,
                -1.59276496239262096340E-4,
                -2.77649108155232920844E-6,
                -1.67787698489114633780E-8,
        };

        static double AFD[] = {
                /* 1.00000000000000000000E0, */
                1.33560420706553243746E1,
                3.26825032795224613948E1,
                2.67367040941499554804E1,
                9.18707402907259625840E0,
                1.47529146771666414581E0,
                1.15687173795188044134E-1,
                4.40291641615211203805E-3,
                7.54720348287414296618E-5,
                4.51850092970580378464E-7,
        };

        static double AGN[] = {
                1.97339932091685679179E-2,
                3.91103029615688277255E-1,
                1.06579897599595591108E0,
                9.39169229816650230044E-1,
                3.51465656105547619242E-1,
                6.33888919628925490927E-2,
                5.85804113048388458567E-3,
                2.82851600836737019778E-4,
                6.98793669997260967291E-6,
                8.11789239554389293311E-8,
                3.41551784765923618484E-10,
        };

        static double AGD[] = {
                /*  1.00000000000000000000E0, */
                9.30892908077441974853E0,
                1.98352928718312140417E1,
                1.55646628932864612953E1,
                5.47686069422975497931E0,
                9.54293611618961883998E-1,
                8.64580826352392193095E-2,
                4.12656523824222607191E-3,
                1.01259085116509135510E-4,
                1.17166733214413521882E-6,
                4.91834570062930015649E-9,
        };

        static double APFN[] = {
                1.85365624022535566142E-1,
                8.86712188052584095637E-1,
                9.87391981747398547272E-1,
                4.01241082318003734092E-1,
                7.10304926289631174579E-2,
                5.90618657995661810071E-3,
                2.33051409401776799569E-4,
                4.08718778289035454598E-6,
                2.48379932900442457853E-8,
        };

        static double APFD[] = {
                /*  1.00000000000000000000E0, */
                1.47345854687502542552E1,
                3.75423933435489594466E1,
                3.14657751203046424330E1,
                1.09969125207298778536E1,
                1.78885054766999417817E0,
                1.41733275753662636873E-1,
                5.44066067017226003627E-3,
                9.39421290654511171663E-5,
                5.65978713036027009243E-7,
        };

        static double APGN[] = {
                -3.55615429033082288335E-2,
                -6.37311518129435504426E-1,
                -1.70856738884312371053E0,
                -1.50221872117316635393E0,
                -5.63606665822102676611E-1,
                -1.02101031120216891789E-1,
                -9.48396695961445269093E-3,
                -4.60325307486780994357E-4,
                -1.14300836484517375919E-5,
                -1.33415518685547420648E-7,
                -5.63803833958893494476E-10,
        };

        static double[] APGD = {
                /*  1.00000000000000000000E0, */
                9.85865801696130355144E0,
                2.16401867356585941885E1,
                1.73130776389749389525E1,
                6.17872175280828766327E0,
                1.08848694396321495475E0,
                9.95005543440888479402E-2,
                4.78468199683886610842E-3,
                1.18159633322838625562E-4,
                1.37480673554219441465E-6,
                5.79912514929147598821E-9,
        };


        /**
         * @param x       the value to find airy of
         * @param results is a four element double array in the order ai, aip, bi, bip
         * @return whether the operation when successfully
         */
        static int airy(double x, double[] results) {
            double z, zz, t, f, g, uf, ug, k, zeta, theta;
            int domflg;

            domflg = 0;
            if (x > MAXAIRY) {
                results[0] = 0;
                results[1] = 0;
                results[2] = Double.POSITIVE_INFINITY;
                results[3] = Double.POSITIVE_INFINITY;
                return (-1);
            }

            if (x < -2.09) {
                domflg = 15;
                t = sqrt(-x);
                zeta = -2.0 * x * t / 3.0;
                t = sqrt(t);
                k = sqpii / t;
                z = 1.0 / zeta;
                zz = z * z;
                uf = 1.0 + zz * polevl(zz, AFN, 8) / p1evl(zz, AFD, 9);
                ug = z * polevl(zz, AGN, 10) / p1evl(zz, AGD, 10);
                theta = zeta + 0.25 * Cephes.NPY_PI;
                f = sin(theta);
                g = cos(theta);
                results[0] = k * (f * uf - g * ug);
                results[2] = k * (g * uf + f * ug);
                uf = 1.0 + zz * polevl(zz, APFN, 8) / p1evl(zz, APFD, 9);
                ug = z * polevl(zz, APGN, 10) / p1evl(zz, APGD, 10);
                k = sqpii * t;
                results[1] = -k * (g * uf + f * ug);
                results[3] = k * (f * uf - g * ug);
                return (0);
            }
            if (x >= 2.09) {        /* cbrt(9) */
                domflg = 5;
                t = sqrt(x);
                zeta = 2.0 * x * t / 3.0;
                g = exp(zeta);
                t = sqrt(t);
                k = 2.0 * t * g;
                z = 1.0 / zeta;
                f = polevl(z, AN, 7) / polevl(z, AD, 7);
                results[0] = sqpii * f / k;
                k = -0.5 * sqpii * t / g;
                f = polevl(z, APN, 7) / polevl(z, APD, 7);
                results[1] = f * k;

                if (x > 8.3203353) {    /* zeta > 16 */
                    f = z * polevl(z, BN16, 4) / p1evl(z, BD16, 5);
                    k = sqpii * g;
                    results[2] = k * (1.0 + f) / t;
                    f = z * polevl(z, BPPN, 4) / p1evl(z, BPPD, 5);
                    results[3] = k * t * (1.0 + f);
                    return (0);
                }
            }

            f = 1.0;
            g = x;
            t = 1.0;
            uf = 1.0;
            ug = x;
            k = 1.0;
            z = x * x * x;
            while (t > MACHEP) {
                uf *= z;
                k += 1.0;
                uf /= k;
                ug *= z;
                k += 1.0;
                ug /= k;
                uf /= k;
                f += uf;
                k += 1.0;
                ug /= k;
                g += ug;
                t = Math.abs(uf / f);
            }
            uf = c1 * f;
            ug = c2 * g;
            if ((domflg & 1) == 0)
                results[0] = uf - ug;
            if ((domflg & 2) == 0)
                results[2] = sqrt3 * (uf + ug);

            /* the deriviative of ai */
            k = 4.0;
            uf = x * x / 2.0;
            ug = z / 3.0;
            f = uf;
            g = 1.0 + ug;
            uf /= 3.0;
            t = 1.0;

            while (t > MACHEP) {
                uf *= z;
                ug /= k;
                k += 1.0;
                ug *= z;
                uf /= k;
                f += uf;
                k += 1.0;
                ug /= k;
                uf /= k;
                g += ug;
                k += 1.0;
                t = Math.abs(ug / g);
            }

            uf = c1 * f;
            ug = c2 * g;
            if ((domflg & 4) == 0)
                results[1] = uf - ug;
            if ((domflg & 8) == 0)
                results[3] = sqrt3 * (uf + ug);
            return (0);
        }


    }
    /*                                                     bdtr.c
     *
     *     Binomial distribution
     *
     *
     *
     * SYNOPSIS:
     *
     * int k, n;
     * double p, y, bdtr();
     *
     * y = bdtr( k, n, p );
     *
     * DESCRIPTION:
     *
     * Returns the sum of the terms 0 through k of the Binomial
     * probability density:
     *
     *   k
     *   --  ( n )   j      n-j
     *   >   (   )  p  (1-p)
     *   --  ( j )
     *  j=0
     *
     * The terms are not summed directly; instead the incomplete
     * beta integral is employed, according to the formula
     *
     * y = bdtr( k, n, p ) = incbet( n-k, k+1, 1-p ).
     *
     * The arguments must be positive, with p ranging from 0 to 1.
     *
     * ACCURACY:
     *
     * Tested at random points (a,b,p), with p between 0 and 1.
     *
     *               a,b                     Relative error:
     * arithmetic  domain     # trials      peak         rms
     *  For p between 0.001 and 1:
     *    IEEE     0,100       100000      4.3e-15     2.6e-16
     * See also incbet.c.
     *
     * ERROR MESSAGES:
     *
     *   message         condition      value returned
     * bdtr domain         k < 0            0.0
     *                     n < k
     *                     x < 0, x > 1
     */
    /*							bdtrc()
     *
     *	Complemented binomial distribution
     *
     *
     *
     * SYNOPSIS:
     *
     * int k, n;
     * double p, y, bdtrc();
     *
     * y = bdtrc( k, n, p );
     *
     * DESCRIPTION:
     *
     * Returns the sum of the terms k+1 through n of the Binomial
     * probability density:
     *
     *   n
     *   --  ( n )   j      n-j
     *   >   (   )  p  (1-p)
     *   --  ( j )
     *  j=k+1
     *
     * The terms are not summed directly; instead the incomplete
     * beta integral is employed, according to the formula
     *
     * y = bdtrc( k, n, p ) = incbet( k+1, n-k, p ).
     *
     * The arguments must be positive, with p ranging from 0 to 1.
     *
     * ACCURACY:
     *
     * Tested at random points (a,b,p).
     *
     *               a,b                     Relative error:
     * arithmetic  domain     # trials      peak         rms
     *  For p between 0.001 and 1:
     *    IEEE     0,100       100000      6.7e-15     8.2e-16
     *  For p between 0 and .001:
     *    IEEE     0,100       100000      1.5e-13     2.7e-15
     *
     * ERROR MESSAGES:
     *
     *   message         condition      value returned
     * bdtrc domain      x<0, x>1, n<k       0.0
     */
    /*							bdtri()
     *
     *	Inverse binomial distribution
     *
     *
     *
     * SYNOPSIS:
     *
     * int k, n;
     * double p, y, bdtri();
     *
     * p = bdtri( k, n, y );
     *
     * DESCRIPTION:
     *
     * Finds the event probability p such that the sum of the
     * terms 0 through k of the Binomial probability density
     * is equal to the given cumulative probability y.
     *
     * This is accomplished using the inverse beta integral
     * function and the relation
     *
     * 1 - p = incbi( n-k, k+1, y ).
     *
     * ACCURACY:
     *
     * Tested at random points (a,b,p).
     *
     *               a,b                     Relative error:
     * arithmetic  domain     # trials      peak         rms
     *  For p between 0.001 and 1:
     *    IEEE     0,100       100000      2.3e-14     6.4e-16
     *    IEEE     0,10000     100000      6.6e-12     1.2e-13
     *  For p between 10^-6 and 0.001:
     *    IEEE     0,100       100000      2.0e-12     1.3e-14
     *    IEEE     0,10000     100000      1.5e-12     3.2e-14
     * See also incbi.c.
     *
     * ERROR MESSAGES:
     *
     *   message         condition      value returned
     * bdtri domain     k < 0, n <= k         0.0
     *                  x < 0, x > 1
     */

    /*                                                             bdtr() */


    /*
     * Cephes Math Library Release 2.3:  March, 1995
     * Copyright 1984, 1987, 1995 by Stephen L. Moshier
     */
    static final class bdtr_c {
        private bdtr_c() {

        }

        static double bdtrc(double k, int n, double p) throws DomainException {
            double dk, dn;
            double fk = floor(k);

            if (Double.isNaN(p) || Double.isNaN(k)) {
                return Double.NaN;
            }

            if (p < 0.0 || p > 1.0 || n < fk) {
                return DomainException.raiseException("bdtrc", Double.NaN);
            }

            if (fk < 0) {
                return 1.0;
            }

            if (fk == n) {
                return 0.0;
            }

            dn = n - fk;
            if (k == 0) {
                if (p < .01)
                    dk = -CephesImpl.unity_c.expm1(dn * CephesImpl.unity_c.log1p(-p));
                else
                    dk = 1.0 - pow(1.0 - p, dn);
            } else {
                dk = fk + 1;
                dk = Cephes.incbet(dk, dn, p);
            }
            return dk;
        }


        static double bdtr(double k, int n, double p) throws DomainException {
            double dk, dn;
            double fk = floor(k);

            if (Double.isNaN(p) || Double.isNaN(k)) {
                return Double.NaN;
            }

            if (p < 0.0 || p > 1.0 || fk < 0 || n < fk) {
                return DomainException.raiseException("bdtr", Double.NaN);
            }

            if (fk == n)
                return 1.0;

            dn = n - fk;
            if (fk == 0) {
                dk = pow(1.0 - p, dn);
            } else {
                dk = fk + 1.;
                dk = Cephes.incbet(dn, dk, 1.0 - p);
            }
            return dk;
        }


        static double bdtri(double k, int n, double y) throws DomainException {
            double p, dn, dk;
            double fk = floor(k);

            if (Double.isNaN(k)) {
                return Double.NaN;
            }

            if (y < 0.0 || y > 1.0 || fk < 0.0 || n <= fk) {
                return DomainException.raiseException("bdtri", Double.NaN);
            }

            dn = n - fk;

            if (fk == n)
                return 1.0;

            if (fk == 0) {
                if (y > 0.8) {
                    p = -CephesImpl.unity_c.expm1(CephesImpl.unity_c.log1p(y - 1.0) / dn);
                } else {
                    p = 1.0 - pow(y, 1.0 / dn);
                }
            } else {
                dk = fk + 1;
                p = Cephes.incbet(dn, dk, 0.5);
                if (p > 0.5)
                    p = Cephes.incbi(dk, dn, 1.0 - y);
                else
                    p = 1.0 - Cephes.incbi(dn, dk, y);
            }
            return p;
        }

    }

    static final class besselpoly_c {
        private besselpoly_c() {

        }

        static final double EPS = 1.0e-17;

        static double besselpoly(double a, double lambda, double nu) {

            int m, factor = 0;
            double Sm, relerr, Sol;
            double sum = 0.0;

            /* Special handling for a = 0.0 */
            if (a == 0.0) {
                if (nu == 0.0) return 1.0 / (lambda + 1);
                else return 0.0;
            }
            /* Special handling for negative and integer nu */
            if ((nu < 0) && (floor(nu) == nu)) {
                nu = -nu;
                factor = ((int) nu) % 2;
            }
            Sm = exp(nu * log(a)) / (Cephes.Gamma(nu + 1) * (lambda + nu + 1));
            m = 0;
            do {
                sum += Sm;
                Sol = Sm;
                Sm *= -a * a * (lambda + nu + 1 + 2 * m) / ((nu + m + 1) * (m + 1) * (lambda + nu + 1 + 2 * m + 2));
                m++;
                relerr = Math.abs((Sm - Sol) / Sm);
            } while (relerr > EPS && m < 1000);
            if (factor == 0)
                return sum;
            else
                return -sum;
        }


    }

    /*                                                     btdtr.c
     *
     *     Beta distribution
     *
     *
     *
     * SYNOPSIS:
     *
     * double a, b, x, y, btdtr();
     *
     * y = btdtr( a, b, x );
     *
     *
     *
     * DESCRIPTION:
     *
     * Returns the area from zero to x under the beta density
     * function:
     *
     *
     *                          x
     *            -             -
     *           | (a+b)       | |  a-1      b-1
     * P(x)  =  ----------     |   t    (1-t)    dt
     *           -     -     | |
     *          | (a) | (b)   -
     *                         0
     *
     *
     * This function is identical to the incomplete beta
     * integral function incbet(a, b, x).
     *
     * The complemented function is
     *
     * 1 - P(1-x)  =  incbet( b, a, x );
     *
     *
     * ACCURACY:
     *
     * See incbet.c.
     *
     */

    /*                                                             btdtr() */


    /*
     * Cephes Math Library Release 2.0:  April, 1987
     * Copyright 1984, 1987, 1995 by Stephen L. Moshier
     */

    static final class btdtr_c {
        private btdtr_c() {

        }

        static double btdtr(double a, double b, double x) {

            return (Cephes.incbet(a, b, x));
        }

    }
    /*                                                     ellie.c
     *
     *     Incomplete elliptic integral of the second kind
     *
     *
     *
     * SYNOPSIS:
     *
     * double phi, m, y, ellie();
     *
     * y = ellie( phi, m );
     *
     *
     *
     * DESCRIPTION:
     *
     * Approximates the integral
     *
     *
     *                 phi
     *                  -
     *                 | |
     *                 |                   2
     * E(phi_\m)  =    |    sqrt( 1 - m sin t ) dt
     *                 |
     *               | |
     *                -
     *                 0
     *
     * of amplitude phi and modulus m, using the arithmetic -
     * geometric mean algorithm.
     *
     *
     *
     * ACCURACY:
     *
     * Tested at random arguments with phi in [-10, 10] and m in
     * [0, 1].
     *                      Relative error:
     * arithmetic   domain     # trials      peak         rms
     *    IEEE     -10,10      150000       3.3e-15     1.4e-16
     */


    /*
     * Cephes Math Library Release 2.0:  April, 1987
     * Copyright 1984, 1987, 1993 by Stephen L. Moshier
     * Direct inquiries to 30 Frost Street, Cambridge, MA 02140
     */
    /* Copyright 2014, Eric W. Moore */

    /*     Incomplete elliptic integral of second kind     */

    static final class ellie_c {
        private ellie_c() {

        }

        static double ellie(double phi, double m) {
            double a, b, c, e, temp;
            double lphi, t, E, denom, npio2;
            int d, mod, sign;

            if (Double.isNaN(phi) || Double.isNaN(m))
                return Double.NaN;
            if (m > 1.0)
                return Double.NaN;
            if (Double.isInfinite(phi))
                return phi;
            if (Double.isInfinite(m))
                return -m;
            if (m == 0.0)
                return (phi);
            lphi = phi;
            npio2 = floor(lphi / Cephes.NPY_PI_2);
            if ((Math.abs(npio2) % 2.0) == 1.0)
                npio2 += 1;
            lphi = lphi - npio2 * Cephes.NPY_PI_2;
            if (lphi < 0.0) {
                lphi = -lphi;
                sign = -1;
            } else {
                sign = 1;
            }
            a = 1.0 - m;
            E = ellpe(m);
            if (a == 0.0) {
                temp = sin(lphi);
                return done(sign, temp, npio2);
            }
            if (a > 1.0) {
                temp = ellie_neg_m(lphi, m);
                return done(sign, temp, npio2);
            }

            if (lphi < 0.135) {
                double m11 = (((((-7.0 / 2816.0) * m + (5.0 / 1056.0)) * m - (7.0 / 2640.0)) * m
                        + (17.0 / 41580.0)) * m - (1.0 / 155925.0)) * m;
                double m9 = ((((-5.0 / 1152.0) * m + (1.0 / 144.0)) * m - (1.0 / 360.0)) * m
                        + (1.0 / 5670.0)) * m;
                double m7 = ((-m / 112.0 + (1.0 / 84.0)) * m - (1.0 / 315.0)) * m;
                double m5 = (-m / 40.0 + (1.0 / 30)) * m;
                double m3 = -m / 6.0;
                double p2 = lphi * lphi;

                temp = ((((m11 * p2 + m9) * p2 + m7) * p2 + m5) * p2 + m3) * p2 * lphi + lphi;
                return done(sign, temp, npio2);
            }
            t = tan(lphi);
            b = sqrt(a);
            /* Thanks to Brian Fitzgerald <fitzgb@mml0.meche.rpi.edu>
             * for pointing out an instability near odd multiples of pi/2.  */
            if (Math.abs(t) > 10.0) {
                /* Transform the amplitude */
                e = 1.0 / (b * t);
                /* ... but avoid multiple recursions.  */
                if (Math.abs(e) < 10.0) {
                    e = atan(e);
                    temp = E + m * sin(lphi) * sin(e) - ellie(e, m);
                    return done(sign, temp, npio2);
                }
            }
            c = sqrt(m);
            a = 1.0;
            d = 1;
            e = 0.0;
            mod = 0;

            while (Math.abs(c / a) > MACHEP) {
                temp = b / a;
                lphi = lphi + atan(t * temp) + mod * Cephes.NPY_PI;
                denom = 1 - temp * t * t;
                if (Math.abs(denom) > 10 * MACHEP) {
                    t = t * (1.0 + temp) / denom;
                    mod = (int) ((lphi + Cephes.NPY_PI_2) / Cephes.NPY_PI);
                } else {
                    t = tan(lphi);
                    mod = (int) floor((lphi - atan(t)) / Cephes.NPY_PI);
                }
                c = (a - b) / 2.0;
                temp = sqrt(a * b);
                a = (a + b) / 2.0;
                b = temp;
                d += d;
                e += c * sin(lphi);
            }

            temp = E / ellpk(1.0 - m);
            temp *= (atan(t) + mod * Cephes.NPY_PI) / (d * a);
            temp += e;

            return done(sign, temp, npio2);
        }

        private static double done(int sign, double temp, double npio2) {
            if (sign < 0)
                temp = -temp;
            temp += npio2 * E;
            return (temp);
        }

        /* To calculate legendre's incomplete elliptical integral of the second kind for
         * negative m, we use a power series in phi for small m*phi*phi, an asymptotic
         * series in m for large m*phi*phi* and the relation to Carlson's symmetric
         * integrals, R_F(x,y,z) and R_D(x,y,z).
         *
         * E(phi, m) = sin(phi) * R_F(cos(phi)^2, 1 - m * sin(phi)^2, 1.0)
         *             - m * sin(phi)^3 * R_D(cos(phi)^2, 1 - m * sin(phi)^2, 1.0) / 3
         *
         *           = R_F(c-1, c-m, c) - m * R_D(c-1, c-m, c) / 3
         *
         * where c = csc(phi)^2. We use the second form of this for (approximately)
         * phi > 1/(sqrt(DBL_MAX) ~ 1e-154, where csc(phi)^2 overflows. Elsewhere we
         * use the first form, accounting for the smallness of phi.
         *
         * The algorithm used is described in Carlson, B. C. Numerical computation of
         * real or complex elliptic integrals. (1994) https://arxiv.org/abs/math/9409227
         * Most variable names reflect Carlson's usage.
         *
         * In this routine, we assume m < 0 and  0 > phi > pi/2.
         */
        static double ellie_neg_m(double phi, double m) {
            double x, y, z, x1, y1, z1, ret, Q;
            double A0f, Af, Xf, Yf, Zf, E2f, E3f, scalef;
            double A0d, Ad, seriesn, seriesd, Xd, Yd, Zd, E2d, E3d, E4d, E5d, scaled;
            int n = 0;
            double mpp = (m * phi) * phi;

            if (-mpp < 1e-6 && phi < -m) {
                return phi + (mpp * phi * phi / 30.0 - mpp * mpp / 40.0 - mpp / 6.0) * phi;
            }

            if (-mpp > 1e6) {
                double sm = sqrt(-m);
                double sp = sin(phi);
                double cp = cos(phi);

                double a = -cosm1(phi);
                double b1 = log(4 * sp * sm / (1 + cp));
                double b = -(0.5 + b1) / 2.0 / m;
                double c = (0.75 + cp / sp / sp - b1) / 16.0 / m / m;
                return (a + b + c) * sm;
            }

            if (phi > 1e-153 && m > -1e200) {
                double s = sin(phi);
                double csc2 = 1.0 / s / s;
                scalef = 1.0;
                scaled = m / 3.0;
                x = 1.0 / tan(phi) / tan(phi);
                y = csc2 - m;
                z = csc2;
            } else {
                scalef = phi;
                scaled = mpp * phi / 3.0;
                x = 1.0;
                y = 1 - mpp;
                z = 1.0;
            }

            if (x == y && x == z) {
                return (scalef + scaled / x) / sqrt(x);
            }

            A0f = (x + y + z) / 3.0;
            Af = A0f;
            A0d = (x + y + 3.0 * z) / 5.0;
            Ad = A0d;
            x1 = x;
            y1 = y;
            z1 = z;
            seriesd = 0.0;
            seriesn = 1.0;
            /* Carlson gives 1/pow(3*r, 1.0/6.0) for this constant. if r == eps,
             * it is ~338.38. */
            Q = 400.0 * Libs.max(Math.abs(A0f - x), Math.abs(A0f - y), Math.abs(A0f - z));

            while (Q > Math.abs(Af) && Q > Math.abs(Ad) && n <= 100) {
                double sx = sqrt(x1);
                double sy = sqrt(y1);
                double sz = sqrt(z1);
                double lam = sx * sy + sx * sz + sy * sz;
                seriesd += seriesn / (sz * (z1 + lam));
                x1 = (x1 + lam) / 4.0;
                y1 = (y1 + lam) / 4.0;
                z1 = (z1 + lam) / 4.0;
                Af = (x1 + y1 + z1) / 3.0;
                Ad = (Ad + lam) / 4.0;
                n += 1;
                Q /= 4.0;
                seriesn /= 4.0;
            }

            Xf = (A0f - x) / Af / (1 << 2 * n);
            Yf = (A0f - y) / Af / (1 << 2 * n);
            Zf = -(Xf + Yf);

            E2f = Xf * Yf - Zf * Zf;
            E3f = Xf * Yf * Zf;

            ret = scalef * (1.0 - E2f / 10.0 + E3f / 14.0 + E2f * E2f / 24.0
                    - 3.0 * E2f * E3f / 44.0) / sqrt(Af);

            Xd = (A0d - x) / Ad / (1 << 2 * n);
            Yd = (A0d - y) / Ad / (1 << 2 * n);
            Zd = -(Xd + Yd) / 3.0;

            E2d = Xd * Yd - 6.0 * Zd * Zd;
            E3d = (3 * Xd * Yd - 8.0 * Zd * Zd) * Zd;
            E4d = 3.0 * (Xd * Yd - Zd * Zd) * Zd * Zd;
            E5d = Xd * Yd * Zd * Zd * Zd;

            ret -= scaled * (1.0 - 3.0 * E2d / 14.0 + E3d / 6.0 + 9.0 * E2d * E2d / 88.0
                    - 3.0 * E4d / 22.0 - 9.0 * E2d * E3d / 52.0 + 3.0 * E5d / 26.0)
                    / (1 << 2 * n) / Ad / sqrt(Ad);
            ret -= 3.0 * scaled * seriesd;
            return ret;
        }
    }

    /*                                                     ellik.c
     *
     *     Incomplete elliptic integral of the first kind
     *
     *
     *
     * SYNOPSIS:
     *
     * double phi, m, y, ellik();
     *
     * y = ellik( phi, m );
     *
     *
     *
     * DESCRIPTION:
     *
     * Approximates the integral
     *
     *
     *
     *                phi
     *                 -
     *                | |
     *                |           dt
     * F(phi | m) =   |    ------------------
     *                |                   2
     *              | |    sqrt( 1 - m sin t )
     *               -
     *                0
     *
     * of amplitude phi and modulus m, using the arithmetic -
     * geometric mean algorithm.
     *
     *
     *
     *
     * ACCURACY:
     *
     * Tested at random points with m in [0, 1] and phi as indicated.
     *
     *                      Relative error:
     * arithmetic   domain     # trials      peak         rms
     *    IEEE     -10,10       200000      7.4e-16     1.0e-16
     *
     *
     */


    /*
     * Cephes Math Library Release 2.0:  April, 1987
     * Copyright 1984, 1987 by Stephen L. Moshier
     * Direct inquiries to 30 Frost Street, Cambridge, MA 02140
     */
    /* Copyright 2014, Eric W. Moore */

    /*     Incomplete elliptic integral of first kind      */

    static final class ellik_c {
        private ellik_c() {

        }

        static double ellik(double phi, double m) throws SingularException {
            double a, b, c, e, temp, t, K, denom, npio2;
            int d, mod, sign;

            if (Double.isNaN(phi) || Double.isNaN(m))
                return Double.NaN;
            if (m > 1.0)
                return Double.NaN;
            if (Double.isInfinite(phi) || Double.isInfinite(m)) {
                if (Double.isInfinite(m) && Double.isFinite(phi))
                    return 0.0;
                else if (Double.isInfinite(phi) && Double.isFinite(m))
                    return phi;
                else
                    return Double.NaN;
            }
            if (m == 0.0)
                return (phi);
            a = 1.0 - m;
            if (a == 0.0) {
                if (Math.abs(phi) >= Cephes.NPY_PI_2) {
                    return SingularException.raiseException("ellik", Double.POSITIVE_INFINITY);

                }
                /* DLMF 19.6.8, and 4.23.42 */
                return asinh(tan(phi));
            }
            npio2 = floor(phi / Cephes.NPY_PI_2);
            if ((Math.abs(npio2) % 2.0) == 1.0)
                npio2 += 1;
            if (npio2 != 0.0) {
                K = ellpk(a);
                phi = phi - npio2 * Cephes.NPY_PI_2;
            } else
                K = 0.0;
            if (phi < 0.0) {
                phi = -phi;
                sign = -1;
            } else
                sign = 0;
            if (a > 1.0) {
                temp = ellik_neg_m(phi, m);
                return done(sign, temp, npio2, K);
            }
            b = sqrt(a);
            t = tan(phi);
            if (Math.abs(t) > 10.0) {
                /* Transform the amplitude */
                e = 1.0 / (b * t);
                /* ... but avoid multiple recursions.  */
                if (Math.abs(e) < 10.0) {
                    e = atan(e);
                    if (npio2 == 0)
                        K = ellpk(a);
                    temp = K - ellik(e, m);
                    return done(sign, temp, npio2, K);
                }
            }
            a = 1.0;
            c = sqrt(m);
            d = 1;
            mod = 0;

            while (Math.abs(c / a) > MACHEP) {
                temp = b / a;
                phi = phi + atan(t * temp) + mod * Cephes.NPY_PI;
                denom = 1.0 - temp * t * t;
                if (Math.abs(denom) > 10 * MACHEP) {
                    t = t * (1.0 + temp) / denom;
                    mod = (int) ((phi + Cephes.NPY_PI_2) / Cephes.NPY_PI);
                } else {
                    t = tan(phi);
                    mod = (int) floor((phi - atan(t)) / Cephes.NPY_PI);
                }
                c = (a - b) / 2.0;
                temp = sqrt(a * b);
                a = (a + b) / 2.0;
                b = temp;
                d += d;
            }

            temp = (atan(t) + mod * Cephes.NPY_PI) / (d * a);

            return done(sign, temp, npio2, K);
        }

        private static double done(int sign, double temp, double npio2, double K) {
            if (sign < 0)
                temp = -temp;
            temp += npio2 * K;
            return (temp);

        }

        static double ellik_neg_m(double phi, double m) {
            double x, y, z, x1, y1, z1, A0, A, Q, X, Y, Z, E2, E3, scale;
            int n = 0;
            double mpp = (m * phi) * phi;

            if (-mpp < 1e-6 && phi < -m) {
                return phi + (-mpp * phi * phi / 30.0 + 3.0 * mpp * mpp / 40.0 + mpp / 6.0) * phi;
            }

            if (-mpp > 4e7) {
                double sm = sqrt(-m);
                double sp = sin(phi);
                double cp = cos(phi);

                double a = log(4 * sp * sm / (1 + cp));
                double b = -(1 + cp / sp / sp - a) / 4 / m;
                return (a + b) / sm;
            }

            if (phi > 1e-153 && m > -1e305) {
                double s = sin(phi);
                double csc2 = 1.0 / (s * s);
                scale = 1.0;
                x = 1.0 / (tan(phi) * tan(phi));
                y = csc2 - m;
                z = csc2;
            } else {
                scale = phi;
                x = 1.0;
                y = 1 - m * scale * scale;
                z = 1.0;
            }

            if (x == y && x == z) {
                return scale / sqrt(x);
            }

            A0 = (x + y + z) / 3.0;
            A = A0;
            x1 = x;
            y1 = y;
            z1 = z;
            /* Carlson gives 1/pow(3*r, 1.0/6.0) for this constant. if r == eps,
             * it is ~338.38. */
            Q = 400.0 * Libs.max(Math.abs(A0 - x), Math.abs(A0 - y), Math.abs(A0 - z));

            while (Q > Math.abs(A) && n <= 100) {
                double sx = sqrt(x1);
                double sy = sqrt(y1);
                double sz = sqrt(z1);
                double lam = sx * sy + sx * sz + sy * sz;
                x1 = (x1 + lam) / 4.0;
                y1 = (y1 + lam) / 4.0;
                z1 = (z1 + lam) / 4.0;
                A = (x1 + y1 + z1) / 3.0;
                n += 1;
                Q /= 4;
            }
            X = (A0 - x) / A / (1 << 2 * n);
            Y = (A0 - y) / A / (1 << 2 * n);
            Z = -(X + Y);

            E2 = X * Y - Z * Z;
            E3 = X * Y * Z;

            return scale * (1.0 - E2 / 10.0 + E3 / 14.0 + E2 * E2 / 24.0
                    - 3.0 * E2 * E3 / 44.0) / sqrt(A);
        }
    }
    /*                                                     ellpj.c
     *
     *     Jacobian Elliptic Functions
     *
     *
     *
     * SYNOPSIS:
     *
     * double u, m, sn, cn, dn, phi;
     * int ellpj();
     *
     * ellpj( u, m, _&sn, _&cn, _&dn, _&phi );
     *
     *
     *
     * DESCRIPTION:
     *
     *
     * Evaluates the Jacobian elliptic functions sn(u|m), cn(u|m),
     * and dn(u|m) of parameter m between 0 and 1, and real
     * argument u.
     *
     * These functions are periodic, with quarter-period on the
     * real axis equal to the complete elliptic integral
     * ellpk(m).
     *
     * Relation to incomplete elliptic integral:
     * If u = ellik(phi,m), then sn(u|m) = sin(phi),
     * and cn(u|m) = cos(phi).  Phi is called the amplitude of u.
     *
     * Computation is by means of the arithmetic-geometric mean
     * algorithm, except when m is within 1e-9 of 0 or 1.  In the
     * latter case with m close to 1, the approximation applies
     * only for phi < pi/2.
     *
     * ACCURACY:
     *
     * Tested at random points with u between 0 and 10, m between
     * 0 and 1.
     *
     *            Absolute error (* = relative error):
     * arithmetic   function   # trials      peak         rms
     *    IEEE      phi         10000       9.2e-16*    1.4e-16*
     *    IEEE      sn          50000       4.1e-15     4.6e-16
     *    IEEE      cn          40000       3.6e-15     4.4e-16
     *    IEEE      dn          10000       1.3e-12     1.8e-14
     *
     *  Peak error observed in consistency check using addition
     * theorem for sn(u+v) was 4e-16 (absolute).  Also tested by
     * the above relation to the incomplete elliptic integral.
     * Accuracy deteriorates when u is large.
     *
     */

    /*                                                     ellpj.c         */


    /*
     * Cephes Math Library Release 2.0:  April, 1987
     * Copyright 1984, 1987 by Stephen L. Moshier
     * Direct inquiries to 30 Frost Street, Cambridge, MA 02140
     */

    /* Scipy changes:
     * - 07-18-2016: improve evaluation of dn near quarter periods
     */

    static final class ellpj_c {
        private ellpj_c() {

        }

        static double[] ellpj(double u, double m) throws DomainException {
            final double[] out = new double[4];
            ellpj(u, m, out);
            return out;

        }

        //results = [sn, cn, dn, ph]
        static int ellpj(double u, double m, double[] results) {
            double ai, b, phi = 0, t = 0, twon, dnfac = 0;
            double[] a = new double[9], c = new double[9];
            int i;

            /* Check for special cases */
            if (m < 0.0 || m > 1.0 || Double.isNaN(m)) {
                DomainException.raiseException("ellpj");
                results[0] = Double.NaN;
                results[1] = Double.NaN;
                results[3] = Double.NaN;
                results[2] = Double.NaN;
                return (-1);
            }
            if (m < 1.0e-9) {
                t = sin(u);
                b = cos(u);
                ai = 0.25 * m * (u - t * b);
                results[0] = t - ai * b;
                results[1] = b + ai * t;
                results[3] = u - ai;
                results[2] = 1.0 - 0.5 * m * t * t;
                return (0);
            }
            if (m >= 0.9999999999) {
                ai = 0.25 * (1.0 - m);
                b = cosh(u);
                t = tanh(u);
                phi = 1.0 / b;
                twon = b * sinh(u);
                results[0] = t + ai * (twon - u) / (b * b);
                results[3] = 2.0 * atan(exp(u)) - Cephes.NPY_PI_2 + ai * (twon - u) / b;
                ai *= t * phi;
                results[1] = phi - ai * (twon - u);
                results[2] = phi + ai * (twon + u);
                return (0);
            }

            /* A. G. M. scale. See DLMF 22.20(ii) */
            a[0] = 1.0;
            b = sqrt(1.0 - m);
            c[0] = sqrt(m);
            twon = 1.0;
            i = 0;

            while (Math.abs(c[i] / a[i]) > MACHEP) {
                if (i > 7) {
                    OverflowException.raiseException("ellpj");
                    return done(phi, twon, a, u, t, c, i, b, results, dnfac, m);
                }
                ai = a[i];
                ++i;
                c[i] = (ai - b) / 2.0;
                t = sqrt(ai * b);
                a[i] = (ai + b) / 2.0;
                b = t;
                twon *= 2.0;
            }

            return done(phi, twon, a, u, t, c, i, b, results, dnfac, m);

        }

        private static int done(double phi, double twon, double[] a, double u, double t, double[] c, int i, double b, double[] results, double dnfac, double m) {
            /* backward recurrence */
            phi = twon * a[i] * u;
            do {
                t = c[i] * sin(phi) / a[i];
                b = phi;
                phi = (asin(t) + phi) / 2.0;
            }
            while (--i != 0);

            results[0] = sin(phi);
            t = cos(phi);
            results[1] = t;
            dnfac = cos(phi - b);
            /* See discussion after DLMF 22.20.5 */
            if (Math.abs(dnfac) < 0.1) {
                results[2] = sqrt(1 - m * (results[0]) * (results[0]));
            } else {
                results[2] = t / dnfac;
            }
            results[3] = phi;
            return (0);
        }

    }
    /*                                                     exp2.c
     *
     *     Base 2 exponential function
     *
     *
     *
     * SYNOPSIS:
     *
     * double x, y, exp2();
     *
     * y = exp2( x );
     *
     *
     *
     * DESCRIPTION:
     *
     * Returns 2 raised to the x power.
     *
     * Range reduction is accomplished by separating the argument
     * into an integer k and fraction f such that
     *     x    k  f
     *    2  = 2  2.
     *
     * A Pade' form
     *
     *   1 + 2x P(x**2) / (Q(x**2) - x P(x**2) )
     *
     * approximates 2**x in the basic range [-0.5, 0.5].
     *
     *
     * ACCURACY:
     *
     *                      Relative error:
     * arithmetic   domain     # trials      peak         rms
     *    IEEE    -1022,+1024   30000       1.8e-16     5.4e-17
     *
     *
     * See exp.c for comments on error amplification.
     *
     *
     * ERROR MESSAGES:
     *
     *   message         condition      value returned
     * exp underflow    x < -MAXL2        0.0
     * exp overflow     x > MAXL2         NPY_INFINITY
     *
     * For IEEE arithmetic, MAXL2 = 1024.
     */


    /*
     * Cephes Math Library Release 2.3:  March, 1995
     * Copyright 1984, 1995 by Stephen L. Moshier
     */


    static final class exp2_c {
        private exp2_c() {

        }

        static double P[] = {
                2.30933477057345225087E-2,
                2.02020656693165307700E1,
                1.51390680115615096133E3,
        };

        static double Q[] = {
                /* 1.00000000000000000000E0, */
                2.33184211722314911771E2,
                4.36821166879210612817E3,
        };

        final static double MAXL2 = 1024.0;
        final static double MINL2 = -1024.0;

        static double exp2(double x) {
            double px, xx;
            short n;

            if (Double.isNaN(x))
                return (x);
            if (x > MAXL2) {
                return (Double.POSITIVE_INFINITY);
            }

            if (x < MINL2) {
                return (0.0);
            }

            xx = x;            /* save x */
            /* separate into integer and fractional parts */
            px = floor(x + 0.5);
            n = (short) px;
            x = x - px;

            /* rational approximation
             * exp2(x) = 1 +  2xP(xx)/(Q(xx) - P(xx))
             * where xx = x**2
             */
            xx = x * x;
            px = x * polevl(xx, P, 2);
            x = px / (p1evl(xx, Q, 2) - px);
            x = 1.0 + ldexp(x, 1);

            /* scale by power of 2 */
            x = ldexp(x, n);
            return (x);
        }

    }
    /*                                                     exp10.c
     *
     *     Base 10 exponential function
     *      (Common antilogarithm)
     *
     *
     *
     * SYNOPSIS:
     *
     * double x, y, exp10();
     *
     * y = exp10( x );
     *
     *
     *
     * DESCRIPTION:
     *
     * Returns 10 raised to the x power.
     *
     * Range reduction is accomplished by expressing the argument
     * as 10**x = 2**n 10**f, with |f| < 0.5 log10(2).
     * The Pade' form
     *
     *    1 + 2x P(x**2)/( Q(x**2) - P(x**2) )
     *
     * is used to approximate 10**f.
     *
     *
     *
     * ACCURACY:
     *
     *                      Relative error:
     * arithmetic   domain     # trials      peak         rms
     *    IEEE     -307,+307    30000       2.2e-16     5.5e-17
     *
     * ERROR MESSAGES:
     *
     *   message         condition      value returned
     * exp10 underflow    x < -MAXL10        0.0
     * exp10 overflow     x > MAXL10       NPY_INFINITY
     *
     * IEEE arithmetic: MAXL10 = 308.2547155599167.
     *
     */

    /*
     * Cephes Math Library Release 2.2:  January, 1991
     * Copyright 1984, 1991 by Stephen L. Moshier
     * Direct inquiries to 30 Frost Street, Cambridge, MA 02140
     */

    static final class exp10_c {
        private exp10_c() {

        }

        private final static double P[] = {
                4.09962519798587023075E-2,
                1.17452732554344059015E1,
                4.06717289936872725516E2,
                2.39423741207388267439E3,
        };

        private final static double Q[] = {
                /* 1.00000000000000000000E0, */
                8.50936160849306532625E1,
                1.27209271178345121210E3,
                2.07960819286001865907E3,
        };

        /* static double LOG102 = 3.01029995663981195214e-1; */
        private final static double LOG210 = 3.32192809488736234787e0;
        private final static double LG102A = 3.01025390625000000000E-1;
        private final static double LG102B = 4.60503898119521373889E-6;

        /* static double MAXL10 = 38.230809449325611792; */
        private final static double MAXL10 = 308.2547155599167;


        static double exp10(double x) {
            double px, xx;
            short n;

            if (Double.isNaN(x))
                return (x);
            if (x > MAXL10) {
                return (Double.POSITIVE_INFINITY);
            }

            if (x < -MAXL10) {        /* Would like to use MINLOG but can't */
                return UnderflowException.raiseException("exp10", (0.0));
            }

            /* Express 10**x = 10**g 2**n
             *   = 10**g 10**( n log10(2) )
             *   = 10**( g + n log10(2) )
             */
            px = floor(LOG210 * x + 0.5);
            n = (short) px;
            x -= px * LG102A;
            x -= px * LG102B;

            /* rational approximation for exponential
             * of the fractional part:
             * 10**x = 1 + 2x P(x**2)/( Q(x**2) - P(x**2) )
             */
            xx = x * x;
            px = x * polevl(xx, P, 3);
            x = px / (p1evl(xx, Q, 3) - px);
            x = 1.0 + ldexp(x, 1);

            /* multiply by power of 2 */
            x = ldexp(x, n);

            return (x);
        }


    }
    /*                                                     fdtr.c
     *
     *     F distribution
     *
     *
     *
     * SYNOPSIS:
     *
     * double df1, df2;
     * double x, y, fdtr();
     *
     * y = fdtr( df1, df2, x );
     *
     * DESCRIPTION:
     *
     * Returns the area from zero to x under the F density
     * function (also known as Snedcor's density or the
     * variance ratio density).  This is the density
     * of x = (u1/df1)/(u2/df2), where u1 and u2 are random
     * variables having Chi square distributions with df1
     * and df2 degrees of freedom, respectively.
     *
     * The incomplete beta integral is used, according to the
     * formula
     *
     *     P(x) = incbet( df1/2, df2/2, (df1*x/(df2 + df1*x) ).
     *
     *
     * The arguments a and b are greater than zero, and x is
     * nonnegative.
     *
     * ACCURACY:
     *
     * Tested at random points (a,b,x).
     *
     *                x     a,b                     Relative error:
     * arithmetic  domain  domain     # trials      peak         rms
     *    IEEE      0,1    0,100       100000      9.8e-15     1.7e-15
     *    IEEE      1,5    0,100       100000      6.5e-15     3.5e-16
     *    IEEE      0,1    1,10000     100000      2.2e-11     3.3e-12
     *    IEEE      1,5    1,10000     100000      1.1e-11     1.7e-13
     * See also incbet.c.
     *
     *
     * ERROR MESSAGES:
     *
     *   message         condition      value returned
     * fdtr domain     a<0, b<0, x<0         0.0
     *
     */

    /*                         fdtrc()
     *
     *  Complemented F distribution
     *
     *
     *
     * SYNOPSIS:
     *
     * double df1, df2;
     * double x, y, fdtrc();
     *
     * y = fdtrc( df1, df2, x );
     *
     * DESCRIPTION:
     *
     * Returns the area from x to infinity under the F density
     * function (also known as Snedcor's density or the
     * variance ratio density).
     *
     *
     *                      inf.
     *                       -
     *              1       | |  a-1      b-1
     * 1-P(x)  =  ------    |   t    (1-t)    dt
     *            B(a,b)  | |
     *                     -
     *                      x
     *
     *
     * The incomplete beta integral is used, according to the
     * formula
     *
     *  P(x) = incbet( df2/2, df1/2, (df2/(df2 + df1*x) ).
     *
     *
     * ACCURACY:
     *
     * Tested at random points (a,b,x) in the indicated intervals.
     *                x     a,b                     Relative error:
     * arithmetic  domain  domain     # trials      peak         rms
     *    IEEE      0,1    1,100       100000      3.7e-14     5.9e-16
     *    IEEE      1,5    1,100       100000      8.0e-15     1.6e-15
     *    IEEE      0,1    1,10000     100000      1.8e-11     3.5e-13
     *    IEEE      1,5    1,10000     100000      2.0e-11     3.0e-12
     * See also incbet.c.
     *
     * ERROR MESSAGES:
     *
     *   message         condition      value returned
     * fdtrc domain    a<0, b<0, x<0         0.0
     *
     */

    /*                         fdtri()
     *
     *  Inverse of F distribution
     *
     *
     *
     * SYNOPSIS:
     *
     * double df1, df2;
     * double x, p, fdtri();
     *
     * x = fdtri( df1, df2, p );
     *
     * DESCRIPTION:
     *
     * Finds the F density argument x such that the integral
     * from -infinity to x of the F density is equal to the
     * given probability p.
     *
     * This is accomplished using the inverse beta integral
     * function and the relations
     *
     *      z = incbi( df2/2, df1/2, p )
     *      x = df2 (1-z) / (df1 z).
     *
     * Note: the following relations hold for the inverse of
     * the uncomplemented F distribution:
     *
     *      z = incbi( df1/2, df2/2, p )
     *      x = df2 z / (df1 (1-z)).
     *
     * ACCURACY:
     *
     * Tested at random points (a,b,p).
     *
     *              a,b                     Relative error:
     * arithmetic  domain     # trials      peak         rms
     *  For p between .001 and 1:
     *    IEEE     1,100       100000      8.3e-15     4.7e-16
     *    IEEE     1,10000     100000      2.1e-11     1.4e-13
     *  For p between 10^-6 and 10^-3:
     *    IEEE     1,100        50000      1.3e-12     8.4e-15
     *    IEEE     1,10000      50000      3.0e-12     4.8e-14
     * See also fdtrc.c.
     *
     * ERROR MESSAGES:
     *
     *   message         condition      value returned
     * fdtri domain   p <= 0 or p > 1       NaN
     *                     v < 1
     *
     */

    /*
     * Cephes Math Library Release 2.3:  March, 1995
     * Copyright 1984, 1987, 1995 by Stephen L. Moshier
     */
    static final class fdtr_c {
        private fdtr_c() {

        }

        static double fdtrc(double a, double b, double x) throws DomainException {
            double w;

            if ((a <= 0.0) || (b <= 0.0) || (x < 0.0)) {
                return DomainException.raiseException("fdtrc", Double.NaN);
            }
            w = b / (b + a * x);
            return Cephes.incbet(0.5 * b, 0.5 * a, w);
        }


        static double fdtr(double a, double b, double x) throws DomainException {
            double w;

            if ((a <= 0.0) || (b <= 0.0) || (x < 0.0)) {
                return DomainException.raiseException("fdtr", Double.NaN);

            }
            w = a * x;
            w = w / (b + w);
            return Cephes.incbet(0.5 * a, 0.5 * b, w);
        }


        static double fdtri(double a, double b, double y) throws DomainException {
            double w, x;

            if ((a <= 0.0) || (b <= 0.0) || (y <= 0.0) || (y > 1.0)) {
                return DomainException.raiseException("fdtri", Double.NaN);

            }
            y = 1.0 - y;
            /* Compute probability for x = 0.5.  */
            w = Cephes.incbet(0.5 * b, 0.5 * a, 0.5);
            /* If that is greater than y, then the solution w < .5.
             * Otherwise, solve at 1-y to remove cancellation in (b - b*w).  */
            if (w > y || y < 0.001) {
                w = Cephes.incbi(0.5 * b, 0.5 * a, y);
                x = (b - b * w) / (a * w);
            } else {
                w = Cephes.incbi(0.5 * a, 0.5 * b, 1.0 - y);
                x = b * w / (a * (1.0 - w));
            }
            return x;
        }

    }
    /*                                                     fresnl.c
     *
     *     Fresnel integral
     *
     *
     *
     * SYNOPSIS:
     *
     * double x, S, C;
     * void fresnl();
     *
     * fresnl( x, _&S, _&C );
     *
     *
     * DESCRIPTION:
     *
     * Evaluates the Fresnel integrals
     *
     *           x
     *           -
     *          | |
     * C(x) =   |   cos(pi/2 t**2) dt,
     *        | |
     *         -
     *          0
     *
     *           x
     *           -
     *          | |
     * S(x) =   |   sin(pi/2 t**2) dt.
     *        | |
     *         -
     *          0
     *
     *
     * The integrals are evaluated by a power series for x < 1.
     * For x >= 1 auxiliary functions f(x) and g(x) are employed
     * such that
     *
     * C(x) = 0.5 + f(x) sin( pi/2 x**2 ) - g(x) cos( pi/2 x**2 )
     * S(x) = 0.5 - f(x) cos( pi/2 x**2 ) - g(x) sin( pi/2 x**2 )
     *
     *
     *
     * ACCURACY:
     *
     *  Relative error.
     *
     * Arithmetic  function   domain     # trials      peak         rms
     *   IEEE       S(x)      0, 10       10000       2.0e-15     3.2e-16
     *   IEEE       C(x)      0, 10       10000       1.8e-15     3.3e-16
     */

    /*
     * Cephes Math Library Release 2.1:  January, 1989
     * Copyright 1984, 1987, 1989 by Stephen L. Moshier
     * Direct inquiries to 30 Frost Street, Cambridge, MA 02140
     */
    static final class fresnl_c {
        private fresnl_c() {

        }

        /* S(x) for small x */
        private final static double sn[] = {
                -2.99181919401019853726E3,
                7.08840045257738576863E5,
                -6.29741486205862506537E7,
                2.54890880573376359104E9,
                -4.42979518059697779103E10,
                3.18016297876567817986E11,
        };

        private final static double sd[] = {
                /* 1.00000000000000000000E0, */
                2.81376268889994315696E2,
                4.55847810806532581675E4,
                5.17343888770096400730E6,
                4.19320245898111231129E8,
                2.24411795645340920940E10,
                6.07366389490084639049E11,
        };

        /* C(x) for small x */
        private final static double cn[] = {
                -4.98843114573573548651E-8,
                9.50428062829859605134E-6,
                -6.45191435683965050962E-4,
                1.88843319396703850064E-2,
                -2.05525900955013891793E-1,
                9.99999999999999998822E-1,
        };

        private final static double cd[] = {
                3.99982968972495980367E-12,
                9.15439215774657478799E-10,
                1.25001862479598821474E-7,
                1.22262789024179030997E-5,
                8.68029542941784300606E-4,
                4.12142090722199792936E-2,
                1.00000000000000000118E0,
        };

        /* Auxiliary function f(x) */
        private final static double fn[] = {
                4.21543555043677546506E-1,
                1.43407919780758885261E-1,
                1.15220955073585758835E-2,
                3.45017939782574027900E-4,
                4.63613749287867322088E-6,
                3.05568983790257605827E-8,
                1.02304514164907233465E-10,
                1.72010743268161828879E-13,
                1.34283276233062758925E-16,
                3.76329711269987889006E-20,
        };

        private final static double fd[] = {
                /*  1.00000000000000000000E0, */
                7.51586398353378947175E-1,
                1.16888925859191382142E-1,
                6.44051526508858611005E-3,
                1.55934409164153020873E-4,
                1.84627567348930545870E-6,
                1.12699224763999035261E-8,
                3.60140029589371370404E-11,
                5.88754533621578410010E-14,
                4.52001434074129701496E-17,
                1.25443237090011264384E-20,
        };

        /* Auxiliary function g(x) */
        private final static double gn[] = {
                5.04442073643383265887E-1,
                1.97102833525523411709E-1,
                1.87648584092575249293E-2,
                6.84079380915393090172E-4,
                1.15138826111884280931E-5,
                9.82852443688422223854E-8,
                4.45344415861750144738E-10,
                1.08268041139020870318E-12,
                1.37555460633261799868E-15,
                8.36354435630677421531E-19,
                1.86958710162783235106E-22,
        };

        private final static double gd[] = {
                /*  1.00000000000000000000E0, */
                1.47495759925128324529E0,
                3.37748989120019970451E-1,
                2.53603741420338795122E-2,
                8.14679107184306179049E-4,
                1.27545075667729118702E-5,
                1.04314589657571990585E-7,
                4.60680728146520428211E-10,
                1.10273215066240270757E-12,
                1.38796531259578871258E-15,
                8.39158816283118707363E-19,
                1.86958710162783236342E-22,
        };

        static double[] fresnl(double xxa) {
            double[] out = new double[2];
            fresnl(xxa, out);
            return out;
        }

        //results = {ssa, cca}
        static int fresnl(double xxa, double[] results) {
            double f, g, cc, ss, c, s, t, u;
            double x, x2;

            if (Double.isInfinite(xxa)) {
                cc = 0.5;
                ss = 0.5;
                return done(xxa, cc, ss, results);
            }

            x = Math.abs(xxa);
            x2 = x * x;
            if (x2 < 2.5625) {
                t = x2 * x2;
                ss = x * x2 * polevl(t, sn, 5) / p1evl(t, sd, 6);
                cc = x * polevl(t, cn, 5) / polevl(t, cd, 6);
                return done(xxa, cc, ss, results);
            }

            if (x > 36974.0) {
                /*
                 * http://functions.wolfram.com/GammaBetaErf/FresnelC/06/02/
                 * http://functions.wolfram.com/GammaBetaErf/FresnelS/06/02/
                 */
                cc = 0.5 + 1 / (Cephes.NPY_PI * x) * sin(Cephes.NPY_PI * x * x / 2);
                ss = 0.5 - 1 / (Cephes.NPY_PI * x) * cos(Cephes.NPY_PI * x * x / 2);
                return done(xxa, cc, ss, results);
            }


            /*             Asymptotic power series auxiliary functions
             *             for large argument
             */
            x2 = x * x;
            t = Cephes.NPY_PI * x2;
            u = 1.0 / (t * t);
            t = 1.0 / t;
            f = 1.0 - u * polevl(u, fn, 9) / p1evl(u, fd, 10);
            g = t * polevl(u, gn, 10) / p1evl(u, gd, 11);

            t = Cephes.NPY_PI_2 * x2;
            c = cos(t);
            s = sin(t);
            t = Cephes.NPY_PI * x;
            cc = 0.5 + (f * s - g * c) / t;
            ss = 0.5 - (f * c + g * s) / t;
            return done(xxa, cc, ss, results);

        }

        private static int done(double xxa, double cc, double ss, double[] results) {
            if (xxa < 0.0) {
                cc = -cc;
                ss = -ss;
            }

            results[1] = cc;
            results[0] = ss;
            return (0);
        }

    }
    /*                                                     nbdtr.c
     *
     *     Negative binomial distribution
     *
     *
     *
     * SYNOPSIS:
     *
     * int k, n;
     * double p, y, nbdtr();
     *
     * y = nbdtr( k, n, p );
     *
     * DESCRIPTION:
     *
     * Returns the sum of the terms 0 through k of the negative
     * binomial distribution:
     *
     *   k
     *   --  ( n+j-1 )   n      j
     *   >   (       )  p  (1-p)
     *   --  (   j   )
     *  j=0
     *
     * In a sequence of Bernoulli trials, this is the probability
     * that k or fewer failures precede the nth success.
     *
     * The terms are not computed individually; instead the incomplete
     * beta integral is employed, according to the formula
     *
     * y = nbdtr( k, n, p ) = incbet( n, k+1, p ).
     *
     * The arguments must be positive, with p ranging from 0 to 1.
     *
     * ACCURACY:
     *
     * Tested at random points (a,b,p), with p between 0 and 1.
     *
     *               a,b                     Relative error:
     * arithmetic  domain     # trials      peak         rms
     *    IEEE     0,100       100000      1.7e-13     8.8e-15
     * See also incbet.c.
     *
     */
    /*							nbdtrc.c
     *
     *	Complemented negative binomial distribution
     *
     *
     *
     * SYNOPSIS:
     *
     * int k, n;
     * double p, y, nbdtrc();
     *
     * y = nbdtrc( k, n, p );
     *
     * DESCRIPTION:
     *
     * Returns the sum of the terms k+1 to infinity of the negative
     * binomial distribution:
     *
     *   inf
     *   --  ( n+j-1 )   n      j
     *   >   (       )  p  (1-p)
     *   --  (   j   )
     *  j=k+1
     *
     * The terms are not computed individually; instead the incomplete
     * beta integral is employed, according to the formula
     *
     * y = nbdtrc( k, n, p ) = incbet( k+1, n, 1-p ).
     *
     * The arguments must be positive, with p ranging from 0 to 1.
     *
     * ACCURACY:
     *
     * Tested at random points (a,b,p), with p between 0 and 1.
     *
     *               a,b                     Relative error:
     * arithmetic  domain     # trials      peak         rms
     *    IEEE     0,100       100000      1.7e-13     8.8e-15
     * See also incbet.c.
     */

    /*                                                     nbdtrc
     *
     *     Complemented negative binomial distribution
     *
     *
     *
     * SYNOPSIS:
     *
     * int k, n;
     * double p, y, nbdtrc();
     *
     * y = nbdtrc( k, n, p );
     *
     * DESCRIPTION:
     *
     * Returns the sum of the terms k+1 to infinity of the negative
     * binomial distribution:
     *
     *   inf
     *   --  ( n+j-1 )   n      j
     *   >   (       )  p  (1-p)
     *   --  (   j   )
     *  j=k+1
     *
     * The terms are not computed individually; instead the incomplete
     * beta integral is employed, according to the formula
     *
     * y = nbdtrc( k, n, p ) = incbet( k+1, n, 1-p ).
     *
     * The arguments must be positive, with p ranging from 0 to 1.
     *
     * ACCURACY:
     *
     * See incbet.c.
     */
    /*							nbdtri
     *
     *	Functional inverse of negative binomial distribution
     *
     *
     *
     * SYNOPSIS:
     *
     * int k, n;
     * double p, y, nbdtri();
     *
     * p = nbdtri( k, n, y );
     *
     * DESCRIPTION:
     *
     * Finds the argument p such that nbdtr(k,n,p) is equal to y.
     *
     * ACCURACY:
     *
     * Tested at random points (a,b,y), with y between 0 and 1.
     *
     *               a,b                     Relative error:
     * arithmetic  domain     # trials      peak         rms
     *    IEEE     0,100       100000      1.5e-14     8.5e-16
     * See also incbi.c.
     */

    /*
     * Cephes Math Library Release 2.3:  March, 1995
     * Copyright 1984, 1987, 1995 by Stephen L. Moshier
     */

    static final class nbdtr_c {
        private nbdtr_c() {

        }

        static double nbdtrc(int k, int n, double p) throws DomainException {
            double dk, dn;

            if ((p < 0.0) || (p > 1.0))
                return (DomainException.raiseException("nbdtrc", Double.NaN));
            if (k < 0) {
                return (DomainException.raiseException("nbdtrc", Double.NaN));

            }

            dk = k + 1;
            dn = n;
            return (Cephes.incbet(dk, dn, 1.0 - p));
        }


        static double nbdtr(int k, int n, double p) throws DomainException {
            double dk, dn;

            if ((p < 0.0) || (p > 1.0))
                return (DomainException.raiseException("nbdtr", Double.NaN));
            if (k < 0) {
                return (DomainException.raiseException("nbdtr", Double.NaN));

            }
            dk = k + 1;
            dn = n;
            return (Cephes.incbet(dn, dk, p));
        }


        static double nbdtri(int k, int n, double p) throws DomainException {
            double dk, dn, w;

            if ((p < 0.0) || (p > 1.0))
                return (DomainException.raiseException("nbdtri", Double.NaN));
            if (k < 0) {
                return (DomainException.raiseException("nbdtri", Double.NaN));
            }
            dk = k + 1;
            dn = n;
            w = Cephes.incbi(dn, dk, p);
            return (w);
        }

    }
    /*                                                     shichi.c
     *
     *     Hyperbolic sine and cosine integrals
     *
     *
     *
     * SYNOPSIS:
     *
     * double x, Chi, Shi, shichi();
     *
     * shichi( x, &Chi, &Shi );
     *
     *
     * DESCRIPTION:
     *
     * Approximates the integrals
     *
     *                            x
     *                            -
     *                           | |   cosh t - 1
     *   Chi(x) = eul + ln x +   |    -----------  dt,
     *                         | |          t
     *                          -
     *                          0
     *
     *               x
     *               -
     *              | |  sinh t
     *   Shi(x) =   |    ------  dt
     *            | |       t
     *             -
     *             0
     *
     * where eul = 0.57721566490153286061 is Euler's constant.
     * The integrals are evaluated by power series for x < 8
     * and by Chebyshev expansions for x between 8 and 88.
     * For large x, both functions approach exp(x)/2x.
     * Arguments greater than 88 in magnitude return NPY_INFINITY.
     *
     *
     * ACCURACY:
     *
     * Test interval 0 to 88.
     *                      Relative error:
     * arithmetic   function  # trials      peak         rms
     *    IEEE         Shi      30000       6.9e-16     1.6e-16
     *        Absolute error, except relative when |Chi| > 1:
     *    IEEE         Chi      30000       8.4e-16     1.4e-16
     */

    /*
     * Cephes Math Library Release 2.0:  April, 1987
     * Copyright 1984, 1987 by Stephen L. Moshier
     * Direct inquiries to 30 Frost Street, Cambridge, MA 02140
     */

    static final class shichi_c {
        private shichi_c() {

        }

        /* x exp(-x) shi(x), inverted interval 8 to 18 */
        static double S1[] = {
                1.83889230173399459482E-17,
                -9.55485532279655569575E-17,
                2.04326105980879882648E-16,
                1.09896949074905343022E-15,
                -1.31313534344092599234E-14,
                5.93976226264314278932E-14,
                -3.47197010497749154755E-14,
                -1.40059764613117131000E-12,
                9.49044626224223543299E-12,
                -1.61596181145435454033E-11,
                -1.77899784436430310321E-10,
                1.35455469767246947469E-9,
                -1.03257121792819495123E-9,
                -3.56699611114982536845E-8,
                1.44818877384267342057E-7,
                7.82018215184051295296E-7,
                -5.39919118403805073710E-6,
                -3.12458202168959833422E-5,
                8.90136741950727517826E-5,
                2.02558474743846862168E-3,
                2.96064440855633256972E-2,
                1.11847751047257036625E0
        };

        /* x exp(-x) shi(x), inverted interval 18 to 88 */
        static double S2[] = {
                -1.05311574154850938805E-17,
                2.62446095596355225821E-17,
                8.82090135625368160657E-17,
                -3.38459811878103047136E-16,
                -8.30608026366935789136E-16,
                3.93397875437050071776E-15,
                1.01765565969729044505E-14,
                -4.21128170307640802703E-14,
                -1.60818204519802480035E-13,
                3.34714954175994481761E-13,
                2.72600352129153073807E-12,
                1.66894954752839083608E-12,
                -3.49278141024730899554E-11,
                -1.58580661666482709598E-10,
                -1.79289437183355633342E-10,
                1.76281629144264523277E-9,
                1.69050228879421288846E-8,
                1.25391771228487041649E-7,
                1.16229947068677338732E-6,
                1.61038260117376323993E-5,
                3.49810375601053973070E-4,
                1.28478065259647610779E-2,
                1.03665722588798326712E0
        };

        /* x exp(-x) chin(x), inverted interval 8 to 18 */
        static double C1[] = {
                -8.12435385225864036372E-18,
                2.17586413290339214377E-17,
                5.22624394924072204667E-17,
                -9.48812110591690559363E-16,
                5.35546311647465209166E-15,
                -1.21009970113732918701E-14,
                -6.00865178553447437951E-14,
                7.16339649156028587775E-13,
                -2.93496072607599856104E-12,
                -1.40359438136491256904E-12,
                8.76302288609054966081E-11,
                -4.40092476213282340617E-10,
                -1.87992075640569295479E-10,
                1.31458150989474594064E-8,
                -4.75513930924765465590E-8,
                -2.21775018801848880741E-7,
                1.94635531373272490962E-6,
                4.33505889257316408893E-6,
                -6.13387001076494349496E-5,
                -3.13085477492997465138E-4,
                4.97164789823116062801E-4,
                2.64347496031374526641E-2,
                1.11446150876699213025E0
        };

        /* x exp(-x) chin(x), inverted interval 18 to 88 */
        static double C2[] = {
                8.06913408255155572081E-18,
                -2.08074168180148170312E-17,
                -5.98111329658272336816E-17,
                2.68533951085945765591E-16,
                4.52313941698904694774E-16,
                -3.10734917335299464535E-15,
                -4.42823207332531972288E-15,
                3.49639695410806959872E-14,
                6.63406731718911586609E-14,
                -3.71902448093119218395E-13,
                -1.27135418132338309016E-12,
                2.74851141935315395333E-12,
                2.33781843985453438400E-11,
                2.71436006377612442764E-11,
                -2.56600180000355990529E-10,
                -1.61021375163803438552E-9,
                -4.72543064876271773512E-9,
                -3.00095178028681682282E-9,
                7.79387474390914922337E-8,
                1.06942765566401507066E-6,
                1.59503164802313196374E-5,
                3.49592575153777996871E-4,
                1.28475387530065247392E-2,
                1.03665693917934275131E0
        };


        /* Sine and cosine integrals */

        //results = {si, ci}
        static int shichi(double x, double[] results) {
            double k, z, c, s, a;
            short sign;

            if (x < 0.0) {
                sign = -1;
                x = -x;
            } else
                sign = 0;


            if (x == 0.0) {
                results[0] = 0.0;
                results[1] = Double.NEGATIVE_INFINITY;
                return (0);
            }

            if (x >= 8.0)
                return chb(x, sign, results);

            if (x >= 88.0)
                return asymp(x, results, sign);

            z = x * x;

            /*     Direct power series expansion   */
            a = 1.0;
            s = 1.0;
            c = 0.0;
            k = 2.0;

            do {
                a *= z / k;
                c += a / k;
                k += 1.0;
                a /= k;
                s += a / k;
                k += 1.0;
            }
            while (Math.abs(a / s) > MACHEP);

            s *= x;
            return done(sign, s, results, x, c);


        }

        /* Chebyshev series expansions */
        private static int chb(double x, int sign, double[] results) {
            double k, s, c, a;
            if (x < 18.0) {
                a = (576.0 / x - 52.0) / 10.0;
                k = exp(x) / x;
                s = k * chbevl(a, S1, 22);
                c = k * chbevl(a, C1, 23);
                return done(sign, s, results, x, c);
            }

            if (x <= 88.0) {
                a = (6336.0 / x - 212.0) / 70.0;
                k = exp(x) / x;
                s = k * chbevl(a, S2, 23);
                c = k * chbevl(a, C2, 24);
                return done(sign, s, results, x, c);
            }

            return asymp(x, results, sign);
        }

        private static int asymp(double x, double[] results, int sign) {
            if (x > 1000) {
                results[0] = Double.POSITIVE_INFINITY;
                results[1] = Double.POSITIVE_INFINITY;
            } else {
                /* Asymptotic expansions
                 * http://functions.wolfram.com/GammaBetaErf/CoshIntegral/06/02/
                 * http://functions.wolfram.com/GammaBetaErf/SinhIntegral/06/02/0001/
                 */
                double a = hyp3f0(0.5, 1, 1, 4.0 / (x * x));
                double b = hyp3f0(1, 1, 1.5, 4.0 / (x * x));
                results[0] = cosh(x) / x * a + sinh(x) / (x * x) * b;
                results[1] = sinh(x) / x * a + cosh(x) / (x * x) * b;
            }
            if (sign != 0) {
                results[0] = -results[0];
            }
            return 0;
        }

        private static int done(int sign, double s, double[] results, double x, double c) {
            if (sign != 0)
                s = -s;

            results[0] = s;

            results[1] = Cephes.NPY_EULER + log(x) + c;
            return (0);
        }


        /*
         * Evaluate 3F0(a1, a2, a3; z)
         *
         * The series is only asymptotic, so this requires z large enough.
         */
        static double hyp3f0(double a1, double a2, double a3, double z) {
            int n, maxiter;
            double err, sum, term, m;

            m = pow(z, -1.0 / 3);
            if (m < 50) {
                maxiter = (int) m;
            } else {
                maxiter = 50;
            }

            term = 1.0;
            sum = term;
            for (n = 0; n < maxiter; ++n) {
                term *= (a1 + n) * (a2 + n) * (a3 + n) * z / (n + 1);
                sum += term;
                if (Math.abs(term) < 1e-13 * Math.abs(sum) || term == 0) {
                    break;
                }
            }

            err = Math.abs(term);

            if (err > 1e-13 * Math.abs(sum)) {
                return Double.NaN;
            }

            return sum;
        }

    }
    /*                                                     sici.c
     *
     *     Sine and cosine integrals
     *
     *
     *
     * SYNOPSIS:
     *
     * double x, Ci, Si, sici();
     *
     * sici( x, &Si, &Ci );
     *
     *
     * DESCRIPTION:
     *
     * Evaluates the integrals
     *
     *                          x
     *                          -
     *                         |  cos t - 1
     *   Ci(x) = eul + ln x +  |  --------- dt,
     *                         |      t
     *                        -
     *                         0
     *             x
     *             -
     *            |  sin t
     *   Si(x) =  |  ----- dt
     *            |    t
     *           -
     *            0
     *
     * where eul = 0.57721566490153286061 is Euler's constant.
     * The integrals are approximated by rational functions.
     * For x > 8 auxiliary functions f(x) and g(x) are employed
     * such that
     *
     * Ci(x) = f(x) sin(x) - g(x) cos(x)
     * Si(x) = pi/2 - f(x) cos(x) - g(x) sin(x)
     *
     *
     * ACCURACY:
     *    Test interval = [0,50].
     * Absolute error, except relative when > 1:
     * arithmetic   function   # trials      peak         rms
     *    IEEE        Si        30000       4.4e-16     7.3e-17
     *    IEEE        Ci        30000       6.9e-16     5.1e-17
     */

    /*
     * Cephes Math Library Release 2.1:  January, 1989
     * Copyright 1984, 1987, 1989 by Stephen L. Moshier
     * Direct inquiries to 30 Frost Street, Cambridge, MA 02140
     */
    static final class sici_c {
        private sici_c() {
        }

        static double SN[] = {
                -8.39167827910303881427E-11,
                4.62591714427012837309E-8,
                -9.75759303843632795789E-6,
                9.76945438170435310816E-4,
                -4.13470316229406538752E-2,
                1.00000000000000000302E0,
        };

        static double SD[] = {
                2.03269266195951942049E-12,
                1.27997891179943299903E-9,
                4.41827842801218905784E-7,
                9.96412122043875552487E-5,
                1.42085239326149893930E-2,
                9.99999999999999996984E-1,
        };

        static double CN[] = {
                2.02524002389102268789E-11,
                -1.35249504915790756375E-8,
                3.59325051419993077021E-6,
                -4.74007206873407909465E-4,
                2.89159652607555242092E-2,
                -1.00000000000000000080E0,
        };

        static double CD[] = {
                4.07746040061880559506E-12,
                3.06780997581887812692E-9,
                1.23210355685883423679E-6,
                3.17442024775032769882E-4,
                5.10028056236446052392E-2,
                4.00000000000000000080E0,
        };

        static double FN4[] = {
                4.23612862892216586994E0,
                5.45937717161812843388E0,
                1.62083287701538329132E0,
                1.67006611831323023771E-1,
                6.81020132472518137426E-3,
                1.08936580650328664411E-4,
                5.48900223421373614008E-7,
        };

        static double FD4[] = {
                /*  1.00000000000000000000E0, */
                8.16496634205391016773E0,
                7.30828822505564552187E0,
                1.86792257950184183883E0,
                1.78792052963149907262E-1,
                7.01710668322789753610E-3,
                1.10034357153915731354E-4,
                5.48900252756255700982E-7,
        };

        static double FN8[] = {
                4.55880873470465315206E-1,
                7.13715274100146711374E-1,
                1.60300158222319456320E-1,
                1.16064229408124407915E-2,
                3.49556442447859055605E-4,
                4.86215430826454749482E-6,
                3.20092790091004902806E-8,
                9.41779576128512936592E-11,
                9.70507110881952024631E-14,
        };

        static double FD8[] = {
                /*  1.00000000000000000000E0, */
                9.17463611873684053703E-1,
                1.78685545332074536321E-1,
                1.22253594771971293032E-2,
                3.58696481881851580297E-4,
                4.92435064317881464393E-6,
                3.21956939101046018377E-8,
                9.43720590350276732376E-11,
                9.70507110881952025725E-14,
        };

        static double GN4[] = {
                8.71001698973114191777E-2,
                6.11379109952219284151E-1,
                3.97180296392337498885E-1,
                7.48527737628469092119E-2,
                5.38868681462177273157E-3,
                1.61999794598934024525E-4,
                1.97963874140963632189E-6,
                7.82579040744090311069E-9,
        };

        static double GD4[] = {
                /*  1.00000000000000000000E0, */
                1.64402202413355338886E0,
                6.66296701268987968381E-1,
                9.88771761277688796203E-2,
                6.22396345441768420760E-3,
                1.73221081474177119497E-4,
                2.02659182086343991969E-6,
                7.82579218933534490868E-9,
        };

        static double GN8[] = {
                6.97359953443276214934E-1,
                3.30410979305632063225E-1,
                3.84878767649974295920E-2,
                1.71718239052347903558E-3,
                3.48941165502279436777E-5,
                3.47131167084116673800E-7,
                1.70404452782044526189E-9,
                3.85945925430276600453E-12,
                3.14040098946363334640E-15,
        };

        static double GD8[] = {
                /*  1.00000000000000000000E0, */
                1.68548898811011640017E0,
                4.87852258695304967486E-1,
                4.67913194259625806320E-2,
                1.90284426674399523638E-3,
                3.68475504442561108162E-5,
                3.57043223443740838771E-7,
                1.72693748966316146736E-9,
                3.87830166023954706752E-12,
                3.14040098946363335242E-15,
        };


        //results ={si,ci}
        static int sici(double x, double[] results) {
            double z = 0, c = 0, s = 0, f = 0, g = 0;
            short sign;

            if (x < 0.0) {
                sign = -1;
                x = -x;
            } else
                sign = 0;


            if (x == 0.0) {
                results[0] = 0.0;
                results[1] = Double.NEGATIVE_INFINITY;
                return (0);
            }


            if (x > 1.0e9) {
                if (Double.isInfinite(x)) {
                    if (sign == -1) {
                        results[0] = -Cephes.NPY_PI_2;
                        results[1] = Double.NaN;
                    } else {
                        results[0] = Cephes.NPY_PI_2;
                        results[1] = 0;
                    }
                    return 0;
                }
                results[0] = Cephes.NPY_PI_2 - cos(x) / x;
                results[1] = sin(x) / x;
            }


            if (x > 4.0)
                return asympt(x, results, sign);

            z = x * x;
            s = x * polevl(z, SN, 5) / polevl(z, SD, 5);
            c = z * polevl(z, CN, 5) / polevl(z, CD, 5);

            if (sign != 0)
                s = -s;
            results[0] = s;
            results[1] = Cephes.NPY_EULER + log(x) + c;    /* real part if x < 0 */
            return (0);



            /* The auxiliary functions are:
             *
             *
             * results[0] = results[0] - NPY_PI_2;
             * c = cos(x);
             * s = sin(x);
             *
             * t = results[1] * s - results[0] * c;
             * a = results[1] * c + results[0] * s;
             *
             * results[0] = t;
             * results[1] = -a;
             */


        }

        private static int asympt(double x, double[] results, short sign) {
            double s = sin(x);
            double c = cos(x);
            double z = 1.0 / (x * x);
            double f;
            double g;
            if (x < 8.0) {
                f = polevl(z, FN4, 6) / (x * p1evl(z, FD4, 7));
                g = z * polevl(z, GN4, 7) / p1evl(z, GD4, 7);
            } else {
                f = polevl(z, FN8, 8) / (x * p1evl(z, FD8, 8));
                g = z * polevl(z, GN8, 8) / p1evl(z, GD8, 9);
            }
            results[0] = Cephes.NPY_PI_2 - f * c - g * s;
            if (sign != 0)
                results[0] = -(results[0]);
            results[1] = f * s - g * c;

            return (0);
        }
    }
    /*                                                     stdtr.c
     *
     *     Student's t distribution
     *
     *
     *
     * SYNOPSIS:
     *
     * double t, stdtr();
     * short k;
     *
     * y = stdtr( k, t );
     *
     *
     * DESCRIPTION:
     *
     * Computes the integral from minus infinity to t of the Student
     * t distribution with integer k > 0 degrees of freedom:
     *
     *                                      t
     *                                      -
     *                                     | |
     *              -                      |         2   -(k+1)/2
     *             | ( (k+1)/2 )           |  (     x   )
     *       ----------------------        |  ( 1 + --- )        dx
     *                     -               |  (      k  )
     *       sqrt( k pi ) | ( k/2 )        |
     *                                   | |
     *                                    -
     *                                   -inf.
     *
     * Relation to incomplete beta integral:
     *
     *        1 - stdtr(k,t) = 0.5 * incbet( k/2, 1/2, z )
     * where
     *        z = k/(k + t**2).
     *
     * For t < -2, this is the method of computation.  For higher t,
     * a direct method is derived from integration by parts.
     * Since the function is symmetric about t=0, the area under the
     * right tail of the density is found by calling the function
     * with -t instead of t.
     *
     * ACCURACY:
     *
     * Tested at random 1 <= k <= 25.  The "domain" refers to t.
     *                      Relative error:
     * arithmetic   domain     # trials      peak         rms
     *    IEEE     -100,-2      50000       5.9e-15     1.4e-15
     *    IEEE     -2,100      500000       2.7e-15     4.9e-17
     */

    /*                                                     stdtri.c
     *
     *     Functional inverse of Student's t distribution
     *
     *
     *
     * SYNOPSIS:
     *
     * double p, t, stdtri();
     * int k;
     *
     * t = stdtri( k, p );
     *
     *
     * DESCRIPTION:
     *
     * Given probability p, finds the argument t such that stdtr(k,t)
     * is equal to p.
     *
     * ACCURACY:
     *
     * Tested at random 1 <= k <= 100.  The "domain" refers to p:
     *                      Relative error:
     * arithmetic   domain     # trials      peak         rms
     *    IEEE    .001,.999     25000       5.7e-15     8.0e-16
     *    IEEE    10^-6,.001    25000       2.0e-12     2.9e-14
     */


    /*
     * Cephes Math Library Release 2.3:  March, 1995
     * Copyright 1984, 1987, 1995 by Stephen L. Moshier
     */
    static final class stdrt_c {
        private stdrt_c() {
        }

        static double stdtr(int k, double t) throws DomainException {
            double x, rk, z, f, tz, p, xsqk;
            int j;

            if (k <= 0) {
                return DomainException.raiseException("stdtr", Double.NaN);

            }

            if (t == 0)
                return (0.5);

            if (t < -2.0) {
                rk = k;
                z = rk / (rk + t * t);
                p = 0.5 * Cephes.incbet(0.5 * rk, 0.5, z);
                return (p);
            }

            /*     compute integral from -t to + t */

            if (t < 0)
                x = -t;
            else
                x = t;

            rk = k;            /* degrees of freedom */
            z = 1.0 + (x * x) / rk;

            /* test if k is odd or even */
            if ((k & 1) != 0) {

                /*      computation for odd k   */

                xsqk = x / sqrt(rk);
                p = atan(xsqk);
                if (k > 1) {
                    f = 1.0;
                    tz = 1.0;
                    j = 3;
                    while ((j <= (k - 2)) && ((tz / f) > MACHEP)) {
                        tz *= (j - 1) / (z * j);
                        f += tz;
                        j += 2;
                    }
                    p += f * xsqk / z;
                }
                p *= 2.0 / Cephes.NPY_PI;
            } else {

                /*      computation for even k  */

                f = 1.0;
                tz = 1.0;
                j = 2;

                while ((j <= (k - 2)) && ((tz / f) > MACHEP)) {
                    tz *= (j - 1) / (z * j);
                    f += tz;
                    j += 2;
                }
                p = f * x / sqrt(z * rk);
            }

            /*     common exit     */


            if (t < 0)
                p = -p;            /* note destruction of relative accuracy */

            p = 0.5 + 0.5 * p;
            return (p);
        }

        static double stdtri(int k, double p) throws DomainException {
            double t, rk, z;
            int rflg;

            if (k <= 0 || p <= 0.0 || p >= 1.0) {
                return DomainException.raiseException("stdtri", Double.NaN);

            }

            rk = k;

            if (p > 0.25 && p < 0.75) {
                if (p == 0.5)
                    return (0.0);
                z = 1.0 - 2.0 * p;
                z = Cephes.incbi(0.5, 0.5 * rk, Math.abs(z));
                t = sqrt(rk * z / (1.0 - z));
                if (p < 0.5)
                    t = -t;
                return (t);
            }
            rflg = -1;
            if (p >= 0.5) {
                p = 1.0 - p;
                rflg = 1;
            }
            z = Cephes.incbi(0.5 * rk, 0.5, 2.0 * p);

            if (Double.MAX_VALUE * z < rk)
                return (rflg * Double.POSITIVE_INFINITY);
            t = sqrt(rk / z - rk);
            return (rflg * t);
        }

    }

    static final class yv_c {
        private yv_c() {
        }

        /*
         * Bessel function of noninteger order
         */
        static double yv(double v, double x) throws DomainException, OverflowException {
            double y, t;
            int n;

            n = (int) v;
            if (n == v) {
                y = yn(n, x);
                return (y);
            } else if (v == floor(v)) {
                /* Zero in denominator. */
                return DomainException.raiseException("yv", Double.NaN);
            }

            t = Cephes.NPY_PI * v;
            y = (cos(t) * jv(v, x) - jv(-v, x)) / sin(t);

            if (Double.isInfinite(y)) {
                if (v > 0) {
                    return OverflowException.raiseException("yv", Double.POSITIVE_INFINITY);
                } else if (v < -1e10) {
                    /* Whether it's +inf or -inf is numerically ill-defined. */
                    return DomainException.raiseException("yv", Double.NaN);
                }
            }

            return (y);
        }

    }
    /*                                                     jv.c
     *
     *     Bessel function of noninteger order
     *
     *
     *
     * SYNOPSIS:
     *
     * double v, x, y, jv();
     *
     * y = jv( v, x );
     *
     *
     *
     * DESCRIPTION:
     *
     * Returns Bessel function of order v of the argument,
     * where v is real.  Negative x is allowed if v is an integer.
     *
     * Several expansions are included: the ascending power
     * series, the Hankel expansion, and two transitional
     * expansions for large v.  If v is not too large, it
     * is reduced by recurrence to a region of best accuracy.
     * The transitional expansions give 12D accuracy for v > 500.
     *
     *
     *
     * ACCURACY:
     * Results for integer v are indicated by *, where x and v
     * both vary from -125 to +125.  Otherwise,
     * x ranges from 0 to 125, v ranges as indicated by "domain."
     * Error criterion is absolute, except relative when |jv()| > 1.
     *
     * arithmetic  v domain  x domain    # trials      peak       rms
     *    IEEE      0,125     0,125      100000      4.6e-15    2.2e-16
     *    IEEE   -125,0       0,125       40000      5.4e-11    3.7e-13
     *    IEEE      0,500     0,500       20000      4.4e-15    4.0e-16
     * Integer v:
     *    IEEE   -125,125   -125,125      50000      3.5e-15*   1.9e-16*
     *
     */


    /*
     * Cephes Math Library Release 2.8:  June, 2000
     * Copyright 1984, 1987, 1989, 1992, 2000 by Stephen L. Moshier
     */

    static final class jv_c {
        private jv_c() {

        }

        static final double MAXGAM = 171.624376956302725;

        private static final class JV {
            double k, q, t, y, an;
            int i, sign, nint;

            double n, newn;

            private JV() {

            }
            /* Reduce the order by backward recurrence.
             * AMS55 #9.1.27 and 9.1.73.
             */

            double recur(double n, double x, int cancel) {
                this.n = n;
                this.newn = n;

                /* double pkp1; */
                double kf = 0;
                int nflag;
                int miniter, maxiter;

                /* Continued fraction for Jn(x)/Jn-1(x)
                 * AMS 9.1.73
                 *
                 *    x       -x^2      -x^2
                 * ------  ---------  ---------   ...
                 * 2 n +   2(n+1) +   2(n+2) +
                 *
                 * Compute it with the simplest possible algorithm.
                 *
                 * This continued fraction starts to converge when (|n| + m) > |x|.
                 * Hence, at least |x|-|n| iterations are necessary before convergence is
                 * achieved. There is a hard limit set below, m <= 30000, which is chosen
                 * so that no branch in `jv` requires more iterations to converge.
                 * The exact maximum number is (500/3.6)^2 - 500 ~ 19000
                 */

                maxiter = 22000;
                miniter = (int) (Math.abs(x) - Math.abs(n));
                if (miniter < 1)
                    miniter = 1;

                if (n < 0.0)
                    nflag = 1;
                else
                    nflag = 0;

                return fstart(x, miniter, maxiter, BIG, nflag, cancel, kf);


            }

            private double fstart(double x, int miniter, int maxiter, double big, int nflag, int cancel, double kf) {

                double pkm2 = 0.0;
                double qkm2 = 1.0;
                double pkm1 = x;
                double qkm1 = n + n;
                double xk = -x * x;
                double yk = qkm1;
                double ans = 0.0;            /* ans=0.0 ensures that t=1.0 in the first iteration */
                int ctr = 0;
                double pk;
                double qk;
                double r;
                do {
                    yk += 2.0;
                    pk = pkm1 * yk + pkm2 * xk;
                    qk = qkm1 * yk + qkm2 * xk;
                    pkm2 = pkm1;
                    pkm1 = pk;
                    qkm2 = qkm1;
                    qkm1 = qk;

                    /* check convergence */
                    if (qk != 0 && ctr > miniter)
                        r = pk / qk;
                    else
                        r = 0.0;

                    if (r != 0) {
                        t = Math.abs((ans - r) / r);
                        ans = r;
                    } else {
                        t = 1.0;
                    }

                    if (++ctr > maxiter) {
                        UnderflowException.raiseException("jv");
                        return recur_done(ans, nflag, kf, x, cancel, miniter, maxiter, big);
                    }
                    if (t < MACHEP)
                        return recur_done(ans, nflag, kf, x, cancel, miniter, maxiter, big);

                    /* renormalize coefficients */
                    if (Math.abs(pk) > big) {
                        pkm2 /= big;
                        pkm1 /= big;
                        qkm2 /= big;
                        qkm1 /= big;
                    }
                }
                while (t > MACHEP);

                return recur_done(ans, nflag, kf, x, cancel, miniter, maxiter, big);
            }

            private double recur_done(double ans, int nflag, double kf, double x, int cancel, int miniter, int maxiter, double big) {
                if (ans == 0)
                    ans = 1.0;

                /* Change n to n-1 if n < 0 and the continued fraction is small */
                if (nflag > 0) {
                    if (Math.abs(ans) < 0.125) {
                        nflag = -1;
                        n = n - 1.0;
                        return fstart(x, miniter, maxiter, big, nflag, cancel, kf);
                    }
                }

                kf = newn;

                /* backward recurrence
                 *              2k
                 *  J   (x)  =  --- J (x)  -  J   (x)
                 *   k-1         x   k         k+1
                 */

                double pk = 1.0;
                double pkm1 = 1.0 / ans;
                k = n - 1.0;
                double r = 2 * k;
                double pkm2;
                do {
                    pkm2 = (pkm1 * r - pk * x) / x;
                    /*      pkp1 = pk; */
                    pk = pkm1;
                    pkm1 = pkm2;
                    r -= 2.0;
                    /*
                     * t = fabs(pkp1) + fabs(pk);
                     * if( (k > (kf + 2.5)) && (fabs(pkm1) < 0.25*t) )
                     * {
                     * k -= 1.0;
                     * t = x*x;
                     * pkm2 = ( (r*(r+2.0)-t)*pk - r*x*pkp1 )/t;
                     * pkp1 = pk;
                     * pk = pkm1;
                     * pkm1 = pkm2;
                     * r -= 2.0;
                     * }
                     */
                    k -= 1.0;
                }
                while (k > (kf + 0.5));

                /* Take the larger of the last two iterates
                 * on the theory that it may have less cancellation error.
                 */

                if (cancel != 0) {
                    if ((kf >= 0.0) && (Math.abs(pk) > Math.abs(pkm1))) {
                        k += 1.0;
                        pkm2 = pk;
                    }
                }
                newn = k;

                return (pkm2);
            }

            public double get(double n, double x) {

                nint = 0;            /* Flag for integer n */
                sign = 1;            /* Flag for sign inversion */
                an = Math.abs(n);
                y = floor(an);
                if (y == an) {
                    nint = 1;
                    i = (int) (an - 16384.0 * floor(an / 16384.0));
                    if (n < 0.0) {
                        if ((i & 1) != 0)
                            sign = -sign;
                        n = an;
                    }
                    if (x < 0.0) {
                        if ((i & 1) != 0)
                            sign = -sign;
                        x = -x;
                    }
                    if (n == 0.0)
                        return (j0(x));
                    if (n == 1.0)
                        return (sign * j1(x));
                }

                if ((x < 0.0) && (y != an)) {
                    DomainException.raiseException("jv");
                    y = Double.NaN;
                    return done();
                }

                if (x == 0 && n < 0 && nint == 0) {
                    OverflowException.raiseException("jv");
                    return Double.POSITIVE_INFINITY / Cephes.Gamma(n + 1);
                }

                y = Math.abs(x);

                if (y * y < Math.abs(n + 1) * MACHEP) {
                    return pow(0.5 * x, n) / Cephes.Gamma(n + 1);
                }

                k = 3.6 * sqrt(y);
                t = 3.6 * sqrt(an);
                if ((y < t) && (an > 21.0))
                    return (sign * jvs(n, x));
                if ((an < k) && (y > 21.0))
                    return (sign * hankel(n, x));

                if (an < 500.0) {
                    /* Note: if x is too large, the continued fraction will fail; but then the
                     * Hankel expansion can be used. */
                    if (nint != 0) {
                        k = 0.0;
                        q = recur(n, x, 1);
                        if (k == 0.0) {
                            y = j0(x) / q;
                            return done();
                        }
                        if (k == 1.0) {
                            y = j1(x) / q;
                            return done();
                        }
                    }

                    if (an > 2.0 * y)
                        return rlarger(n, x);

                    if ((n >= 0.0) && (n < 20.0)
                            && (y > 6.0) && (y < 20.0)) {
                        /* Recur backwards from a larger value of n */
                        return rlarger(n, x);

                    }

                    if (k <= 30.0) {
                        k = 2.0;
                    } else if (k < 90.0) {
                        k = (3 * k) / 4;
                    }
                    if (an > (k + 3.0)) {
                        if (n < 0.0)
                            k = -k;
                        q = n - floor(n);
                        k = floor(k) + q;
                        if (n > 0.0)
                            q = recur(n, x, 1);
                        else {
                            t = k;
                            k = n;
                            q = recur(t, x, 1);
                            k = t;
                        }
                        if (q == 0.0) {
                            y = 0.0;
                            return done();
                        }
                    } else {
                        k = n;
                        q = 1.0;
                    }

                    /* boundary between convergence of
                     * power series and Hankel expansion
                     */
                    y = Math.abs(k);
                    if (y < 26.0)
                        t = (0.0083 * y + 0.09) * y + 12.9;
                    else
                        t = 0.9 * y;

                    if (x > t)
                        y = hankel(k, x);
                    else
                        y = jvs(k, x);

                    if (n > 0.0)
                        y /= q;
                    else
                        y *= q;
                } else {
                    /* For large n, use the uniform expansion or the transitional expansion.
                     * But if x is of the order of n**2, these may blow up, whereas the
                     * Hankel expansion will then work.
                     */
                    if (n < 0.0) {
                        LossException.raiseException("jv");
                        y = Double.NaN;
                        return done();
                    }
                    t = x / n;
                    t /= n;
                    if (t > 0.3)
                        y = hankel(n, x);
                    else
                        y = jnx(n, x);
                }

                return done();
            }

            double done() {
                return (sign * y);
            }

            double rlarger(double n, double x) {
                k = n;

                y = y + an + 1.0;
                if (y < 30.0)
                    y = 30.0;
                y = n + floor(y - n);
                q = recur(y, x, 0);
                y = jvs(y, x) * q;
                return done();
            }
        }


        static final double BIG = 1.44115188075855872E+17;

        static double jv(double v, double x) throws DomainException, OverflowException, LossException, UnderflowException {
            return new JV().get(v, x);
        }

        /* Ascending power series for Jv(x).
         * AMS55 #9.1.10.
         */

        static double jvs(double n, double x) throws OverflowException {
            double t, u, y, z, k;
            final int[] ex = new int[1], sgngam = new int[1];

            z = -x * x / 4.0;
            u = 1.0;
            y = u;
            k = 1.0;
            t = 1.0;

            while (t > MACHEP) {
                u *= z / (k * (n + k));
                y += u;
                k += 1.0;
                if (y != 0)
                    t = Math.abs(u / y);
            }

            t = frexp(0.5 * x, ex);
            ex[0] = (int) (ex[0] * n);
            if ((ex[0] > -1023)
                    && (ex[0] < 1023)
                    && (n > 0.0)
                    && (n < (MAXGAM - 1.0))) {
                t = pow(0.5 * x, n) / Cephes.Gamma(n + 1.0);

                y *= t;
            } else {

                t = n * log(0.5 * x) - lgam_sgn(n + 1.0, sgngam);
                if (y < 0) {
                    sgngam[0] = -sgngam[0];
                    y = -y;
                }
                t += log(y);

                if (t < -Cephes.MAXLOG) {
                    return (0.0);
                }
                if (t > Cephes.MAXLOG) {
                    return (OverflowException.raiseException("jv", Double.POSITIVE_INFINITY));
                }
                y = sgngam[0] * exp(t);
            }
            return (y);
        }
        /* Hankel's asymptotic expansion
         * for large x.
         * AMS55 #9.2.5.
         */

        static double hankel(double n, double x) {
            double t, u, z, k, sign, conv;
            double p, q, j, m, pp, qq;
            int flag;

            m = 4.0 * n * n;
            j = 1.0;
            z = 8.0 * x;
            k = 1.0;
            p = 1.0;
            u = (m - 1.0) / z;
            q = u;
            sign = 1.0;
            conv = 1.0;
            flag = 0;
            t = 1.0;
            pp = 1.0e38;
            qq = 1.0e38;

            while (t > MACHEP) {
                k += 2.0;
                j += 1.0;
                sign = -sign;
                u *= (m - k * k) / (j * z);
                p += sign * u;
                k += 2.0;
                j += 1.0;
                u *= (m - k * k) / (j * z);
                q += sign * u;
                t = Math.abs(u / p);
                if (t < conv) {
                    conv = t;
                    qq = q;
                    pp = p;
                    flag = 1;
                }
                /* stop if the terms start getting larger */
                if ((flag != 0) && (t > conv)) {

                    return hank1(x, n, pp, qq);
                }
            }

            return hank1(x, n, pp, qq);

        }

        private static double hank1(double x, double n, double pp, double qq) {
            double u = x - (0.5 * n + 0.25) * Cephes.NPY_PI;
            return (sqrt(2.0 / (Cephes.NPY_PI * x)) * (pp * cos(u) - qq * sin(u)));
        }

        /* Asymptotic expansion for large n.
         * AMS55 #9.3.35.
         */

        static double lambda[] = {
                1.0,
                1.041666666666666666666667E-1,
                8.355034722222222222222222E-2,
                1.282265745563271604938272E-1,
                2.918490264641404642489712E-1,
                8.816272674437576524187671E-1,
                3.321408281862767544702647E+0,
                1.499576298686255465867237E+1,
                7.892301301158651813848139E+1,
                4.744515388682643231611949E+2,
                3.207490090890661934704328E+3
        };

        static double mu[] = {
                1.0,
                -1.458333333333333333333333E-1,
                -9.874131944444444444444444E-2,
                -1.433120539158950617283951E-1,
                -3.172272026784135480967078E-1,
                -9.424291479571202491373028E-1,
                -3.511203040826354261542798E+0,
                -1.572726362036804512982712E+1,
                -8.228143909718594444224656E+1,
                -4.923553705236705240352022E+2,
                -3.316218568547972508762102E+3
        };

        static double P1[] = {
                -2.083333333333333333333333E-1,
                1.250000000000000000000000E-1
        };

        static double P2[] = {
                3.342013888888888888888889E-1,
                -4.010416666666666666666667E-1,
                7.031250000000000000000000E-2
        };

        static double P3[] = {
                -1.025812596450617283950617E+0,
                1.846462673611111111111111E+0,
                -8.912109375000000000000000E-1,
                7.324218750000000000000000E-2
        };

        static double P4[] = {
                4.669584423426247427983539E+0,
                -1.120700261622299382716049E+1,
                8.789123535156250000000000E+0,
                -2.364086914062500000000000E+0,
                1.121520996093750000000000E-1
        };

        static double P5[] = {
                -2.8212072558200244877E1,
                8.4636217674600734632E1,
                -9.1818241543240017361E1,
                4.2534998745388454861E1,
                -7.3687943594796316964E0,
                2.27108001708984375E-1
        };

        static double P6[] = {
                2.1257013003921712286E2,
                -7.6525246814118164230E2,
                1.0599904525279998779E3,
                -6.9957962737613254123E2,
                2.1819051174421159048E2,
                -2.6491430486951555525E1,
                5.7250142097473144531E-1
        };

        static double P7[] = {
                -1.9194576623184069963E3,
                8.0617221817373093845E3,
                -1.3586550006434137439E4,
                1.1655393336864533248E4,
                -5.3056469786134031084E3,
                1.2009029132163524628E3,
                -1.0809091978839465550E2,
                1.7277275025844573975E0
        };


        static double jnx(double n, double x) {
            double zeta, sqz, zz, zp, np;
            double cbn, n23, t, z, sz;
            double pp, qq, z32i, zzi;
            double ak, bk, akl, bkl;
            int sign, doa, dob, nflg, k, s, tk, tkp1, m;
            final double[] u = new double[8];
            final double[] airy_results = new double[4];

            /* Test for x very close to n. Use expansion for transition region if so. */
            cbn = cbrt(n);
            z = (x - n) / cbn;
            if (Math.abs(z) <= 0.7)
                return (jnt(n, x));

            z = x / n;
            zz = 1.0 - z * z;
            if (zz == 0.0)
                return (0.0);

            if (zz > 0.0) {
                sz = sqrt(zz);
                t = 1.5 * (log((1.0 + sz) / z) - sz);    /* zeta ** 3/2          */
                zeta = cbrt(t * t);
                nflg = 1;
            } else {
                sz = sqrt(-zz);
                t = 1.5 * (sz - acos(1.0 / z));
                zeta = -cbrt(t * t);
                nflg = -1;
            }
            z32i = Math.abs(1.0 / t);
            sqz = cbrt(t);

            /* Airy function */
            n23 = cbrt(n * n);
            t = n23 * zeta;
            Cephes.airy(t, airy_results);

            /* polynomials in expansion */
            u[0] = 1.0;
            zzi = 1.0 / zz;
            u[1] = polevl(zzi, P1, 1) / sz;
            u[2] = polevl(zzi, P2, 2) / zz;
            u[3] = polevl(zzi, P3, 3) / (sz * zz);
            pp = zz * zz;
            u[4] = polevl(zzi, P4, 4) / pp;
            u[5] = polevl(zzi, P5, 5) / (pp * sz);
            pp *= zz;
            u[6] = polevl(zzi, P6, 6) / pp;
            u[7] = polevl(zzi, P7, 7) / (pp * sz);

            pp = 0.0;
            qq = 0.0;
            np = 1.0;
            /* flags to stop when terms get larger */
            doa = 1;
            dob = 1;
            akl = Double.POSITIVE_INFINITY;
            bkl = Double.POSITIVE_INFINITY;

            for (k = 0; k <= 3; k++) {
                tk = 2 * k;
                tkp1 = tk + 1;
                zp = 1.0;
                ak = 0.0;
                bk = 0.0;
                for (s = 0; s <= tk; s++) {
                    if (doa != 0) {
                        if ((s & 3) > 1)
                            sign = nflg;
                        else
                            sign = 1;
                        ak += sign * mu[s] * zp * u[tk - s];
                    }

                    if (dob != 0) {
                        m = tkp1 - s;
                        if (((m + 1) & 3) > 1)
                            sign = nflg;
                        else
                            sign = 1;
                        bk += sign * lambda[s] * zp * u[m];
                    }
                    zp *= z32i;
                }

                if (doa != 0) {
                    ak *= np;
                    t = Math.abs(ak);
                    if (t < akl) {
                        akl = t;
                        pp += ak;
                    } else
                        doa = 0;
                }

                if (dob != 0) {
                    bk += lambda[tkp1] * zp * u[0];
                    bk *= -np / sqz;
                    t = Math.abs(bk);
                    if (t < bkl) {
                        bkl = t;
                        qq += bk;
                    } else
                        dob = 0;
                }

                if (np < MACHEP)
                    break;
                np /= n * n;
            }

            /* normalizing factor ( 4*zeta/(1 - z**2) )**1/4    */
            t = 4.0 * zeta / zz;
            t = sqrt(sqrt(t));

            t *= airy_results[0] * pp / cbrt(n) + airy_results[1] * qq / (n23 * n);
            return (t);
        }
        /* Asymptotic expansion for transition region,
         * n large and x close to n.
         * AMS55 #9.3.23.
         */

        static double[] PF2 = {
                -9.0000000000000000000e-2,
                8.5714285714285714286e-2
        };

        static double[] PF3 = {
                1.3671428571428571429e-1,
                -5.4920634920634920635e-2,
                -4.4444444444444444444e-3
        };

        static double[] PF4 = {
                1.3500000000000000000e-3,
                -1.6036054421768707483e-1,
                4.2590187590187590188e-2,
                2.7330447330447330447e-3
        };

        static double[] PG1 = {
                -2.4285714285714285714e-1,
                1.4285714285714285714e-2
        };

        static double[] PG2 = {
                -9.0000000000000000000e-3,
                1.9396825396825396825e-1,
                -1.1746031746031746032e-2
        };

        static double[] PG3 = {
                1.9607142857142857143e-2,
                -1.5983694083694083694e-1,
                6.3838383838383838384e-3
        };


        static double jnt(double n, double x) {
            double z, zz, z3;
            double cbn, n23, cbtwo;
            double[] airy_results = new double[4];    /* Airy functions */
            double nk, fk, gk, pp, qq;
            double[] F = new double[5], G = new double[4];
            int k;

            cbn = cbrt(n);
            z = (x - n) / cbn;
            cbtwo = cbrt(2.0);

            /* Airy function */
            zz = -cbtwo * z;
            Cephes.airy(zz, airy_results);

            /* polynomials in expansion */
            zz = z * z;
            z3 = zz * z;
            F[0] = 1.0;
            F[1] = -z / 5.0;
            F[2] = polevl(z3, PF2, 1) * zz;
            F[3] = polevl(z3, PF3, 2);
            F[4] = polevl(z3, PF4, 3) * z;
            G[0] = 0.3 * zz;
            G[1] = polevl(z3, PG1, 1);
            G[2] = polevl(z3, PG2, 2) * z;
            G[3] = polevl(z3, PG3, 2) * zz;

            pp = 0.0;
            qq = 0.0;
            nk = 1.0;
            n23 = cbrt(n * n);

            for (k = 0; k <= 4; k++) {
                fk = F[k] * nk;
                pp += fk;
                if (k != 4) {
                    gk = G[k] * nk;
                    qq += gk;
                }

                nk /= n23;
            }

            fk = cbtwo * airy_results[0] * pp / cbn + cbrt(4.0) * airy_results[1] * qq / n;
            return (fk);
        }

    }

}
