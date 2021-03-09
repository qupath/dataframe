package net.mahdilamb.dataframe.utils;

import net.mahdilamb.dataframe.functions.BooleanConsumer;
import net.mahdilamb.dataframe.functions.CharConsumer;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.PrimitiveIterator;
import java.util.function.Consumer;

/**
 * Primitive iterators not implemented in JDK
 */
public final class PrimitiveIterators {
    private PrimitiveIterators() {

    }

    /**
     * An Iterator specialized for {@code boolean} values.
     */
    public interface OfCharacter extends PrimitiveIterator<Character, CharConsumer> {

        /**
         * Returns the next {@code character} element in the iteration.
         *
         * @return the next {@code character} element in the iteration
         * @throws NoSuchElementException if the iteration has no more elements
         */
        char nextChar();

        /**
         * Override the for each remaining method so that it uses the primitive version
         */
        @Override
        default void forEachRemaining(CharConsumer action) {
            Objects.requireNonNull(action);
            while (hasNext())
                action.accept(nextChar());
        }

        /**
         * @return the next value, boxed
         */
        @Override
        default Character next() {
            return nextChar();
        }

        /**
         * Override the for each remaining method so that it uses the primitive version
         */
        @Override
        default void forEachRemaining(Consumer<? super Character> action) {
            if (action instanceof CharConsumer) {
                forEachRemaining((CharConsumer) action);
            } else {
                Objects.requireNonNull(action);
                forEachRemaining((CharConsumer) action::accept);
            }
        }
    }

    /**
     * An Iterator specialized for {@code boolean} values.
     */
    public interface OfBoolean extends PrimitiveIterator<Boolean, BooleanConsumer> {

        /**
         * @return the next value, boxed
         */
        boolean nextBoolean();

        /**
         * Override the for each remaining method so that it uses the primitive version
         */
        default void forEachRemaining(BooleanConsumer action) {
            Objects.requireNonNull(action);
            while (hasNext())
                action.accept(nextBoolean());
        }

        /**
         * @return the next value, boxed
         */
        @Override
        default Boolean next() {
            return nextBoolean();
        }

        /**
         * Override the for each remaining method so that it uses the primitive version
         */
        @Override
        default void forEachRemaining(Consumer<? super Boolean> action) {
            if (action instanceof BooleanConsumer) {
                forEachRemaining((BooleanConsumer) action);
            } else {
                // The method reference action::accept is never null
                Objects.requireNonNull(action);
                forEachRemaining((BooleanConsumer) action::accept);
            }
        }
    }
}
