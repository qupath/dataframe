package net.mahdilamb.dataframe.utils;


import net.mahdilamb.dataframe.functions.CharacterPredicate;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.regex.Pattern;

/**
 * Utility class for working with strings
 */
public final class StringUtils {
    private StringUtils() {

    }

    /**
     * Powers of 10 that fit into integer range
     */
    private static final long[] POWERS_OF_10 = {
            1,
            10,
            100,
            1000,
            10000,
            100000,
            1000000,
            10000000,
            100000000,
            1000000000,
            10000000000L,
            100000000000L,
            1000000000000L,
            10000000000000L,
            100000000000000L,
            1000000000000000L,
            10000000000000000L,
            100000000000000000L,
            1000000000000000000L
    };

    /**
     * Regex pattern
     */
    public static final String intPattern = "(?:-[1-9]|-214748364[0-8]|-?[1-9]\\d{1,8}|-?1\\d{9}|-?20\\d{8}|-?21[0-3]\\d{7}|-?214[0-6]\\d{6}|-?2147[0-3]\\d{5}|-?21474[0-7]\\d{4}|-?214748[0-2]\\d{3}|-?2147483[0-5]\\d{2}|-?21474836[0-3]\\d|\\d|214748364[0-7])";

    /**
     * Floating point pattern that allows for strings that can be parsed by  {@link Double#parseDouble}, but excludes
     * hexadecimal representation.
     */
    public final static String fpPatternWithoutHex = "[+-]?(?:NaN|Infinity|(?:\\d++(?:\\.\\d*+)?|\\.\\d++)(?:[eE][+-]?\\d++)?[fFdD]?)";
    /**
     * Floating point pattern that represents valid strings that can be parsed by {@link Double#parseDouble}.
     * Adapted from Google Guava
     */
    public final static String fpPattern = fpPatternWithoutHex.substring(0, fpPatternWithoutHex.length() - 1) + "|0[xX](?:[0-9a-fA-F]++(?:\\.[0-9a-fA-F]*+)?|\\.[0-9a-fA-F]++)[pP][+-]?\\d++[fFdD]?)";
    /**
     * Regex pattern that matches integers
     */
    public final static String longPattern = "[+-]?(?:\\d++[Ll]?)";
    /**
     * Regex pattern that matches true or false, case-insensitive
     */
    public final static String boolPattern = "(?:[Tt][Rr][Uu][Ee]|[Ff][Aa][Ll][Ss][Ee]|[01])";
    /**
     * Regex pattern for a line with a terminator
     * Copied from JDK
     */
    public static final String linePattern = ".*(\r\n|[\n\r\u2028\u2029\u0085])|.+$";
    /**
     * The compiled pattern for {@link #fpPatternWithoutHex}
     */
    public final static Pattern FLOATING_POINT_PATTERN_WITHOUT_HEX = Pattern.compile(fpPatternWithoutHex);
    /**
     * The compiled pattern for {@link #fpPattern}
     */
    public final static Pattern FLOATING_POINT_PATTERN = Pattern.compile(fpPattern);
    /**
     * The compiled pattern for {@link #longPattern}
     */
    public final static Pattern LONG_PATTERN = Pattern.compile(longPattern);

    /**
     * Regex pattern to search if a number is in the range of an int
     */
    public final static Pattern INT_PATTERN = Pattern.compile(intPattern);

    /**
     * The compiled pattern for {@link #boolPattern}
     */
    public final static Pattern BOOLEAN_PATTERN = Pattern.compile(boolPattern);
    /**
     * Regex patter to test for a line
     */
    public final static Pattern LINE_PATTERN = Pattern.compile(linePattern);

    /**
     * Return the last n characters of a string. The number is defined by the length of the output array
     *
     * @param out    the output array
     * @param string the string
     * @return the last n characters of a string.
     * @throws IndexOutOfBoundsException if the length of the output array is longer than the input string (not fail-fast)
     */
    public static String getLastCharacters(final char[] out, final String string) {
        int i = string.length();
        int j = out.length;
        while (i > 0 && j > 0) {
            out[--j] = string.charAt(--i);
        }
        return new String(out);
    }

    /**
     * Return the last n characters of a string (converted to lower case).
     * The number is defined by the length of the output array
     *
     * @param out    the output array
     * @param string the string
     * @return the last n characters of a string.
     * @throws IndexOutOfBoundsException if the length of the output array is longer than the input string (not fail-fast)
     */
    public static String getLastCharactersToLowerCase(final char[] out, final String string) {
        int i = string.length();
        int j = out.length;
        while (i > 0 && j > 0) {
            out[--j] = Character.toLowerCase(string.charAt(--i));
        }
        return new String(out);
    }

    /**
     * Iterate over a line, separated by the provided separator
     *
     * @param line      the line to iterate over
     * @param offset    the starting position
     * @param sepChar   the cell separator e.g. comma or tab
     * @param quoteChar the quote character
     * @param func      the function to apply to the cell
     * @return the ending position
     */
    public static int iterateLine(String line, final int offset, char sepChar, char quoteChar, Consumer<String> func) {
        int end = offset;
        boolean trimQuotes = line.charAt(offset) == quoteChar;
        if (line.charAt(offset) == '\\') {
            if ((offset + 1) < line.length() && line.charAt(offset + 1) == quoteChar) {
                trimQuotes = true;
            }
        }
        if (trimQuotes) {
            ++end;
            while (end < line.length()) {
                final char c = line.charAt(end);
                if (c == quoteChar) {
                    break;
                }
                ++end;
            }

        }
        while (end < line.length()) {
            final char c = line.charAt(end);
            if (c == sepChar) {
                break;
            }
            ++end;
        }
        if (trimQuotes) {
            func.accept(line.substring(offset + 1, end - 1));

        } else {
            func.accept(line.substring(offset, end));

        }


        return Math.min(end + 1, line.length());
    }

