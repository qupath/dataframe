package net.mahdilamb.dataframe;

import net.mahdilamb.dataframe.utils.GroupBy;
import net.mahdilamb.dataframe.utils.IntroSort;
import net.mahdilamb.dataframe.utils.IteratorUtils;
import net.mahdilamb.dataframe.utils.StringUtils;

import java.util.Hashtable;
import java.util.Map;
import java.util.function.*;

import static net.mahdilamb.dataframe.DataFrameImpl.COLUMN_SEPARATOR;
import static net.mahdilamb.dataframe.DataFrameImpl.range;

/**
 * Implementations of the various different types of series
 *
 * @param <T> the type of the elements in the series
 */
abstract class SeriesImpl<T extends Comparable<T>> implements Series<T>, SeriesWithFunctionalOperators<T> {

    /**
     * Default implementation of a series backed by a double array
     */
    static final class OfDoubleArray extends SeriesImpl<Double> implements DoubleSeries {
        final double[] data;

        OfDoubleArray(final String name, final double... data) {
            super(name);
            this.data = data;
            this.end = data.length;
        }

        <S extends Number & Comparable<S>> OfDoubleArray(NumericSeries<S> source, ToDoubleFunction<S> converter) {
            super(source.getName());
            this.data = new double[source.size()];
            this.end = source.size();
            for (int i = 0; i < source.size(); ++i) {
                data[i] = source.isNaN(i) ? Double.NaN : converter.applyAsDouble(source.get(i));
            }
        }

        <S> OfDoubleArray(Series<S> source, ToDoubleFunction<S> converter) {
            super(source.getName());
            this.data = new double[source.size()];
            this.end = source.size();
            for (int i = 0; i < source.size(); ++i) {
                data[i] = converter.applyAsDouble(source.get(i));
            }
        }

        @Override
        public double getDouble(int index) {
            return data[index];
        }

        @Override
        void sortArgs(int[] args, int size, boolean ascending) {
            IntroSort.argSort(args, size, data, ascending);
        }
    }

    /**
     * Default implementation of a series backed by a boolean array
     */
    static final class OfBooleanArray extends SeriesImpl<Boolean> implements BooleanSeries {
        final boolean[] data;

        OfBooleanArray(final String name, final boolean... data) {
            super(name);
            this.data = data;
            this.end = data.length;
        }

        @Override
        public boolean getBoolean(int index) {
            return data[index];
        }

        <S> OfBooleanArray(Series<S> source, Predicate<S> converter) {
            super(source.getName());

            data = new boolean[source.size()];
            this.end = data.length;

            for (int i = 0; i < end; ++i) {
                data[i] = converter.test(source.get(i));
            }
        }

        @Override
        void sortArgs(int[] args, int size, boolean ascending) {
            IntroSort.argSort(args, size, data, ascending);
        }
    }

    /**
     * Default implementation of a series backed by a double array
     */
    static final class OfStringArray extends SeriesImpl<String> implements StringSeries {
        final String[] data;

        OfStringArray(final String name, final String... data) {
            super(name);
            this.data = data;
            this.end = data.length;
        }

        <S> OfStringArray(Series<S> source, Function<S, String> converter) {
            super(source.getName());
            this.end = source.size();
            data = new String[source.size()];
            for (int i = 0; i < source.size(); ++i) {
                data[i] = converter.apply(source.get(i));
            }
        }

        @Override
        public String get(int index) {
            return data[index];
        }

        @Override
        void sortArgs(int[] args, int size, boolean ascending) {
            IntroSort.argSort(args, size, data, ascending);

        }
    }

    /**
     * Default implementation of a series backed by a string, which parses to double when required
     */
    static final class OfStringToDoubleArray extends SeriesImpl<Double> implements DoubleSeries {

        private final String[] data;

        OfStringToDoubleArray(final String name, final String... data) {
            super(name);
            this.data = data;
            this.end = data.length;
        }

        @Override
        public double getDouble(int index) {
            return DataType.toDouble(data[index]);
        }

