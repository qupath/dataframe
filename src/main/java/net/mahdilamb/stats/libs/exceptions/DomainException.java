package net.mahdilamb.stats.libs.exceptions;

import net.mahdilamb.stats.libs.Libs;

/**
 * An argument has been provided that is not in the correct range for the functions
 */
public class DomainException extends ArithmeticException {
    private DomainException(final String message) {
        super(message);
    }

    /**
     * Raise an exception, if exceptions are enabled
     *
     * @param message the message to print along with the exceptions
     * @param orOut   or the value to return if exceptions are not enabled
     * @return the value, or throw an exception
     * @throws DomainException the exception to throw
     */
    public static double raiseException(final String message, double orOut) throws DomainException {
        raiseException(message);
        return orOut;
    }

    /**
     * Raise an exception or output a message, if exceptions are not enabled
     *
     * @param message the message to output
     * @throws DomainException the exception that is thrown
     */
    public static void raiseException(final String message) throws DomainException {
        if (Libs.SILENT_EXCEPTIONS) {
            System.err.println(DomainException.class.getSimpleName() + ": " + message);
            return;
        }
        throw new DomainException(message);
    }
}