    /**
     * Repeat a character
     *
     * @param c the character to repeat
     * @param n the number of times to repeat
     * @return a string of a repeated character
     */
    public static String repeatCharacter(char c, int n) {
        final char[] d = new char[n];
        Arrays.fill(d, c);
        return new String(d);
    }

    /**
     * Create a string with each word beginning with an upper case letter, the rest are lower case
     *
     * @param source the source string
     * @return the source string to title case
     */
    public static String toTitleCase(final String source) {
        return toTitleCase(source, Character::isWhitespace);
    }

    /**
     * Create a string with each word beginning with an upper case letter, the rest are lower case
     *
     * @param source the source string
     * @return the source string to title case
     */
    public static String snakeToTitleCase(final String source) {
        return toTitleCase(source, c -> c == '_');
    }

    /**
     * Create a string with each word beginning with an upper case letter, the rest are lower case
     *
     * @param source            the source string
     * @param wordSeparatorTest the test for if this is a wordSeparator
     * @return the source string to title case
     */
    private static String toTitleCase(final String source, CharacterPredicate wordSeparatorTest) {
        final char[] out = new char[source.length()];
        int i = 0;
        boolean title = true;
        while (i < out.length) {
            char c = source.charAt(i++);
            if (wordSeparatorTest.test(c)) {
                out[i - 1] = ' ';
                title = true;
                continue;
            }
            out[i - 1] = title ? Character.toTitleCase(c) : Character.toLowerCase(c);
            title = false;

        }
        return new String(out);
    }


    static boolean isDigit(final char c) {
        return c >= '0' && c <= '9';
    }

    /**
     * Test if a string containing a number can be converted to an int without a regular exception.
     *
     * @param number the number to test
     * @return whether the number can be converted to an int. Also returns false if the string is not a number
     */
    public static boolean canInt(final String number) {
        int i = 0;
        int e_i;
        long sign = 1;
        if (number.charAt(i) < '0') {
            if (number.charAt(i) != '-' || number.charAt(i) == '+') {
                return false;
            }
            sign = number.charAt(i) == '-' ? -1 : 1;
            if (number.length() > 11 || (number.length() == 1 && (number.charAt(0) == '-' || number.charAt(0) == '+'))) {
                return false;
            } else if (number.length() > 10 && isDigit(number.charAt(i)) && number.charAt(1) > '2') {
                return false;
            }
            e_i = number.length() - 2;
            ++i;
        } else if (number.length() > 10 || number.length() > 9 && isDigit(number.charAt(i)) && number.charAt(1) > '2') {
            return false;
        } else {
            e_i = number.length() - 1;
        }
        long out = 0;

        while (i < number.length()) {
            char c = number.charAt(i++);
            int j = c - '0';
            if (j < 0 || j > 9) {
                return false;
            }
            out += POWERS_OF_10[e_i--] * j * sign;

            if (out > Integer.MAX_VALUE || out < Integer.MIN_VALUE) {
                return false;
            }
        }
        return true;
    }

    /**
     * Compare the string from an offset.
     *
     * @param query       the query string
     * @param against     the target string
     * @param againstFrom where to start the comparison from
     * @return return whether the strings are the same
     */
    public static boolean compareTo(final String query, final String against, int againstFrom) {
        int i = 0;
        while (i < query.length()) {
            if (query.charAt(i++) != against.charAt(againstFrom++)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Compare the string from an offset. Case is ignored
     *
     * @param query       the query string
     * @param against     the target string
     * @param againstFrom where to start the comparison from
     * @return return whether the strings are the same
     */
    public static boolean compareToIgnoreCase(final String query, final String against, int againstFrom) {
        int i = 0;
        while (i < query.length()) {
            if (Character.toLowerCase(query.charAt(i++)) != Character.toLowerCase(against.charAt(againstFrom++))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Find the mismatch between two strings.
     *
     * @param query       the query string
     * @param against     the target string
     * @param againstFrom where to start the comparison from
     * @return return the position of the mismatch or -1 if the query and target are the same
     */
    public static int mismatch(final String query, final String against, int againstFrom) {
        int i = 0;
        while (i < query.length()) {
            if (query.charAt(i++) != against.charAt(againstFrom++)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Find the mismatch between two strings. Case is ignored
     *
     * @param query       the query string
     * @param against     the target string
     * @param againstFrom where to start the comparison from
     * @return return the position of the mismatch or -1 if the query and target are the same
     */
    public static int mismatchIgnoreCase(final String query, final String against, int againstFrom) {
        int i = 0;
        while (i < query.length()) {
            if (Character.toLowerCase(query.charAt(i++)) != Character.toLowerCase(against.charAt(againstFrom++))) {
                return i;
            }
        }
        return -1;
    }
}