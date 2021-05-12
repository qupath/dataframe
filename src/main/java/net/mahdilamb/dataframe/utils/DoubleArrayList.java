package net.mahdilamb.dataframe.utils;

import java.util.Arrays;
import java.util.PrimitiveIterator;

/**
 * Array list backed by an array of primitive doubles
 */
public final class DoubleArrayList implements Iterable<Double> {
    static final int INITIAL_CAPACITY = 8;
    private double[] arr;
    private int size;

    /**
     * Create an double array with a specified initial capacity
     *
     * @param initialCapacity the initial capacity
     */
    public DoubleArrayList(int initialCapacity) {
        arr = new double[initialCapacity];
    }

    /**
     * Create an double array with given values
     *
     * @param values the values to use
     */
    public DoubleArrayList(double... values) {
        arr = values;
        size = values.length;
    }

    /**
     * Create an empty array list with the default initial capacity
     */
    public DoubleArrayList() {
        this(INITIAL_CAPACITY);
    }

    /**
     * Add an element to the array list at the specified index
     *
     * @param value the value to add
     * @param index the index to add to
     */
    public void add(double value, int index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException(index);
        }
        add0(value, index);

    }

    private void add0(double value, int index) {
        if (size >= arr.length) {
            arr = Arrays.copyOf(arr, arr.length + Math.max(1, arr.length >>> 1));
        }
        arr[index] = value;
        ++size;
    }

    /**
     * Add an element to the end of the array
     *
     * @param value the value to add
     */
    public void add(double value) {
        add0(value, size);
    }

    /**
     * Add all the values to the end of the list
     *
     * @param values the values to add
     */
    public void addAll(double... values) {
        if (size < arr.length + values.length) {
            arr = Arrays.copyOf(arr, arr.length + Math.max(values.length, arr.length >>> 1));
        }
        System.arraycopy(values, 0, arr, size, values.length);
        size += values.length;
    }

    /**
     * Remove elements between indices
     *
     * @param from the starting index (inclusive)
     * @param to   the ending index (exclusive)
     * @throws IllegalArgumentException if from is greater than to
     */
    public void remove(int from, int to) {
        if (from > to) {
            throw new IllegalArgumentException("to must be greater then from");
        }
        if (from != to) {
            if (size != arr.length) {
                System.arraycopy(arr, to, arr, from, size - to);
            }
            size -= to - from;
        }
    }

    /**
     * Remove a single element
     *
     * @param index the index to remove
     */
    public void remove(int index) {
        remove(index, index + 1);
    }

    /**
     * Get the index of a value, or -1 if not present
     *
     * @param value the value
     * @return the index of the value
     */
    public int indexOf(double value) {
        for (int i = 0; i < size; ++i) {
            if (arr[i] == value) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Get the last index of a value, or -1 if not present
     *
     * @param value the value to get
     * @return the last index of the given value, or -1 if not present
     */
    public int lastIndexOf(double value) {
        for (int i = size - 1; i >= 0; --i) {
            if (arr[i] == value) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Test if the list contains a value
     *
     * @param value the value to test
     * @return whether the value is contained in the array list
     */
    public boolean contains(double value) {
        return indexOf(value) != -1;
    }

    /**
     * Get a value from a position
     *
     * @param index the index
     * @return the value at the index
     * @throws IndexOutOfBoundsException if the requested index is out of range
     */
    public double get(int index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException(index);
        }
        return arr[index];
    }

    /**
     * Set a value
     *
     * @param index the index
     * @param value the value to set
     * @throws IndexOutOfBoundsException if the requested index is out of range
     */
    public void set(int index, double value) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException(index);
        }
        arr[index] = value;
    }

    /**
     * @return the size of the array
     */
    public int size() {
        return size;
    }

    /**
     * Clear the list
     */
    public void clear() {
        size = 0;
    }

    /**
     * @return the values as an array
     */
    public double[] toArray() {
        return Arrays.copyOf(arr, size);
    }

    @Override
    public PrimitiveIterator.OfDouble iterator() {
        return new PrimitiveIterator.OfDouble() {
            private int i = 0;

            @Override
            public boolean hasNext() {
                return i < size();
            }

            @Override
            public double nextDouble() {
                return arr[i++];
            }
        };
    }

    @Override
    public int hashCode() {
        int result = 1;
        for (int i = 0; i < size; ++i) {
            long bits = Double.doubleToLongBits(arr[i]);
            result = 31 * result + (int) (bits ^ (bits >>> 32));
        }
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof DoubleArrayList)) {
            return false;
        }
        if (((DoubleArrayList) obj).size != size()) {
            return false;
        }
        for (int i = 0; i < size; ++i) {
            if (arr[i] != ((DoubleArrayList) obj).arr[i]) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }
        final StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < size; ++i) {
            (i == 0 ? stringBuilder.append('[') : stringBuilder.append(", ")).append(arr[i]);
        }
        return stringBuilder.append(']').toString();
    }


}
