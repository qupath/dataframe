package net.mahdilamb.dataframe.utils;

import net.mahdilamb.dataframe.functions.BooleanConsumer;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.PrimitiveIterator;
import java.util.function.Consumer;
/**
 * Other primitive iterators. Modified from the JDK
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
         * Performs the given action for each remaining element until all elements
         * have been processed or the action throws an exception.  Actions are
         * performed in the order of iteration, if that order is specified.
         * Exceptions thrown by the action are relayed to the caller.
         *
         * @param action The action to be performed for each element
         * @throws NullPointerException if the specified action is null
         * @implSpec <p>The default implementation behaves as if:
         * <pre>{@code
         *     while (hasNext())
         *         action.accept(nextCharacter());
         * }</pre>
         */
        default void forEachRemaining(CharConsumer action) {
            Objects.requireNonNull(action);
            while (hasNext())
                action.accept(nextChar());
        }

        /**
         * {@inheritDoc}
         *
         * @implSpec The default implementation boxes the result of calling
         * {@link #nextChar()}, and returns that boxed result.
         */
        @Override
        default Character next() {
            return nextChar();
        }

        /**
         * {@inheritDoc}
         *
         * @implSpec If the action is an instance of {@code CharacterConsumer} then it is cast
         * to {@code CharacterConsumer} and passed to {@link #forEachRemaining};
         * otherwise the action is adapted to an instance of
         * {@code CharacterConsumer}, by boxing the argument of {@code CharacterConsumer},
         * and then passed to {@link #forEachRemaining}.
         */
        @Override
        default void forEachRemaining(Consumer<? super Character> action) {
            if (action instanceof CharConsumer) {
                forEachRemaining((CharConsumer) action);
            } else {
                // The method reference action::accept is never null
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
         * Returns the next {@code boolean} element in the iteration.
         *
         * @return the next {@code boolean} element in the iteration
         * @throws NoSuchElementException if the iteration has no more elements
         */
        boolean nextBoolean();

        /**
         * Performs the given action for each remaining element until all elements
         * have been processed or the action throws an exception.  Actions are
         * performed in the order of iteration, if that order is specified.
         * Exceptions thrown by the action are relayed to the caller.
         *
         * @param action The action to be performed for each element
         * @throws NullPointerException if the specified action is null
         * @implSpec <p>The default implementation behaves as if:
         * <pre>{@code
         *     while (hasNext())
         *         action.accept(nextBoolean());
         * }</pre>
         */
        default void forEachRemaining(BooleanConsumer action) {
            Objects.requireNonNull(action);
            while (hasNext())
                action.accept(nextBoolean());
        }

        /**
         * {@inheritDoc}
         *
         * @implSpec The default implementation boxes the result of calling
         * {@link #nextBoolean()}, and returns that boxed result.
         */
        @Override
        default Boolean next() {
            return nextBoolean();
        }

        /**
         * {@inheritDoc}
         *
         * @implSpec If the action is an instance of {@code BooleanConsumer} then it is cast
         * to {@code BooleanConsumer} and passed to {@link #forEachRemaining};
         * otherwise the action is adapted to an instance of
         * {@code BooleanConsumer}, by boxing the argument of {@code BooleanConsumer},
         * and then passed to {@link #forEachRemaining}.
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
