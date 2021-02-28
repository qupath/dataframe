package net.mahdilamb.statistics;

/**
 * An arithmetic exception for when the amount of values is insufficient for an operation
 */
public class InsufficientDataException extends ArithmeticException {
    /**
     * Create an arithmetic exception for when there is insufficient data
     * @param message the message to use when throwing the exception
     */
    public InsufficientDataException(String message) {
        super(message);
    }

    /**
     * Create an arithmetic exception for when there is insufficient data and some of the data can be null
     * @return a new insufficient data exception
     */
    public static InsufficientDataException createForIterable() {
        return new InsufficientDataException("Either there are no values in this iterable or all values are null.");
    }
    /**
     * Create an arithmetic exception for when there is insufficient data.
     * @return a new insufficient data exception
     */
    public static InsufficientDataException createForArray() {
        return new InsufficientDataException("There are no values that can be used for this operation.");

    }
}
