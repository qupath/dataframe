package net.mahdilamb.charts.dataframe;

import net.mahdilamb.charts.dataframe.utils.GroupBy;
import net.mahdilamb.charts.dataframe.utils.IntroSort;
import net.mahdilamb.charts.dataframe.utils.IteratorUtils;
import net.mahdilamb.charts.dataframe.utils.StringUtils;

import java.util.function.*;

import static net.mahdilamb.charts.dataframe.DataFrameImpl.COLUMN_SEPARATOR;
import static net.mahdilamb.charts.dataframe.DataFrameImpl.range;

/**
 * Implementations of the various different types of series
 *
 * @param <T> the type of the elements in the series
 */
abstract class SeriesImpl<T extends Comparable<T>> implements Series<T>, SeriesWithFunctionalOperators<T> {
    static int MAX_VISIBLE_CELLS = 25;

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
    private final String name;
    int start = 0;
    int end;

    @Override
    public GroupBy<T> groups() {
        if (group == null) {
            group = new GroupBy<>(this);
        }
        return group;
    }

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

    static String formatCell(Series<?> series, int index) {
        return String.valueOf(series.get(index));
    }

    @Override
    public String toString() {
        final StringBuilder stringBuilder = new StringBuilder();
        if (size() > 1) {

            int width = IteratorUtils.skippedIterator(COLUMN_SEPARATOR.length(), (n, o) -> Math.max(n, (o != -1 ? String.valueOf(getID(o)) : COLUMN_SEPARATOR).length()), size(), MAX_VISIBLE_CELLS);
            if (size() <= MAX_VISIBLE_CELLS) {
                for (int i = 0; i < size(); ++i) {
                    DataFrameImpl.alignRight(stringBuilder, String.valueOf(getID(i)), width).append(COLUMN_SEPARATOR).append(formatCell(this, i)).append('\n');
                }
            } else {
                int halfRows = MAX_VISIBLE_CELLS >>> 1;
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

    static class SeriesView<T extends Comparable<T>> extends SeriesImpl<T> {

        final Series<T> dataSeries;
        int[] rows;
        int numRows;

        public SeriesView(Series<T> dataSeries, int[] ids, int numRows) {
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


        public SeriesView(Series<T> dataSeries, int[] ids) {
            this(dataSeries, ids, ids.length);
        }

        public SeriesView(Series<T> dataSeries, IntPredicate test) {
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

        protected SeriesView(Series<T> dataSeries, int start, int end) {
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

    static class DoubleSeriesView extends SeriesView<Double> implements DoubleSeries {

        public DoubleSeriesView(SeriesView<Double> dataSeries) {
            super(dataSeries.dataSeries, dataSeries.rows, dataSeries.numRows);
        }

        @Override
        public double getDouble(int index) {
            return ((DoubleSeries) dataSeries).getDouble(rows[index]);
        }

    }

    static class StringSeriesView extends SeriesView<String> implements StringSeries {

        public StringSeriesView(SeriesView<String> dataSeries) {
            super(dataSeries.dataSeries, dataSeries.rows, dataSeries.numRows);
        }

    }

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

    abstract void sortArgs(int[] args, int size, boolean ascending);
}
