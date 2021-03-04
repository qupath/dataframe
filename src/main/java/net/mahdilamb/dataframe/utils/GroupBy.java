package net.mahdilamb.dataframe.utils;

import net.mahdilamb.dataframe.functions.BiDoublePredicate;
import net.mahdilamb.dataframe.functions.BiIntConsumer;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.ToDoubleFunction;

/**
 * A group by object that takes an iterable as input. Backed by a minimal hash table
 *
 * @param <T> the type of the group key
 */
public class GroupBy<T> implements Iterable<GroupBy.Group<T>> {


    /**
     * A group of items
     *
     * @param <T> the type of the key in the group
     */
    public static final class Group<T> implements Iterable<Integer> {
        int id;
        final T key;
        final IntArrayList indices = new IntArrayList();

        @Override
        public String toString() {
            return String.format("Group {id %d, key: %s, indices: %s}", id, key, indices.size() < 10 ? indices : String.format("%s (n)", indices.size()));
        }

        @Override
        public int hashCode() {
            return key.hashCode();
        }

        Group(T key) {
            this.key = key;
        }

        /**
         * @return the key of this group
         */
        public T get() {
            return key;
        }

        /**
         * @return get the id of the group
         */
        public int getID() {
            return id;
        }

        /**
         * Get the group index from the position
         *
         * @param position the position
         * @return the index from the position
         */
        public int get(int position) {
            return indices.get(position);
        }

        /**
         * @return the indices of the group
         */
        public IntArrayList getIndices() {
            return indices;
        }

        /**
         * @return the size of the group
         */
        public int size() {
            return indices.size();
        }

