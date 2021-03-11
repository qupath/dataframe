package net.mahdilamb.stats;


import net.mahdilamb.stats.distributions.NormalDistributions;
import net.mahdilamb.stats.libs.Boost;
import net.mahdilamb.stats.libs.Cephes;
import net.mahdilamb.stats.libs.Libs;
import org.junit.Test;

import java.util.Arrays;

import static net.mahdilamb.stats.libs.Cephes.chbevl;
import static net.mahdilamb.stats.libs.Cephes.ndtri;
import static org.junit.Assert.assertEquals;

public final class JUnitTests {
    private static final double precision = 0.00000001;

    @Test
    public void medianCalculatedCorrectlyForOddNTest() {
        assertEquals(30, StatUtils.median(10, 20, 30, 40, 50), 0.0);
        assertEquals(30, StatUtils.quantile(0.5, 10, 20, 30, 40, 50), 0.0);
    }

    @Test
    public void medianCalculatedCorrectlyForEvenNTest() {
        assertEquals(25, StatUtils.median(20, 40, 10, 30), 0.0);
    }

    @Test
    public void binWidthTest() {
        final double[] x = new double[]{10, 20, 30, 40, 50, 2, 12, 4, 76798, 12, 2};
        assertEquals(23154.865218193936, StatUtils.sqrtBinWidthEstimator(x), precision);
        assertEquals(17221.028724612923, StatUtils.sturgesBinWidthEstimator(x), precision);
        assertEquals(17265.442331442147, StatUtils.riceBinWidthEstimator(x), precision);
        assertEquals(34645.93000117464, StatUtils.scottBinWidthEstimator(x), precision);
        assertEquals(25.180081529266115, StatUtils.freedmanDiaconisBinWidthEstimator(x), precision);
        assertEquals(767.96, StatUtils.stoneBinWidthEstimator(x), precision);
        assertEquals(10894.309786128277, StatUtils.doaneBinWidthEstimator(x), precision);

    }

    @Test
    public void filterNaNTest() {
        assertEquals(7, StatUtils.NaNFilter(10, 20, 30, 40, 50, 60, 70, Double.NaN).length);
    }

    @Test
    public void quartilesCalculatedCorrectlyTest() {
        assertEquals(93.5, StatUtils.quartileThree(73, 75, 80, 84, 90, 92, 93, 94, 96), 0);
        assertEquals(77, StatUtils.quartileThree(62, 63, 64, 64, 70, 72, 76, 77, 81, 81), 0);
        assertEquals(77.5, StatUtils.quartileOne(73, 75, 80, 84, 90, 92, 93, 94, 96), 0);
        assertEquals(64, StatUtils.quartileOne(62, 63, 64, 64, 70, 72, 76, 77, 81, 81), 0);
    }

    @Test
    public void skewTest() {
        assertEquals(0.0, StatUtils.skewness(1, 2, 3, 4, 5),0);
    }

    /**
     * Compare values to scipy
     */
    @Test
    public void NormalDistributionTest() {

        assertEquals(0.0044318484119380075, NormalDistributions.calculatePDF(-3), precision);
        assertEquals(0.05399096651318806, NormalDistributions.calculatePDF(-2), precision);
        assertEquals(0.24197072451914337, NormalDistributions.calculatePDF(-1), precision);
        assertEquals(0.3989422804014327, NormalDistributions.calculatePDF(0), precision);

        assertEquals(-5.418938533204672, NormalDistributions.calculateLogPDF(-3), precision);
        assertEquals(-2.9189385332046727, NormalDistributions.calculateLogPDF(-2), precision);
        assertEquals(-1.4189385332046727, NormalDistributions.calculateLogPDF(-1), precision);
        assertEquals(-0.9189385332046727, NormalDistributions.calculateLogPDF(0), precision);

        assertEquals(0.0013498980316300957, NormalDistributions.calculateCDF(-3), precision);
        assertEquals(0.022750131948179195, NormalDistributions.calculateCDF(-2), precision);
        assertEquals(0.15865525393145707, NormalDistributions.calculateCDF(-1), precision);
        assertEquals(0.5, NormalDistributions.calculateCDF(0), precision);

        assertEquals(-6.60772622151035, NormalDistributions.calculateLogCDF(-3), precision);
        assertEquals(-3.7831843336820326, NormalDistributions.calculateLogCDF(-2), precision);
        assertEquals(-1.8410216450092634, NormalDistributions.calculateLogCDF(-1), precision);
        assertEquals(-0.6931471805599453, NormalDistributions.calculateLogCDF(0), precision);

        assertEquals(0.9986501019683699, NormalDistributions.calculateSF(-3), precision);
        assertEquals(0.9772498680518208, NormalDistributions.calculateSF(-2), precision);
        assertEquals(0.8413447460685429, NormalDistributions.calculateSF(-1), precision);
        assertEquals(0.5, NormalDistributions.calculateSF(0), precision);

        assertEquals(-0.0013508099647482027, NormalDistributions.calculateLogSF(-3), precision);
        assertEquals(-0.02301290932896349, NormalDistributions.calculateLogSF(-2), precision);
        assertEquals(-0.1727537790234499, NormalDistributions.calculateLogSF(-1), precision);
        assertEquals(-0.6931471805599453, NormalDistributions.calculateLogSF(0), precision);

        assertEquals(-1.1503493803760079, NormalDistributions.calculatePPF(.125), precision);
        assertEquals(0, NormalDistributions.calculatePPF(.5), precision);
        assertEquals(Double.POSITIVE_INFINITY, NormalDistributions.calculatePPF(1), precision);
        assertEquals(1.1503493803760079, NormalDistributions.calculatePPF(0.875), precision);

        assertEquals(1.1503493803760079, NormalDistributions.calculateInverseSF(.125), precision);
        assertEquals(0, NormalDistributions.calculateInverseSF(.5), precision);
        assertEquals(Double.NEGATIVE_INFINITY, NormalDistributions.calculateInverseSF(1), precision);
        assertEquals(-1.1503493803760079, NormalDistributions.calculateInverseSF(0.875), precision);
    }

