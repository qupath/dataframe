package net.mahdilamb.dataframe.utils;

import java.util.Arrays;

/**
 * Array list backed by an array of primitive doubles
 */
public final class BooleanArrayList implements Iterable<Boolean> {
    static final int INITIAL_CAPACITY = 8;
    private boolean[] arr;
    private int size;

    /**
     * Create an double array with a specified initial capacity
     *
     * @param initialCapacity the initial capacity
     */
    public BooleanArrayList(int initialCapacity) {
        arr = new boolean[initialCapacity];
    }

    /**
     * Create an double array with given values
     *
     * @param values the values to use
     */
    public BooleanArrayList(boolean... values) {
        arr = values;
        size = values.length;
    }

    /**
     * Create an empty array list with the default initial capacity
     */
    public BooleanArrayList() {
        this(INITIAL_CAPACITY);
    }

    /**
     * Add an element to the array list at the specified index
     *
     * @param value the value to add
     * @param index the index to add to
     */
    public void add(boolean value, int index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException(index);
        }
        ensureCapacity(arr.length + 1);
        arr[index] = value;
        ++size;
    }

    /**
     * Add an element to the end of the array
     *
     * @param value the value to add
     */
    public void add(boolean value) {
        add(value, size);
    }

    /**
     * Add all the elements to the end of the array list
     *
     * @param values the values to add
     */
    public void addAll(boolean... values) {
        if (size < arr.length + values.length) {
            ensureCapacity(arr.length + values.length);
        }
        System.arraycopy(values, 0, arr, size, values.length);
        size += values.length;
    }

    private void ensureCapacity(int newLength) {
        if (size < newLength) {
            arr = Arrays.copyOf(arr, Math.max(newLength, arr.length >>> 1));
        }
    }

    /**
     * Remove elements between indices
     *
     * @param from the starting index (inclusive)
     * @param to   the ending index (exclusive)
     */
    public void remove(int from, int to) {
        if (from > to) {
            throw new IllegalArgumentException("to must be greater then from");
        }
        if (from != to) {
            final int elements = to - from;
            if (size != arr.length) {

                System.arraycopy(arr, to, arr, from, elements);
            }
            size -= elements;
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
    public int indexOf(boolean value) {
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
    public int lastIndexOf(boolean value) {
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
    public boolean contains(boolean value) {
        return indexOf(value) != -1;
    }

    /**
     * Get a value from a position
     *
     * @param index the index
     * @return the value at the index
     */
    public boolean get(int index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException(index);
        }
        return arr[index];
    }

    /**
     * Set the element at the given index
     *
     * @param index the index to set at
     * @param value the value to set
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    public void set(int index, boolean value) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException(index);
        }
        arr[index] = value;
    }

    /**
     * Fill the array with n copies of the given value
     *
     * @param value the value
     * @param n     the number of times to set the value. If n is greater than the size of the list, then the
     *              list will increase in size
     */
    public void fill(boolean value, int n) {
        ensureCapacity(n);
        Arrays.fill(arr, 0, n, value);
        this.size = Math.max(size, n);
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

    @Override
    public PrimitiveIterators.OfBoolean iterator() {
        return new PrimitiveIterators.OfBoolean() {
            private int i = 0;

            @Override
            public boolean hasNext() {
                return i < size();
            }

            @Override
            public boolean nextBoolean() {
                return arr[i++];
            }
        };
    }

    @Override
    public int hashCode() {
        int result = 1;
        for (int i = 0; i < size; ++i) {
            result = 31 * result + (arr[i] ? 1231 : 1237);
        }
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof BooleanArrayList)) {
            return false;
        }
        if (((BooleanArrayList) obj).size != size()) {
            return false;
        }
        for (int i = 0; i < size; ++i) {
            if (arr[i] != ((BooleanArrayList) obj).arr[i]) {
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
