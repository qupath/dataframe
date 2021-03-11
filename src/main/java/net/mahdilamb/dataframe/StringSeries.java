package net.mahdilamb.dataframe;

import java.util.regex.Pattern;

/**
 * Series of string
 */
public interface StringSeries extends Series<String>, SeriesWithFunctionalOperators<String> {
    @Override
    default DataType getType() {
        return DataType.STRING;
    }

    /**
     * @param pattern the pattern to match
     * @return a boolean series containing whether this series matches the given pattern
     */
    default BooleanSeries matches(Pattern pattern) {
        return mapToBool(el -> pattern.matcher(el).matches());
    }

}
