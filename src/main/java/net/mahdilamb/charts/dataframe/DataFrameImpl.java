package net.mahdilamb.charts.dataframe;


import net.mahdilamb.charts.dataframe.utils.GroupBy;
import net.mahdilamb.charts.dataframe.utils.IteratorUtils;
import net.mahdilamb.charts.dataframe.utils.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;
import java.util.function.Function;
import java.util.function.IntPredicate;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

import static net.mahdilamb.charts.dataframe.DataType.*;
import static net.mahdilamb.charts.dataframe.utils.StringUtils.iterateLine;

/**
 * Default implementation of datasets
 */
//TODO filter without intermediate boolean series
abstract class DataFrameImpl implements DataFrame {
    private enum HistoryType {
        SORT_BY,
        SORT_BY_DESC,
        FILTER;

        @Override
        public String toString() {
            switch (this) {
                case SORT_BY:
                    return "sortedBy";
                case SORT_BY_DESC:
                    return "sortedBy [desc]";
                case FILTER:
                    return "filteredBy";
                default:
                    throw new UnsupportedOperationException(this.name());
            }
        }
    }

    static boolean TRACK_CHANGES = true;
    Map<HistoryType, StringBuilder> toStringData;
    /**
     * The prefix to use for unnamed columns
     */
    static final String EMPTY_COLUMN_PREFIX = "Col$";
    /**
     * The maximum number of rows to display in the {@link #toString()} method
     */
    static final int MAX_DISPLAY_ROWS = 25;
    /**
     * The maximum number of columns to display in the {@link #toString()} method
     */
    static final int MAX_DISPLAY_COLUMNS = 11;
    /**
     * The minimum width of a row when printing
     */
    static final int MIN_WIDTH = 4;
    /**
     * The maximum width of a row when printing
     */
    static final int MAX_WIDTH = 12;
    /**
     * The string used to separate columns
     */
    static final String COLUMN_SEPARATOR = "  ";

    static final String SKIP_ROWS = "...";
    static final String SKIP_COLUMNS = "...";

    static final class DataFrameView extends DataFrameImpl {
        //todo hashmap series names
        private final DataFrame dataFrame;
        int numCols = -1;
        int[] cols;
        Series<?>[] series;
        int[] rows;
        int numRows = -1;


        public DataFrameView(DataFrame dataFrame, int[] cols) {
            super(dataFrame.getName());
            this.dataFrame = extract(dataFrame);
            this.numCols = cols.length;
            this.cols = cols;
            if (((DataFrameImpl) dataFrame).toStringData != null) {
                getHistory().putAll(((DataFrameImpl) dataFrame).getHistory());
            }
        }

        public DataFrameView(DataFrame dataFrame, int[] cols, int numCols, int[] rows, int numRows) {
            super(dataFrame.getName());
            this.dataFrame = extract(dataFrame);
            this.rows = rows;
            this.numRows = numRows;
            this.cols = cols;
            this.numCols = numCols;
            if (((DataFrameImpl) dataFrame).toStringData != null) {
                getHistory().putAll(((DataFrameImpl) dataFrame).getHistory());
            }
        }

        public DataFrameView(DataFrame dataFrame, int[] rows, int numRows) {
            super(dataFrame.getName());
            this.dataFrame = extract(dataFrame);
            this.rows = rows;
            this.numRows = numRows;
            if (((DataFrameImpl) dataFrame).toStringData != null) {
                getHistory().putAll(((DataFrameImpl) dataFrame).getHistory());
            }
        }

        public DataFrameView(DataFrame dataFrame, IntPredicate test) {
            super(dataFrame.getName());
            this.dataFrame = extract(dataFrame);
            this.cols = new int[dataFrame.numSeries()];
            int j = 0;
            int i = 0;
            while (i < dataFrame.numSeries()) {
                if (test.test(i)) {
                    cols[j++] = i;
                }
                ++i;
            }
            numCols = j;
            if (((DataFrameImpl) dataFrame).toStringData != null) {
                getHistory().putAll(((DataFrameImpl) dataFrame).getHistory());
            }
        }

