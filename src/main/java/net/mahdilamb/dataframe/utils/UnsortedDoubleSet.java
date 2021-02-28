package net.mahdilamb.dataframe.utils;

import java.util.Iterator;
import java.util.PrimitiveIterator;

/**
 * A hash set of doubles
 */
public class UnsortedDoubleSet implements Iterable<Double> {

    static final int DEFAULT_INITIAL_CAPACITY = 8;
    static final float MAX_LOAD_FACTOR = 0.7f;

    private int size = 0;
    private Double[] data;

    /**
     * Create a set with the minimum initial capacity
     *
     * @param capacity the minimum initial capacity
     */
    public UnsortedDoubleSet(int capacity) {
        data = new Double[nextPowerOfTwo(capacity)];
    }

    /**
     * Create a set with the default initial capacity
     */
    public UnsortedDoubleSet() {
        this(DEFAULT_INITIAL_CAPACITY);
    }

    /**
     * Create a set initialized with the given values
     *
     * @param values the values to initialize with.
     */
    public UnsortedDoubleSet(double[] values) {
        this();
        for (final double v : values) {
            add(v);
        }
    }

    /**
     * Add an element to the set
     *
     * @param value the value to add
     * @return whether the value was added or not (i.e. is false if the value is already contained)
     */
    public boolean add(double value) {
        int index = index(value);
        if (data[index] == null) {
            data[index] = value;
            ++size;
            return true;
        }
        if (contains(value)) {
            return false;
        }
        add0(index, value);
        return true;
    }

    /**
     * @param value the value to check for
     * @return whether this set contains the value
     */
    public boolean contains(double value) {
        return indexOf(value) != -1;
    }

    private int indexOf(double value) {
        int index = index(value);
        if (data[index] != null) {
            if (data[index] == value) {
                return index;
            }
            for (int i = index + 1, k = data.length - 1; i != index; ++i) {
                if (data[i] == null) {
                    //end of linear stream
                    return -1;
                } else if (data[i] == value) {
                    //found
                    return i;
                }
                if (i == k) {
                    i = -1;
                }
            }
        }
        return -1;
    }

    /**
     * @return the number of elements in the set
     */
    public int size() {
        return size;
    }

    /**
     * @return whether the set is empty
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * @return the set as an array of doubles
     */
    public double[] toArray() {
        double[] data = new double[size];
        int i = 0;
        for (final Double d : this.data) {
            if (d == null) {
                continue;
            }
            data[i++] = d;
        }
        return data;
    }

    @Override
    public Iterator<Double> iterator() {
        return new PrimitiveIterator.OfDouble() {
            int i = 0;
            int j = 0;

            @Override
            public boolean hasNext() {
                return j < size();
            }

            @Override
            public double nextDouble() {
                while (i < data.length) {
                    if (data[i] != null) {
                        ++j;
                        return data[i++];
                    }
                    ++i;
                }
                throw new UnsupportedOperationException();
            }
        };
    }

    private static int nextPowerOfTwo(int i) {
        --i;
        i |= i >>> 1;
        i |= i >>> 2;
        i |= i >>> 4;
        i |= i >>> 8;
        i |= i >>> 16;
        ++i;
        return i;
    }

    private void grow() {
        final Double[] old = data;
        data = new Double[data.length << 1];
        for (final Double d : old) {
            if (d == null) {
                continue;
            }
            int hash = index(d);
            if (data[hash] == null) {
                data[hash] = d;
            } else {
                data[getNextBucket(data, hash)] = d;
            }
        }
    }

    private int index(double value) {
        return (Double.hashCode(value) & 0x7fffffff) % (data.length - 1);
    }

    private void add0(int index, double value) {
        if (size > MAX_LOAD_FACTOR * data.length) {
            grow();
            add(value);
            return;
        }
        data[getNextBucket(data, index)] = value;
        ++size;
    }

    private static int getNextBucket(Double[] data, int from) {
        for (int i = from + 1, k = data.length - 1; i != from; ++i) {
            if (data[i] == null) {
                return i;
            }
            if (i == k) {
                //wrap around
                i = -1;
            }
        }
        throw new UnsupportedOperationException("Buckets are fully packed but shouldn't be!");
    }

    @Override
    public String toString() {
        final StringBuilder out = new StringBuilder();
        for (final double d : this) {
            (out.append(out.length() == 0 ? '[' : ", ")).append(d);
        }
        return out.append(']').toString();
    }

}
