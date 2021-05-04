package net.mahdilamb.dataframe.utils;

import java.util.function.IntToDoubleFunction;

//Adapted from https://www.baeldung.com/java-merge-sort

/**
 * Merge sort implementation
 */
public final class MergeSort {
    private MergeSort() {


    }

    private static void merge(double[] a, double[] l, double[] r, int left, int right) {
        int i = 0, j = 0, k = 0;
        while (i < left && j < right) {
            if (l[i] <= r[j]) {
                a[k++] = l[i++];
            } else {
                a[k++] = r[j++];
            }
        }
        while (i < left) {
            a[k++] = l[i++];
        }
        while (j < right) {
            a[k++] = r[j++];
        }
    }

    private static void merge(IntToDoubleFunction a, int[] args, int[] l, int[] r, int left, int right) {
        int i = 0, j = 0, k = 0;
        while (i < left && j < right) {
            if (a.applyAsDouble(r[j]) < a.applyAsDouble(l[i])) {
                args[k++] = r[j++];
            } else {
                args[k++] = l[i++];
            }
        }
        while (i < left) {
            args[k++] = l[i++];
        }
        while (j < right) {
            args[k++] = r[j++];
        }
    }

    /**
     * Perform merge sort in a functional style
     *
     * @param args the args to sort
     * @param data the index-to-double getter
     * @param size the number of elements in the original object
     */
    public static void argSort(int[] args, IntToDoubleFunction data, int size) {
        if (size < 2) {
            return;
        }
        int mid = size >> 1;
        int[] l = new int[mid];
        int[] r = new int[size - mid];

        System.arraycopy(args, 0, l, 0, mid);
        System.arraycopy(args, mid, r, 0, size - mid);

        argSort(l, data, mid);
        argSort(r, data, size - mid);

        merge(data, args, l, r, mid, size - mid);
    }

    /**
     * Perform merge sort in a functional style
     *
     * @param args the args to sort
     * @param data the index-to-double getter
     */
    public static void argSort(int[] args, IntToDoubleFunction data) {
        argSort(args, data, args.length);
    }

}
