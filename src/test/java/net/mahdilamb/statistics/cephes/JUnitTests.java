package net.mahdilamb.statistics.cephes;

import static net.mahdilamb.statistics.libs.Cephes.*;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
public final class JUnitTests {
	private static final double precision = 0.00000001;

	/**
	 * Run auto-generated ndtriTest
	*/
	@Test
	public void ndtriTest(){
		assertEquals(Double.NaN, ndtri(Double.NaN), precision);
		assertEquals(Double.NaN, ndtri(Double.NEGATIVE_INFINITY), precision);
		assertEquals(Double.NaN, ndtri(-100), precision);
		assertEquals(Double.NaN, ndtri(-10), precision);
		assertEquals(Double.NaN, ndtri(-5), precision);
		assertEquals(Double.NaN, ndtri(-1), precision);
		assertEquals(Double.NaN, ndtri(-0.9), precision);
		assertEquals(Double.NaN, ndtri(-0.75), precision);
		assertEquals(Double.NaN, ndtri(-0.5), precision);
		assertEquals(Double.NaN, ndtri(-0.25), precision);
		assertEquals(Double.NaN, ndtri(-0.1), precision);
		assertEquals(Double.NaN, ndtri(-0.01), precision);
		assertEquals(Double.NEGATIVE_INFINITY, ndtri(0), precision);
		assertEquals(-2.3263478740408408, ndtri(0.01), precision);
		assertEquals(-1.2815515655446004, ndtri(0.1), precision);
		assertEquals(-0.6744897501960817, ndtri(0.25), precision);
		assertEquals(0.0, ndtri(0.5), precision);
		assertEquals(0.6744897501960817, ndtri(0.75), precision);
		assertEquals(1.2815515655446004, ndtri(0.9), precision);
		assertEquals(Double.POSITIVE_INFINITY, ndtri(1), precision);
		assertEquals(Double.NaN, ndtri(5), precision);
		assertEquals(Double.NaN, ndtri(10), precision);
		assertEquals(Double.NaN, ndtri(100), precision);
		assertEquals(Double.NaN, ndtri(Double.POSITIVE_INFINITY), precision);
	}

	/**
	 * Run auto-generated zetacTest
	*/
	@Test
	public void zetacTest(){
		assertEquals(Double.NaN, zetac(Double.NaN), precision);
		assertEquals(Double.NaN, zetac(Double.NEGATIVE_INFINITY), precision);
		assertEquals(-1.0, zetac(-100), precision);
		assertEquals(-1.0, zetac(-10), precision);
		assertEquals(-1.003968253968254, zetac(-5), precision);
		assertEquals(-1.0833333333333335, zetac(-1), precision);
		assertEquals(-1.101193503985352, zetac(-0.9), precision);
		assertEquals(-1.1336427744365847, zetac(-0.75), precision);
		assertEquals(-1.2078862249773548, zetac(-0.5), precision);
		assertEquals(-1.3204512642285773, zetac(-0.25), precision);
		assertEquals(-1.4172280407673665, zetac(-0.1), precision);
		assertEquals(-1.4909099416053366, zetac(-0.01), precision);
		assertEquals(-1.5, zetac(0), precision);
		assertEquals(-1.50929071403984, zetac(0.01), precision);
		assertEquals(-1.6030375198562419, zetac(0.1), precision);
		assertEquals(-1.8132784052618918, zetac(0.25), precision);
		assertEquals(-2.4603545088095866, zetac(0.5), precision);
		assertEquals(-4.441285386945223, zetac(0.75), precision);
		assertEquals(-10.430114019402255, zetac(0.9), precision);
		assertEquals(Double.POSITIVE_INFINITY, zetac(1), precision);
		assertEquals(0.03692775514336993, zetac(5), precision);
		assertEquals(0.0009945751278180853, zetac(10), precision);
		assertEquals(7.888609052210118e-31, zetac(100), precision);
		assertEquals(0.0, zetac(Double.POSITIVE_INFINITY), precision);
	}

