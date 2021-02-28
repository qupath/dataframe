package net.mahdilamb.dataframe.functions;

/**
 * A functional interface that accepts a boolean and doesn't return a value
 */
@FunctionalInterface
public interface BooleanConsumer {
    /**
     * Performs this operation on the given argument.
     *
     * @param value the input argument
     */
    void accept(boolean value);

}