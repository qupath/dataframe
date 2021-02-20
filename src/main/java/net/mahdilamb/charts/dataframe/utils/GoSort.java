package net.mahdilamb.charts.dataframe.utils;

import java.util.Comparator;
import java.util.List;
import java.util.function.IntPredicate;
import java.util.function.IntToDoubleFunction;
import java.util.function.IntToLongFunction;
import java.util.function.IntUnaryOperator;

import static net.mahdilamb.charts.dataframe.utils.Sorts.gt;
import static net.mahdilamb.charts.dataframe.utils.Sorts.lt;

/**
 * Java port of default sort algorithm in GO(lang). Combination of insertion/heap/quick sort
 * See <a href="https://golang.org/src/sort/sort.go">go source for more information</a>
 */
public final class GoSort {
    private GoSort() {

    }

    /**
     * Return the indices that would sort the array
     *
     * @param args      the original indices
     * @param numArgs   the number of indices to sort
     * @param data      the data to sort
     * @param ascending whether to sort ascending or descending
     */
    public static void argSort(int[] args, int numArgs, double[] data, boolean ascending) {
        if (ascending) {
            quickSort(args, data, GoSort::lessThan, numArgs);
        } else {
            quickSort(args, data, GoSort::greaterThan, numArgs);
        }
    }

    /**
     * Return the indices that would sort the array
     *
     * @param args      the original indices
     * @param numArgs   the number of indices to sort
     * @param data      the data to sort
     * @param ascending whether to sort ascending or descending
     */
    public static void argSort(int[] args, int numArgs, long[] data, boolean ascending) {
        if (ascending) {
            quickSort(args, data, GoSort::lessThan, numArgs);
        } else {
            quickSort(args, data, GoSort::greaterThan, numArgs);
        }
    }

    public static void argSort(int[] args, int numArgs, IntToDoubleFunction getter, boolean ascending) {
        if (ascending) {
            quickSort(args, null, LessThan.fromDoubleGetter(getter), numArgs);
        } else {
            quickSort(args, null, LessThan.fromDoubleGetter(getter).reversed(), numArgs);
        }
    }

    public static void argSort(int[] args, int numArgs, IntToLongFunction getter, boolean ascending) {
        if (ascending) {
            quickSort(args, null, LessThan.fromLongGetter(getter), numArgs);
        } else {
            quickSort(args, null, LessThan.fromLongGetter(getter).reversed(), numArgs);
        }
    }

    public static void argSort(int[] args, int numArgs, IntPredicate getter, boolean ascending) {
        if (ascending) {
            quickSort(args, null, LessThan.fromBooleanGetter(getter), numArgs);
        } else {
            quickSort(args, null, LessThan.fromBooleanGetter(getter).reversed(), numArgs);
        }
    }

    /**
     * Return the indices that would sort the array
     *
     * @param args      the original indices
     * @param numArgs   the number of indices to sort
     * @param data      the data to sort
     * @param ascending whether to sort ascending or descending
     */
    public static void argSort(int[] args, int numArgs, int[] data, boolean ascending) {
        if (ascending) {
            quickSort(args, data, GoSort::lessThan, numArgs);
        } else {
            quickSort(args, data, GoSort::greaterThan, numArgs);
        }
    }

    /**
     * Return the indices that would sort the array
     *
     * @param args      the original indices
     * @param numArgs   the number of indices to sort
     * @param data      the data to sort
     * @param ascending whether to sort ascending or descending
     */
    public static void argSort(int[] args, int numArgs, boolean[] data, boolean ascending) {
        if (ascending) {
            quickSort(args, data, GoSort::lessThan, numArgs);
        } else {
            quickSort(args, data, GoSort::greaterThan, numArgs);
        }
    }

    /**
     * Return the indices that would sort the array
     *
     * @param args      the original indices
     * @param numArgs   the number of indices to sort
     * @param data      the data to sort
     * @param cmp       the comparator
     * @param ascending whether to sort ascending or descending
     * @param <T>       the type of the data
     */
    public static <T> void argSort(int[] args, int numArgs, T[] data, Comparator<T> cmp, boolean ascending) {
        if (ascending) {
            quickSort(args, data, LessThan.fromArray(cmp), numArgs);
        } else {
            quickSort(args, data, LessThan.fromArray(cmp.reversed()), numArgs);
        }
    }