	/**
	 * Run auto-generated zetaTest
	*/
	@Test
	public void zetaTest(){
		assertEquals(Double.NaN, riemann_zeta(Double.NaN), precision);
		assertEquals(Double.NaN, riemann_zeta(Double.NEGATIVE_INFINITY), precision);
		assertEquals(0.0, riemann_zeta(-100), precision);
		assertEquals(0.0, riemann_zeta(-10), precision);
		assertEquals(-0.003968253968253969, riemann_zeta(-5), precision);
		assertEquals(-0.08333333333333338, riemann_zeta(-1), precision);
		assertEquals(-0.10119350398535193, riemann_zeta(-0.9), precision);
		assertEquals(-0.1336427744365846, riemann_zeta(-0.75), precision);
		assertEquals(-0.20788622497735465, riemann_zeta(-0.5), precision);
		assertEquals(-0.3204512642285772, riemann_zeta(-0.25), precision);
		assertEquals(-0.4172280407673665, riemann_zeta(-0.1), precision);
		assertEquals(-0.4909099416053367, riemann_zeta(-0.01), precision);
		assertEquals(-0.5, riemann_zeta(0), precision);
		assertEquals(-0.5092907140398399, riemann_zeta(0.01), precision);
		assertEquals(-0.6030375198562419, riemann_zeta(0.1), precision);
		assertEquals(-0.8132784052618918, riemann_zeta(0.25), precision);
		assertEquals(-1.4603545088095866, riemann_zeta(0.5), precision);
		assertEquals(-3.441285386945223, riemann_zeta(0.75), precision);
		assertEquals(-9.430114019402255, riemann_zeta(0.9), precision);
		assertEquals(Double.POSITIVE_INFINITY, riemann_zeta(1), precision);
		assertEquals(1.03692775514337, riemann_zeta(5), precision);
		assertEquals(1.000994575127818, riemann_zeta(10), precision);
		assertEquals(1.0, riemann_zeta(100), precision);
		assertEquals(1.0, riemann_zeta(Double.POSITIVE_INFINITY), precision);
	}

	/**
	 * Run auto-generated erfTest
	*/
	@Test
	public void erfTest(){
		assertEquals(Double.NaN, erf(Double.NaN), precision);
		assertEquals(-1.0, erf(Double.NEGATIVE_INFINITY), precision);
		assertEquals(-1.0, erf(-100), precision);
		assertEquals(-1.0, erf(-10), precision);
		assertEquals(-0.9999999999984626, erf(-5), precision);
		assertEquals(-0.8427007929497148, erf(-1), precision);
		assertEquals(-0.7969082124228322, erf(-0.9), precision);
		assertEquals(-0.7111556336535151, erf(-0.75), precision);
		assertEquals(-0.5204998778130465, erf(-0.5), precision);
		assertEquals(-0.2763263901682369, erf(-0.25), precision);
		assertEquals(-0.1124629160182849, erf(-0.1), precision);
		assertEquals(-0.011283415555849616, erf(-0.01), precision);
		assertEquals(0.0, erf(0), precision);
		assertEquals(0.011283415555849616, erf(0.01), precision);
		assertEquals(0.1124629160182849, erf(0.1), precision);
		assertEquals(0.2763263901682369, erf(0.25), precision);
		assertEquals(0.5204998778130465, erf(0.5), precision);
		assertEquals(0.7111556336535151, erf(0.75), precision);
		assertEquals(0.7969082124228322, erf(0.9), precision);
		assertEquals(0.8427007929497148, erf(1), precision);
		assertEquals(0.9999999999984626, erf(5), precision);
		assertEquals(1.0, erf(10), precision);
		assertEquals(1.0, erf(100), precision);
		assertEquals(1.0, erf(Double.POSITIVE_INFINITY), precision);
	}

