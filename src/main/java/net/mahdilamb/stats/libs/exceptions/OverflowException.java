package net.mahdilamb.stats.libs.exceptions;

import net.mahdilamb.stats.libs.Libs;

/**
 * An overflow exception is thrown if the value of an operation exceeds the maximum value of its return type
 */
public class OverflowException extends ArithmeticException {
    private OverflowException(final String message) {
        super(message);
    }

    /**
     * Raise an exception, if exceptions are enabled
     *
     * @param message the message to print along with the exceptions
     * @param orOut   or the value to return if exceptions are not enabled
     * @return the value, or throw an exception
     * @throws OverflowException the exception to throw
     */
    public static double raiseException(final String message, double orOut) throws OverflowException {
        raiseException(message);
        return orOut;
    }

    /**
     * Raise an exception or output a message, if exceptions are not enabled
     *
     * @param message the message to output
     * @throws OverflowException the exception that is thrown
     */
    public static void raiseException(final String message) throws OverflowException {
        if (Libs.SILENT_EXCEPTIONS) {
            System.err.println(OverflowException.class.getSimpleName() + ": " + message);
            return;
        }
        throw new OverflowException(message);
    }
}
