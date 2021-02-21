package net.mahdilamb.charts.dataframe;

import net.mahdilamb.charts.dataframe.utils.StringUtils;

import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.PrimitiveIterator;
import java.util.function.Function;
import java.util.function.Predicate;

import static net.mahdilamb.charts.dataframe.utils.StringUtils.getStringFromClipboard;

/**
 * Datasets are a series of named series. They can be thought of as a table.
 */
public interface DataFrame extends Iterable<Series<Comparable<Object>>> {
    /**
     * The default quote character to use when reading a text file
     */
    char DEFAULT_QUOTE_CHARACTER = '"';
    /**
     * The default charset to use when reading a text file
     */
    Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    /**
     * @return the name of the dataset
     */
    String getName();

    /**
     * @param series the index of the series
     * @return the type of the series
     */
    default DataType getType(int series) {
        return get(series).getType();
    }

    /**
     * @param series the name of the series
     * @return the type of the series
     */
    default DataType getType(final String series) {
        return get(series).getType();
    }

    /**
     * @return the number of series in this dataset
     */
    int numSeries();

    /**
     * Get a series from an index.
     *
     * @param series the index of interest
     * @return the series at the index
     */
    Series<Comparable<Object>> get(final int series);

    /**
     * @param name name of the series
     * @return series by its name or {@code null} if series not found
     */
    default Series<Comparable<Object>> get(final String name) {
        for (int i = 0; i < numSeries(); ++i) {
            final Series<Comparable<Object>> s = get(i);
            if (s.getName().compareTo(name) == 0) {
                return s;
            }
        }
        System.err.println("No series could be found with the name " + name);
        return null;
    }

    /**
     * @param names the names of the columns of interest
     * @return a new data frame with the column names as specified
     */
    DataFrame subset(String... names);

    /**
     * Get a value from a series
     *
     * @param series the index of the series
     * @param index  the index in the series
     * @return the value in the given series
     * @apiNote the return type is intentionally an object. For primitive return types, get a series of known
     * type using the relevant methods
     */
    default Comparable<Object> get(int series, int index) {
        return get(series).get(index);
    }

    /**
     * Get an iterable over the names of the series
     */
    default Iterable<String> seriesNames() {
        return () -> new Iterator<String>() {
            private int i = 0;

            @Override
            public boolean hasNext() {
                return i < numSeries();
            }

            @Override
            public String next() {
                return get(i++).getName();
            }
        };
    }

    /**
     * Get an iterable over the datatypes of the series
     */
    default Iterable<DataType> dataTypes() {
        return () -> new Iterator<DataType>() {
            private int i = 0;

            @Override
            public boolean hasNext() {
                return i < numSeries();
            }

            @Override
            public DataType next() {
                return get(i++).getType();
            }
        };
    }

    /**
     * @return a list of the series names
     */
    default List<String> listSeriesNames() {
        final List<String> result = new ArrayList<>(numSeries());
        seriesNames().forEach(result::add);
        return result;
    }

    /**
     * @return a list of the series names
     */
    default List<DataType> listDataTypes() {
        final List<DataType> result = new ArrayList<>(numSeries());
        dataTypes().forEach(result::add);
        return result;
    }

    /**
     * @return an iterable over the indices of the items in the dataset
     */
    default Iterable<Integer> indices() {
        return () -> new PrimitiveIterator.OfInt() {
            private int i = 0;

            @Override
            public int nextInt() {
                return i++;
            }

            @Override
            public boolean hasNext() {
                return i < get(0).size();
            }
        };
    }

    /**
     * @return the first series
     */
    default Series<Comparable<Object>> first() {
        return get(0);
    }

    /**
     * @return the last series
     */
    default Series<Comparable<Object>> last() {
        return get(numSeries() - 1);
    }

    /**
     * Return a subset of series
     *
     * @param start the start index (inclusive)
     * @param end   the end index (exclusive)
     * @return a dataframe
     * @implNote this will return a sliced view into the dataframe
     */
    DataFrame subset(int start, int end);

    /**
     * Get a subset of the series based on a test of the series names
     *
     * @param test the test of the series names
     * @return a sliced view of this data frame
     */
    DataFrame subset(Predicate<String> test);

    DataFrame filter(BooleanSeries filter);

    <S extends Comparable<S>> DataFrame filter(String series, Predicate<S> test);

    default DataFrame filter(Function<DataFrame, BooleanSeries> filter) {
        return filter(filter.apply(this));
    }


    /**
     * Perform a simple query on the dataframe. Currently only supports single equality operators
     *
     * @param query the query
     * @return a dataframe that is the subset as specified by the query
     */
    DataFrame query(final String query);

    DataFrame sortBy(final int index, boolean ascending);

    DataFrame sortBy(final String name, boolean ascending);

    default DataFrame sortBy(final int index) {
        return sortBy(index, true);
    }

    default DataFrame sortBy(final String name){
        return sortBy(name, true);
    }

    Iterable<DataFrame> groupBy(final String name);

    /**
     * Get the series at the specified index
     *
     * @param index the index of the series
     * @return the series
     */
    default DoubleSeries getDoubleSeries(final int index) throws SeriesCastException {
        return get(index).asDouble();
    }

