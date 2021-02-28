package net.mahdilamb.dataframe.functions;

public interface ObjIntDoubleFunction<T> {
    double applyAsDouble(T obj, int index);
}
