package net.mahdilamb.stats;


import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;

import static net.mahdilamb.stats.MathUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public final class CompensatedArithmeticTests {
    static final double PRECISION = 1e-15;
    static double t = 0.1;
    static double[] a = {10000.0, 3.14159, 2.71828};
    static double[] b = {t, t, t, t, t, t, t, t, t, t};

    static double[] c = {t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t};
    static double[] d = {.1, .2, .3, .4};
    static double[] e = ArrayUtils.full(ThreadLocalRandom.current()::nextGaussian, 20);
    static double[] f = {.1, .2, .3};
    static double[][] tests = {a, b, c, d, e, f};

    static double bdProd(double... values) {
        BigDecimal accu = BigDecimal.valueOf(values[0]);
        for (int i = 1; i < values.length; ++i) {
            accu = accu.multiply(BigDecimal.valueOf(values[i]));
        }
        return accu.doubleValue();
    }

    static double bdSum(double... values) {
        BigDecimal accu = BigDecimal.valueOf(values[0]);
        for (int i = 1; i < values.length; ++i) {
            accu = accu.add(BigDecimal.valueOf(values[i]));
        }
        return accu.doubleValue();
    }

    /**
     * Compare compensated products with big decimal
     */
    @Test
    public void compensatedProductTests() {
        for (double[] test : tests) {
            double exact = bdProd(test);
            double comp = compProd(test);
            double compFMA = compProdFMA(test);
            //System.out.printf("exact: %s; naive: %s; comp: %s; compFMA: %s;%n", exact, StatUtils.product(test), comp, compFMA);
            assertEquals(exact, comp, PRECISION);
            assertEquals(exact, compFMA, PRECISION);
        }
    }

    /**
     * Compare compensated summations with big decimal
     */
    @Test
    public void compensatedSumTests() {

        for (double[] test : tests) {
            double exact = bdSum(test);
            assertEquals(exact, kahanSum(test), PRECISION);
            assertEquals(exact, neumaierSum(test), PRECISION);
            assertEquals(exact, kleinSum(test), PRECISION);
            assertEquals(exact, fsum(test), PRECISION);
        }

    }

}
