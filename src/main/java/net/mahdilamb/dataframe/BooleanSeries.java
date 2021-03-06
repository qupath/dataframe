package net.mahdilamb.dataframe;


import net.mahdilamb.dataframe.utils.PrimitiveIterators;

/**
 * A boolean series
 */
public interface BooleanSeries extends Series<Boolean>, SeriesWithFunctionalOperators<Boolean> {

    /**
     * @param index the index
     * @return the value at the index
     */
    boolean getBoolean(int index);

    @Override
    default Boolean get(int index) {
        return getBoolean(index);
    }

    @Override
    default DataType getType() {
        return DataType.BOOLEAN;
    }

    @Override
    default PrimitiveIterators.OfBoolean iterator() {
        return new PrimitiveIterators.OfBoolean() {
            private int i = 0;

            @Override
            public boolean nextBoolean() {
                return i < size();
            }

            @Override
            public boolean hasNext() {
                return getBoolean(i++);
            }
        };
    }

    /**
     * Convert this series to an array of primitive booleans
     *
     * @param output the output array
     * @return the output array (if correctly sized) or a new array if not
     */
    default boolean[] toArray(boolean[] output) {
        if (output.length != size()) {
            output = new boolean[size()];
        }
        for (int i = 0; i < size(); ++i) {
            output[i] = getBoolean(i);
        }
        return output;
    }

    /**
     * @return if all the elements are true
     */
    default boolean all() {
        return reduce((a, b) -> a & b, a -> !a);
    }

    /**
     * @return if any of the elements are true
     */
    default boolean any() {
        return reduce((a, b) -> a | b, a -> a);
    }

}
