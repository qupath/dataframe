package net.mahdilamb.dataframe.functions;

/**
 * Functional interface that takes two primitive booleans and returns
 * a boolean
 */
@FunctionalInterface
public interface BooleanBinaryOperator {
    /**
     * Apply a binary operator
     *
     * @param lhs left hand side operand
     * @param rhs right hand side operand
     * @return the result of the binary operation
     */
    boolean applyAsBoolean(boolean lhs, boolean rhs);
}
