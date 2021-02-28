package net.mahdilamb.dataframe.utils;

/**
 * Exception raised when a string has not been parsed
 */
public class StringParseException extends RuntimeException {
    private static final String prefix = "Error parsing ";
    private final String string;
    private final int position;

    /**
     * Create a string parse exception
     *
     * @param string   the string with the failed parse
     * @param position the position the parse failed at
     */
    public StringParseException(final String string, int position) {
        super();
        this.string = string;
        this.position = position;
    }

    @Override
    public String toString() {
        if (string == null) {
            return super.toString();
        }
        if (position < 0 || position < string.length()) {
            return String.format("\n\t%s\"%s\"\n", prefix, string);
        }
        return String.format("\n\t%s\"%s\"\n\t%s^", prefix, string, StringUtils.repeatCharacter(' ', prefix.length() + position));
    }
}
