package net.mahdilamb.stats.libs;

import net.mahdilamb.stats.libs.exceptions.*;
/*
 * Cephes Math Library Release 2.0:  April, 1987
 * Copyright 1985, 1987 by Stephen L. Moshier
 * Direct inquiries to 30 Frost Street, Cambridge, MA 02140
 */

/**
 * Methods from the Cephes maths package
 */
public final class Cephes {
    private Cephes() {
    }

    public static final double NPY_PI = 3.141592653589793238462643383279502884;
    public static final double NPY_PI_2 = 1.570796326794896619231321691639751442;
    public static final double NPY_PI_4 = 0.785398163397448309615660845819875721;
    public static final double NPY_E = 2.718281828459045235360287471352662498;
    public static final double NPY_1_PI = 0.318309886183790671537767526745028724;
    public static final double NPY_2_PI = 0.636619772367581343075535053490057448;
    public static final double NPY_SQRT2 = 1.414213562373095048801688724209698079;
    public static final double NPY_SQRT1_2 = 0.707106781186547524400844362104849039;
    public static final double NPY_EULER = 0.577215664901532860606512090082402431;
    /**
     * Constant for the maximum log
     */
    static final double MAXLOG = Math.log(Double.MAX_VALUE);
    static final double MINLOG = Math.log(Double.MIN_VALUE);


    public static final double NPY_LOG2E = 1.442695040888963407359924681001892137;  /* log_2 e */
    public static final double NPY_LOG10E = 0.434294481903251827651128918916605082;  /* log_10 e */
    public static final double NPY_LOGE2 = 0.693147180559945309417232121458176568;  /* log_e 2 */
    public static final double NPY_LOGE10 = 2.302585092994045684017991454684364208;  /* log_e 10 */

    static final double SQ2OPI = 7.9788456080286535587989E-1;    /* sqrt( 2/pi ) */
    static final double LOGSQ2 = 3.46573590279972654709E-1;    /* log(2)/2 */
    static final double THPIO4 = 2.35619449019234492885;    /* 3*pi/4 */

    /**
     * @param aa aa
     * @param bb bb
     * @param xx xx
     * @return the incomplete beta integral
     * @throws ArithmeticException if aa orr bb are less than 0, or if xx is less than 0 or greater than 1
     */
    public static double incbet(double aa, double bb, double xx) throws ArithmeticException {
        return CephesImpl.incbet_c.incbet(aa, bb, xx);
    }

    /**
     * @param a a
     * @param b b
     * @param x x
     * @return contiunued fraction expansion #1 for incomplete beta integral
     */
    public static double incbcf(double a, double b, double x) {
        return CephesImpl.incbet_c.incbcf(a, b, x);

    }

    /**
     * @param a a
     * @param b b
     * @param x x
     * @return Continued fraction expansion #2 for incomplete beta integral
     */
    public static double incbd(double a, double b, double x) {
        return CephesImpl.incbet_c.incbd(a, b, x);
    }

    /**
     * @param a a
     * @param b b
     * @param x x
     * @return Power series for incomplete beta integral
     */
    public static double pseries(double a, double b, double x) {
        return CephesImpl.incbet_c.pseries(a, b, x);
    }

    /**
     * @param aa  aa
     * @param bb  bb
     * @param yy0 yy0
     * @return the inverse of incomplete beta integral
     */
    public static double incbi(double aa, double bb, double yy0) {
        return CephesImpl.incbi_c.incbi(aa, bb, yy0);
    }

    /**
     * @param a a
     * @param x x
     * @return the complemented incomplete Gamma integral
     * @throws DomainException if either of the arguments are below 0
     */
    public static double igam(double a, double x) throws DomainException {
        return CephesImpl.igam_c.igam(a, x);
    }

    /**
     * @param a a
     * @param x x
     * @return the incomplete Gamma integral
     * @throws DomainException if either of the arguments are below 0
     */
    public static double igamc(double a, double x) throws DomainException {
        return CephesImpl.igam_c.igamc(a, x);
    }

    public static double igam_fac(double a, double x) throws UnderflowException {
        return CephesImpl.igam_c.igam_fac(a, x);
    }

    public static double igamc_continued_fraction(double a, double x) {
        return CephesImpl.igam_c.igamc_continued_fraction(a, x);
    }