        @Override
        void sortArgs(int[] args, int size, boolean ascending) {
            IntroSort.argSort(args, size, this::getDouble, ascending);
        }
    }

    /**
     * Default implementation of a series backed by a string, which parses to long when required
     */
    static final class OfStringToLongArray extends SeriesImpl<Long> implements LongSeries {

        private final String[] data;

        OfStringToLongArray(final String name, final String... data) {
            super(name);
            this.data = data;
            this.end = data.length;
        }

        @Override
        public long getLong(int index) {
            return DataType.toLong(data[index]);
        }

        @Override
        public boolean isNaN(int index) {
            return !DataType.LONG.matches(data[index]);
        }

        @Override
        void sortArgs(int[] args, int size, boolean ascending) {
            IntroSort.argSort(args, size, this::getLong, ascending);

        }
    }

    /**
     * A series backed by objects holding Long
     */
    static final class OfLongArray extends SeriesImpl<Long> implements LongSeries {
        final Long[] data;

        /**
         * Create an abstract named series
         *
         * @param name the name of the series
         */
        OfLongArray(String name, Long[] data) {
            super(name);
            this.data = data;
            this.end = data.length;
        }

        @Override
        public long getLong(int index) {
            return data[index];
        }

        @Override
        public boolean isNaN(int index) {
            return data[index] == null;
        }

        <S> OfLongArray(Series<S> source, ToLongFunction<S> converter, Predicate<S> notNanTest) {
            super(source.getName());
            data = new Long[source.size()];
            this.end = source.size();

            for (int i = 0; i < source.size(); ++i) {
                if (notNanTest.test(source.get(i))) {
                    data[i] = converter.applyAsLong(source.get(i));
                } else {
                    data[i] = null;
                }
            }
        }

        @Override
        void sortArgs(int[] args, int size, boolean ascending) {
            IntroSort.argSort(args, size, data, ascending);

        }
    }

    /**
     * A series back by an array of primitive longs
     */
    static final class OfNonNaNLongArray extends SeriesImpl<Long> implements LongSeries {
        final long[] data;


        /**
         * Create an abstract named series
         *
         * @param name the name of the series
         */
        OfNonNaNLongArray(String name, long[] data) {
            super(name);
            this.data = data;
            this.end = data.length;
        }

        <S> OfNonNaNLongArray(Series<S> source, ToLongFunction<S> converter) {
            super(source.getName());
            data = new long[source.size()];
            this.end = source.size();

            for (int i = 0; i < source.size(); ++i) {
                data[i] = converter.applyAsLong(source.get(i));
            }

        }

        @Override
        public long getLong(int index) {
            return data[index];
        }

        @Override
        public boolean isNaN(int index) {
            return false;
        }

        @Override
        void sortArgs(int[] args, int size, boolean ascending) {
            IntroSort.argSort(args, size, data, ascending);

        }
    }

    /**
     * Default implementation of a series backed by a collection of objects
     */
    static final class OfFunctionalDouble extends SeriesImpl<Double> implements DoubleSeries {
        private final IntToDoubleFunction dataGetter;

        OfFunctionalDouble(final String name, int size, IntToDoubleFunction dataGetter) {
            super(name);
            this.end = size;
            this.dataGetter = dataGetter;
        }

        @Override
        public double getDouble(int index) {
            return dataGetter.applyAsDouble(index);
        }

        @Override
        void sortArgs(int[] args, int size, boolean ascending) {
            IntroSort.argSort(args, size, this::getDouble, ascending);

        }
    }

    /**
     * Default implementation of a series backed by a string, which parses to boolean when accessed
     */
    static final class OfStringToBooleanArray extends SeriesImpl<Boolean> implements BooleanSeries {

        private final String[] data;

        OfStringToBooleanArray(final String name, final String... data) {
            super(name);
            this.data = data;
            this.end = data.length;
        }

        @Override
        public boolean getBoolean(int index) {
            return DataType.toBoolean(data[index]);
        }

