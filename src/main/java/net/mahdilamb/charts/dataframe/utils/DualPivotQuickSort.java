package net.mahdilamb.charts.dataframe.utils;


import java.util.Comparator;
import java.util.function.IntFunction;

import static net.mahdilamb.charts.dataframe.utils.Sorts.intRange;

/**
 * Dual pivot quick sort based on https://www.geeksforgeeks.org/dual-pivot-quicksort/
 */
public final class DualPivotQuickSort {
    private DualPivotQuickSort() {

    }

    static void dualPivotQuickSort(double[] arr, int low, int high) {
        if (low < high) {
            if (arr[low] > arr[high]) {
                Sorts.swap(arr, low, high);
            }
            int pivLow = low + 1;
            int pivHigh = high - 1, k = low + 1;
            double p = arr[low], q = arr[high];

            while (k <= pivHigh) {
                if (arr[k] < p) {
                    Sorts.swap(arr, k, pivLow);
                    ++pivLow;
                } else if (arr[k] >= q) {
                    while (arr[pivHigh] > q && k < pivHigh)
                        --pivHigh;

                    Sorts.swap(arr, k, pivHigh);
                    --pivHigh;

                    if (arr[k] < p) {
                        Sorts.swap(arr, k, pivLow);
                        ++pivLow;
                    }
                }
                ++k;
            }
            --pivLow;
            ++pivHigh;

            Sorts.swap(arr, low, pivLow);
            Sorts.swap(arr, high, pivHigh);
            dualPivotQuickSort(arr, low, pivLow - 1);
            dualPivotQuickSort(arr, pivLow + 1, pivHigh - 1);
            dualPivotQuickSort(arr, pivHigh + 1, high);
        }
    }

    static <T> void dualPivotQuickSort(T[] arr, int low, int high, Comparator<T> cmp) {
        if (low < high) {
            if (cmp.compare(arr[low], arr[high]) > 0) {
                Sorts.swap(arr, low, high);
            }
            int pivLow = low + 1;
            int pivHigh = high - 1, k = low + 1;
            T p = arr[low], q = arr[high];

            while (k <= pivHigh) {
                if (cmp.compare(arr[k], p) < 0) {
                    Sorts.swap(arr, k, pivLow);
                    pivLow++;
                } else if (cmp.compare(arr[k], q) >= 0) {
                    while (cmp.compare(arr[pivHigh], q) > 0 && k < pivHigh)
                        --pivHigh;

                    Sorts.swap(arr, k, pivHigh);
                    --pivHigh;

                    if (cmp.compare(arr[k], p) < 0) {
                        Sorts.swap(arr, k, pivLow);
                        ++pivLow;
                    }
                }
                ++k;
            }
            --pivLow;
            ++pivHigh;

            Sorts.swap(arr, low, pivLow);
            Sorts.swap(arr, high, pivHigh);
            dualPivotQuickSort(arr, low, pivLow - 1, cmp);
            dualPivotQuickSort(arr, pivLow + 1, pivHigh - 1, cmp);
            dualPivotQuickSort(arr, pivHigh + 1, high, cmp);
        }
    }

    static <T> void dualPivotQuickSort(int[] with, T[] arr, int low, int high, Comparator<T> cmp) {
        if (low < high) {
            if (cmp.compare(arr[with[low]], arr[with[high]]) > 0) {
                Sorts.swap(with, low, high);
            }
            int pivLow = low + 1;
            int pivHigh = high - 1, k = low + 1;
            T p = arr[with[low]], q = arr[with[high]];

            while (k <= pivHigh) {
                if (cmp.compare(arr[with[k]], p) < 0) {
                    Sorts.swap(with, k, pivLow);
                    ++pivLow;
                } else if (cmp.compare(arr[with[k]], q) >= 0) {
                    while (cmp.compare(arr[with[pivHigh]], q) > 0 && k < pivHigh)
                        --pivHigh;

                    Sorts.swap(with, k, pivHigh);
                    --pivHigh;

                    if (cmp.compare(arr[with[k]], p) < 0) {
                        Sorts.swap(with, k, pivLow);
                        ++pivLow;
                    }
                }
                ++k;
            }
            --pivLow;
            ++pivHigh;

            Sorts.swap(with, low, pivLow);
            Sorts.swap(with, high, pivHigh);
            dualPivotQuickSort(with, arr, low, pivLow - 1, cmp);
            dualPivotQuickSort(with, arr, pivLow + 1, pivHigh - 1, cmp);
            dualPivotQuickSort(with, arr, pivHigh + 1, high, cmp);
        }
    }