    /**
     * Return the indices that would sort the list
     *
     * @param args      the original indices
     * @param numArgs   the number of indices to sort
     * @param data      the data to sort
     * @param cmp       the comparator
     * @param ascending whether to sort ascending or descending
     * @param <T>       the type of the data
     */
    public static <T> void argSort(int[] args, int numArgs, List<T> data, Comparator<T> cmp, boolean ascending) {
        if (ascending) {
            quickSort(args, data, LessThan.fromList(cmp), numArgs);
        } else {
            quickSort(args, data, LessThan.fromList(cmp.reversed()), numArgs);
        }
    }

    /**
     * Return the indices that would sort the array
     *
     * @param args      the original indices
     * @param numArgs   the number of indices to sort
     * @param data      the data to sort
     * @param ascending whether to sort ascending or descending
     * @param <T>       the type of the data
     */
    public static <T extends Comparable<T>> void argSort(int[] args, int numArgs, T[] data, boolean ascending) {
        argSort(args, numArgs, data, Comparator.naturalOrder(), ascending);
    }

    /**
     * Trait for comparing elements in an array
     *
     * @param <T> the type of the index store
     */
    @FunctionalInterface
    public interface LessThan<T> {
        /**
         * @param data       the indexed data
         * @param rightIndex the right index
         * @param leftIndex  the left index
         * @return whether the element at the left index is lower than the element at the right index
         */
        boolean isLessThan(T data, int rightIndex, int leftIndex);

        default LessThan<T> reversed() {
            return (d, l, r) -> !isLessThan(d, l, r);
        }

        /**
         * @param cmp the comparator
         * @param <T> the type of the data in the array
         * @return a less for an array from a comparator
         */
        static <T> LessThan<T[]> fromArray(Comparator<T> cmp) {
            return (d, l, r) -> cmp.compare(d[l], d[r]) < 0;
        }

        /**
         * @param cmp the comparator
         * @param <T> the type of the data in the list
         * @return a less for an list from a comparator
         */
        static <T> LessThan<List<T>> fromList(Comparator<T> cmp) {
            return (d, l, r) -> cmp.compare(d.get(l), d.get(r)) < 0;
        }

        static LessThan<?> fromDoubleGetter(IntToDoubleFunction func) {
            return (d, l, r) -> func.applyAsDouble(l) < func.applyAsDouble(r);
        }

        static LessThan<?> fromLongGetter(IntToLongFunction func) {
            return (d, l, r) -> func.applyAsLong(l) < func.applyAsLong(r);
        }

        static LessThan<?> fromIntGetter(IntUnaryOperator func) {
            return (d, l, r) -> func.applyAsInt(l) < func.applyAsInt(r);
        }

        static LessThan<?> fromBooleanGetter(IntPredicate func) {
            return (d, l, r) -> lt(func.test(l), func.test(r));
        }


    }

    static <T> void insertionSort(int[] args, T data, LessThan<T> comparator, int a, int b) {
        for (int i = a + 1; i < b; ++i) {
            for (int j = i; j > a && comparator.isLessThan(data, args[j], args[j - 1]); --j) {
                Sorts.swap(args, j, j - 1);
            }
        }
    }

    static void insertionSort(double[] data, int a, int b) {
        for (int i = a + 1; i < b; ++i) {
            for (int j = i; j > a && data[j] < data[j - 1]; --j) {
                Sorts.swap(data, j, j - 1);
            }
        }
    }

    static <T> void siftDown(int[] args, T data, LessThan<T> comparator, int lo, int hi, int first) {
        int root = lo;
        for (; ; ) {
            int child = 2 * root + 1;
            if (child >= hi) {
                break;
            }
            if (child + 1 < hi && comparator.isLessThan(data, args[first + child], args[first + child + 1])) {
                child++;
            }
            if (!comparator.isLessThan(data, args[first + root], args[first + child])) {
                return;
            }
            Sorts.swap(args, first + root, first + child);
            root = child;
        }
    }

    static void siftDown(double[] data, int lo, int hi, int first) {
        int root = lo;
        for (; ; ) {
            int child = 2 * root + 1;
            if (child >= hi) {
                break;
            }
            if (child + 1 < hi && data[first + child] < data[first + child + 1]) {
                child++;
            }
            if (data[first + root] >= data[first + child]) {
                return;
            }
            Sorts.swap(data, first + root, first + child);
            root = child;
        }
    }