        @Override
        void sortArgs(int[] args, int size, boolean ascending) {
            IntroSort.argSort(args, size, this::getBoolean, ascending);

        }
    }

    private GroupBy<T> group;
    String name;
    int start = 0;
    int end;

    /**
     * Create an abstract named series
     *
     * @param name the name of the series
     */
    protected SeriesImpl(final String name) {
        this.name = name;
    }

    @Override
    public final String getName() {
        return name;
    }

    @Override
    public int size() {
        return end - start;
    }

    @Override
    public int getID(int index) {
        return index;
    }

    private static String formatCell(Series<?> series, int index) {
        return String.valueOf(series.get(index));
    }

    @Override
    public String toString() {
        return toString(DEFAULT_DISPLAY_ROWS);
    }

    @Override
    public String toString(int maxRows) {
        final StringBuilder stringBuilder = new StringBuilder();
        if (size() > 1) {
            int width = IteratorUtils.skippedIterator(COLUMN_SEPARATOR.length(), (n, o) -> Math.max(n, (o != -1 ? String.valueOf(getID(o)) : COLUMN_SEPARATOR).length()), size(), maxRows);
            if (size() <= maxRows) {
                for (int i = 0; i < size(); ++i) {
                    DataFrameImpl.alignRight(stringBuilder, String.valueOf(getID(i)), width).append(COLUMN_SEPARATOR).append(formatCell(this, i)).append('\n');
                }
            } else {
                int halfRows = maxRows >>> 1;
                for (int i = 0; i < halfRows; ++i) {
                    DataFrameImpl.alignRight(stringBuilder, String.valueOf(getID(i)), width).append(COLUMN_SEPARATOR).append(formatCell(this, i)).append('\n');
                }
                //ellipses
                DataFrameImpl.alignRight(stringBuilder, StringUtils.repeatCharacter('.', Math.min(3, Math.max(width, 1))), width).append(COLUMN_SEPARATOR).append("...\n");
                for (int i = size() - halfRows; i < size(); ++i) {
                    DataFrameImpl.alignRight(stringBuilder, String.valueOf(getID(i)), width).append(COLUMN_SEPARATOR).append(formatCell(this, i)).append('\n');
                }
            }


        }
        return stringBuilder.append(String.format("{Series: \"%s\", size: %d, type: %s}", getName(), size(), StringUtils.toTitleCase(getType().name()))).toString();
    }

    @Override
    public Series<T> subset(int start, int end) {
        return new SeriesView<>(this, start, end);
    }

    @Override
    public Series<T> subset(IntPredicate test) {
        return new SeriesView<>(this, test);
    }

    /**
     * A view into a series
     *
     * @param <T> the type of the elements in the series
     */
    static class SeriesView<T extends Comparable<T>> extends SeriesImpl<T> {

        final Series<T> dataSeries;
        int[] rows;
        int numRows;

        SeriesView(Series<T> dataSeries, int[] ids, int numRows) {
            super(dataSeries.getName());
            if (dataSeries instanceof SeriesView) {
                this.dataSeries = ((SeriesView<T>) dataSeries).dataSeries;
                rows = new int[ids.length];
                for (int i = 0; i < ids.length; ++i) {
                    rows[i] = dataSeries.getID(ids[i]);
                }
            } else {
                this.dataSeries = dataSeries;
                rows = ids;
            }
            this.numRows = numRows;
        }


        SeriesView(Series<T> dataSeries, int[] ids) {
            this(dataSeries, ids, ids.length);
        }

        SeriesView(Series<T> dataSeries, IntPredicate test) {
            super(dataSeries.getName());
            int j = 0;
            int i = 0;
            if (dataSeries instanceof SeriesView) {
                this.dataSeries = ((SeriesView<T>) dataSeries).dataSeries;
                this.rows = new int[dataSeries.size()];

                while (i < dataSeries.size()) {
                    int k = dataSeries.getID(i);
                    if (test.test(k)) {
                        rows[j++] = k;
                    }
                    ++i;
                }
            } else {
                this.dataSeries = dataSeries;
                this.rows = new int[dataSeries.size()];
                while (i < dataSeries.size()) {
                    if (test.test(i)) {
                        rows[j++] = i;
                    }
                    ++i;
                }
            }
            numRows = j;

        }

