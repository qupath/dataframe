package net.mahdilamb.statistics.utils;

import static net.mahdilamb.statistics.utils.Sorts.swap;

/**
 * A selection algorithm that finds the kth ordered element in an unordered array
 */
public final class FloydRivestSelection {
    private FloydRivestSelection() {

    }

    public static double min(double... data) {
        select(data, 0, data.length - 1, 0);
        return data[0];
    }

    public static double max(double... data) {
        int last = data.length - 1;
        select(data, 0, last, last);
        return data[last];
    }

    public static double median(double... data) {
        int mid = (data.length >> 1);
        select(data, 0, data.length - 1, mid);
        if ((data.length & 1) == 1) {
            return data[mid];
        } else {
            select(data, 0, data.length - 1, mid - 1);
            return (data[mid - 1] + data[mid]) * .5;
        }
    }

    private static void select(double[] array, int left, int right, int k) {
        while (right > left) {
            if (right - left > 600) {
                int n = right - left + 1;
                int i = k - left + 1;
                double z = Math.log(n);
                double s = 0.5 * Math.exp(2 * z / 3);
                double sd = 0.5 * Math.sqrt(z * s * (n - s) / n) * Math.signum(i - n >> 1);
                int newLeft = (int) Math.max(left, k - i * s / n + sd);
                int newRight = (int) Math.min(right, k + (n - i) * s / n + sd);
                select(array, newLeft, newRight, k);
            }
            double t = array[k];
            int i = left;
            int j = right;
            swap(array, left, k);
            if (array[right] > t) {
                swap(array, right, left);
            }
            while (i < j) {
                swap(array, i, j);
                ++i;
                --j;
                while (array[i] < t) i++;
                while (array[j] > t) j--;
            }

            if (array[left] == t) {
                swap(array, left, j);
            } else {
                swap(array, ++j, right);
            }
            // Adjust left and right towards the boundaries of the subset
            // containing the (k − left + 1)th smallest element.
            if (j <= k) {
                left = j + 1;
            }
            if (k <= j) {
                right = j - 1;
            }
        }
    }

}
