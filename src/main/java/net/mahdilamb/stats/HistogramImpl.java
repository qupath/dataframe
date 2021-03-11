package net.mahdilamb.stats;

import net.mahdilamb.stats.distributions.SummaryStatistics;

import java.util.Arrays;

/**
 * Default implementation of a histogram that can be used as
 * either a bin-counted histogram or probability density histogram
 */
final class HistogramImpl implements DensityHistogram {
    private final int[] binCount;
    private final double[] binEdges;
    double[] density;

    HistogramImpl(int[] binCount, double[] binEdges) {
        this.binCount = binCount;
        this.binEdges = binEdges;
    }

    @Override
    public int[] getCount() {
        return binCount;
    }

    @Override
    public double[] getBinEdges() {
        return binEdges;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof DensityHistogram)) {
            return false;
        }
        return Arrays.equals(binCount, ((DensityHistogram) o).getCount()) && Arrays.equals(binEdges, ((DensityHistogram) o).getBinEdges()) && Arrays.equals(density, ((DensityHistogram) o).getDensity());
    }

    @Override
    public String toString() {
        final StringBuilder out = new StringBuilder("Histogram {binEdges: ");

        for (int i = 0; i < binEdges.length; ++i) {
            out
                    .append(i == 0 ? '[' : " | ")
                    .append(String.format("%." + SummaryStatistics.PRECISION + "f", binEdges[i]));
        }
        out.append("],");
        final int currentOut = out.length();
        int i = 23;
        out.append("\n           binCount: [");
        for (int k = 0, j = 0; j < binCount.length; ++i) {
            final char c = out.charAt(i);
            if (c == '|') {
                final String val = Integer.toString(binCount[j++]);
                out.append(String.format("%0" + (i - (k == 0 ? 20 : k) - val.length() - 2) + "d", 0).replace('0', ' ')).append('(').append(val).append(')');
                k = i;
            }
        }
        if (currentOut > i) {
            out.append(String.format("%0" + (currentOut - i - 3) + "d", 0).replace('0', ' '));
        }
        return (density == null ? out.append(']') : out.append("]\n           density : ").append(Arrays.toString(density))).append('}').toString();
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(binCount);
        result = 31 * result + Arrays.hashCode(binEdges);
        if (density != null) {
            result = 31 * result + Arrays.hashCode(density);
        }
        return result;
    }

    @Override
    public double[] getDensity() {
        if (density == null) {
            density = StatUtils.toDensity(this);
        }
        return density;
    }
}