	/**
	 * Run auto-generated erfcTest
	*/
	@Test
	public void erfcTest(){
		assertEquals(Double.NaN, erfc(Double.NaN), precision);
		assertEquals(2.0, erfc(Double.NEGATIVE_INFINITY), precision);
		assertEquals(2.0, erfc(-100), precision);
		assertEquals(2.0, erfc(-10), precision);
		assertEquals(1.9999999999984626, erfc(-5), precision);
		assertEquals(1.8427007929497148, erfc(-1), precision);
		assertEquals(1.7969082124228322, erfc(-0.9), precision);
		assertEquals(1.7111556336535152, erfc(-0.75), precision);
		assertEquals(1.5204998778130465, erfc(-0.5), precision);
		assertEquals(1.276326390168237, erfc(-0.25), precision);
		assertEquals(1.1124629160182848, erfc(-0.1), precision);
		assertEquals(1.0112834155558497, erfc(-0.01), precision);
		assertEquals(1.0, erfc(0), precision);
		assertEquals(0.9887165844441503, erfc(0.01), precision);
		assertEquals(0.8875370839817152, erfc(0.1), precision);
		assertEquals(0.7236736098317631, erfc(0.25), precision);
		assertEquals(0.4795001221869535, erfc(0.5), precision);
		assertEquals(0.2888443663464849, erfc(0.75), precision);
		assertEquals(0.20309178757716784, erfc(0.9), precision);
		assertEquals(0.15729920705028516, erfc(1), precision);
		assertEquals(1.5374597944280347e-12, erfc(5), precision);
		assertEquals(2.0884875837625446e-45, erfc(10), precision);
		assertEquals(0.0, erfc(100), precision);
		assertEquals(0.0, erfc(Double.POSITIVE_INFINITY), precision);
	}

	/**
	 * Run auto-generated gammaTest
	*/
	@Test
	public void gammaTest(){
		assertEquals(Double.NaN, Gamma(Double.NaN), precision);
		assertEquals(Double.NEGATIVE_INFINITY, Gamma(Double.NEGATIVE_INFINITY), precision);
		assertEquals(Double.POSITIVE_INFINITY, Gamma(-100), precision);
		assertEquals(Double.POSITIVE_INFINITY, Gamma(-10), precision);
		assertEquals(Double.POSITIVE_INFINITY, Gamma(-5), precision);
		assertEquals(Double.POSITIVE_INFINITY, Gamma(-1), precision);
		assertEquals(-10.570564109631928, Gamma(-0.9), precision);
		assertEquals(-4.834146544295876, Gamma(-0.75), precision);
		assertEquals(-3.5449077018110318, Gamma(-0.5), precision);
		assertEquals(-4.90166680986071, Gamma(-0.25), precision);
		assertEquals(-10.686287021193193, Gamma(-0.1), precision);
		assertEquals(-100.5871979644108, Gamma(-0.01), precision);
		assertEquals(Double.POSITIVE_INFINITY, Gamma(0), precision);
		assertEquals(99.43258511915059, Gamma(0.01), precision);
		assertEquals(9.513507698668732, Gamma(0.1), precision);
		assertEquals(3.625609908221908, Gamma(0.25), precision);
		assertEquals(1.7724538509055159, Gamma(0.5), precision);
		assertEquals(1.2254167024651774, Gamma(0.75), precision);
		assertEquals(1.0686287021193195, Gamma(0.9), precision);
		assertEquals(1.0, Gamma(1), precision);
		assertEquals(24.0, Gamma(5), precision);
		assertEquals(362880.0, Gamma(10), precision);
		assertEquals(9.332621544394417e+155, Gamma(100), precision);
		assertEquals(Double.POSITIVE_INFINITY, Gamma(Double.POSITIVE_INFINITY), precision);
	}

	/**
	 * Run auto-generated dawsnTest
	*/
	@Test
	public void dawsnTest(){
		assertEquals(Double.NaN, dawsn(Double.NaN), precision);
		assertEquals(-0.0, dawsn(Double.NEGATIVE_INFINITY), precision);
		assertEquals(-0.005000250037509379, dawsn(-100), precision);
		assertEquals(-0.05025384718759854, dawsn(-10), precision);
		assertEquals(-0.10213407442427686, dawsn(-5), precision);
		assertEquals(-0.5380795069127684, dawsn(-1), precision);
		assertEquals(-0.5407243187262987, dawsn(-0.9), precision);
		assertEquals(-0.5230127677445182, dawsn(-0.75), precision);
		assertEquals(-0.4244363835020223, dawsn(-0.5), precision);
		assertEquals(-0.23983916356289822, dawsn(-0.25), precision);
		assertEquals(-0.0993359923978529, dawsn(-0.1), precision);
		assertEquals(-0.00999933335999924, dawsn(-0.01), precision);
		assertEquals(0.0, dawsn(0), precision);
		assertEquals(0.00999933335999924, dawsn(0.01), precision);
		assertEquals(0.0993359923978529, dawsn(0.1), precision);
		assertEquals(0.23983916356289822, dawsn(0.25), precision);
		assertEquals(0.4244363835020223, dawsn(0.5), precision);
		assertEquals(0.5230127677445182, dawsn(0.75), precision);
		assertEquals(0.5407243187262987, dawsn(0.9), precision);
		assertEquals(0.5380795069127684, dawsn(1), precision);
		assertEquals(0.10213407442427686, dawsn(5), precision);
		assertEquals(0.05025384718759854, dawsn(10), precision);
		assertEquals(0.005000250037509379, dawsn(100), precision);
		assertEquals(0.0, dawsn(Double.POSITIVE_INFINITY), precision);
	}

