package net.mahdilamb.statistics.libs;

public final class Boost {
    private Boost() {
    }

    /**
     * Lancos G constant
     */
    public static final double lanczos_g = 6.024680040776729583740234375;

    public static double lanczos_sum(double x) {
        return lanczos_c.lanczos_sum(x);
    }

    public static double lanczos_sum_expg_scaled(double x) {
        return lanczos_c.lanczos_sum_expg_scaled(x);
    }

    public static double lanczos_sum_near_1(double dx) {
        return lanczos_c.lanczos_sum_near_1(dx);
    }

    public static double lanczos_sum_near_2(double dx) {
        return lanczos_c.lanczos_sum_near_2(dx);
    }

    public static double ratevl(double x, double[] num, double[] denom) {
        return rational_hpp.ratevl(x, num, denom);
    }

    /*
     * Lanczos Coefficients for N=13 G=6.024680040776729583740234375
     * Max experimental error (with arbitrary precision arithmetic) 1.196214e-17
     * Generated with compiler: Microsoft Visual C++ version 8.0 on Win32 at Mar 23 2006
     *
     * Use for double precision.
     */
   private  static final class lanczos_c {
        private lanczos_c() {

        }


        /**
         * Calculate the Lanczos sum at x
         *
         * @param x the value
         * @return the Lanczos sum at x
         */
        public static double lanczos_sum(double x) {
            final double[] lanczos_num = {
                    2.506628274631000270164908177133837338626,
                    210.8242777515793458725097339207133627117,
                    8071.672002365816210638002902272250613822,
                    186056.2653952234950402949897160456992822,
                    2876370.628935372441225409051620849613599,
                    31426415.58540019438061423162831820536287,
                    248874557.8620541565114603864132294232163,
                    1439720407.311721673663223072794912393972,
                    6039542586.35202800506429164430729792107,
                    17921034426.03720969991975575445893111267,
                    35711959237.35566804944018545154716670596,
                    42919803642.64909876895789904700198885093,
                    23531376880.41075968857200767445163675473
            };
            final double[] lanczos_denom = {
                    1,
                    66,
                    1925,
                    32670,
                    357423,
                    2637558,
                    13339535,
                    45995730,
                    105258076,
                    150917976,
                    120543840,
                    39916800,
                    0
            };
            return rational_hpp.ratevl(x, lanczos_num, lanczos_denom);
        }

        /**
         * Calculate the Lanczos sum expg scaled at x
         *
         * @param x the value
         * @return the Lanczos sum expg scaled at x
         */
        public static double lanczos_sum_expg_scaled(double x) {
            final double[] lanczos_sum_expg_scaled_denom = {
                    1,
                    66,
                    1925,
                    32670,
                    357423,
                    2637558,
                    13339535,
                    45995730,
                    105258076,
                    150917976,
                    120543840,
                    39916800,
                    0
            };
            final double[] lanczos_sum_expg_scaled_num = {
                    0.006061842346248906525783753964555936883222,
                    0.5098416655656676188125178644804694509993,
                    19.51992788247617482847860966235652136208,
                    449.9445569063168119446858607650988409623,
                    6955.999602515376140356310115515198987526,
                    75999.29304014542649875303443598909137092,
                    601859.6171681098786670226533699352302507,
                    3481712.15498064590882071018964774556468,
                    14605578.08768506808414169982791359218571,
                    43338889.32467613834773723740590533316085,
                    86363131.28813859145546927288977868422342,
                    103794043.1163445451906271053616070238554,
                    56906521.91347156388090791033559122686859
            };
            return rational_hpp.ratevl(x, lanczos_sum_expg_scaled_num, lanczos_sum_expg_scaled_denom);
        }

