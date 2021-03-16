package net.mahdilamb.stats.utils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.IntToDoubleFunction;

import static net.mahdilamb.stats.utils.Sorts.swap;

/**
 * A selection algorithm that finds the kth ordered element in an unordered array
 */
public final class FloydRivestSelection {
    private FloydRivestSelection() {

    }

    /**
     * Calculate the minimum element in the data using QuickSelect
     *
     * @param data the data
     * @return the minimum element in the data
     */
    public static double min(double... data) {
        select(data, 0, data.length - 1, 0);
        return data[0];
    }

    /**
     * Calculate the maximum element in the data using QuickSelect
     *
     * @param data the data
     * @return the maximum element in the data
     */
    public static double max(double... data) {
        int last = data.length - 1;
        select(data, 0, last, last);
        return data[last];
    }

    /**
     * Calculate the middle element in the data using QuickSelect
     *
     * @param data the data
     * @return the median element in the data
     */
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

    /**
     * Select the kth element in an array of doubles
     *
     * @param array the array of doubles
     * @param left  the starting index for selection
     * @param right the last index for selection (inclusive)
     * @param k     the kth element to select
     */
    public static void select(double[] array, int left, int right, int k) {
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

    /**
     * Select the kth element in an array
     *
     * @param args  an array of distinct indices that includes the partial sort of the array
     * @param array the value of the arrays
     * @param left  the start index
     * @param right the end index
     * @param k     the k element
     */
    public static double select(int[] args, double[] array, int left, int right, int k) {
        while (right > left) {
            if (right - left > 600) {
                int n = right - left + 1;
                int i = k - left + 1;
                double z = Math.log(n);
                double s = 0.5 * Math.exp(2 * z / 3);
                double sd = 0.5 * Math.sqrt(z * s * (n - s) / n) * Math.signum(i - n >> 1);
                int newLeft = (int) Math.max(left, k - i * s / n + sd);
                int newRight = (int) Math.min(right, k + (n - i) * s / n + sd);
                return select(args, array, newLeft, newRight, k);
            }
            double t = array[args[k]];
            int i = left;
            int j = right;
            swap(args, left, k);
            if (array[args[right]] > t) {
                swap(args, right, left);
            }
            while (i < j) {
                swap(args, i, j);
                ++i;
                --j;
                while (array[args[i]] < t) i++;
                while (array[args[j]] > t) j--;
            }

            if (array[args[left]] == t) {
                swap(args, left, j);
            } else {
                swap(args, ++j, right);
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
        return array[args[k]];
    }
    /**
     * Select the kth element in an array using a functional style
     *
     * @param args  an array of distinct indices that includes the partial sort of the array
     * @param array the index-to-value getter
     * @param left  the start index
     * @param right the end index
     * @param k     the k element
     */
    public static double select(int[] args, IntToDoubleFunction array, int left, int right, int k) {
        while (right > left) {
            if (right - left > 600) {
                int n = right - left + 1;
                int i = k - left + 1;
                double z = Math.log(n);
                double s = 0.5 * Math.exp(2 * z / 3);
                double sd = 0.5 * Math.sqrt(z * s * (n - s) / n) * Math.signum(i - n >> 1);
                int newLeft = (int) Math.max(left, k - i * s / n + sd);
                int newRight = (int) Math.min(right, k + (n - i) * s / n + sd);
                return select(args, array, newLeft, newRight, k);
            }
            double t = array.applyAsDouble(args[k]);
            int i = left;
            int j = right;
            swap(args, left, k);
            if (array.applyAsDouble(args[right]) > t) {
                swap(args, right, left);
            }
            while (i < j) {
                swap(args, i, j);
                ++i;
                --j;
                while (array.applyAsDouble(args[i]) < t) i++;
                while (array.applyAsDouble(args[j]) > t) j--;
            }

            if (array.applyAsDouble(args[left]) == t) {
                swap(args, left, j);
            } else {
                swap(args, ++j, right);
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
        return array.applyAsDouble(args[k]);
    }

    /**
     * Select the kth element in an array of objects
     *
     * @param array the array
     * @param left  the starting index
     * @param right the ending index (inclusive)
     * @param k     the kth element to select
     * @param cmp   the comparator
     * @param <T>   the type of the objects in the array
     */
    public static <T> void select(T[] array, int left, int right, int k, Comparator<T> cmp) {
        while (right > left) {
            if (right - left > 600) {
                int n = right - left + 1;
                int i = k - left + 1;
                double z = Math.log(n);
                double s = 0.5 * Math.exp(2 * z / 3);
                double sd = 0.5 * Math.sqrt(z * s * (n - s) / n) * Math.signum(i - n >> 1);
                int newLeft = (int) Math.max(left, k - i * s / n + sd);
                int newRight = (int) Math.min(right, k + (n - i) * s / n + sd);
                select(array, newLeft, newRight, k, cmp);
            }
            T t = array[k];
            int i = left;
            int j = right;
            swap(array, left, k);
            if (cmp.compare(array[right], t) > 0) {
                swap(array, right, left);
            }
            while (i < j) {
                swap(array, i, j);
                ++i;
                --j;
                while (cmp.compare(array[i], t) < 0) i++;
                while (cmp.compare(array[j], t) > 0) j--;
            }

            if (cmp.compare(array[left], t) == 0) {
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

    /**
     * Select the kth element in a list
     *
     * @param list  the list
     * @param left  the starting index
     * @param right the ending index (inclusive)
     * @param k     the kth element to select
     * @param cmp   the comparator
     * @param <T>   the type of the objects in the list
     */
    public static <T> void select(List<T> list, int left, int right, int k, Comparator<T> cmp) {
        while (right > left) {
            if (right - left > 600) {
                int n = right - left + 1;
                int i = k - left + 1;
                double z = Math.log(n);
                double s = 0.5 * Math.exp(2 * z / 3);
                double sd = 0.5 * Math.sqrt(z * s * (n - s) / n) * Math.signum(i - n >> 1);
                int newLeft = (int) Math.max(left, k - i * s / n + sd);
                int newRight = (int) Math.min(right, k + (n - i) * s / n + sd);
                select(list, newLeft, newRight, k, cmp);
            }
            T t = list.get(k);
            int i = left;
            int j = right;
            Collections.swap(list, left, k);
            if (cmp.compare(list.get(right), t) > 0) {
                Collections.swap(list, right, left);
            }
            while (i < j) {
                Collections.swap(list, i, j);
                ++i;
                --j;
                while (cmp.compare(list.get(i), t) < 0) i++;
                while (cmp.compare(list.get(j), t) > 0) j--;
            }

            if (cmp.compare(list.get(left), t) == 0) {
                Collections.swap(list, left, j);
            } else {
                Collections.swap(list, ++j, right);
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
