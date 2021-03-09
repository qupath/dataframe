package net.mahdilamb.dataframe.functions;

/**
 * Functional interface that accepts and object and an int and returns a double
 *
 * @param <T> the type of the object
 */
@FunctionalInterface
public interface ObjIntDoubleFunction<T> {
    /**
     * Take an int and an object and return a double
     *
     * @param obj   the object
     * @param index the int
     * @return the evaluation of the method
     */
    double applyAsDouble(T obj, int index);
}
