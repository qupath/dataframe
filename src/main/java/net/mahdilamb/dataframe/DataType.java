package net.mahdilamb.dataframe;

import net.mahdilamb.dataframe.utils.StringUtils;

import java.util.regex.Pattern;


/**
 * Datatype to be used by dataseries
 */
public enum DataType {
    /**
     * A 64-bit integer. If a value cannot be parsed, it will be 0, though the index will be stored as NaN. This
     * can be retrieved using the {@link NumericSeries#isNaN} method
     *
     * @apiNote Series that contain longs should also contain a {@code #isNaN(int index)} method to test whether it has
     * been zeroed during parse.
     */
    LONG(10, StringUtils.LONG_PATTERN, false),
    /**
     * A 64-bit float. If a value cannot be parsed, it will be {@code NaN}.
     */
    DOUBLE(9, StringUtils.FLOATING_POINT_PATTERN_WITHOUT_HEX, true),
    /**
     * Values are either true or false. If a value cannot be parsed, it will be false.
     */
    BOOLEAN(8, StringUtils.BOOLEAN_PATTERN, false),
    /**
     * String type
     */
    STRING(0, Pattern.compile("(?:.*)"), true);

    /**
     * Test if a data type is numeric
     *
     * @param type the data type
     * @return whether the type is numeric
     */
    public static boolean isNumeric(DataType type) {
        return type == LONG || type == DOUBLE;
    }

    /**
     * Test whether a string can be converted to this type
     *
     * @param string the string
     * @return whether this string can be cast as the given type
     */
    public boolean matches(String string) {
        return matcher.matcher(string).matches();
    }

    /**
     * @return whether the data type supports null types
     */
    public boolean supportsNull() {
        return supportsNull;
    }

    /**
     * Test whether a string can be convert to this type
     *
     * @param string the string
     * @param start  the start index
     * @param end    the end index
     * @return whether the string can be cast to this type
     */
    public boolean matches(String string, int start, int end) {
        return string != null && matcher.matcher(string).region(start, end).matches();
    }

    /**
     * Cast a string to a double
     *
     * @param value the string
     * @return a double of the string if parsable, or {@code NaN} if not
     */
    public static double toDouble(String value) {
        if (value != null && value.length() > 0 && StringUtils.FLOATING_POINT_PATTERN.matcher(value).matches()) {
            return java.lang.Double.parseDouble(value);
        }
        return java.lang.Double.NaN;
    }

    /**
     * Convert a boolean to a double
     *
     * @param value the boolean value
     * @return a double representation
     */
    public static double toDouble(boolean value) {
        return value ? 1. : 0.;
    }

    /**
     * Convert a long to a double
     *
     * @param value the long
     * @return a double representation of the long
     */
    public static double toDouble(long value) {
        return value;
    }

    /**
     * Convert a double to a long
     *
     * @param value the double
     * @return the long (retains the integer part)
     */
    public static long toLong(double value) {
        return (long) value;
    }

    /**
     * Convert a boolean to a long
     *
     * @param value the boolean
     * @return a long of the boolean
     */
    public static long toLong(boolean value) {
        return value ? 1 : 0;
    }

    /**
     * Convert a string to a long
     *
     * @param value the string
     * @return the value as a long. Or 0 if not parsable.
     */
    public static long toLong(String value) {
        if (StringUtils.LONG_PATTERN.matcher(value).matches()) {
            try {
                return java.lang.Long.parseLong(value);
            } catch (NumberFormatException ignored) {

            }
        }
        return 0;
    }

    /**
     * Convert a boolean to a double
     *
     * @param value the value to convert
     * @return the double as a boolean
     */
    public static boolean toBoolean(double value) {
        return value != 0;
    }

    /**
     * Convert a long to a boolean
     *
     * @param value the value
     * @return the value as a boolean
     */
    public static boolean toBoolean(long value) {
        return value != 0;
    }

    /**
     * Only the words that are case-insensitively "true" are true. All others are false
     *
     * @param value the value to convert
     * @return the value converted to a boolean
     */
    public static boolean toBoolean(String value) {
        return Boolean.parseBoolean(value);
    }

    /**
     * Convert a string to a boolean using a lenient version of truthiness (0 length strings are false, all else are true)
     *
     * @param value the value to parse
     * @return the value as a boolean
     */
    public static boolean toBooleanLenient(String value) {
        return value.length() > 0;
    }

    /**
     * Get a string representation of a boolean
     *
     * @param value the boolean
     * @return the boolean as a string
     */
    public static String toString(boolean value) {
        return Boolean.toString(value);
    }

    /**
     * Get a string representation of a double
     *
     * @param value the double
     * @return the double as a string
     */
    public static String toString(double value) {
        return Double.toString(value);
    }

    /**
     * Get a string representation of a long
     *
     * @param value the long
     * @return the long as a string
     */
    public static String toString(long value) {
        return Long.toString(value);
    }

    /**
     * The score represents the preferred type in auto-selecting if all the values could be either of a type.
     * <p>
     * E.g. if all the values in a column could be Long or Double, then the weight decides which is chosen
     */
    protected final int score;
    private final Pattern matcher;
    private final boolean supportsNull;

    DataType(int score, Pattern matcher, boolean supportsNull) {
        this.score = score;
        this.matcher = matcher;
        this.supportsNull = supportsNull;
    }

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}
