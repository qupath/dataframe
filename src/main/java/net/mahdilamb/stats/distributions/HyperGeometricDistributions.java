package net.mahdilamb.stats.distributions;

import net.mahdilamb.stats.libs.Cephes;

public final class HyperGeometricDistributions {
    private HyperGeometricDistributions() {

    }

    public static double logPMF(double k, double M, double n, double N) {
        double bad = M - n;
        return (Cephes.betaln(n + 1, 1) + Cephes.betaln(bad + 1, 1) + Cephes.betaln(M - N + 1, N + 1) -
                Cephes.betaln(k + 1, n - k + 1) - Cephes.betaln(N - k + 1, bad - N + k + 1) -
                Cephes.betaln(M + 1, 1));
    }

    public static double PMF(double k, double M, double n, double N) {
        return Math.exp(logPMF(k, M, n, N));
    }

    public static SummaryStatistics stats(double M, double n, double N) {
        double m = M - n;
        double p = n / M;
        double mu = N * p;

        double var = m * n * N * (M - N) * 1.0 / (M * M * (M - 1));
        double g1 = (m - n) * (M - 2 * N) / (M - 2.0) * Math.sqrt((M - 1.0) / (m * n * N * (M - N)));

        double g2 = M * (M + 1) - 6. * N * (M - N) - 6. * n * m;
        g2 *= (M - 1) * M * M;
        g2 += 6. * n * N * (M - N) * m * (5. * M - 6);
        g2 /= n * N * (M - N) * m * (M - 2.) * (M - 3.);
        return new SummaryStatistics(mu, var, g1, g2);
    }
}