        public DataFrameView(DataFrame dataFrame, Predicate<String> test) {
            super(dataFrame.getName());
            this.dataFrame = extract(dataFrame);
            this.cols = new int[dataFrame.numSeries()];
            int j = 0;
            for (int i = 0; i < dataFrame.numSeries(); ++i) {
                if (test.test(dataFrame.get(i).getName())) {
                    cols[j++] = i;
                }
            }
            numCols = j;
            if (((DataFrameImpl) dataFrame).toStringData != null) {
                getHistory().putAll(((DataFrameImpl) dataFrame).getHistory());
            }
        }

        public DataFrameView(DataFrame dataFrame, int start, int end) {
            this(dataFrame, range(start, end));
        }

        private static DataFrame extract(DataFrame d) {
            while (d.getClass() == DataFrameView.class) {
                d = ((DataFrameView) d).dataFrame;
            }
            return d;
        }

        @Override
        @SuppressWarnings("unchecked")
        public Series<Comparable<Object>> get(int series) {
            //todo bounds check
            series = cols == null ? series : cols[series];
            if (numRows == -1 && numCols == -1) {
                return dataFrame.get(series);
            } else {
                if (this.series == null) {
                    this.series = new Series[dataFrame.numSeries()];
                }
                if (this.series[series] == null) {
                    if (rows != null) {
                        switch (dataFrame.get(series).getType()) {
                            case STRING:
                                this.series[series] = new SeriesImpl.SeriesView<>(dataFrame.getStringSeries(series), rows, numRows);
                                break;
                            case LONG:
                                this.series[series] = new SeriesImpl.SeriesView<>(dataFrame.getLongSeries(series), rows, numRows);
                                break;
                            case DOUBLE:
                                this.series[series] = new SeriesImpl.SeriesView<>(dataFrame.getDoubleSeries(series), rows, numRows);
                                break;
                            case BOOLEAN:
                                this.series[series] = new SeriesImpl.SeriesView<>(dataFrame.getBooleanSeries(series), rows, numRows);
                                break;
                            default:
                                throw new UnsupportedOperationException();
                        }
                    } else {
                        this.series[series] = dataFrame.get(series);
                    }
                }
                //todo bounds checking incorporating what is visible
                return (Series<Comparable<Object>>) this.series[series];
            }
        }


        @Override
        public int numSeries() {
            return numCols == -1 ? dataFrame.numSeries() : numCols;
        }

        @Override
        public int size(Axis axis) {
            switch (axis) {
                case COLUMN:
                    return numSeries();
                case INDEX:
                default:
                    return numSeries() == 0 ? 0 : (numRows != -1 ? numRows : get(0).size());
            }
        }

        @Override
        protected DataFrame getSource() {
            return dataFrame;
        }
    }

    static final class DataFrameGroupBy implements Iterable<DataFrame> {

        private final GroupBy<?> groupBy;
        private final DataFrame dataFrame;
        private final String grouping;

        Group[] groups;

        int[] getIndices(int index) {
            final int[] indices = new int[groupBy.getGroup(index).size()];
            for (int i = 0; i < indices.length; ++i) {
                indices[i] = groupBy.getGroup(index).get(i);
            }
            return indices;
        }

        static String formatName(DataFrameGroupBy groupBy, int index) {
            //TODO merge multiple groupings
            return String.format("%s {%s: \"%s\"}", groupBy.dataFrame.getName(), groupBy.grouping, groupBy.groupBy.getGroup(index).get());
        }

        static final class Group extends DataFrameImpl {
            //TODO by name
            private final int index;
            private final DataFrameGroupBy groupBy;
            Series<?>[] series;
            int[] indices;

            public Group(DataFrameGroupBy groupBy, int index) {
                super(formatName(groupBy, index));
                this.groupBy = groupBy;
                this.index = index;
            }

