package net.mahdilamb.dataframe.utils;

import net.mahdilamb.dataframe.functions.ObjIntDoubleFunction;

public final class Searches {
    private Searches() {

    }

    private static <T> int floorBinarySearch0(T arr, ObjIntDoubleFunction<T> a, int fromIndex, int toIndex,
                                              double key) {
        int low = fromIndex;
        int high = toIndex - 1;

        while (low <= high) {
            int mid = (low + high) >>> 1;
            double midVal = a.applyAsDouble(arr, mid);

            if (midVal < key) {
                low = mid + 1;
            } else if (midVal > key) {
                high = mid - 1;
            } else {
                while (true) {
                    int m = mid--;
                    if (mid < 0 || a.applyAsDouble(arr, mid) != key) {
                        return m;
                    }

                }
            }
        }
        return -1;
    }

    private static <T> int ceilBinarySearch0(T arr, ObjIntDoubleFunction<T> a, int fromIndex, int toIndex,
                                             double key) {
        int low = fromIndex;
        int high = toIndex - 1;

        while (low <= high) {
            int mid = (low + high) >>> 1;
            double midVal = a.applyAsDouble(arr, mid);

            if (midVal < key) {
                low = mid + 1;
            } else if (midVal > key) {
                high = mid - 1;
            } else {
                while (true) {
                    int m = mid++;
                    if (mid >= toIndex || a.applyAsDouble(arr, mid) != key) {
                        return m;
                    }
                }
            }
        }
        return -1;
    }

    private static <T> int nearestBinarySearch0(T arr, ObjIntDoubleFunction<T> a, int fromIndex, int toIndex,
                                                double key) {
        int low = fromIndex;
        int high = toIndex - 1;

        while (low <= high) {
            int mid = (low + high) >>> 1;
            double midVal = a.applyAsDouble(arr, mid);

            if (midVal < key) {
                low = mid + 1;
            } else if (midVal > key) {
                high = mid - 1;
            } else {
                while (true) {
                    int m = mid++;
                    if (mid >= toIndex || a.applyAsDouble(arr, mid) != key) {
                        return m;
                    }
                }
            }
        }
        return (a.applyAsDouble(arr, low) - key) < (key - a.applyAsDouble(arr, high)) ? low : high;
    }

    public static <T> int floorBinarySearch(T arr, ObjIntDoubleFunction<T> haystack, int size, double needle) {
        return floorBinarySearch0(arr, haystack, 0, size, needle);
    }

    public static <T> int ceilBinarySearch(T arr, ObjIntDoubleFunction<T> haystack, int size, double needle) {
        return ceilBinarySearch0(arr, haystack, 0, size, needle);
    }
    public static <T> int nearestBinarySearch(T arr, ObjIntDoubleFunction<T> haystack, int size, double needle) {
        return nearestBinarySearch0(arr, haystack, 0, size, needle);
    }
}
