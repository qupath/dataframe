package net.mahdilamb.dataframe.utils;

import java.util.Arrays;

/**
 * Array list backed by an array of primitive ints
 */
public final class CharArrayList implements Iterable<Character> {
    static final int INITIAL_CAPACITY = 8;
    private char[] arr;
    private int size;

    /**
     * Create an int array with a specified initial capacity
     *
     * @param initialCapacity the initial capacity
     */
    public CharArrayList(int initialCapacity) {
        arr = new char[initialCapacity];
    }

    /**
     * Create an int array with given values
     *
     * @param values the values to use
     */
    public CharArrayList(char... values) {
        arr = values;
        size = values.length;
    }

    /**
     * Create an empty array list with the defauly initial capacity
     */
    public CharArrayList() {
        this(INITIAL_CAPACITY);
    }

    /**
     * Add an element to the array list at the specified index
     *
     * @param index the index to add to
     * @param value the value to add
     */
    public void add(int index, char value) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException(index);
        }
        add0(index, value);

    }

    private void add0(int index, char value) {
        if (size >= arr.length) {
            arr = Arrays.copyOf(arr, arr.length + Math.max(1, arr.length >>> 1));
        }
        System.arraycopy(arr, index, arr, index + 1, size - index);
        arr[index] = value;
        ++size;
    }

    /**
     * Add an element to the end of the array
     *
     * @param value the value to add
     */
    public void add(char value) {
        add0(size, value);
    }

    /**
     * Add all the values to the end of the list
     *
     * @param values the values to add
     */
    public void addAll(char... values) {
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
     */
    public void remove(int from, int to) {
        if (from > to) {
            throw new IllegalArgumentException("to must be greater then from");
        }
        if (from != to) {
            System.arraycopy(arr, to, arr, from, size - to);
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
    public int indexOf(char value) {
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
    public int lastIndexOf(char value) {
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
    public boolean contains(char value) {
        return indexOf(value) != -1;
    }

    /**
     * Get a value from a position
     *
     * @param index the index
     * @return the value at the index
     */
    public char get(int index) {
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
    public void set(int index, char value) {
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

    @Override
    public PrimitiveIterators.OfCharacter iterator() {
        return new PrimitiveIterators.OfCharacter() {
            private int i = 0;

            @Override
            public boolean hasNext() {
                return i < size();
            }

            @Override
            public char nextChar() {
                return arr[i++];
            }
        };
    }

    @Override
    public int hashCode() {
        int result = 1;
        for (int i = 0; i < size; ++i) {
            result = 31 * result + arr[i];
        }
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof CharArrayList)) {
            return false;
        }
        if (((CharArrayList) obj).size != size()) {
            return false;
        }
        for (int i = 0; i < size; ++i) {
            if (arr[i] != ((CharArrayList) obj).arr[i]) {
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