    default DoubleSeries getDoubleSeries(final String seriesName) throws SeriesCastException {
        return get(seriesName).asDouble();
    }

    default StringSeries getStringSeries(final String seriesName) throws SeriesCastException {
        return get(seriesName).asString();
    }

    default LongSeries getLongSeries(final String seriesName) throws SeriesCastException {
        return get(seriesName).asLong();
    }

    /**
     * Get the series at the specified index
     *
     * @param index the index of the series
     * @return the series
     * @throws SeriesCastException if the series cannot be cast to a boolean series
     */
    default BooleanSeries getBooleanSeries(final int index) throws SeriesCastException {
        return get(index).asBoolean();
    }

    /**
     * Get the series at the specified index
     *
     * @param index the index of the series
     * @return the series
     * @throws SeriesCastException if the series cannot be cast to a long series
     */
    default LongSeries getLongSeries(final int index) throws SeriesCastException {
        return get(index).asLong();
    }

    /**
     * Get a string a string from the specified index.
     *
     * @param index the index of index
     * @return a string series at the index
     * @throws SeriesCastException if the series cannot be cast to a string series
     */
    default StringSeries getStringSeries(final int index) throws SeriesCastException {
        return get(index).asString();
    }

    /**
     * Get the size of an axis in the data frame
     *
     * @param axis the axis
     * @return the size of the axis
     */
    default int size(Axis axis) {
        switch (axis) {
            case COLUMN:
                return numSeries();
            case INDEX:
            default:
                return numSeries() == 0 ? 0 : get(0).size();
        }
    }

    /**
     * @return the number of elements in the data frame
     */
    default int size() {
        return size(Axis.COLUMN) * size(Axis.INDEX);
    }

    /**
     * @return an iterator over the names of the series
     */
    @Override
    default Iterator<Series<Comparable<Object>>> iterator() {
        return new Iterator<Series<Comparable<Object>>>() {
            private int i = 0;

            @Override
            public boolean hasNext() {
                return i < numSeries();
            }

            @Override
            public Series<Comparable<Object>> next() {
                return get(i++);
            }
        };
    }

    /**
     * Factory method to create a dataset from an array of numeric series
     *
     * @param name   the name of the dataset
     * @param series the array of series
     * @return a dataset wrapping the series
     */
    @SafeVarargs
    static <S extends Comparable<S>, T extends Series<? extends S>> DataFrame from(final String name, T... series) {
        return new DataFrameImpl.OfArray(name, series);
    }

    /**
     * Get an importer to use while importing a text file
     *
     * @param source         the source of the file
     * @param separator      the separator (e.g. comma or tab)
     * @param quoteCharacter the quote character
     * @param charset        the character set used to read the file
     * @return a dataset importer.
     * @see DataFrameImporter
     */
    static DataFrameImporter importer(final File source, char separator, char quoteCharacter, Charset charset) {
        return new DataFrameImporter.FromFile(source, separator, quoteCharacter, charset, true);
    }

    /**
     * Get an text file importer which guesses the separator based on the file extension and using the defaults
     * {@link #DEFAULT_CHARSET} and {@link #DEFAULT_QUOTE_CHARACTER} to read the text file
     *
     * @param file the file
     * @return a dataset importer
     */
    static DataFrameImporter importer(final File file) {
        final String ext = StringUtils.getLastCharactersToLowerCase(new char[4], file.getName());
        switch (ext) {
            case ".csv":
                return importer(file, ',', DEFAULT_QUOTE_CHARACTER, DEFAULT_CHARSET);
            case ".tsv":
                return importer(file, '\t', DEFAULT_QUOTE_CHARACTER, DEFAULT_CHARSET);
            default:
                throw new UnsupportedOperationException("Reading " + file + " is not currently supported");
        }
    }

    /**
     * Create a dataset from a file, skipping the import checking phase
     *
     * @param source         the file to import
     * @param separator      the character separator
     * @param quoteCharacter the quote character used in the file
     * @param charset        the character set used by the file
     * @return a dataset from the file
     */
    static DataFrame from(final File source, char separator, char quoteCharacter, Charset charset) {
        return new DataFrameImporter.FromFile(source, separator, quoteCharacter, charset, false).build();
    }


    /**
     * Create a dataset from a file, skipping the import phase. Uses the defaults as described in
     * {@link #importer(File, char, char, Charset)}
     *
     * @param file the file to import
     * @return a dataset from the file
     */
    static DataFrame from(File file) {
        final String ext = StringUtils.getLastCharactersToLowerCase(new char[4], file.getName());
        switch (ext) {
            case ".csv":
                return from(file, ',', DEFAULT_QUOTE_CHARACTER, DEFAULT_CHARSET);
            case ".tsv":
                return from(file, '\t', DEFAULT_QUOTE_CHARACTER, DEFAULT_CHARSET);
            default:
                throw new UnsupportedOperationException("Reading " + file + " is not currently supported");
        }
    }

    //TODO
    static DataFrameImporter clipboardImport(char separator, char quoteCharacter, Charset charset) throws IOException, UnsupportedFlavorException {
        return new DataFrameImporter.FromString(getStringFromClipboard(), separator, quoteCharacter, charset, true);
    }


}