    public static double igam_series(double a, double x) {
        return CephesImpl.igam_c.igam_series(a, x);
    }

    public static double igamc_series(double a, double x) {
        return CephesImpl.igam_c.igamc_series(a, x);
    }

    public static double asymptotic_series(double a, double x, int func) {
        return CephesImpl.igam_c.asymptotic_series(a, x, func);
    }

    public static double igamci(double a, double q) throws DomainException {
        return CephesImpl.igami_c.igamci(a, q);
    }

    public static double igami(double a, double p) throws DomainException {
        return CephesImpl.igami_c.igami(a, p);
    }

    public static double find_inverse_gamma(double a, double p, double q) {
        return CephesImpl.igami_c.find_inverse_gamma(a, p, q);
    }

    public static double didonato_SN(double a, double x, int N, double tolerance) {
        return CephesImpl.igami_c.didonato_SN(a, x, N, tolerance);
    }

    public static double find_inverse_s(double p, double q) {
        return CephesImpl.igami_c.find_inverse_s(p, q);
    }

    public static double erf(double x) throws DomainException {
        return CephesImpl.ndtr_c.erf(x);
    }

    public static double erfc(double a) throws DomainException, UnderflowException {
        return CephesImpl.ndtr_c.erfc(a);
    }

    public static double ndtr(double a) throws DomainException {
        return CephesImpl.ndtr_c.ndtr(a);
    }

    public static double ndtri(double y0) throws DomainException {
        return CephesImpl.ndtri_c.ndtri(y0);
    }

    public static double zeta(double x, double q) throws DomainException, SingularException {
        return CephesImpl.zeta_c.zeta(x, q);
    }

    public static double zetac(double x) {
        return CephesImpl.zetac_c.zetac(x);
    }

    public static double riemann_zeta(double x) {
        return CephesImpl.zetac_c.riemann_zeta(x);
    }

    public static double chbevl(double x, double[] array, int n) {
        return CephesImpl.chbevl_c.chbevl(x, array, n);
    }

    public static double stirf(double x) {
        return CephesImpl.gamma_c.stirf(x);
    }

    public static double Gamma(double x) throws OverflowException {
        return CephesImpl.gamma_c.Gamma(x);
    }

    public static double lgam(double x) {
        return CephesImpl.gamma_c.lgam(x);
    }

    public static double lgam_sgn(double x, int[] sign) throws SingularException {
        return CephesImpl.gamma_c.lgam_sgn(x, sign);
    }

    public static double[] airy(double x) {
        double[] out = new double[4];
        airy(x, out);
        return out;

    }

    public static int airy(double x, double[] results) {
        return CephesImpl.airy_c.airy(x, results);
    }

    public static double bdtrc(double k, int n, double p) throws DomainException {
        return CephesImpl.bdtr_c.bdtrc(k, n, p);
    }

    public static double bdtr(double k, int n, double p) throws DomainException {
        return CephesImpl.bdtr_c.bdtr(k, n, p);
    }

    public static double bdtri(double k, int n, double y) throws DomainException {
        return CephesImpl.bdtr_c.bdtri(k, n, y);
    }

    public static double besselpoly(double a, double lambda, double nu) {
        return CephesImpl.besselpoly_c.besselpoly(a, lambda, nu);
    }

    public static double beta(double a, double b) throws OverflowException {
        return CephesImpl.beta_c.beta(a, b);
    }

    public static double betaln(double a, double b) throws OverflowException {
        return Math.log(Math.abs(beta(a, b)));
    }

    public static double lbeta(double a, double b) throws OverflowException {
        return CephesImpl.beta_c.lbeta(a, b);
    }

    public static double btdtr(double a, double b, double x) {
        return CephesImpl.btdtr_c.btdtr(a, b, x);
    }

    public static double chdtrc(double df, double x) {
        return CephesImpl.chdtr_c.chdtrc(df, x);
    }

    public static double chdtri(double df, double y) throws DomainException {
        return CephesImpl.chdtr_c.chdtri(df, y);
    }

    public static double chdtr(double df, double x) throws DomainException {
        return CephesImpl.chdtr_c.chdtr(df, x);
    }

    public static double dawsn(double xx) {
        return CephesImpl.dawsn_c.dawsn(xx);
    }