    static <T> void dualPivotQuickSort(int[] with, IntFunction<T> arr, int low, int high, Comparator<T> cmp) {
        if (low < high) {
            if (cmp.compare(arr.apply(with[low]), arr.apply(with[high])) > 0) {
                Sorts.swap(with, low, high);
            }
            int pivLow = low + 1;
            int pivHigh = high - 1, k = low + 1;
            T p = arr.apply(with[low]), q = arr.apply(with[high]);

            while (k <= pivHigh) {
                if (cmp.compare(arr.apply(with[k]), p) < 0) {
                    Sorts.swap(with, k, pivLow);
                    ++pivLow;
                } else if (cmp.compare(arr.apply(with[k]), q) >= 0) {
                    while (cmp.compare(arr.apply(with[pivHigh]), q) > 0 && k < pivHigh)
                        --pivHigh;

                    Sorts.swap(with, k, pivHigh);
                    --pivHigh;

                    if (cmp.compare(arr.apply(with[k]), p) < 0) {
                        Sorts.swap(with, k, pivLow);
                        ++pivLow;
                    }
                }
                ++k;
            }
            --pivLow;
            ++pivHigh;

            Sorts.swap(with, low, pivLow);
            Sorts.swap(with, high, pivHigh);
            dualPivotQuickSort(with, arr, low, pivLow - 1, cmp);
            dualPivotQuickSort(with, arr, pivLow + 1, pivHigh - 1, cmp);
            dualPivotQuickSort(with, arr, pivHigh + 1, high, cmp);
        }
    }

    public static <T> void sort(T[] arr, Comparator<T> cmp) {
        dualPivotQuickSort(arr, 0, arr.length - 1, cmp);
    }

    public static <T extends Comparable<T>> void sort(T[] arr) {
        dualPivotQuickSort(arr, 0, arr.length - 1, Comparator.naturalOrder());
    }

    public static <T extends Comparable<T>> int[] argSort(int[] with, T[] arr) {
        dualPivotQuickSort(with, arr, 0, arr.length - 1, Comparator.naturalOrder());
        return with;
    }

    /**
     * Get the order that would sort an array, using the default comparator.
     *
     * @param arr the unsorted array
     * @param <T> the type of the objects in the array
     * @return the argument array
     */
    public static <T extends Comparable<T>> int[] argSort(T[] arr) {
        int[] with = intRange(arr.length);
        dualPivotQuickSort(with, arr, 0, arr.length - 1, Comparator.naturalOrder());
        return with;
    }

    /**
     * Get the order that would sort an array
     *
     * @param arr the unsorted array
     * @param cmp the comparator to use
     * @param <T> the type of the objects in the array
     * @return the argument array
     */
    public static <T> int[] argSort(T[] arr, Comparator<T> cmp) {
        int[] with = intRange(arr.length);
        dualPivotQuickSort(with, arr, 0, arr.length - 1, cmp);
        return with;
    }

    public static <T extends Comparable<T>> int[] argSort(IntFunction<T> getter, int size) {
        return argSort(getter, size, Comparator.naturalOrder());
    }

    public static <T extends Comparable<T>> int[] argSort(IntFunction<T> getter, int size, Comparator<T> cmp) {
        final int[] args = intRange(size);
        dualPivotQuickSort(args, getter, 0, size - 1, cmp);
        return args;
    }

}