    static <T> void heapSort(int[] args, T data, LessThan<T> comparator, int a, int b) {
        int first = a,
                lo = 0,
                hi = b - a;
        // Build heap with greatest element at top.
        for (int i = (hi - 1) / 2; i >= 0; i--) {
            siftDown(args, data, comparator, i, hi, first);
        }

        // Pop elements, largest first, into end of data.
        for (int i = (hi - 1); i >= 0; i--) {
            Sorts.swap(args, first, first + i);
            siftDown(args, data, comparator, lo, i, first);
        }
    }

    static void heapSort(double[] data, int a, int b) {
        int first = a,
                lo = 0,
                hi = b - a;
        // Build heap with greatest element at top.
        for (int i = (hi - 1) / 2; i >= 0; i--) {
            siftDown(data, i, hi, first);
        }

        // Pop elements, largest first, into end of data.
        for (int i = (hi - 1); i >= 0; i--) {
            Sorts.swap(data, first, first + i);
            siftDown(data, lo, i, first);
        }
    }

    static void medianOfThree(double[] data, int m1, int m0, int m2) {
        // sort 3 elements
        if (data[m1] < data[m0]) {
            Sorts.swap(data, m1, m0);
        }
        // data[m0] <= data[m1]
        if (data[m2] < data[m1]) {
            Sorts.swap(data, m2, m1);
            // data[m0] <= data[m2] && data[m1] < data[m2]
            if (data[m1] < data[m0]) {
                Sorts.swap(data, m1, m0);
            }
        }
        // now data[m0] <= data[m1] <= data[m2]
    }

    static <T> void medianOfThree(int[] args, T data, LessThan<T> lt, int m1, int m0, int m2) {
        // sort 3 elements
        if (lt.isLessThan(data, args[m1], args[m0])) {
            Sorts.swap(args, m1, m0);
        }
        // data[m0] <= data[m1]
        if (lt.isLessThan(data, args[m2], args[m1])) {
            Sorts.swap(args, m2, m1);
            // data[m0] <= data[m2] && data[m1] < data[m2]
            if (lt.isLessThan(data, args[m1], args[m0])) {
                Sorts.swap(args, m1, m0);
            }
        }
        // now data[m0] <= data[m1] <= data[m2]
    }

    static void swapRange(double[] data, int a, int b, int n) {
        for (int i = 0; i < n; ++i) {
            Sorts.swap(data, a + i, b + i);
        }
    }

    static void swapRange(int[] data, int a, int b, int n) {
        for (int i = 0; i < n; ++i) {
            Sorts.swap(data, a + i, b + i);
        }
    }