    public static double ellie(double phi, double m) {
        return CephesImpl.ellie_c.ellie(phi, m);
    }

    public static double ellik(double phi, double m) throws SingularException {
        return CephesImpl.ellik_c.ellik(phi, m);
    }

    public static double ellpe(double x) throws DomainException {
        return CephesImpl.ellpe_c.ellpe(x);
    }

    public static double[] ellpj(double u, double m) throws DomainException {
        return CephesImpl.ellpj_c.ellpj(u, m);
    }

    public static double ellpk(double x) throws DomainException, SingularException {
        return CephesImpl.ellpk_c.ellpk(x);
    }

    public static double ellipk(double x) throws DomainException, SingularException {
        return CephesImpl.ellpk_c.ellpk(1 - x);
    }

    public static double erfinv(double y) throws DomainException {
        return CephesImpl.erfinv_c.erfinv(y);
    }

    public static double erfcinv(double y) throws DomainException {
        return CephesImpl.erfinv_c.erfcinv(y);
    }

    public static double exp2(double x) {
        return CephesImpl.exp2_c.exp2(x);
    }

    public static double exp10(double x) {
        return CephesImpl.exp10_c.exp10(x);
    }

    public static double fdtrc(double a, double b, double x) throws DomainException {
        return CephesImpl.fdtr_c.fdtrc(a, b, x);
    }

    public static double fdtr(double a, double b, double x) throws DomainException {
        return CephesImpl.fdtr_c.fdtr(a, b, x);
    }

    public static double fdtri(double a, double b, double y) throws DomainException {
        return CephesImpl.fdtr_c.fdtri(a, b, y);
    }

    public static double[] fresnl(double xxa) {
        return CephesImpl.fresnl_c.fresnl(xxa);
    }

    public static double gdtr(double a, double b, double x) throws DomainException {
        return CephesImpl.gdtr_c.gdtr(a, b, x);
    }

    public static double gdtrc(double a, double b, double x) throws DomainException {
        return CephesImpl.gdtr_c.gdtrc(a, b, x);
    }

    public static double gdtri(double a, double b, double y) throws DomainException {
        return CephesImpl.gdtr_c.gdtri(a, b, y);
    }

    public static double nbdtrc(int k, int n, double p) throws DomainException {
        return CephesImpl.nbdtr_c.nbdtrc(k, n, p);
    }

    public static double nbdtr(int k, int n, double p) throws DomainException {
        return CephesImpl.nbdtr_c.nbdtr(k, n, p);
    }

    public static double nbdtri(int k, int n, double p) throws DomainException {
        return CephesImpl.nbdtr_c.nbdtri(k, n, p);

    }

    public static int shichi(double x, double[] results) {
        return CephesImpl.shichi_c.shichi(x, results);
    }

    public static double[] shichi(double x) {
        double[] results = new double[2];
        CephesImpl.shichi_c.shichi(x, results);
        return results;
    }

    public static double hyp3f0(double a1, double a2, double a3, double z) {
        return CephesImpl.shichi_c.hyp3f0(a1, a2, a3, z);
    }

    public static int sici(double x, double[] results) {
        return CephesImpl.sici_c.sici(x, results);
    }

    public static double[] sici(double x) {
        double[] results = new double[2];
        CephesImpl.sici_c.sici(x, results);
        return results;
    }

    public static double stdtri(int k, double p) throws DomainException {
        return CephesImpl.stdrt_c.stdtri(k, p);
    }

    public static double stdtr(int k, double t) throws DomainException {
        return CephesImpl.stdrt_c.stdtr(k, t);
    }

    public static double yv(double v, double x) throws DomainException, OverflowException {
        return CephesImpl.yv_c.yv(v, x);
    }

    public static double jnt(double n, double x) {
        return CephesImpl.jv_c.jnt(n, x);
    }

    public static double jnx(double n, double x) {
        return CephesImpl.jv_c.jnx(n, x);
    }

    public static double hankel(double n, double x) {
        return CephesImpl.jv_c.hankel(n, x);
    }

    public static double jvs(double n, double x) throws OverflowException {
        return CephesImpl.jv_c.jvs(n, x);
    }

    public static double jv(double v, double x) throws DomainException, OverflowException, LossException, UnderflowException {
        return CephesImpl.jv_c.jv(v, x);
    }


}
