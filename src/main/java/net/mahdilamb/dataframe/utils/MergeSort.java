package net.mahdilamb.dataframe.utils;

import java.util.function.IntToDoubleFunction;

//Adapted from https://www.baeldung.com/java-merge-sort
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

    public static void argSort(double[] a, int n) {
        if (n < 2) {
            return;
        }
        int mid = n >> 1;
        double[] l = new double[mid];
        double[] r = new double[n - mid];

        System.arraycopy(a, 0, l, 0, mid);
        System.arraycopy(a, mid, r, 0, n - mid);
        argSort(l, mid);
        argSort(r, n - mid);

        merge(a, l, r, mid, n - mid);
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

    public static void argSort(int[] args, IntToDoubleFunction a, int n) {
        if (n < 2) {
            return;
        }
        int mid = n >> 1;
        int[] l = new int[mid];
        int[] r = new int[n - mid];

        System.arraycopy(args, 0, l, 0, mid);
        System.arraycopy(args, mid, r, 0, n - mid);

        argSort(l, a, mid);
        argSort(r, a, n - mid);

        merge(a, args, l, r, mid, n - mid);
    }

    public static void argSort(int[] args, IntToDoubleFunction a) {
        argSort(args, a, args.length);
    }

}
