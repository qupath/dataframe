package net.mahdilamb.dataframe;

import net.mahdilamb.dataframe.utils.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

import static net.mahdilamb.dataframe.DataType.STRING;


/**
 * Builder for importing data from a file or strong.
 */
public abstract class DataFrameImporter {

    /**
     * @return a temporary dataframe with the top n rows.
     */
    public final DataFrame preview() {
        preparePreview();
        return currentPreview;
    }

    /**
     * @return the final dataframe. If this is reused, it will create a new dataframe with the same backing data
     */
    public abstract DataFrame build();

    /**
     * Set a column type
     *
     * @param index the index of the column
     * @param type  the type of the column
     * @return this importer
     */
    public DataFrameImporter setType(int index, DataType type) {
        types[index] = Objects.requireNonNull(type);
        return clear();
    }

    /**
     * Set whether this file has column names
     *
     * @param hasColumnNames whether the file has column names
     * @return this data frame importer
     */
    public DataFrameImporter setHasColumnNames(boolean hasColumnNames) {
        this.hasColumnNames = hasColumnNames;
        return clear();
    }

    /**
     * @return the name of the data frame to be created
     */
    protected abstract String getName();

    static int TEST_LINES = 20;
    String[] putativeHeader;
    protected String[] previewLines;

    char separator;
    Charset charset;
    char quoteCharacter;

    /**
     * Fields that should be calculated in the initial read
     */
    protected int numLines;
    protected int numColumns;
    protected DataType[] types;
    protected transient DataFrame currentPreview;

    /**
     * Fields that should be populated with user input
     */
    protected boolean hasColumnNames;

    private DataFrameImporter() {

    }

    protected DataFrameImporter clear() {
        currentPreview = null;
        return this;
    }

    static final class FromFile extends DataFrameImporter {

        final File source;