        SeriesView(Series<T> dataSeries, int start, int end) {
            this(dataSeries, DataFrameImpl.range(start, end));
        }

        @Override
        public int getID(int i) {
            if (i >= numRows) {
                throw new IndexOutOfBoundsException(i);
            }
            return rows[i];
        }

        @Override
        void sortArgs(int[] args, int size, boolean ascending) {
            ((SeriesImpl<T>) dataSeries).sortArgs(args, size, ascending);
        }

        @Override
        public T get(int index) {
            if (index >= numRows) {
                throw new IndexOutOfBoundsException(index);
            }
            return dataSeries.get(rows[index]);
        }

        @Override
        public int size() {
            return numRows;
        }

        @Override
        public DataType getType() {
            return dataSeries.getType();
        }
    }

    /**
     * A view of a long series
     */
    static class LongSeriesView extends SeriesView<Long> implements LongSeries {

        public LongSeriesView(SeriesView<Long> dataSeries) {
            super(dataSeries.dataSeries, dataSeries.rows, dataSeries.numRows);
        }

        @Override
        public long getLong(int index) {
            return ((LongSeries) dataSeries).getLong(rows[index]);
        }

        @Override
        public boolean isNaN(int index) {
            return ((LongSeries) dataSeries).isNaN(rows[index]);
        }
    }

    /**
     * A view of a double series
     */
    static class DoubleSeriesView extends SeriesView<Double> implements DoubleSeries {

        public DoubleSeriesView(SeriesView<Double> dataSeries) {
            super(dataSeries.dataSeries, dataSeries.rows, dataSeries.numRows);
        }

        @Override
        public double getDouble(int index) {
            return ((DoubleSeries) dataSeries).getDouble(rows[index]);
        }

    }

    /**
     * A view of a string series
     */
    static class StringSeriesView extends SeriesView<String> implements StringSeries {

        public StringSeriesView(SeriesView<String> dataSeries) {
            super(dataSeries.dataSeries, dataSeries.rows, dataSeries.numRows);
        }

    }

    /**
     * A view of boolean series
     */
    static class BooleanSeriesView extends SeriesView<Boolean> implements BooleanSeries {

        public BooleanSeriesView(SeriesView<Boolean> dataSeries) {
            super(dataSeries.dataSeries, dataSeries.rows, dataSeries.numRows);
        }

        @Override
        public boolean getBoolean(int index) {
            return ((BooleanSeries) dataSeries).getBoolean(rows[index]);
        }

    }

    @Override
    public Series<T> filter(BooleanSeries filter) {
        final int[] ids = new int[filter.size()];
        int size = 0;
        for (int i = 0; i < filter.size(); ++i) {
            if (filter.get(i)) {
                ids[size++] = i;
            }
        }
        return new SeriesView<>(this, ids, size);
    }

    @Override
    public Series<T> sort(boolean ascending) {
        final int[] ids;
        final int numIds;
        if (this instanceof SeriesView) {
            ids = ((SeriesView<T>) this).rows.clone();
            numIds = ((SeriesView<T>) this).numRows;
        } else {
            ids = range(0, size());
            numIds = size();
        }
        sortArgs(ids, numIds, ascending);
        return this instanceof SeriesView ? new SeriesView<>(((SeriesView<T>) this).dataSeries, ids, numIds) : new SeriesView<>(this, ids, numIds);
    }


