package net.mahdilamb.dataframe.functions;

import java.util.Objects;

/**
 * Functional interface for a predicate of two primitive doubles
 */
@FunctionalInterface
public interface BiDoublePredicate {
    /**
     * Perform the operation on two doubles and return a boolean
     *
     * @param a the left param
     * @param b the right param
     * @return the output boolean
     */
    boolean test(double a, double b);

    /**
     * And operator to be used when compositing predicates
     *
     * @param other the other predicated
     * @return a composite predicate
     */
    default BiDoublePredicate and(BiDoublePredicate other) {
        Objects.requireNonNull(other);
        return (a, b) -> test(a, b) && other.test(a, b);
    }

    /**
     * Invert operator
     *
     * @return a negated predicate
     */
    default BiDoublePredicate negate() {
        return (a, b) -> !test(a, b);
    }

    /**
     * Or operator to be used when compositing predicates
     *
     * @param other the other predicated
     * @return a composite predicate
     */
    default BiDoublePredicate or(BiDoublePredicate other) {
        Objects.requireNonNull(other);
        return (a, b) -> test(a, b) || other.test(a, b);
    }
}
