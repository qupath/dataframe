package net.mahdilamb.dataframe.functions;

/**
 * Functional interface for a ternary operator
 */
@FunctionalInterface
public interface DoubleTernaryOperator {
    /**
     * Applies this operator to the given operands.
     *
     * @param left  the first operand
     * @param mid   the middle operand
     * @param right the right operand
     * @return the operator result
     */
    double applyAsDouble(double left, double mid, double right);
}
