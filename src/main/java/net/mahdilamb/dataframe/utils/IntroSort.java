package net.mahdilamb.dataframe.utils;

import java.util.Comparator;
import java.util.List;
import java.util.function.IntPredicate;
import java.util.function.IntToDoubleFunction;
import java.util.function.IntToLongFunction;
import java.util.function.IntUnaryOperator;

/**
 * Java port of default sort algorithm in GO(lang). Combination of insertion/heap/quick sort
 * See <a href="https://golang.org/src/sort/sort.go">go source for more information</a>
 * The original implementation is copyright to the GO authors:
 * <p>
 * Copyright 2009 The Go Authors. All rights reserved.
 * Use of this source code is governed by a BSD-style
 * license that can be found in the <a href="https://golang.org/LICENSE">LICENSE</a> file (as below).
 */
/*
Copyright (c) 2009 The Go Authors. All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are
met:

   * Redistributions of source code must retain the above copyright
notice, this list of conditions and the following disclaimer.
   * Redistributions in binary form must reproduce the above
copyright notice, this list of conditions and the following disclaimer
in the documentation and/or other materials provided with the
distribution.
   * Neither the name of Google Inc. nor the names of its
contributors may be used to endorse or promote products derived from
this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
public final class IntroSort {
    private IntroSort() {

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
            quickSort(args, data, IntroSort::lessThan, numArgs);
        } else {
            quickSort(args, data, IntroSort::greaterThan, numArgs);
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
            quickSort(args, data, IntroSort::lessThan, numArgs);
        } else {
            quickSort(args, data, IntroSort::greaterThan, numArgs);
        }
    }

    public static void argSort(int[] args, int numArgs, IntToDoubleFunction getter, boolean ascending) {
        if (ascending) {
            quickSort(args, null, LessThan.fromDoubleGetter(getter), numArgs);
        } else {
            quickSort(args, null, LessThan.fromDoubleGetterReversed(getter), numArgs);
        }
    }

    public static void argSort(int[] args, int numArgs, IntToLongFunction getter, boolean ascending) {
        if (ascending) {
            quickSort(args, null, LessThan.fromLongGetter(getter), numArgs);
        } else {
            quickSort(args, null, LessThan.fromLongGetterReversed(getter), numArgs);
        }
    }

    public static void argSort(int[] args, int numArgs, IntPredicate getter, boolean ascending) {
        if (ascending) {
            quickSort(args, null, LessThan.fromBooleanGetter(getter), numArgs);
        } else {
            quickSort(args, null, LessThan.fromBooleanGetterReversed(getter), numArgs);
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
            quickSort(args, data, IntroSort::lessThan, numArgs);
        } else {
            quickSort(args, data, IntroSort::greaterThan, numArgs);
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
            quickSort(args, data, IntroSort::lessThan, numArgs);
        } else {
            quickSort(args, data, IntroSort::greaterThan, numArgs);
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
    private interface LessThan<T> {
        /**
         * @param data       the indexed data
         * @param rightIndex the right index
         * @param leftIndex  the left index
         * @return whether the element at the left index is lower than the element at the right index
         */
        boolean test(T data, int leftIndex, int rightIndex);

        default LessThan<T> reversed() {
            return (d, l, r) -> test(d, r, l);
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
            return (d, l, r) -> Sorts.lt(func.test(l), func.test(r));
        }

        static LessThan<?> fromDoubleGetterReversed(IntToDoubleFunction func) {
            return (d, l, r) -> func.applyAsDouble(r) < func.applyAsDouble(l);
        }

        static LessThan<?> fromLongGetterReversed(IntToLongFunction func) {
            return (d, l, r) -> func.applyAsLong(r) < func.applyAsLong(l);
        }

        static LessThan<?> fromIntGetterReversed(IntUnaryOperator func) {
            return (d, l, r) -> func.applyAsInt(r) < func.applyAsInt(l);
        }

        static LessThan<?> fromBooleanGetterReversed(IntPredicate func) {
            return (d, l, r) -> Sorts.lt(func.test(r), func.test(l));
        }


    }

    static <T> void insertionSort(int[] args, T data, LessThan<T> lessThan, int a, int b) {
        for (int i = a + 1; i < b; ++i) {
            for (int j = i; j > a && lessThan.test(data, args[j], args[j - 1]); --j) {
                Sorts.swap(args, j, j - 1);
            }
        }
    }

    static void insertionSort(int[] args, double[] data, int a, int b) {
        for (int i = a + 1; i < b; ++i) {
            for (int j = i; j > a && data[args[j]] < data[args[j - 1]]; --j) {
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
            if (child + 1 < hi && comparator.test(data, args[first + child], args[first + child + 1])) {
                ++child;
            }
            if (!comparator.test(data, args[first + root], args[first + child])) {
                return;
            }
            Sorts.swap(args, first + root, first + child);
            root = child;
        }
    }

    static void siftDown(int[] args, double[] data, int lo, int hi, int first) {
        int root = lo;
        for (; ; ) {
            int child = 2 * root + 1;
            if (child >= hi) {
                break;
            }
            if (child + 1 < hi && data[args[first + child]] < data[args[first + child + 1]]) {
                ++child;
            }
            if (data[args[first + root]] >= data[args[first + child]]) {
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

    static void heapSort(int[] args, double[] data, int a, int b) {
        int first = a,
                lo = 0,
                hi = b - a;
        // Build heap with greatest element at top.
        for (int i = (hi - 1) / 2; i >= 0; i--) {
            siftDown(args, data, i, hi, first);
        }

        // Pop elements, largest first, into end of data.
        for (int i = (hi - 1); i >= 0; i--) {
            Sorts.swap(args, first, first + i);
            siftDown(args, data, lo, i, first);
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
        if (lt.test(data, args[m1], args[m0])) {
            Sorts.swap(args, m1, m0);
        }
        // data[m0] <= data[m1]
        if (lt.test(data, args[m2], args[m1])) {
            Sorts.swap(args, m2, m1);
            // data[m0] <= data[m2] && data[m1] < data[m2]
            if (lt.test(data, args[m1], args[m0])) {
                Sorts.swap(args, m1, m0);
            }
        }
        // now data[m0] <= data[m1] <= data[m2]
    }

    static void medianOfThree(int[] args, double[] data, int m1, int m0, int m2) {
        // sort 3 elements
        if (data[args[m1]] < data[args[m0]]) {
            Sorts.swap(args, m1, m0);
        }
        // data[m0] <= data[m1]
        if (data[args[m2]] < data[args[m1]]) {
            Sorts.swap(args, m2, m1);
            // data[m0] <= data[m2] && data[m1] < data[m2]
            if (data[args[m1]] < data[args[m0]]) {
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
                int m = ((a + b) >> 1);
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

    static <T> void quickSort(int[] args, T data, LessThan<T> lt, int a, int b, int maxDepth) {
        for (; b - a > 12; ) { // Use ShellSort for slices <= 12 elements
            if (maxDepth == 0) {
                heapSort(args, data, lt, a, b);
                return;
            }
            maxDepth--;
            int mlo, mhi;
            //doPivot
            {
                int m = ((a + b) >> 1);
                if (b - a > 40) {
                    // Tukey's ``Ninther,'' median of three medians of three.
                    int s = (b - a) / 8;
                    medianOfThree(args, data, lt, a, a + s, a + 2 * s);
                    medianOfThree(args, data, lt, m, m - s, m + s);
                    medianOfThree(args, data, lt, b - 1, b - 1 - s, b - 1 - 2 * s);
                }
                medianOfThree(args, data, lt, a, m, b - 1);

                // Invariants are:
                //	data[a] = pivot (set up by ChoosePivot)
                //	data[a < i < _a] < pivot
                //	data[_a <= i < _b] <= pivot
                //	data[_b <= i < c] unexamined
                //	data[c <= i < b-1] > pivot
                //	data[b-1] >= pivot
                int pivot = a;
                int _a = a + 1, c = b - 1;

                for (; _a < c && lt.test(data, args[_a], args[pivot]); _a++) {
                }
                int _b = _a;
                for (; ; ) {
                    for (; _b < c && !(lt.test(data, args[pivot], args[_b])); _b++) { // data[_b] <= pivot
                    }
                    for (; _b < c && lt.test(data, args[pivot], args[c - 1]); c--) { // data[c-1] > pivot
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
                    if (!(lt.test(data, args[pivot], args[_b - 1]))) { // data[b-1] = pivot
                        Sorts.swap(args, c, b - 1);
                        c++;
                        dups++;
                    }
                    if (!(lt.test(data, args[_b - 1], args[pivot]))) { // data[_b-1] = pivot
                        _b--;
                        dups++;
                    }
                    // m-a = (b-a)/2 > 6
                    // _b-a > (b-a)*3/4-1 > 8
                    // ==> m < _b ==> data[m] <= pivot
                    if (!(lt.test(data, args[m], args[pivot]))) { // data[m] = pivot
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
                        for (; _a < _b && !lt.test(data, args[_b - 1], args[pivot]); _b--) { // data[_b] == pivot
                        }
                        for (; _a < _b && lt.test(data, args[_a], args[pivot]); _a++) { // data[_a] < pivot
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
                quickSort(args, data, lt, a, mlo, maxDepth);
                a = mhi; // i.e., quickSort(data, mhi, b)
            } else {
                quickSort(args, data, lt, mhi, b, maxDepth);
                b = mlo; // i.e., quickSort(data, a, mlo)
            }
        }
        if (b - a > 1) {
            // Do ShellSort pass with gap 6
            // It could be written in this simplified form cause b-a <= 12
            for (int i = a + 6; i < b; ++i) {
                if (lt.test(data, args[i], args[i - 6])) {
                    Sorts.swap(args, i, i - 6);
                }
            }
            insertionSort(args, data, lt, a, b);
        }
    }

    static void quickSort(int[] args, double[] data, int a, int b, int maxDepth) {
        for (; b - a > 12; ) { // Use ShellSort for slices <= 12 elements
            if (maxDepth == 0) {
                heapSort(args, data, a, b);
                return;
            }
            maxDepth--;
            int mlo, mhi;
            //doPivot
            {
                int m = ((a + b) >> 1);
                if (b - a > 40) {
                    // Tukey's ``Ninther,'' median of three medians of three.
                    int s = (b - a) / 8;
                    medianOfThree(args, data, a, a + s, a + 2 * s);
                    medianOfThree(args, data, m, m - s, m + s);
                    medianOfThree(args, data, b - 1, b - 1 - s, b - 1 - 2 * s);
                }
                medianOfThree(args, data, a, m, b - 1);

                // Invariants are:
                //	data[a] = pivot (set up by ChoosePivot)
                //	data[a < i < _a] < pivot
                //	data[_a <= i < _b] <= pivot
                //	data[_b <= i < c] unexamined
                //	data[c <= i < b-1] > pivot
                //	data[b-1] >= pivot
                int pivot = a;
                int _a = a + 1, c = b - 1;

                for (; _a < c && data[args[_a]] < data[args[pivot]]; _a++) {
                }
                int _b = _a;
                for (; ; ) {
                    for (; _b < c && data[args[pivot]] >= data[args[_b]]; _b++) { // data[_b] <= pivot
                    }
                    for (; _b < c && data[args[pivot]] < data[args[c - 1]]; c--) { // data[c-1] > pivot
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
                    if (data[args[pivot]] >= data[args[_b - 1]]) { // data[b-1] = pivot
                        Sorts.swap(args, c, b - 1);
                        c++;
                        dups++;
                    }
                    if (data[args[_b - 1]] >= data[args[pivot]]) { // data[_b-1] = pivot
                        _b--;
                        dups++;
                    }
                    // m-a = (b-a)/2 > 6
                    // _b-a > (b-a)*3/4-1 > 8
                    // ==> m < _b ==> data[m] <= pivot
                    if (data[args[m]] >= data[args[pivot]]) { // data[m] = pivot
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
                        for (; _a < _b && data[args[_b - 1]] >= data[args[pivot]]; _b--) { // data[_b] == pivot
                        }
                        for (; _a < _b && data[args[_a]] < data[args[pivot]]; _a++) { // data[_a] < pivot
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
                quickSort(args, data, a, mlo, maxDepth);
                a = mhi; // i.e., quickSort(data, mhi, b)
            } else {
                quickSort(args, data, mhi, b, maxDepth);
                b = mlo; // i.e., quickSort(data, a, mlo)
            }
        }
        if (b - a > 1) {
            // Do ShellSort pass with gap 6
            // It could be written in this simplified form cause b-a <= 12
            for (int i = a + 6; i < b; ++i) {
                if (data[args[i]] < data[args[i - 6]]) {
                    Sorts.swap(args, i, i - 6);
                }
            }
            insertionSort(args, data, a, b);
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
        return Sorts.lt(data[lhs], data[rhs]);
    }

    static boolean greaterThan(boolean[] data, int lhs, int rhs) {
        return Sorts.gt(data[lhs], data[rhs]);
    }

}
