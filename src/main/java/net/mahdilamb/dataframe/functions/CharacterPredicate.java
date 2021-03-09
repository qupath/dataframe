package net.mahdilamb.dataframe.functions;


/**
 * Functional interface for testing if a primitive char is applicable
 */
@FunctionalInterface
public interface CharacterPredicate {
    /**
     * @param value the value of interest
     * @return a boolean for the value
     */
    boolean test(char value);

}
