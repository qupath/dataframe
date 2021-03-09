package net.mahdilamb.dataframe.functions;

/**
 * Functional interface for a method that accepts a primitive char
 */
@FunctionalInterface
public interface CharConsumer {
    /**
     *
     * @param v the char to accept
     */
    void accept(char v);
}