        FromFile(final File source, char separator, char quoteCharacter, Charset charset, boolean storePreview) {
            this.source = source;
            this.separator = separator;
            this.charset = charset;
            this.quoteCharacter = quoteCharacter;
            numLines = 1;
            try (FileInputStream inputStream = new FileInputStream(source); Scanner scanner = new Scanner(inputStream, charset.name())) {
                String line = scanner.nextLine();
                int[] headerNameLength = new int[20];
                char quote = 0;
                int g = 0, k = 1;
                for (int j = 0; g < line.length(); ++g, ++k) {
                    char c = line.charAt(g);
                    if (isSeparator(c) && quote == 0) {
                        if (j >= headerNameLength.length) {
                            headerNameLength = Arrays.copyOf(headerNameLength, headerNameLength.length + 20);
                        }
                        headerNameLength[j++] = k - 1;
                        k = 0;
                        numColumns++;
                        quote = 0;
                    } else {
                        if (isQuote(c)) {
                            quote = quote != c ? c : 0;
                        }
                    }
                }
                if (g == line.length()) {
                    if (g >= headerNameLength.length) {
                        headerNameLength = Arrays.copyOf(headerNameLength, headerNameLength.length + 1);
                    }
                    headerNameLength[numColumns] = k - 1;
                    numColumns++;
                }
                putativeHeader = new String[numColumns];
                types = new DataType[numColumns];
                if (numColumns > 1) {
                    for (int i = 0, l = 0; i < numColumns; l += headerNameLength[i] + 1, ++i) {
                        if (headerNameLength[i] == 0) {
                            putativeHeader[i] = DataFrameImpl.EMPTY_COLUMN_PREFIX + i;
                        } else {
                            int s = l, e = l + headerNameLength[i];
                            if (line.charAt(s) == line.charAt(e - 1) && isQuote(line.charAt(s))) {
                                ++s;
                                --e;
                            }
                            putativeHeader[i] = line.substring(s, e);
                        }
                    }
                } else {
                    if (line.length() == 0) {
                        putativeHeader[0] = DataFrameImpl.EMPTY_COLUMN_PREFIX + "0";
                    } else {
                        if (isQuote(line.charAt(0)) && line.charAt(0) == line.charAt(line.length() - 1)) {
                            putativeHeader[0] = line.substring(1, line.length() - 1);
                        } else {
                            putativeHeader[0] = line;
                        }
                    }
                }

                int scanCount = 0;
                final DataType[] seriesTypes = DataType.values();

                final int[] typeCounts = new int[seriesTypes.length * numColumns];
                if (storePreview) {
                    previewLines = new String[TEST_LINES];
                }
                while (scanner.hasNextLine()) {
                    line = scanner.nextLine();
                    if (storePreview && scanCount < TEST_LINES) {
                        previewLines[scanCount] = line;
                    }
                    int h = 0, o = 0;
                    while (true) {
                        int currentO = o;
                        h = StringUtils.iterateLine(line, h, separator, quoteCharacter, str -> updateCounts(currentO, seriesTypes, typeCounts, str));
                        if (h >= line.length()){
                            break;
                        }
                        ++o;
                    }
                    ++scanCount;
                    ++numLines;
                }
                for (int i = 0; i < numColumns; ++i) {
                    DataType favored = null;
                    int favoredScored = 0;
                    for (int j = 0; j < seriesTypes.length; ++j) {
                        int count = typeCounts[i * seriesTypes.length + j];
                        DataType t = seriesTypes[j];
                        if (favored == null || count >= favoredScored) {
                            if (favored == null || count > favoredScored) {
                                favored = t;
                                favoredScored = count;
                            } else {
                                if (favored.score < t.score) {
                                    favored = t;
                                }
                            }
                        }
                    }
                    types[i] = favored;
                }
                guessHasColumnNames();
                while (scanner.findWithinHorizon(StringUtils.linePattern, 0) != null) {
                    numLines++;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        protected static void updateCounts(int o, DataType[] types, int[] counts, final String str) {
            for (int b = 0; b < types.length; ++b) {
                final DataType t = types[b];
                if (str.length() == 0) {
                    if (t.supportsNull()) {
                        counts[o * types.length + b] += 1;
                    }
                } else if (t.matches(str)) {
                    counts[o * types.length + b] += 1;
                }
            }

        }

        @Override
        public String toString() {
            return String.format("DatasetImporter {file: '%s'}", source);
        }

        @Override
        public DataFrame build() {
            return new DataFrameImpl.FromFile(this);
        }

        @Override
        protected String getName() {
            return source.getName();
        }
    }

    @SuppressWarnings("unchecked")
    protected <S extends Comparable<S>, T extends Series<? extends S>> void preparePreview() {
        if (currentPreview == null) {
            int rows = Math.min(TEST_LINES, numLines - 1);

            final String[][] data = new String[numColumns][rows];
            for (int i = 0; i < numColumns; ++i) {
                data[i] = new String[rows];
            }
            if (hasColumnNames) {
                for (int i = 0; i < rows; ++i) {
                    final String line = previewLines[i];
                    int h = 0, o = 0;
                    while (h < line.length()) {
                        int col = o;
                        int row = i;
                        h = StringUtils.iterateLine(line, h, separator, quoteCharacter, str -> data[col][row] = str);
                        ++o;
                    }
                }
            } else {
                for (int j = 0; j < numColumns; ++j) {
                    data[j][0] = putativeHeader[j];
                }
                for (int i = 1; i < data[0].length; ++i) {
                    final String line = previewLines[i];
                    int h = 0, o = 0;
                    while (h < line.length()) {
                        int col = o;
                        int row = i;
                        h = StringUtils.iterateLine(line, h, separator, quoteCharacter, str -> data[col][row] = str);
                        ++o;
                    }
                }
            }
            final T[] series = (T[]) new Series[numColumns];
            for (int i = 0; i < numColumns; ++i) {
                switch (types[i]) {
                    case LONG:
                        series[i] = (T) new SeriesImpl.OfStringToLongArray(null, data[i]);
                        break;
                    case DOUBLE:
                        series[i] = (T) new SeriesImpl.OfStringToDoubleArray(null, data[i]);
                        break;
                    case BOOLEAN:
                        series[i] = (T) new SeriesImpl.OfStringToBooleanArray(null, data[i]);
                        break;
                    case STRING:
                        series[i] = (T) new SeriesImpl.OfStringArray(null, data[i]);
                        break;
                    default:
                        throw new UnsupportedOperationException();
                }
            }
            currentPreview = new DataFrameImpl.OfArray(getName(), series);

        }

    }

    protected boolean isQuote(char c) {
        return c == quoteCharacter;
    }

    protected boolean isSeparator(char c) {
        return c == separator;
    }

    protected void guessHasColumnNames() {
        hasColumnNames = false;
        for (int i = 0; i < numColumns; ++i) {
            if (types[i] != STRING && !types[i].matches(putativeHeader[i])) {
                hasColumnNames = true;
                break;
            }
        }
    }

}