    @Override
    @SuppressWarnings("unchecked")
    public StringSeries asString() {
        switch (getType()) {
            case STRING:
                return this.getClass() == SeriesImpl.SeriesView.class ? new SeriesImpl.StringSeriesView((SeriesImpl.SeriesView<String>) this) : (StringSeries) this;
            case BOOLEAN:
                return new SeriesImpl.OfStringArray(this, el -> DataType.toString((Boolean) el));
            case LONG:
                return new SeriesImpl.OfStringArray(this, el -> DataType.toString((Long) el));
            case DOUBLE:
                return new SeriesImpl.OfStringArray(this, el -> DataType.toString((Double) el));
            default:
                throw new SeriesCastException();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public DoubleSeries asDouble() {
        switch (getType()) {
            case DOUBLE:
                return this.getClass() == SeriesImpl.SeriesView.class ? new SeriesImpl.DoubleSeriesView((SeriesImpl.SeriesView<Double>) this) : (DoubleSeries) this;
            case BOOLEAN:
                return new SeriesImpl.OfDoubleArray(this, el -> DataType.toDouble((Boolean) el));
            case LONG:
                return new SeriesImpl.OfDoubleArray((Series<Long>) this, DataType::toDouble);
            case STRING:
                return new SeriesImpl.OfDoubleArray(this, el -> DataType.toDouble((String) el));
            default:
                throw new SeriesCastException();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public LongSeries asLong() {
        switch (getType()) {
            case LONG:
                return this.getClass() == SeriesImpl.SeriesView.class ? new SeriesImpl.LongSeriesView((SeriesImpl.SeriesView<Long>) this) : (LongSeries) this;
            case DOUBLE:
                return new SeriesImpl.OfLongArray(this, el -> DataType.toLong((Double) el), v -> !Double.isNaN((Double) v));
            case BOOLEAN:
                return new SeriesImpl.OfNonNaNLongArray(this, el -> DataType.toLong((Boolean) el));
            case STRING:
                return new SeriesImpl.OfLongArray(this, el -> DataType.toLong((String) el), v -> DataType.LONG.matches((String) v));
            default:
                throw new SeriesCastException();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public BooleanSeries asBoolean() {
        switch (getType()) {
            case BOOLEAN:
                return this.getClass() == SeriesImpl.SeriesView.class ? new SeriesImpl.BooleanSeriesView((SeriesImpl.SeriesView<Boolean>) this) : (BooleanSeries) this;
            case STRING:
                return new SeriesImpl.OfBooleanArray(this, el -> DataType.toBoolean((String) el));
            case LONG:
                return new SeriesImpl.OfBooleanArray(this, el -> DataType.toBoolean((Long) el));
            case DOUBLE:
                return new SeriesImpl.OfBooleanArray(this, el -> DataType.toBoolean((Double) el));
            default:
                throw new SeriesCastException();
        }
    }

    @Override
    public DataFrame frequencies() {
        final Map<T, Double> map = new Hashtable<>();
        for (int i = 0; i < size(); ++i) {
            final Double key = map.get(get(i));
            if (key == null) {
                map.put(get(i), 1. / size());
                continue;
            }
            map.put(get(i), key + (1. / size()));
        }
        return new DataFrameImpl.OfMap(String.format("Frequencies of %s", getName()), getName(), getType(), "frequencies", DataType.DOUBLE, map);

    }

    @Override
    public DataFrame valueCounts() {
        final Map<T, Long> map = new Hashtable<>();
        for (int i = 0; i < size(); ++i) {
            final Long key = map.get(get(i));
            if (key == null) {
                map.put(get(i), 1L);
                continue;
            }
            map.put(get(i), key + 1);
        }
        return new DataFrameImpl.OfMap(String.format("Counts of %s", getName()), getName(), getType(), "counts", DataType.LONG, map);
    }

    @Override
    public GroupBy<T> groups() {
        if (group == null) {
            group = new GroupBy<>(this);
        }
        return group;
    }

    /**
     * Given an array of indices, use the values in this series to sort them
     *
     * @param args      the arguments to sort
     * @param size      the last element
     * @param ascending whether to sort ascending
     */
    abstract void sortArgs(int[] args, int size, boolean ascending);
}
