package net.mahdilamb.charts.dataframe.utils;

final class Sorts {
    private Sorts() {

    }

    /**
     * @param a left operand
     * @param b right operand
     * @return whether one boolean is 'greater' than another
     */
    static boolean gt(boolean a, boolean b) {
        return a && !b;
    }

    /**
     * @param a left operand
     * @param b right operand
     * @return whether one boolean is 'greater' than or equal to another
     */
    static boolean ge(boolean a, boolean b) {
        return (a & !b) | (a == b);
    }

    /**
     * @param a left operand
     * @param b right operand
     * @return whether one boolean is 'less' than another
     */
    static boolean lt(boolean a, boolean b) {
        return !a && b;
    }
    static void swap(double[] arr, int i, int j) {
        double temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    static <T> void swap(T[] arr, int i, int j) {
        T temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    static void swap(int[] arr, int i, int j) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    /**
     * Get an integer range starting from 0
     *
     * @param value the end point (exclusive)
     * @return an int range
     */
    static int[] intRange(int value) {
        final int[] out = new int[value];
        for (int i = 0; i < value; ) {
            out[i] = i++;
        }
        return out;
    }

}
