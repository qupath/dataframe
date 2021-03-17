package net.mahdilamb.dataframe.utils;


import java.util.Comparator;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;
import java.util.function.IntToDoubleFunction;
import java.util.function.IntToLongFunction;

import static net.mahdilamb.dataframe.utils.Sorts.*;

/**
 * Dual pivot quick sort based on https://www.geeksforgeeks.org/dual-pivot-quicksort/
 */
public final class DualPivotQuickSort {
    private DualPivotQuickSort() {

    }

    /**
     * Sort an array of objects using a custom comparator
     *
     * @param arr the array to sort (sorting occurs in place)
     * @param cmp the comparator
     * @param <T> the type of the data in the array
     */
    public static <T> void sort(T[] arr, Comparator<T> cmp) {
        dualPivotQuickSort(arr, 0, arr.length - 1, cmp);
    }

    /**
     * Sort an array of comparable objects using the default comparator
     *
     * @param arr the array to sort (sorting occurs in place)
     * @param <T> the type of the data in the array
     */
    public static <T extends Comparable<T>> void sort(T[] arr) {
        dualPivotQuickSort(arr, 0, arr.length - 1, Comparator.naturalOrder());
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

    /**
     * Get the indices that would sort the array. The data array is not sorted but the args are
     *
     * @param args the indices used for sort
     * @param arr  the array of data
     * @param <T>  the type of the data.
     * @return the args, sorted using the data
     */
    public static <T extends Comparable<T>> int[] argSort(int[] args, T[] arr) {
        dualPivotQuickSort(args, arr, 0, args.length - 1, Comparator.naturalOrder());
        return args;
    }

    /**
     * Get the indices that would sort the array. The data array is not sorted but the args are
     *
     * @param args the indices used for sort
     * @param arr  the array of data
     * @return the args, sorted using the data
     */
    public static int[] argSort(int[] args, long[] arr) {
        dualPivotQuickSort(args, arr, 0, args.length - 1);
        return args;
    }

    /**
     * Get the indices that would sort the array. The data array is not sorted but the args are
     *
     * @param args the indices used for sort
     * @param arr  the array of data
     * @return the args, sorted using the data
     */
    public static int[] argSort(int[] args, double[] arr) {
        dualPivotQuickSort(args, arr, 0, args.length - 1);
        return args;
    }

    /**
     * Get the indices that would sort the array. The data array is not sorted but the args are
     *
     * @param args the indices used for sort
     * @param arr  the data getter
     * @return the args, sorted using the data
     */
    public static int[] argSort(int[] args, IntToDoubleFunction arr) {
        dualPivotQuickSort(args, arr, 0, args.length - 1);
        return args;
    }

    /**
     * Get the indices that would sort the array. The data array is not sorted but the args are
     *
     * @param args the indices used for sort
     * @param arr  the data getter
     * @return the args, sorted using the data
     */
    public static int[] argSort(int[] args, IntToLongFunction arr) {
        dualPivotQuickSort(args, arr, 0, args.length - 1);
        return args;
    }

    /**
     * Get the indices that would sort the array. The data array is not sorted but the args are
     *
     * @param args the indices used for sort
     * @param arr  the data getter
     * @return the args, sorted using the data
     */
    public static int[] argSort(int[] args, IntPredicate arr) {
        dualPivotQuickSort(args, arr, 0, args.length - 1);
        return args;
    }

    /**
     * Get the indices that would sort the array. The data array is not sorted but the args are
     *
     * @param args the indices used for sort
     * @param arr  the array of data
     * @return the args, sorted using the data
     */
    public static int[] argSort(int[] args, boolean[] arr) {
        dualPivotQuickSort(args, arr, 0, args.length - 1);
        return args;
    }

    public static <T extends Comparable<T>> int[] argSort(IntFunction<T> getter, int size) {
        return argSort(getter, size, Comparator.naturalOrder());
    }

    public static <T extends Comparable<T>> int[] argSort(IntFunction<T> getter, int size, Comparator<T> cmp) {
        final int[] args = intRange(size);
        dualPivotQuickSort(args, getter, 0, size - 1, cmp);
        return args;
    }

    public static <T extends Comparable<T>> int[] argSort(int[] args, T[] arr, boolean ascending) {
        return argSort(args, arr, ascending, Comparator.naturalOrder());
    }

    public static int[] argSort(int[] args, double[] arr, boolean ascending) {
        if (ascending) {
            dualPivotQuickSort(args, arr, 0, args.length - 1);
        } else {
            dualPivotQuickSortReversed(args, arr, 0, args.length - 1);
        }
        return args;
    }

    public static int[] argSort(int[] args, long[] arr, boolean ascending) {
        if (ascending) {
            dualPivotQuickSort(args, arr, 0, args.length - 1);
        } else {
            dualPivotQuickSortReversed(args, arr, 0, args.length - 1);
        }
        return args;
    }

    public static int[] argSort(int[] args, IntToDoubleFunction arr, boolean ascending) {
        if (ascending) {
            dualPivotQuickSort(args, arr, 0, args.length - 1);
        } else {
            dualPivotQuickSortReversed(args, arr, 0, args.length - 1);
        }
        return args;
    }

    public static int[] argSort(int[] args, IntToLongFunction arr, boolean ascending) {
        if (ascending) {
            dualPivotQuickSort(args, arr, 0, args.length - 1);
        } else {
            dualPivotQuickSortReversed(args, arr, 0, args.length - 1);
        }
        return args;
    }

    public static int[] argSort(int[] args, IntPredicate arr, boolean ascending) {
        if (ascending) {
            dualPivotQuickSort(args, arr, 0, args.length - 1);
        } else {
            dualPivotQuickSortReversed(args, arr, 0, args.length - 1);
        }
        return args;
    }

    public static int[] argSort(int[] args, boolean[] arr, boolean ascending) {
        if (ascending) {
            dualPivotQuickSort(args, arr, 0, args.length - 1);
        } else {
            dualPivotQuickSortReversed(args, arr, 0, args.length - 1);
        }
        return args;
    }

    public static <T> int[] argSort(int[] args, T[] arr, boolean ascending, Comparator<T> cmp) {
        if (ascending) {
            dualPivotQuickSort(args, arr, 0, args.length - 1, cmp);
        } else {
            dualPivotQuickSort(args, arr, 0, args.length - 1, cmp.reversed());
        }
        return args;
    }


    private static void dualPivotQuickSort(int[] args, long[] arr, int low, int high) {
        if (low < high) {
            if (arr[args[low]] > arr[args[high]]) {
                swap(args, low, high);
            }
            int pivLow = low + 1;
            int pivHigh = high - 1, k = low + 1;
            long p = arr[args[low]], q = arr[args[high]];

            while (k <= pivHigh) {
                if (arr[args[k]] < p) {
                    swap(args, k, pivLow);
                    ++pivLow;
                } else if (arr[args[k]] >= q) {
                    while (arr[args[pivHigh]] > q && k < pivHigh)
                        --pivHigh;

                    swap(args, k, pivHigh);
                    --pivHigh;

                    if (arr[args[k]] < p) {
                        swap(args, k, pivLow);
                        ++pivLow;
                    }
                }
                ++k;
            }
            --pivLow;
            ++pivHigh;

            swap(args, low, pivLow);
            swap(args, high, pivHigh);
            dualPivotQuickSort(args, arr, low, pivLow - 1);
            dualPivotQuickSort(args, arr, pivLow + 1, pivHigh - 1);
            dualPivotQuickSort(args, arr, pivHigh + 1, high);
        }
    }

    private static void dualPivotQuickSort(int[] args, int[] array, int low, int high) {
        if (low < high) {
            if (array[args[low]] > array[args[high]]) {
                swap(args, low, high);
            }
            int pivLow = low + 1;
            int pivHigh = high - 1, k = low + 1;
            int p = array[args[low]], q = array[args[high]];

            while (k <= pivHigh) {
                if (array[args[k]] < p) {
                    swap(args, k, pivLow);
                    ++pivLow;
                } else if (array[args[k]] >= q) {
                    while (array[args[pivHigh]] > q && k < pivHigh)
                        --pivHigh;

                    swap(args, k, pivHigh);
                    --pivHigh;

                    if (array[args[k]] < p) {
                        swap(args, k, pivLow);
                        ++pivLow;
                    }
                }
                ++k;
            }
            --pivLow;
            ++pivHigh;

            swap(args, low, pivLow);
            swap(args, high, pivHigh);
            dualPivotQuickSort(args, array, low, pivLow - 1);
            dualPivotQuickSort(args, array, pivLow + 1, pivHigh - 1);
            dualPivotQuickSort(args, array, pivHigh + 1, high);
        }
    }

    private static void dualPivotQuickSort(int[] args, double[] arr, int low, int high) {
        if (low < high) {
            if (arr[args[low]] > arr[args[high]]) {
                swap(args, low, high);
            }
            int pivLow = low + 1;
            int pivHigh = high - 1, k = low + 1;
            double p = arr[args[low]], q = arr[args[high]];

            while (k <= pivHigh) {
                if (arr[args[k]] < p) {
                    swap(args, k, pivLow);
                    ++pivLow;
                } else if (arr[args[k]] >= q) {
                    while (arr[args[pivHigh]] > q && k < pivHigh)
                        --pivHigh;

                    swap(args, k, pivHigh);
                    --pivHigh;

                    if (arr[args[k]] < p) {
                        swap(args, k, pivLow);
                        ++pivLow;
                    }
                }
                ++k;
            }
            --pivLow;
            ++pivHigh;

            swap(args, low, pivLow);
            swap(args, high, pivHigh);
            dualPivotQuickSort(args, arr, low, pivLow - 1);
            dualPivotQuickSort(args, arr, pivLow + 1, pivHigh - 1);
            dualPivotQuickSort(args, arr, pivHigh + 1, high);
        }
    }

    private static void dualPivotQuickSort(int[] args, boolean[] arr, int low, int high) {
        if (low < high) {
            if (gt(arr[args[low]], arr[args[high]])) {
                swap(args, low, high);
            }
            int pivLow = low + 1;
            int pivHigh = high - 1, k = low + 1;
            boolean p = arr[args[low]], q = arr[args[high]];

            while (k <= pivHigh) {
                if (lt(arr[args[k]], p)) {
                    swap(args, k, pivLow);
                    ++pivLow;
                } else if (ge(arr[args[k]], q)) {
                    while (gt(arr[args[pivHigh]], q) && k < pivHigh)
                        --pivHigh;

                    swap(args, k, pivHigh);
                    --pivHigh;

                    if (lt(arr[args[k]], p)) {
                        swap(args, k, pivLow);
                        ++pivLow;
                    }
                }
                ++k;
            }
            --pivLow;
            ++pivHigh;

            swap(args, low, pivLow);
            swap(args, high, pivHigh);
            dualPivotQuickSort(args, arr, low, pivLow - 1);
            dualPivotQuickSort(args, arr, pivLow + 1, pivHigh - 1);
            dualPivotQuickSort(args, arr, pivHigh + 1, high);
        }
    }

    private static void dualPivotQuickSort(double[] arr, int low, int high) {
        if (low < high) {
            if (arr[low] > arr[high]) {
                swap(arr, low, high);
            }
            int pivLow = low + 1;
            int pivHigh = high - 1, k = low + 1;
            double p = arr[low], q = arr[high];

            while (k <= pivHigh) {
                if (arr[k] < p) {
                    swap(arr, k, pivLow);
                    ++pivLow;
                } else if (arr[k] >= q) {
                    while (arr[pivHigh] > q && k < pivHigh)
                        --pivHigh;

                    swap(arr, k, pivHigh);
                    --pivHigh;

                    if (arr[k] < p) {
                        swap(arr, k, pivLow);
                        ++pivLow;
                    }
                }
                ++k;
            }
            --pivLow;
            ++pivHigh;

            swap(arr, low, pivLow);
            swap(arr, high, pivHigh);
            dualPivotQuickSort(arr, low, pivLow - 1);
            dualPivotQuickSort(arr, pivLow + 1, pivHigh - 1);
            dualPivotQuickSort(arr, pivHigh + 1, high);
        }
    }

    private static <T> void dualPivotQuickSort(T[] arr, int low, int high, Comparator<T> cmp) {
        if (low < high) {
            if (cmp.compare(arr[low], arr[high]) > 0) {
                swap(arr, low, high);
            }
            int pivLow = low + 1;
            int pivHigh = high - 1, k = low + 1;
            T p = arr[low], q = arr[high];

            while (k <= pivHigh) {
                if (cmp.compare(arr[k], p) < 0) {
                    swap(arr, k, pivLow);
                    pivLow++;
                } else if (cmp.compare(arr[k], q) >= 0) {
                    while (cmp.compare(arr[pivHigh], q) > 0 && k < pivHigh)
                        --pivHigh;

                    swap(arr, k, pivHigh);
                    --pivHigh;

                    if (cmp.compare(arr[k], p) < 0) {
                        swap(arr, k, pivLow);
                        ++pivLow;
                    }
                }
                ++k;
            }
            --pivLow;
            ++pivHigh;

            swap(arr, low, pivLow);
            swap(arr, high, pivHigh);
            dualPivotQuickSort(arr, low, pivLow - 1, cmp);
            dualPivotQuickSort(arr, pivLow + 1, pivHigh - 1, cmp);
            dualPivotQuickSort(arr, pivHigh + 1, high, cmp);
        }
    }

    private static <T> void dualPivotQuickSort(int[] args, T[] arr, int low, int high, Comparator<T> cmp) {
        if (low < high) {
            if (cmp.compare(arr[args[low]], arr[args[high]]) > 0) {
                swap(args, low, high);
            }
            int pivLow = low + 1;
            int pivHigh = high - 1, k = low + 1;
            T p = arr[args[low]], q = arr[args[high]];

            while (k <= pivHigh) {
                if (cmp.compare(arr[args[k]], p) < 0) {
                    swap(args, k, pivLow);
                    ++pivLow;
                } else if (cmp.compare(arr[args[k]], q) >= 0) {
                    while (cmp.compare(arr[args[pivHigh]], q) > 0 && k < pivHigh)
                        --pivHigh;

                    swap(args, k, pivHigh);
                    --pivHigh;

                    if (cmp.compare(arr[args[k]], p) < 0) {
                        swap(args, k, pivLow);
                        ++pivLow;
                    }
                }
                ++k;
            }
            --pivLow;
            ++pivHigh;

            swap(args, low, pivLow);
            swap(args, high, pivHigh);
            dualPivotQuickSort(args, arr, low, pivLow - 1, cmp);
            dualPivotQuickSort(args, arr, pivLow + 1, pivHigh - 1, cmp);
            dualPivotQuickSort(args, arr, pivHigh + 1, high, cmp);
        }
    }

    private static <T> void dualPivotQuickSort(int[] args, IntFunction<T> getter, int low, int high, Comparator<T> cmp) {
        if (low < high) {
            if (cmp.compare(getter.apply(args[low]), getter.apply(args[high])) > 0) {
                swap(args, low, high);
            }
            int pivLow = low + 1;
            int pivHigh = high - 1, k = low + 1;
            T p = getter.apply(args[low]), q = getter.apply(args[high]);

            while (k <= pivHigh) {
                if (cmp.compare(getter.apply(args[k]), p) < 0) {
                    swap(args, k, pivLow);
                    ++pivLow;
                } else if (cmp.compare(getter.apply(args[k]), q) >= 0) {
                    while (cmp.compare(getter.apply(args[pivHigh]), q) > 0 && k < pivHigh)
                        --pivHigh;

                    swap(args, k, pivHigh);
                    --pivHigh;

                    if (cmp.compare(getter.apply(args[k]), p) < 0) {
                        swap(args, k, pivLow);
                        ++pivLow;
                    }
                }
                ++k;
            }
            --pivLow;
            ++pivHigh;

            swap(args, low, pivLow);
            swap(args, high, pivHigh);
            dualPivotQuickSort(args, getter, low, pivLow - 1, cmp);
            dualPivotQuickSort(args, getter, pivLow + 1, pivHigh - 1, cmp);
            dualPivotQuickSort(args, getter, pivHigh + 1, high, cmp);
        }
    }

    private static void dualPivotQuickSort(int[] args, IntToDoubleFunction getter, int low, int high) {
        if (low < high) {
            if (getter.applyAsDouble(args[low]) > getter.applyAsDouble(args[high])) {
                swap(args, low, high);
            }
            int pivLow = low + 1;
            int pivHigh = high - 1, k = low + 1;
            double p = getter.applyAsDouble(args[low]), q = getter.applyAsDouble(args[high]);

            while (k <= pivHigh) {
                if (getter.applyAsDouble(args[k]) < p) {
                    swap(args, k, pivLow);
                    ++pivLow;
                } else if (getter.applyAsDouble(args[k]) >= q) {
                    while (getter.applyAsDouble(args[pivHigh]) > q && k < pivHigh)
                        --pivHigh;

                    swap(args, k, pivHigh);
                    --pivHigh;

                    if (getter.applyAsDouble(args[k]) < p) {
                        swap(args, k, pivLow);
                        ++pivLow;
                    }
                }
                ++k;
            }
            --pivLow;
            ++pivHigh;

            swap(args, low, pivLow);
            swap(args, high, pivHigh);
            dualPivotQuickSort(args, getter, low, pivLow - 1);
            dualPivotQuickSort(args, getter, pivLow + 1, pivHigh - 1);
            dualPivotQuickSort(args, getter, pivHigh + 1, high);
        }
    }

    private static void dualPivotQuickSort(int[] args, IntToLongFunction getter, int low, int high) {
        if (low < high) {
            if (getter.applyAsLong(args[low]) > getter.applyAsLong(args[high])) {
                swap(args, low, high);
            }
            int pivLow = low + 1;
            int pivHigh = high - 1, k = low + 1;
            double p = getter.applyAsLong(args[low]), q = getter.applyAsLong(args[high]);

            while (k <= pivHigh) {
                if (getter.applyAsLong(args[k]) < p) {
                    swap(args, k, pivLow);
                    ++pivLow;
                } else if (getter.applyAsLong(args[k]) >= q) {
                    while (getter.applyAsLong(args[pivHigh]) > q && k < pivHigh)
                        --pivHigh;

                    swap(args, k, pivHigh);
                    --pivHigh;

                    if (getter.applyAsLong(args[k]) < p) {
                        swap(args, k, pivLow);
                        ++pivLow;
                    }
                }
                ++k;
            }
            --pivLow;
            ++pivHigh;

            swap(args, low, pivLow);
            swap(args, high, pivHigh);
            dualPivotQuickSort(args, getter, low, pivLow - 1);
            dualPivotQuickSort(args, getter, pivLow + 1, pivHigh - 1);
            dualPivotQuickSort(args, getter, pivHigh + 1, high);
        }
    }

    private static void dualPivotQuickSort(int[] args, IntPredicate getter, int low, int high) {
        if (low < high) {
            if (gt(getter.test(args[low]), getter.test(args[high]))) {
                swap(args, low, high);
            }
            int pivLow = low + 1;
            int pivHigh = high - 1, k = low + 1;
            boolean p = getter.test(args[low]), q = getter.test(args[high]);

            while (k <= pivHigh) {
                if (lt(getter.test(args[k]), p)) {
                    swap(args, k, pivLow);
                    ++pivLow;
                } else if (ge(getter.test(args[k]), q)) {
                    while (gt(getter.test(args[pivHigh]), q) && k < pivHigh)
                        --pivHigh;

                    swap(args, k, pivHigh);
                    --pivHigh;

                    if (lt(getter.test(args[k]), p)) {
                        swap(args, k, pivLow);
                        ++pivLow;
                    }
                }
                ++k;
            }
            --pivLow;
            ++pivHigh;

            swap(args, low, pivLow);
            swap(args, high, pivHigh);
            dualPivotQuickSort(args, getter, low, pivLow - 1);
            dualPivotQuickSort(args, getter, pivLow + 1, pivHigh - 1);
            dualPivotQuickSort(args, getter, pivHigh + 1, high);
        }
    }

    private static void dualPivotQuickSortReversed(int[] args, double[] arr, int low, int high) {
        if (low < high) {
            if (arr[args[high]] > arr[args[low]]) {
                swap(args, low, high);
            }
            int pivLow = low + 1;
            int pivHigh = high - 1, k = low + 1;
            double p = arr[args[low]], q = arr[args[high]];

            while (k <= pivHigh) {
                if (p < arr[args[k]]) {
                    swap(args, k, pivLow);
                    ++pivLow;
                } else if (q >= arr[args[k]]) {
                    while (q > arr[args[pivHigh]] && k < pivHigh)
                        --pivHigh;

                    swap(args, k, pivHigh);
                    --pivHigh;

                    if (p < arr[args[k]]) {
                        swap(args, k, pivLow);
                        ++pivLow;
                    }
                }
                ++k;
            }
            --pivLow;
            ++pivHigh;

            swap(args, low, pivLow);
            swap(args, high, pivHigh);
            dualPivotQuickSortReversed(args, arr, low, pivLow - 1);
            dualPivotQuickSortReversed(args, arr, pivLow + 1, pivHigh - 1);
            dualPivotQuickSortReversed(args, arr, pivHigh + 1, high);
        }
    }

    private static void dualPivotQuickSortReversed(int[] args, long[] arr, int low, int high) {
        if (low < high) {
            if (arr[args[high]] > arr[args[low]]) {
                swap(args, low, high);
            }
            int pivLow = low + 1;
            int pivHigh = high - 1, k = low + 1;
            long p = arr[args[low]], q = arr[args[high]];

            while (k <= pivHigh) {
                if (p < arr[args[k]]) {
                    swap(args, k, pivLow);
                    ++pivLow;
                } else if (q >= arr[args[k]]) {
                    while (q > arr[args[pivHigh]] && k < pivHigh)
                        --pivHigh;

                    swap(args, k, pivHigh);
                    --pivHigh;

                    if (p < arr[args[k]]) {
                        swap(args, k, pivLow);
                        ++pivLow;
                    }
                }
                ++k;
            }
            --pivLow;
            ++pivHigh;

            swap(args, low, pivLow);
            swap(args, high, pivHigh);
            dualPivotQuickSortReversed(args, arr, low, pivLow - 1);
            dualPivotQuickSortReversed(args, arr, pivLow + 1, pivHigh - 1);
            dualPivotQuickSortReversed(args, arr, pivHigh + 1, high);
        }
    }

    private static void dualPivotQuickSortReversed(int[] args, boolean[] arr, int low, int high) {
        if (low < high) {
            if (gt(arr[args[high]], arr[args[low]])) {
                swap(args, low, high);
            }
            int pivLow = low + 1;
            int pivHigh = high - 1, k = low + 1;
            boolean p = arr[args[low]], q = arr[args[high]];

            while (k <= pivHigh) {
                if (lt(p, arr[args[k]])) {
                    swap(args, k, pivLow);
                    ++pivLow;
                } else if (ge(q, arr[args[k]])) {
                    while (gt(q, arr[args[pivHigh]]) && k < pivHigh)
                        --pivHigh;

                    swap(args, k, pivHigh);
                    --pivHigh;

                    if (lt(p, arr[args[k]])) {
                        swap(args, k, pivLow);
                        ++pivLow;
                    }
                }
                ++k;
            }
            --pivLow;
            ++pivHigh;

            swap(args, low, pivLow);
            swap(args, high, pivHigh);
            dualPivotQuickSortReversed(args, arr, low, pivLow - 1);
            dualPivotQuickSortReversed(args, arr, pivLow + 1, pivHigh - 1);
            dualPivotQuickSortReversed(args, arr, pivHigh + 1, high);
        }
    }

    private static void dualPivotQuickSortReversed(int[] args, IntToDoubleFunction getter, int low, int high) {
        if (low < high) {
            if (getter.applyAsDouble(args[high]) > getter.applyAsDouble(args[low])) {
                swap(args, low, high);
            }
            int pivLow = low + 1;
            int pivHigh = high - 1, k = low + 1;
            double p = getter.applyAsDouble(args[low]), q = getter.applyAsDouble(args[high]);

            while (k <= pivHigh) {
                if (p < getter.applyAsDouble(args[k])) {
                    swap(args, k, pivLow);
                    ++pivLow;
                } else if (q >= getter.applyAsDouble(args[k])) {
                    while (q > getter.applyAsDouble(args[pivHigh]) && k < pivHigh)
                        --pivHigh;

                    swap(args, k, pivHigh);
                    --pivHigh;

                    if (p < getter.applyAsDouble(args[k])) {
                        swap(args, k, pivLow);
                        ++pivLow;
                    }
                }
                ++k;
            }
            --pivLow;
            ++pivHigh;

            swap(args, low, pivLow);
            swap(args, high, pivHigh);
            dualPivotQuickSortReversed(args, getter, low, pivLow - 1);
            dualPivotQuickSortReversed(args, getter, pivLow + 1, pivHigh - 1);
            dualPivotQuickSortReversed(args, getter, pivHigh + 1, high);
        }
    }

    private static void dualPivotQuickSortReversed(int[] args, IntToLongFunction getter, int low, int high) {
        if (low < high) {
            if (getter.applyAsLong(args[high]) > getter.applyAsLong(args[low])) {
                swap(args, low, high);
            }
            int pivLow = low + 1;
            int pivHigh = high - 1, k = low + 1;
            double p = getter.applyAsLong(args[low]), q = getter.applyAsLong(args[high]);

            while (k <= pivHigh) {
                if (p < getter.applyAsLong(args[k])) {
                    swap(args, k, pivLow);
                    ++pivLow;
                } else if (q >= getter.applyAsLong(args[k])) {
                    while (q > getter.applyAsLong(args[pivHigh]) && k < pivHigh)
                        --pivHigh;

                    swap(args, k, pivHigh);
                    --pivHigh;

                    if (p < getter.applyAsLong(args[k])) {
                        swap(args, k, pivLow);
                        ++pivLow;
                    }
                }
                ++k;
            }
            --pivLow;
            ++pivHigh;

            swap(args, low, pivLow);
            swap(args, high, pivHigh);
            dualPivotQuickSortReversed(args, getter, low, pivLow - 1);
            dualPivotQuickSortReversed(args, getter, pivLow + 1, pivHigh - 1);
            dualPivotQuickSortReversed(args, getter, pivHigh + 1, high);
        }
    }

    private static void dualPivotQuickSortReversed(int[] args, IntPredicate getter, int low, int high) {
        if (low < high) {
            if (gt(getter.test(args[high]), getter.test(args[low]))) {
                swap(args, low, high);
            }
            int pivLow = low + 1;
            int pivHigh = high - 1, k = low + 1;
            boolean p = getter.test(args[low]), q = getter.test(args[high]);

            while (k <= pivHigh) {
                if (lt(p, getter.test(args[k]))) {
                    swap(args, k, pivLow);
                    ++pivLow;
                } else if (ge(q, getter.test(args[k]))) {
                    while (gt(q, getter.test(args[pivHigh])) && k < pivHigh)
                        --pivHigh;

                    swap(args, k, pivHigh);
                    --pivHigh;

                    if (lt(p, getter.test(args[k]))) {
                        swap(args, k, pivLow);
                        ++pivLow;
                    }
                }
                ++k;
            }
            --pivLow;
            ++pivHigh;

            swap(args, low, pivLow);
            swap(args, high, pivHigh);
            dualPivotQuickSortReversed(args, getter, low, pivLow - 1);
            dualPivotQuickSortReversed(args, getter, pivLow + 1, pivHigh - 1);
            dualPivotQuickSortReversed(args, getter, pivHigh + 1, high);
        }
    }

}