    static void quickSort(double[] data, int a, int b, int maxDepth) {
        for (; b - a > 12; ) { // Use ShellSort for slices <= 12 elements
            if (maxDepth == 0) {
                heapSort(data, a, b);
                return;
            }
            maxDepth--;
            int mlo, mhi;
            //doPivot
            {
                int m = ((a + b) >> 1); // Written like this to avoid integer overflow.
                if (b - a > 40) {
                    // Tukey's ``Ninther,'' median of three medians of three.
                    int s = (b - a) / 8;
                    medianOfThree(data, a, a + s, a + 2 * s);
                    medianOfThree(data, m, m - s, m + s);
                    medianOfThree(data, b - 1, b - 1 - s, b - 1 - 2 * s);
                }
                medianOfThree(data, a, m, b - 1);

                // Invariants are:
                //	data[a] = pivot (set up by ChoosePivot)
                //	data[a < i < _a] < pivot
                //	data[_a <= i < _b] <= pivot
                //	data[_b <= i < c] unexamined
                //	data[c <= i < b-1] > pivot
                //	data[b-1] >= pivot
                int pivot = a;
                int _a = a + 1, c = b - 1;

                for (; _a < c && data[_a] < data[pivot]; _a++) {
                }
                int _b = _a;
                for (; ; ) {
                    for (; _b < c && data[pivot] >= data[_b]; _b++) { // data[_b] <= pivot
                    }
                    for (; _b < c && data[pivot] < data[c - 1]; c--) { // data[c-1] > pivot
                    }
                    if (_b >= c) {
                        break;
                    }
                    // data[_b] > pivot; data[c-1] <= pivot
                    Sorts.swap(data, _b, c - 1);
                    _b++;
                    c--;
                }
                // If b-c<3 then there are duplicates (by property of median of nine).
                // Let's be _a bit more conservative, and set border to 5.
                boolean protect = b - c < 5;
                if (!protect && b - c < (b - a) / 4) {
                    // Lets test some points for equality to pivot
                    int dups = 0;
                    if (data[pivot] >= data[_b - 1]) { // data[b-1] = pivot
                        Sorts.swap(data, c, b - 1);
                        c++;
                        dups++;
                    }
                    if (data[_b - 1] >= data[pivot]) { // data[_b-1] = pivot
                        _b--;
                        dups++;
                    }
                    // m-a = (b-a)/2 > 6
                    // _b-a > (b-a)*3/4-1 > 8
                    // ==> m < _b ==> data[m] <= pivot
                    if (data[m] >= data[pivot]) { // data[m] = pivot
                        Sorts.swap(data, m, _b - 1);
                        _b--;
                        dups++;
                    }
                    // if at least 2 points are equal to pivot, assume skewed distribution
                    protect = dups > 1;
                }
                if (protect) {
                    // Protect against _a lot of duplicates
                    // Add invariant:
                    //	data[_a <= i < _b] unexamined
                    //	data[_b <= i < c] = pivot
                    for (; ; ) {
                        for (; _a < _b && data[_b - 1] >= data[pivot]; _b--) { // data[_b] == pivot
                        }
                        for (; _a < _b && data[_a] < data[pivot]; _a++) { // data[_a] < pivot
                        }
                        if (_a >= _b) {
                            break;
                        }
                        // data[_a] == pivot; data[_b-1] < pivot
                        Sorts.swap(data, _a, _b - 1);
                        _a++;
                        _b--;
                    }
                }
                // Swap pivot into middle
                Sorts.swap(data, pivot, _b - 1);
                mlo = _b - 1;
                mhi = c;

            }
            // Avoiding recursion on the larger subproblem guarantees
            // a stack depth of at most lg(b-a).
            if (mlo - a < b - mhi) {
                quickSort(data, a, mlo, maxDepth);
                a = mhi; // i.e., quickSort(data, mhi, b)
            } else {
                quickSort(data, mhi, b, maxDepth);
                b = mlo; // i.e., quickSort(data, a, mlo)
            }
        }
        if (b - a > 1) {
            // Do ShellSort pass with gap 6
            // It could be written in this simplified form cause b-a <= 12
            for (int i = a + 6; i < b; ++i) {
                if (data[i] < data[i - 6]) {
                    Sorts.swap(data, i, i - 6);
                }
            }
            insertionSort(data, a, b);
        }
    }