            @Override
            public int numSeries() {
                return groupBy.dataFrame.numSeries();
            }

            private int[] getIndices() {
                if (indices == null) {
                    indices = groupBy.getIndices(index);
                }
                return indices;

            }

            @Override
            @SuppressWarnings("unchecked")
            public Series<Comparable<Object>> get(int series) {
                if (this.series == null) {
                    this.series = new Series[numSeries()];
                }
                //TODO make these kind of blocks more maintainable
                if (this.series[series] == null) {
                    switch (groupBy.dataFrame.get(series).getType()) {
                        case LONG:
                            this.series[series] = new SeriesImpl.SeriesView<>(groupBy.dataFrame.getLongSeries(series), getIndices());
                            break;
                        case BOOLEAN:
                            this.series[series] = new SeriesImpl.SeriesView<>(groupBy.dataFrame.getBooleanSeries(series), getIndices());
                            break;
                        case DOUBLE:
                            this.series[series] = new SeriesImpl.SeriesView<>(groupBy.dataFrame.getDoubleSeries(series), getIndices());
                            break;
                        case STRING:
                            this.series[series] = new SeriesImpl.SeriesView<>(groupBy.dataFrame.getStringSeries(series), getIndices());
                            break;
                        default:
                            throw new UnsupportedOperationException();
                    }
                }
                return (Series<Comparable<Object>>) this.series[series];
            }

            @Override
            protected DataFrame getSource() {
                return groupBy.dataFrame;
            }
        }

        public DataFrameGroupBy(DataFrame dataFrame, final String grouping, final GroupBy<?> groupBy) {
            this.groupBy = groupBy;
            this.dataFrame = dataFrame;
            this.grouping = grouping;
        }

        private Group getGroup(int index) {
            if (groups == null) {
                groups = new Group[groupBy.numGroups()];
            }
            if (groups[index] == null) {
                groups[index] = new Group(this, index);
            }
            return groups[index];
        }

        @Override
        public Iterator<DataFrame> iterator() {

            return new Iterator<DataFrame>() {
                private int i = 0;

                @Override
                public boolean hasNext() {
                    return i < groupBy.numGroups();
                }

                @Override
                public DataFrame next() {
                    return getGroup(i++);
                }
            };
        }
    }

    /**
     * Dataset from an array of series
     */
    static final class OfArray extends DataFrameImpl {
        private final Series<?>[] series;
        private HashMap<String, Series<?>> seriesMap;

        /**
         * Create a dataset from an array of series
         *
         * @param name   the name of the dataset
         * @param series the array of series
         */
        @SuppressWarnings("unchecked")
        <S extends Comparable<S>, T extends Series<? extends S>> OfArray(final String name, final T[] series) {
            super(name);
            this.series = new Series[series.length];
            int size = -1;
            int i = 0;
            for (T s : series) {
                this.series[i++] = s.getClass() == SeriesImpl.SeriesView.class ? ((SeriesImpl.SeriesView<S>) s).dataSeries : s;
                if (size == -1) {
                    size = s.size();
                    continue;
                }
                if (s.size() != size) {
                    throw new IllegalArgumentException("series must be of equal length");
                }
            }
        }

        @Override
        @SuppressWarnings("unchecked")
        public Series<Comparable<Object>> get(String name) {
            if (seriesMap == null) {
                seriesMap = new HashMap<>(series.length);
                for (Series<?> s : series) {
                    seriesMap.put(s.getName(), s);
                }
            }
            return (Series<Comparable<Object>>) seriesMap.get(name);
        }

        @Override
        @SuppressWarnings("unchecked")
        public Series<Comparable<Object>> get(int series) {
            return (Series<Comparable<Object>>) this.series[series];
        }

        @Override
        public int numSeries() {
            return series.length;
        }

    }

    /**
     * The name of the dataset
     */
    private final String name;


