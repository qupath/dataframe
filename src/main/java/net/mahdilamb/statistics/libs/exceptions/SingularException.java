package net.mahdilamb.statistics.libs.exceptions;

import net.mahdilamb.statistics.libs.Libs;

public class SingularException extends ArithmeticException {
    private SingularException(final String message) {
        super(message);
    }

    /**
     * Raise an exception, if exceptions are enabled
     *
     * @param message the message to print along with the exceptions
     * @param orOut   or the value to return if exceptions are not enabled
     * @return the value, or throw an exception
     * @throws SingularException the exception to throw
     */
    public static double raiseException(final String message, double orOut) throws SingularException {
        raiseException(message);
        return orOut;
    }

    /**
     * Raise an exception or output a message, if exceptions are not enabled
     *
     * @param message the message to output
     * @throws SingularException the exception that is thrown
     */
    public static void raiseException(final String message) throws SingularException {
        if (Libs.SILENT_EXCEPTIONS) {
            System.err.println(SingularException.class.getSimpleName() + ": " + message);
            return;
        }
        throw new SingularException(message);
    }
}
