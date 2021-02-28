package net.mahdilamb.dataframe;

import java.util.regex.Pattern;

public interface StringSeries extends Series<String>, SeriesWithFunctionalOperators<String> {
    @Override
    default DataType getType() {
        return DataType.STRING;
    }

    default BooleanSeries matches(Pattern pattern) {
        return map(el -> pattern.matcher(el).matches());
    }

}
