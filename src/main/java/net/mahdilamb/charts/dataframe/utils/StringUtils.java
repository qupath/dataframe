package net.mahdilamb.charts.dataframe.utils;


import net.mahdilamb.utils.functions.CharacterPredicate;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
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
    private static final int[] POWERS_OF_10 = {1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000, 1000000000};

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
    public static int iterateLine(String line, int offset, char sepChar, char quoteChar, Consumer<String> func) {
        int s = offset, e = offset;
        char quote = 0;
        while (e < line.length()) {
            final char c = line.charAt(e++);
            if (c == quoteChar) {
                quote = quote != c ? c : 0;
            }
            if (c == sepChar && quote == 0) {
                break;
            }
        }
        int f;
        if (e == line.length()) {
            f = line.length() - 1;
            if (line.charAt(s) == line.charAt(f) && line.charAt(f) == quoteChar) {
                if (s != 0) {
                    --f;
                }
                ++s;
            } else {
                f = line.length();
            }

        } else {
            f = e - 2;
            if (line.charAt(s) == line.charAt(f) && line.charAt(s) == quoteChar) {
                ++s;
            } else {
                ++f;
            }
        }
        func.accept(line.substring(s, f));
        return e;
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

    /**
     * Use AWT to get the string from the clipboard
     *
     * @return the string data from the clipboard
     * @throws IOException                if the data cannot be retrieved
     * @throws UnsupportedFlavorException if the clipboard does not contain a string
     */
    public static String getStringFromClipboard() throws IOException, UnsupportedFlavorException {
        return (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
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
            if (number.length() > 11) {
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

}