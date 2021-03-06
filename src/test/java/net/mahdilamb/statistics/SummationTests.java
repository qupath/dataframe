package net.mahdilamb.statistics;

import org.junit.Test;

import static net.mahdilamb.statistics.MathUtils.*;

public final class SummationTests {

    @Test
    public void preciseSummationTests() {
        double t = 0.1;
        double[] a = {10000.0, 3.14159, 2.71828};
        double[] b = {t, t, t, t, t, t, t, t, t, t};

        double[] c = {t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t, t};
        double[][] tests = {a, b, c};

        for (double[] test : tests) {
            System.out.println(StatUtils.sum(test));
            System.out.println(kahanSummation(test));
            System.out.println(neumaierSum(test));
            System.out.println(kleinSum(test));
            System.out.println(fsum(test));
            System.out.println();
        }


    }

}
