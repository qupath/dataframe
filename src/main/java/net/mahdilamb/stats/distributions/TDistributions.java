package net.mahdilamb.stats.distributions;

import net.mahdilamb.stats.libs.Cephes;

import static net.mahdilamb.stats.MathUtils.gammaLN;


/**
 * Utility class for working with Student's t-distribution
 */
public final class TDistributions {
    private TDistributions() {

    }

    /**
     * Calculate the probability density function of the value at x in a t-distribution of the specified degrees of freedom
     *
     * @param x                the x value
     * @param degreesOfFreedom the degrees of freedom
     * @return the PDF at x in a t-distribution of the given degrees of freedom
     */
    public static double PDF(double x, int degreesOfFreedom) {
        final double r = degreesOfFreedom;
        return Math.exp(gammaLN((r + 1) / 2) - (gammaLN(r / 2))) / (Math.sqrt(Math.PI * r) * Math.pow((1 + x * x / r), ((r + 1) / 2)));
    }

    /**
     * Calculate the log of the probability distribution function
     *
     * @param x                the position to evaluate at
     * @param degreesOfFreedom the degrees of freedom
     * @return the log PDF at x in a t-distribution of the given degrees of freedom
     */
    public static double logPDF(double x, int degreesOfFreedom) {
        final double r = degreesOfFreedom;
        double lPx = gammaLN((r + 1) / 2) - gammaLN(r / 2);
        lPx -= 0.5 * Math.log(r * Math.PI) + (r + 1) / 2 * Math.log(1 + (x * x) / r);
        return lPx;
    }

    /**
     * @param x  the position to evaluate at
     * @param df the degrees of freedom
     * @return the cumulative distribution function at x
     */
    public static double CDF(double x, int df) {
        return Cephes.stdtr(df, x);
    }

    /**
     * @param x  the position along at the distribution
     * @param df the degrees of freedom
     * @return the survival function at x
     */
    public static double SF(double x, int df) {
        return Cephes.stdtr(df, -x);
    }

    /**
     * @param q  the probability
     * @param df the degrees of freedom
     * @return the percentile point function in the t distribution
     */
    public static double PPF(double q, int df) {
        return Cephes.stdtri(df, q);
    }

    /**
     * Calculate the inverse survival function
     *
     * @param q  the probability
     * @param df the degrees of freedom
     * @return the inverse survival function of the t-distribution at q
     */
    public static double inverseSF(double q, int df) {
        return -Cephes.stdtri(df, q);
    }

    /**
     * Return the first four moments of a t-distribution given the degrees of freedom
     *
     * @param df the degrees of freedom
     * @return the first four moments of the t-distribution
     */
    public static SummaryStatistics stats(int df) {
        double mu = df > 1 ? 0 : Double.POSITIVE_INFINITY;
        double mu2 = df <= 1 ? Double.NaN : (df > 2 ? df / (df - 2.0) : Double.POSITIVE_INFINITY);
        double g1 = df > 3 ? 0 : Double.NaN;
        return new SummaryStatistics(
                mu,
                mu2,
                g1,
                df <= 2 ? Double.NaN : df > 4 ? (6. / (df - 4.)) : Double.POSITIVE_INFINITY
        );
    }



}
