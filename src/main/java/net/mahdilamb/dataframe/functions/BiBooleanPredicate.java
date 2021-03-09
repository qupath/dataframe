package net.mahdilamb.dataframe.functions;

import java.util.Objects;

/**
 * Functional interface that takes two primitive booleans and returns
 * a boolean
 */
@FunctionalInterface
public interface BiBooleanPredicate {
    /**
     * Apply a binary operator
     *
     * @param lhs left hand side operand
     * @param rhs right hand side operand
     * @return the result of the binary operation
     */
    boolean test(boolean lhs, boolean rhs);

    /**
     * And operator to be used when compositing predicates
     *
     * @param other the other predicated
     * @return a composite predicate
     */
    default BiBooleanPredicate and(BiBooleanPredicate other) {
        Objects.requireNonNull(other);
        return (a, b) -> test(a, b) && other.test(a, b);
    }

    /**
     * Invert operator
     *
     * @return a negated predicate
     */
    default BiBooleanPredicate negate() {
        return (a, b) -> !test(a, b);
    }

    /**
     * Or operator to be used when compositing predicates
     *
     * @param other the other predicated
     * @return a composite predicate
     */
    default BiBooleanPredicate or(BiBooleanPredicate other) {
        Objects.requireNonNull(other);
        return (a, b) -> test(a, b) || other.test(a, b);
    }
}