        /**
         * Compute the Lanczos sum near 1
         *
         * @param dx value to compute
         * @return Lanczos sum near 1
         */
        public static double lanczos_sum_near_1(double dx) {
            double result = 0;
            final double[] lanczos_sum_near_1_d = {
                    0.3394643171893132535170101292240837927725e-9,
                    -0.2499505151487868335680273909354071938387e-8,
                    0.8690926181038057039526127422002498960172e-8,
                    -0.1933117898880828348692541394841204288047e-7,
                    0.3075580174791348492737947340039992829546e-7,
                    -0.2752907702903126466004207345038327818713e-7,
                    -0.1515973019871092388943437623825208095123e-5,
                    0.004785200610085071473880915854204301886437,
                    -0.1993758927614728757314233026257810172008,
                    1.483082862367253753040442933770164111678,
                    -3.327150580651624233553677113928873034916,
                    2.208709979316623790862569924861841433016
            };
            for (int k = 1; k <= lanczos_sum_near_1_d.length; ++k) {
                result += (-lanczos_sum_near_1_d[k - 1] * dx) / (k * dx + k * k);
            }
            return result;
        }

        /**
         * @param dx value to compute
         * @return compute Lanczos sum near 2
         */
        public static double lanczos_sum_near_2(double dx) {
            double result = 0;
            final double x = dx + 2;
            final double[] lanczos_sum_near_2_d = {
                    0.1009141566987569892221439918230042368112e-8,
                    -0.7430396708998719707642735577238449585822e-8,
                    0.2583592566524439230844378948704262291927e-7,
                    -0.5746670642147041587497159649318454348117e-7,
                    0.9142922068165324132060550591210267992072e-7,
                    -0.8183698410724358930823737982119474130069e-7,
                    -0.4506604409707170077136555010018549819192e-5,
                    0.01422519127192419234315002746252160965831,
                    -0.5926941084905061794445733628891024027949,
                    4.408830289125943377923077727900630927902,
                    -9.8907772644920670589288081640128194231,
                    6.565936202082889535528455955485877361223
            };
            for (int k = 1; k <= lanczos_sum_near_2_d.length; ++k) {
                result += (-lanczos_sum_near_2_d[k - 1] * dx) / (x + k * x + k * k - 1);
            }
            return result;
        }
    }

    /*
        Licence for including SciPy code

        Copyright © 2001, 2002 Enthought, Inc.
        All rights reserved.

        Copyright © 2003-2019 SciPy Developers.
        All rights reserved.
         */
    static final class rational_hpp {
        private rational_hpp() {

        }

        /* Sources:
         * [1] Holin et. al., "Polynomial and Rational Function Evaluation",
         *     https://www.boost.org/doc/libs/1_61_0/libs/math/doc/html/math_toolkit/roots/rational.html
         */

        /**
         * Evaluate a rational function
         *
         * @param x     the value to evaluate the function on
         * @param num   the numerators
         * @param denom the denominators
         * @return the result of evaluating the rational function on x
         */
        public static double ratevl(double x, double[] num,
                                    double[] denom) {
            final int M = num.length - 1;
            final int N = denom.length - 1;
            int i, dir, q;
            double y, num_ans, denom_ans;
            final double absx = Math.abs(x);

            if (absx > 1) {
                /* Evaluate as a polynomial in 1/x. */
                dir = -1;
                q = M;
                y = 1 / x;
            } else {
                dir = 1;
                q = 0;
                y = x;
            }

            /* Evaluate the numerator */
            num_ans = num[q];
            q += dir;
            for (i = 1; i <= M; ++i) {
                num_ans = num_ans * y + num[q];
                q += dir;
            }

            /* Evaluate the denominator */
            q = absx > 1 ? N : 0;

            denom_ans = denom[q];
            q += dir;
            for (i = 1; i <= N; ++i) {
                denom_ans = denom_ans * y + denom[q];
                q += dir;
            }

            if (absx > 1) {
                return Math.pow(x, N - M) * num_ans / denom_ans;
            } else {
                return num_ans / denom_ans;
            }
        }

    }
}