    @Test
    public void rateEvlTest() {
        assertEquals(0.013029539329753806, Boost.ratevl(-1, new double[]{0.234, 0.653, 12.4, 12879, 11}, new double[]{123.543, 1123, 987652., 45, 87, 12}), precision);
    }

    @Test
    public void ndtriTest() {
        assertEquals(Double.POSITIVE_INFINITY, ndtri(1), precision);
        assertEquals(Double.NEGATIVE_INFINITY, ndtri(0), precision);
        assertEquals(-1.17498679206609010000, ndtri(0.12), precision);
        assertEquals(-0.67448975019608171000, ndtri(0.25), precision);
        assertEquals(0.88529044882964181000, ndtri(0.812), precision);
    }

    @Test
    public void chbevlTest() {
        assertEquals(38.88412499999999700000, chbevl(-.25, new double[]{12, .34, 213.542, 123.}, 4), precision);
        assertEquals(61.15999999999999700000, chbevl(0, new double[]{12, .34, 213.542, 123.}, 4), precision);
        assertEquals(106.33799999999999000000, chbevl(.5, new double[]{12, .34, 213.542, 123.}, 4), precision);
        assertEquals(156.10100000000003000000, chbevl(1, new double[]{12, .34, 213.542, 123.}, 4), precision);
    }

    @Test
    public void gammaTest() {
        assertEquals(Double.POSITIVE_INFINITY, Cephes.Gamma(0), precision);
        assertEquals(1.7724538509055159, Cephes.Gamma(0.5), precision);
        assertEquals(1, Cephes.Gamma(1), precision);
        assertEquals(0.8862269254527579, Cephes.Gamma(1.5), precision);
    }


    @Test
    public void rangeTest() {
        final double[] a = ArrayUtils.range(0, 1, 0.1);
        System.out.println(Arrays.toString(a));
    }

    /* @Test
     public void AnglitDistributionTest() {
         assertEquals(0, AnglitDistributions.calculatePDF(-3), precision);
         assertEquals(1, AnglitDistributions.calculatePDF(0), precision);
         assertEquals(0.9689124217106447, AnglitDistributions.calculatePDF(0.125), precision);
         assertEquals(0.8775825618903728, AnglitDistributions.calculatePDF(.25), precision);
         assertEquals(0.5403023058681398, AnglitDistributions.calculatePDF(0.5), precision);
         assertEquals(0, AnglitDistributions.calculatePDF(1), precision);
         assertEquals(0, AnglitDistributions.calculatePDF(2), precision);

         assertEquals(0, AnglitDistributions.calculateCDF(-3), precision);
         assertEquals(0.5000000000000001, AnglitDistributions.calculateCDF(0), precision);
         assertEquals(0.6237019796272614, AnglitDistributions.calculateCDF(0.125), precision);
         assertEquals(0.7397127693021015, AnglitDistributions.calculateCDF(.25), precision);
         assertEquals(0.9207354924039483, AnglitDistributions.calculateCDF(0.5), precision);
         assertEquals(1, AnglitDistributions.calculateCDF(1), precision);
         assertEquals(1, AnglitDistributions.calculateCDF(2), precision);

         assertEquals(Double.NaN, AnglitDistributions.calculatePPF(-3), precision);
         assertEquals(-0.7853981633974483, AnglitDistributions.calculatePPF(0), precision);
         assertEquals(-0.42403103949074045, AnglitDistributions.calculatePPF(0.125), precision);
         assertEquals(-0.26179938779914935, AnglitDistributions.calculatePPF(.25), precision);
         assertEquals(1.1102230246251565e-16, AnglitDistributions.calculatePPF(0.5), precision);
         assertEquals(0.7853981633974483, AnglitDistributions.calculatePPF(1), precision);
         assertEquals(Double.NaN, AnglitDistributions.calculatePPF(2), precision);
     }*/
    @Test
    public void ldexpTests() {
        assertEquals(40, Libs.ldexp(10, 2), 0);
        assertEquals(-4.4924, Libs.ldexp(-1.1231, 2), 0);
        assertEquals(-35.9392, Libs.ldexp(-1.1231, 5), 0);
        assertEquals(-1264498185374951.0, Libs.ldexp(-1.1231, 50), 0);
        assertEquals(0.4375, Libs.ldexp(7, -4), 0);
        assertEquals(4.94066e-324, Libs.ldexp(1, -1074), 0);
        assertEquals(Double.POSITIVE_INFINITY, Libs.ldexp(1, 1024), 0);
        assertEquals(Double.NEGATIVE_INFINITY, Libs.ldexp(Double.NEGATIVE_INFINITY, -1), 0);
        assertEquals(-0., Libs.ldexp(-0., 10), 0);

    }

}