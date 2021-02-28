package net.mahdilamb.statistics.libs.exceptions;

import net.mahdilamb.statistics.libs.Libs;

/**
 * An exception for an operation when no result can be found
 */
public class NoResultException extends ArithmeticException {
    private NoResultException(final String message) {
        super(message);
    }

    /**
     * Raise an exception, if exceptions are enabled
     *
     * @param message the message to print along with the exceptions
     * @param orOut   or the value to return if exceptions are not enabled
     * @return the value, or throw an exception
     * @throws NoResultException the exception to throw
     */
    public static double raiseException(final String message, double orOut) throws NoResultException {
        raiseException(message);
        return orOut;
    }

    /**
     * Raise an exception or output a message, if exceptions are not enabled
     *
     * @param message the message to output
     * @throws NoResultException the exception that is thrown
     */
    public static void raiseException(final String message) throws NoResultException {
        if (Libs.SILENT_EXCEPTIONS) {
            System.err.println(NoResultException.class.getSimpleName() + ": " + message);
            return;
        }
        throw new NoResultException(message);
    }
}