    /**
     * Create an abstract named dataset
     *
     * @param name the name of the dataset
     */
    protected DataFrameImpl(final String name) {
        this.name = name;
    }

    /**
     * Implementation of a dataset created from a file
     */
    static final class FromFile extends DataFrameImpl {
        private final Series<?>[] series;
        private HashMap<String, Series<Comparable<Object>>> seriesMap;

        FromFile(DataFrameImporter.FromFile importer) {
            this(importer.source, importer.separator, importer.quoteCharacter, importer.charset, importer.putativeHeader, importer.types, importer.hasColumnNames, importer.numColumns, (importer.hasColumnNames ? -1 : 0) + importer.numLines);
        }

        FromFile(File name, char separator, char quoteCharacter, Charset charset, String[] columnNames, DataType[] types, boolean hasColumnNames, int numColumns, int numRows) {
            super(name.getName());
            this.series = new Series[numColumns];
            boolean getColumnNames = columnNames == null && hasColumnNames;
            for (int i = 0; i < numColumns; ++i) {
                final String columnName = columnNames == null ? (hasColumnNames ? (EMPTY_COLUMN_PREFIX + i) : null) : columnNames[i] == null ? EMPTY_COLUMN_PREFIX + i : columnNames[i];
                switch (types[i]) {
                    case LONG:
                        series[i] = new SeriesImpl.OfLongArray(columnName, new Long[numRows]);
                        break;
                    case DOUBLE:
                        series[i] = new SeriesImpl.OfDoubleArray(columnName, new double[numRows]);
                        break;
                    case BOOLEAN:
                        series[i] = new SeriesImpl.OfBooleanArray(columnName, new boolean[numRows]);
                        break;
                    case STRING:
                        series[i] = new SeriesImpl.OfStringArray(columnName, new String[numRows]);
                        break;
                    default:
                        throw new UnsupportedOperationException();
                }

            }

            try (FileInputStream inputStream = new FileInputStream(name); Scanner scanner = new Scanner(inputStream, charset.name())) {
                if (hasColumnNames) {
                    final String colNames = scanner.nextLine();
                    if (getColumnNames) {
                        //TODO get column names

                    }
                }
                int rowCount = 0;
                while (scanner.hasNextLine()) {
                    final String line = scanner.nextLine();
                    int h = 0, o = 0;
                    while (h < line.length()) {
                        int currentO = o;
                        int row = rowCount;
                        h = iterateLine(line, h, separator, quoteCharacter, str -> {
                            switch (types[currentO]) {
                                case LONG:
                                    final SeriesImpl.OfLongArray s = (SeriesImpl.OfLongArray) series[currentO];
                                    if (LONG.matches(str)) {
                                        s.data[row] = Long.parseLong(str);
                                    }
                                    break;
                                case DOUBLE:
                                    ((SeriesImpl.OfDoubleArray) series[currentO]).data[row] = toDouble(str);
                                    break;
                                case BOOLEAN:
                                    ((SeriesImpl.OfBooleanArray) series[currentO]).data[row] = toBoolean(str);
                                    break;
                                case STRING:
                                    ((SeriesImpl.OfStringArray) series[currentO]).data[row] = str;
                                    break;
                                default:
                                    throw new UnsupportedOperationException();

                            }
                        });
                        ++o;
                    }
                    ++rowCount;
                }
                //update the size
                for (int i = 0; i < numColumns; ++i) {

                    switch (types[i]) {
                        case LONG:
                            ((SeriesImpl.OfLongArray) series[i]).end = rowCount;
                            break;
                        case DOUBLE:
                            ((SeriesImpl.OfDoubleArray) series[i]).end = rowCount;
                            break;
                        case BOOLEAN:
                            ((SeriesImpl.OfBooleanArray) series[i]).end = rowCount;
                            break;
                        case STRING:
                            ((SeriesImpl.OfStringArray) series[i]).end = rowCount;
                            break;
                        default:
                            throw new UnsupportedOperationException();
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        @SuppressWarnings("unchecked")
        public Series<Comparable<Object>> get(String name) {
            if (seriesMap == null) {
                seriesMap = new HashMap<>(series.length);
                for (Series<?> s : series) {
                    seriesMap.put(s.getName(), (Series<Comparable<Object>>) s);
                }
            }
            return seriesMap.get(name);
        }

        @Override
        @SuppressWarnings("unchecked")
        public Series<Comparable<Object>> get(int series) {
            return (Series<Comparable<Object>>) this.series[series];
        }

        @Override
        public int numSeries() {
            return this.series.length;
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        final int[] widths = new int[numSeries()];
        int rows = get(0).size();
        int halfRow = MAX_DISPLAY_ROWS >>> 1;
        int halfCols = MAX_DISPLAY_COLUMNS >>> 1;

        int rowStop = rows < MAX_DISPLAY_ROWS ? (rows >>> 1) : halfRow;
        int rowStart = rows < MAX_DISPLAY_ROWS ? rowStop : (rows - halfRow);
        int idColWidth = IteratorUtils.skippedIterator(SKIP_ROWS.length(), (n, o) -> Math.max(n, (o != -1 ? String.valueOf(get(0).getID(o)) : SKIP_COLUMNS).length()), get(0).size(), MAX_DISPLAY_ROWS);
        final StringBuilder stringBuilder = new StringBuilder(StringUtils.repeatCharacter(' ', idColWidth)).append(COLUMN_SEPARATOR);

        int colStop = numSeries() < MAX_DISPLAY_COLUMNS ? (numSeries() >>> 1) : halfCols;
        int colStart = numSeries() < MAX_DISPLAY_COLUMNS ? colStop : (numSeries() - halfCols);
        for (int i = 0; i < numSeries(); ++i) {
            if (i == colStop && colStart != colStop) {
                stringBuilder.append(SKIP_ROWS).append(COLUMN_SEPARATOR);
                i = colStart;
            }
            final String th = get(i).getName() == null ? (EMPTY_COLUMN_PREFIX + i) : get(i).getName();
            int finalI = i;
            int maxWidth = Math.min(MAX_WIDTH, IteratorUtils.skippedIterator(th.length(), (n, o) -> Math.max(n, (o != -1 ? String.valueOf(get(finalI).get(o)) : SKIP_COLUMNS).length()), get(i).size(), MAX_DISPLAY_ROWS));
            widths[i] = Math.max(maxWidth, MIN_WIDTH);

            alignRight(stringBuilder, th, widths[i]);
            stringBuilder.append(COLUMN_SEPARATOR);
        }
        stringBuilder.delete(stringBuilder.length() - COLUMN_SEPARATOR.length(), stringBuilder.length()).append('\n');

        for (int j = 0; j < rows; ++j) {
            if (j == rowStop && rowStart != rowStop) {
                alignRight(stringBuilder, SKIP_ROWS, idColWidth).append(COLUMN_SEPARATOR);
                for (int i = 0; i < numSeries(); ++i) {
                    if (i == colStop && colStop != colStart) {
                        stringBuilder.append(SKIP_ROWS).append(COLUMN_SEPARATOR);
                        i = colStart;
                    }
                    alignRight(stringBuilder, SKIP_COLUMNS, widths[i]).append(COLUMN_SEPARATOR);
                }
                stringBuilder.delete(stringBuilder.length() - COLUMN_SEPARATOR.length(), stringBuilder.length()).append('\n');
                j = rowStart;
            }

            alignRight(stringBuilder, String.valueOf(get(0).getID(j)), idColWidth).append(COLUMN_SEPARATOR);

            for (int i = 0; i < colStop; ++i) {
                alignRight(stringBuilder, i, j, widths[i]).append(COLUMN_SEPARATOR);
            }
            if (colStart != colStop) {
                stringBuilder.append(SKIP_ROWS).append(COLUMN_SEPARATOR);
            }

            for (int i = colStart; i < numSeries(); ++i) {
                alignRight(stringBuilder, i, j, widths[i]).append(COLUMN_SEPARATOR);
            }
            stringBuilder.delete(stringBuilder.length() - COLUMN_SEPARATOR.length(), stringBuilder.length()).append('\n');
        }

        stringBuilder.append(String.format("DataFrame {name: \"%s\", cols: %d, rows: %d", getName(), numSeries(), size(Axis.INDEX)));
        if (toStringData != null) {
            for (final Map.Entry<HistoryType, StringBuilder> el : getHistory().entrySet()) {
                stringBuilder.append(',').append(' ').append(el.getKey()).append(':').append(' ').append('"').append(el.getValue()).append('"');
            }
        }
        return stringBuilder.append('}').toString();
    }

    static StringBuilder alignRight(StringBuilder stringBuilder, final String td, int width, UnaryOperator<StringBuilder> trimmer) {

        if (td.length() < width) {
            return stringBuilder.append(StringUtils.repeatCharacter(' ', width - td.length())).append(td);
        } else {
            if (td.length() == width) {
                return stringBuilder.append(td, 0, width);
            }

            return trimmer.apply(stringBuilder);
        }
    }

    static StringBuilder alignRight(StringBuilder stringBuilder, final String td, int width) {
        return alignRight(stringBuilder, td, width, sb -> sb.append(td, 0, width - 3).append("..."));
    }

    @SuppressWarnings("unchecked")
    private StringBuilder alignRight(StringBuilder stringBuilder, int col, int row, int width) {
        final String td;
        switch (get(col).getType()) {
            case LONG:
                td = String.valueOf(get(col).get(row));
                break;
            case DOUBLE:
                double val = (getDoubleSeries(col)).get(row);
                td = String.valueOf(val);
                return alignRight(stringBuilder, td, width, sb -> formatDouble((val < 0 ? sb : sb.append(' ')), val, width));
            case BOOLEAN:
                td = String.valueOf((getBooleanSeries(col)).get(row));
                break;
            case STRING:
                td = getStringSeries(col).get(row);
                break;
            default:
                throw new UnsupportedOperationException();
        }
        return alignRight(stringBuilder, td, width);
    }

    private static StringBuilder formatDouble(final StringBuilder stringBuilder, double val, int width) {
        final String v = String.valueOf(val);
        if (v.indexOf('.') == -1) {
            return stringBuilder.append(v);
        }
        return stringBuilder.append(String.format(String.format("%%.%df", (width - v.indexOf('.') - 2)), val));
    }

    @Override
    public DataFrame subset(int start, int end) {
        return new DataFrameView(this, start, end);
    }

    @Override
    public DataFrame subset(Predicate<String> test) {
        return new DataFrameView(this, test);
    }

    static int[] range(int[] out, int start, int end) {
        for (int i = start, j = 0; i < end; ++i) {
            out[j++] = i;
        }
        return out;
    }

    @Override
    public DataFrame filter(BooleanSeries filter) {
        final int[] ids = new int[filter.size()];
        int size = 0;
        for (int i = 0; i < filter.size(); ++i) {
            if (filter.get(i)) {
                ids[size++] = get(0).getID(i);
            }
        }
        addFilter(filter);
        return new DataFrameView(this, ids, size);
    }

    static boolean isComparator(final char c) {
        return c == '<' || c == '>' || c == '=' || c == '!';
    }

    @Override
    //todo  | and & and consider type precedence e.g. long v double should compare as double
    public DataFrame query(String query) {
        int nameStart = 0;
        int nameEnd = -1;
        int opStart = 0;
        int opEnd = -1;
        int valStart = 0;
        int valEnd = -1;
        int i = 0;
        while (true) {
            char c = query.charAt(i);
            if (c != ' ') {
                if (isComparator(c)) {
                    opStart = i;
                    opEnd = isComparator(query.charAt(i + 1)) ? i + 2 : i + 1;
                    i = opEnd;
                    continue;
                } else if (c == '`') {
                    if (nameEnd != -1) {
                        throw new IllegalArgumentException();
                    }
                    nameStart = i + 1;
                    nameEnd = nameStart;
                    while (query.charAt(nameEnd) != '`') {
                        nameEnd++;

                    }
                    i = nameEnd;
                } else {
                    //todo consider escape characters
                    if (nameEnd != -1) {
                        valStart = i;
                        valEnd = valStart;
                        while (query.charAt(valEnd) != ' ' && !isComparator(query.charAt(valEnd))) {
                            valEnd++;
                            if (valEnd == query.length()) {
                                break;
                            }
                        }
                        i = query.length();
                    } else {
                        //TODO check for correct quotes
                        nameStart = i;
                        nameEnd = nameStart;
                        while (query.charAt(nameEnd) != ' ' && !isComparator(query.charAt(nameEnd))) {
                            nameEnd++;
                        }
                        i = nameEnd - 1;

                    }
                }
            }
            ++i;

            if (i >= query.length()) {
                final Series<?> series = get(query.substring(nameStart, nameEnd));
                switch (series.getType()) {
                    case STRING:
                        return filter(series.getName(), (Predicate<String>) query0(this, query, nameStart, nameEnd, opStart, opEnd, valStart + 1, valEnd - 1, it -> it));
                    case BOOLEAN:
                        return filter(series.getName(), (Predicate<Boolean>) query0(this, query, nameStart, nameEnd, opStart, opEnd, valStart, valEnd, DataType::toBoolean));
                    case LONG:
                        return filter(series.getName(), (Predicate<Long>) query0(this, query, nameStart, nameEnd, opStart, opEnd, valStart, valEnd, DataType::toLong));
                    case DOUBLE:
                        return filter(series.getName(), (Predicate<Double>) query0(this, query, nameStart, nameEnd, opStart, opEnd, valStart, valEnd, DataType::toDouble));
                    default:
                        throw new UnsupportedOperationException();
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    static <U extends Comparable<U>> Predicate<U> query0(final DataFrame source, String query, int nameStart, int nameEnd, int opStart, int opEnd, int valStart, int valEnd, Function<String, U> converter) {
        final String value = query.substring(valStart, valEnd);
        final Series<?> series = source.get(query.substring(nameStart, nameEnd));
        int len = opEnd - opStart;
        if (len == 2 && query.charAt(opEnd - 1) != '=') {
            throw new IllegalArgumentException();
        }
        final U val = converter.apply(value);
        switch (query.charAt(opStart)) {
            case '<':
            case '>':
                if (len == 2) {
                    if (query.charAt(opStart) == '<') {
                        return it -> it.compareTo(val) <= 0;
                    } else {
                        return it -> it.compareTo(val) >= 0;
                    }
                } else if (len == 1) {
                    if (query.charAt(opStart) == '<') {
                        return it -> it.compareTo(val) < 0;
                    } else {
                        return it -> it.compareTo(val) > 0;
                    }
                } else {
                    throw new IllegalArgumentException();
                }
            case '=':
            case '!':
                if (len != 2) {
                    throw new IllegalArgumentException();
                }
                if (query.charAt(opStart) == '=') {
                    return it -> it.equals(val);
                } else {
                    return it -> !it.equals(val);
                }
            default:
                throw new UnsupportedOperationException();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <S extends Comparable<S>> DataFrame filter(String series, Predicate<S> test) {
        final Series<?> s = get(series);
        if (s == null) {
            throw new IllegalArgumentException("Could not find");
        }
        switch (s.getType()) {
            case LONG:
                return filter(s.asLong().map((Predicate<Long>) test));
            case BOOLEAN:
                return filter(s.asBoolean().map((Predicate<Boolean>) test));
            case STRING:
                return filter(s.asString().map((Predicate<String>) test));
            case DOUBLE:
                return filter(s.asDouble().map((Predicate<Double>) test));
            default:
                throw new UnsupportedOperationException();
        }
    }

    static int[] range(int start, int end) {
        return range(new int[end - start], start, end);
    }

    @SuppressWarnings("unchecked")
    static <S extends Comparable<S>, T extends Series<S>> DataFrame createSubset(final DataFrame dataFrame, String[] names) {
        @SuppressWarnings("unchecked") final T[] series = (T[]) new Series[names.length];
        for (int j = 0; j < names.length; ++j) {
            boolean found = false;
            for (int i = 0; i < dataFrame.numSeries(); ++i) {
                final Object c = dataFrame.get(names[j]);
                if (c != null) {
                    series[j] = (T) c;
                    found = true;
                    break;
                }

            }
            if (!found) {
                throw new IllegalArgumentException("Could not find column by name " + names[j]);
            }
        }
        return new DataFrameImpl.OfArray(dataFrame.getName(), series);
    }

    Map<HistoryType, StringBuilder> getHistory() {
        if (toStringData == null) {
            toStringData = new LinkedHashMap<>();
        }
        return toStringData;
    }

    @Override
    public DataFrame subset(String... names) {
        return createSubset(this, names);
    }

    private DataFrame sortBySeries(final SeriesImpl<?> series, boolean ascending) {
        final int[] ids;
        int numIds;
        if (getClass() == DataFrameView.class) {
            ids = ((DataFrameView) this).rows == null ? range(0, size(Axis.INDEX)) : ((DataFrameView) this).rows;
            numIds = ((DataFrameView) this).numRows == -1 ? size(Axis.INDEX) : ((DataFrameView) this).numRows;
        } else if (getClass() == DataFrameGroupBy.Group.class) {
            numIds = size(Axis.INDEX);
            ids = new int[numIds];
            for (int i = 0; i < numIds; ++i) {
                ids[i] = this.get(0).getID(i);
            }
        } else {
            ids = range(0, size(Axis.INDEX));
            numIds = ids.length;
        }
        series.sortArgs(ids, numIds, ascending);
        if (TRACK_CHANGES) {
            getHistory().put(ascending ? HistoryType.SORT_BY : HistoryType.SORT_BY_DESC, new StringBuilder(series.getName()));
        }
        if (getClass() == DataFrameView.class) {
            return new DataFrameView(getSource(), ((DataFrameView) this).cols, ((DataFrameView) this).numCols, ids, numIds);
        } else if (getClass() == DataFrameGroupBy.Group.class) {
            //TODO simplify
            DataFrame df = this;
            while (true ) {
                if (df.getClass() == DataFrameGroupBy.Group.class) {
                    df = ((DataFrameGroupBy.Group) df).groupBy.dataFrame;
                } else if(df.getClass() == DataFrameView.class){
                    df = ((DataFrameView) df).dataFrame;
                }else{
                    break;
                }
            }
            return new DataFrameView(df, ids, numIds);
        }
        return new DataFrameView(getSource(), ids, numIds);
    }

    private void addFilter(final Series<?> series) {
        if (!TRACK_CHANGES) {
            return;
        }
        final StringBuilder sn = getHistory().get(HistoryType.FILTER);
        if (sn == null) {
            getHistory().put(HistoryType.FILTER, new StringBuilder(series.getName()));
        } else {
            sn.append(", ").append(series.getName());
        }
    }

    @Override
    public DataFrame sortBy(int index, boolean ascending) {
        return sortBySeries((SeriesImpl<?>) DataFrameView.extract(this).get(index), ascending);
    }

    @Override
    public DataFrame sortBy(String name, boolean ascending) {
        return sortBySeries((SeriesImpl<?>) DataFrameView.extract(this).get(name), ascending);
    }

    protected DataFrame getSource() {
        return this;
    }

    @Override
    public Iterable<DataFrame> groupBy(String name) {
        return new DataFrameGroupBy(this, get(name).getName(), new GroupBy<>(get(name)));
    }
}