    static <T> void quickSort(int[] args, T data, LessThan<T> comparator, int a, int b, int maxDepth) {
        for (; b - a > 12; ) { // Use ShellSort for slices <= 12 elements
            if (maxDepth == 0) {
                heapSort(args, data, comparator, a, b);
                return;
            }
            maxDepth--;
            int mlo, mhi;
            //doPivot
            {
                int m = ((a + b) >> 1); // Written like this to avoid integer overflow.
                if (b - a > 40) {
                    // Tukey's ``Ninther,'' median of three medians of three.
                    int s = (b - a) / 8;
                    medianOfThree(args, data, comparator, a, a + s, a + 2 * s);
                    medianOfThree(args, data, comparator, m, m - s, m + s);
                    medianOfThree(args, data, comparator, b - 1, b - 1 - s, b - 1 - 2 * s);
                }
                medianOfThree(args, data, comparator, a, m, b - 1);

                // Invariants are:
                //	data[a] = pivot (set up by ChoosePivot)
                //	data[a < i < _a] < pivot
                //	data[_a <= i < _b] <= pivot
                //	data[_b <= i < c] unexamined
                //	data[c <= i < b-1] > pivot
                //	data[b-1] >= pivot
                int pivot = a;
                int _a = a + 1, c = b - 1;

                for (; _a < c && comparator.isLessThan(data, args[_a], args[pivot]); _a++) {
                }
                int _b = _a;
                for (; ; ) {
                    for (; _b < c && !(comparator.isLessThan(data, args[pivot], args[_b])); _b++) { // data[_b] <= pivot
                    }
                    for (; _b < c && comparator.isLessThan(data, args[pivot], args[c - 1]); c--) { // data[c-1] > pivot
                    }
                    if (_b >= c) {
                        break;
                    }
                    // data[_b] > pivot; data[c-1] <= pivot
                    Sorts.swap(args, _b, c - 1);
                    _b++;
                    c--;
                }
                // If b-c<3 then there are duplicates (by property of median of nine).
                // Let's be _a bit more conservative, and set border to 5.
                boolean protect = b - c < 5;
                if (!protect && b - c < (b - a) / 4) {
                    // Lets test some points for equality to pivot
                    int dups = 0;
                    if (!(comparator.isLessThan(data, args[pivot], args[_b - 1]))) { // data[b-1] = pivot
                        Sorts.swap(args, c, b - 1);
                        c++;
                        dups++;
                    }
                    if (!(comparator.isLessThan(data, args[_b - 1], args[pivot]))) { // data[_b-1] = pivot
                        _b--;
                        dups++;
                    }
                    // m-a = (b-a)/2 > 6
                    // _b-a > (b-a)*3/4-1 > 8
                    // ==> m < _b ==> data[m] <= pivot
                    if (!(comparator.isLessThan(data, args[m], args[pivot]))) { // data[m] = pivot
                        Sorts.swap(args, m, _b - 1);
                        _b--;
                        dups++;
                    }
                    // if at least 2 points are equal to pivot, assume skewed distribution
                    protect = dups > 1;
                }
                if (protect) {
                    // Protect against _a lot of duplicates
                    // Add invariant:
                    //	data[_a <= i < _b] unexamined
                    //	data[_b <= i < c] = pivot
                    for (; ; ) {
                        for (; _a < _b && !comparator.isLessThan(data, args[_b - 1], args[pivot]); _b--) { // data[_b] == pivot
                        }
                        for (; _a < _b && comparator.isLessThan(data, args[_a], args[pivot]); _a++) { // data[_a] < pivot
                        }
                        if (_a >= _b) {
                            break;
                        }
                        // data[_a] == pivot; data[_b-1] < pivot
                        Sorts.swap(args, _a, _b - 1);
                        _a++;
                        _b--;
                    }
                }
                // Swap pivot into middle
                Sorts.swap(args, pivot, _b - 1);
                mlo = _b - 1;
                mhi = c;

            }
            // Avoiding recursion on the larger subproblem guarantees
            // a stack depth of at most lg(b-a).
            if (mlo - a < b - mhi) {
                quickSort(args, data, comparator, a, mlo, maxDepth);
                a = mhi; // i.e., quickSort(data, mhi, b)
            } else {
                quickSort(args, data, comparator, mhi, b, maxDepth);
                b = mlo; // i.e., quickSort(data, a, mlo)
            }
        }
        if (b - a > 1) {
            // Do ShellSort pass with gap 6
            // It could be written in this simplified form cause b-a <= 12
            for (int i = a + 6; i < b; ++i) {
                if (comparator.isLessThan(data, args[i], args[i - 6])) {
                    Sorts.swap(args, i, i - 6);
                }
            }
            insertionSort(args, data, comparator, a, b);
        }
    }

    static void quickSort(double[] data, int a, int b) {
        quickSort(data, a, b, maxDepth(data.length));
    }

    // maxDepth returns a threshold at which quicksort should switch
    // to heapsort. It returns 2*ceil(lg(n+1)).
    static int maxDepth(int n) {
        int depth = 0;
        for (int i = n; i > 0; i >>= 1) {
            depth++;
        }
        return depth * 2;
    }

    static <T> void quickSort(int[] args, T data, LessThan<T> comparator, int size) {
        quickSort(args, data, comparator, 0, size, maxDepth(size));
    }

    static boolean lessThan(double[] data, int lhs, int rhs) {
        return data[lhs] < data[rhs];
    }

    static boolean greaterThan(double[] data, int lhs, int rhs) {
        return data[lhs] > data[rhs];
    }

    static boolean lessThan(int[] data, int lhs, int rhs) {
        return data[lhs] < data[rhs];
    }

    static boolean greaterThan(int[] data, int lhs, int rhs) {
        return data[lhs] > data[rhs];
    }

    static boolean lessThan(long[] data, int lhs, int rhs) {
        return data[lhs] < data[rhs];
    }

    static boolean greaterThan(long[] data, int lhs, int rhs) {
        return data[lhs] > data[rhs];
    }

    static boolean lessThan(boolean[] data, int lhs, int rhs) {
        return lt(data[lhs], data[rhs]);
    }

    static boolean greaterThan(boolean[] data, int lhs, int rhs) {
        return gt(data[lhs], data[rhs]);
    }

}
