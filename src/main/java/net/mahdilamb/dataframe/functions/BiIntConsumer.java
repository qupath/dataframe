package net.mahdilamb.dataframe.functions;

/**
 * Functional interface for a method that takes two primitive ints
 */
@FunctionalInterface
public interface BiIntConsumer {
    /**
     * Accept two ints
     * @param i left int
     * @param j right int
     */
    void accept(int i, int j);
}