	/**
	 * Run auto-generated ellipeTest
	*/
	@Test
	public void ellipeTest(){
		assertEquals(Double.NaN, ellpe(Double.NaN), precision);
		assertEquals(Double.POSITIVE_INFINITY, ellpe(Double.NEGATIVE_INFINITY), precision);
		assertEquals(10.209260919814572, ellpe(-100), precision);
		assertEquals(3.639138038417769, ellpe(-10), precision);
		assertEquals(2.830198246345877, ellpe(-5), precision);
		assertEquals(1.9100988945138562, ellpe(-1), precision);
		assertEquals(1.879834732670387, ellpe(-0.9), precision);
		assertEquals(1.8332049670486221, ellpe(-0.75), precision);
		assertEquals(1.7517712756948174, ellpe(-0.5), precision);
		assertEquals(1.664791805391338, ellpe(-0.25), precision);
		assertEquals(1.6093590249375296, ellpe(-0.1), precision);
		assertEquals(1.5747159850169883, ellpe(-0.01), precision);
		assertEquals(1.5707963267948966, ellpe(0), precision);
		assertEquals(1.5668619420216683, ellpe(0.01), precision);
		assertEquals(1.5307576368977633, ellpe(0.1), precision);
		assertEquals(1.4674622093394272, ellpe(0.25), precision);
		assertEquals(1.3506438810476755, ellpe(0.5), precision);
		assertEquals(1.2110560275684594, ellpe(0.75), precision);
		assertEquals(1.1047747327040733, ellpe(0.9), precision);
		assertEquals(1.0, ellpe(1), precision);
		assertEquals(Double.NaN, ellpe(5), precision);
		assertEquals(Double.NaN, ellpe(10), precision);
		assertEquals(Double.NaN, ellpe(100), precision);
		assertEquals(Double.NaN, ellpe(Double.POSITIVE_INFINITY), precision);
	}

	/**
	 * Run auto-generated ellipkTest
	*/
	@Test
	public void ellipkTest(){
		assertEquals(Double.NaN, ellipk(Double.NaN), precision);
		assertEquals(0.0, ellipk(Double.NEGATIVE_INFINITY), precision);
		assertEquals(0.368219248609141, ellipk(-100), precision);
		assertEquals(0.7908718902387385, ellipk(-10), precision);
		assertEquals(0.9555039270640441, ellipk(-5), precision);
		assertEquals(1.3110287771460598, ellipk(-1), precision);
		assertEquals(1.3293621856564093, ellipk(-0.9), precision);
		assertEquals(1.3590628922507417, ellipk(-0.75), precision);
		assertEquals(1.415737208425956, ellipk(-0.5), precision);
		assertEquals(1.4844124734223865, ellipk(-0.25), precision);
		assertEquals(1.5335928197134567, ellipk(-0.1), precision);
		assertEquals(1.5668912730681963, ellipk(-0.01), precision);
		assertEquals(1.5707963267948966, ellipk(0), precision);
		assertEquals(1.5747455615173558, ellipk(0.01), precision);
		assertEquals(1.6124413487202192, ellipk(0.1), precision);
		assertEquals(1.685750354812596, ellipk(0.25), precision);
		assertEquals(1.8540746773013719, ellipk(0.5), precision);
		assertEquals(2.156515647499643, ellipk(0.75), precision);
		assertEquals(2.5780921133481733, ellipk(0.9), precision);
		assertEquals(Double.POSITIVE_INFINITY, ellipk(1), precision);
		assertEquals(Double.NaN, ellipk(5), precision);
		assertEquals(Double.NaN, ellipk(10), precision);
		assertEquals(Double.NaN, ellipk(100), precision);
		assertEquals(Double.NaN, ellipk(Double.POSITIVE_INFINITY), precision);
	}

}