        @Override
        public Iterator<Integer> iterator() {
            return indices.iterator();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Group)) return false;
            Group<?> group = (Group<?>) o;
            return key.equals(group.key);
        }
    }


    private final Map<T, Group<T>> groupTable;
    int size = 0;

    /**
     * Create a groupBy with a suggested initial number of groups
     *
     * @param size the initial number of groups
     */
    private GroupBy(int size) {
        this.groupTable = new LinkedHashMap<>(size);
    }

    /**
     * Create a group by from an ordered iterable
     *
     * @param data the data
     */
    public GroupBy(Iterable<T> data) {
        this();
        int i = 0;
        for (final T key : data) {
            add(key, i++);
        }
    }

    /**
     * Create a group by from an ordered iterable
     *
     * @param data the data
     * @param size the number of data elements
     */
    public GroupBy(Iterable<T> data, int size) {
        this(size);
        int i = 0;
        final Iterator<T> it = data.iterator();
        while (it.hasNext() && i < size) {
            add(it.next(), i++);
        }

    }

    /**
     * Create a group by from an ordered array
     *
     * @param data the data
     */
    @SafeVarargs
    public GroupBy(T... data) {
        this();
        int i = 0;
        for (final T key : data) {
            add(key, i++);
        }
    }

    /**
     * Create a groupby with an initial capacity of 4
     */
    public GroupBy() {
        this.groupTable = new LinkedHashMap<>();
    }

    /**
     * @return the number of groups
     */
    public int numGroups() {
        return groupTable.size();
    }

    /**
     * Get a group by key, or {@code null}, if it does not exist
     *
     * @param key the key
     * @return the group
     */
    public Group<T> getGroup(T key) {
        return groupTable.get(key);
    }

    public Group<T> getGroup(int index) {
        for (final Map.Entry<T, Group<T>> g : groupTable.entrySet()) {
            if (g.getValue().getID() == index) {
                return g.getValue();
            }
        }
        return null;
    }

    /**
     * Test if a group contains a key
     *
     * @param key the key to test
     * @return whether the groupby contains the key
     */
    public boolean contains(T key) {
        return groupTable.containsKey(key);
    }

    /**
     * Add a key-index pair
     *
     * @param key the key
     * @param i   the index
     */
    private void add(T key, int i) {
        ++size;
        final Group<T> g = groupTable.get(key);
        if (g != null) {
            g.indices.add(i);
            return;
        }
        final Group<T> h = new Group<>(key);
        h.id = groupTable.size();
        h.indices.add(i);
        groupTable.put(key, h);
    }

    @Override
    public String toString() {
        final StringBuilder stringBuilder = new StringBuilder();
        for (final Group<T> group : this) {
            (stringBuilder.length() == 0 ? stringBuilder.append("GroupBy {groups: ") : stringBuilder.append(", ")).append(String.format("%s (n=%d)", group.key, group.indices.size()));
        }
        return stringBuilder.append('}').toString();
    }

    @Override
    public Iterator<Group<T>> iterator() {
        return groupTable.values().iterator();
    }

    /**
     * @return the number of elements in the group
     */
    public int size() {
        return size;
    }

    /**
     * Reduce the groupings to one group
     *
     * @param test      the function used to convert a group into a double score
     * @param predicate the function to apply to see which score is better (lhs is the current best score, rhs is the score
     *                  of the element)
     * @return the group with the best score
     */
    public Group<T> reduce(ToDoubleFunction<Group<T>> test, BiDoublePredicate predicate) {
        Group<T> out = null;
        double best = 0;

        for (final Map.Entry<T, Group<T>> e : groupTable.entrySet()) {
            double t = test.applyAsDouble(e.getValue());
            if (out == null || predicate.test(best, t)) {
                best = t;
                out = e.getValue();
            }
        }

        return out;
    }

    /**
     * @return the group with the greatest number of indices
     */
    public Group<T> mode() {
        return reduce(Group::size, (best, current) -> current >= best);
    }

    /**
     * Map a consumer over the groups
     *
     * @param consumer the consumer to use. The signature is: {group_index, index}
     */
    public void forEach(BiIntConsumer consumer) {
        for (final Map.Entry<T, Group<T>> e : groupTable.entrySet()) {
            for (final int j : e.getValue()) {
                consumer.accept(e.getValue().getID(), j);
            }
        }
    }

    /**
     * Get an array containing the factors
     *
     * @param array the array
     * @return the array, if the size is less than the number of indices. Or a new array otherwise
     */
    public int[] toMeltedArray(int[] array) {
        if (array == null || array.length < size()) {
            array = new int[size()];
        }
        for (final Map.Entry<T, Group<T>> e : groupTable.entrySet()) {
            for (final int j : e.getValue()) {
                array[j] = e.getValue().getID();
            }
        }
        return array;
    }

    /**
     * Get an array containing the factors as doubles
     *
     * @param array the array
     * @return the array, if the size is less than the number of indices. Or a new array otherwise
     */
    public double[] toMeltedArray(double[] array) {
        if (array == null || array.length < size()) {
            array = new double[size()];
        }
        for (final Map.Entry<T, Group<T>> e : groupTable.entrySet()) {
            for (final int j : e.getValue()) {
                array[j] = e.getValue().getID();
            }
        }
        return array;
    }

    @SuppressWarnings("unchecked")
    public T[] toMeltedArray(T[] array) {
        if (array == null || array.length < size()) {
            array = (T[]) new Object[size()];
        }
        for (final Map.Entry<T, Group<T>> e : groupTable.entrySet()) {
            for (final int j : e.getValue()) {
                array[j] = e.getValue().get();
            }
        }
        return array;
    }

    /**
     * @return an array of the indices of the groups
     */
    public int[] toMeltedArray() {
        final int[] out = new int[size()];
        for (final Map.Entry<T, Group<T>> e : groupTable.entrySet()) {
            for (final int j : e.getValue()) {
                out[j] = e.getValue().getID();
            }
        }
        return out;
    }

    /**
     * Get the unique groups
     *
     * @return an array of the groups
     */
    @SuppressWarnings("unchecked")
    public T[] getGroups(T[] groups) {
        if ( groups.length < size()) {
            groups = (T[]) Array.newInstance(groups.getClass().getComponentType(), numGroups());
        }
        for (final Map.Entry<T, Group<T>> e : groupTable.entrySet()) {
            groups[e.getValue().getID()] = e.getValue().get();
        }
        return groups;
    }
}
