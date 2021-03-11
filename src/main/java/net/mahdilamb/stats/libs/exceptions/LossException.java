package net.mahdilamb.stats.libs.exceptions;

import net.mahdilamb.stats.libs.Libs;

public class LossException extends ArithmeticException {
    private LossException(final String message) {
        super(message);
    }
    /**
     * Raise an exception, if exceptions are enabled
     *
     * @param message the message to print along with the exceptions
     * @param orOut   or the value to return if exceptions are not enabled
     * @return the value, or throw an exception
     * @throws LossException the exception to throw
     */
    public static double raiseException(final String message, double orOut) throws LossException {
        raiseException(message);
        return orOut;
    }
    /**
     * Raise an exception or output a message, if exceptions are not enabled
     *
     * @param message the message to output
     * @throws LossException the exception that is thrown
     */
    public static void raiseException(final String message) throws LossException {
        if (Libs.SILENT_EXCEPTIONS) {
            System.err.println(LossException.class.getSimpleName() + ": " + message);
            return;
        }
        throw new LossException(message);
    }
}
