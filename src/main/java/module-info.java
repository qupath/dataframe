/**
 * Export public api for Java 9+
 */
module net.mahdilamb.dataframe {
    requires net.mahdilamb.utils.tuples;

    exports net.mahdilamb.dataframe;
    exports net.mahdilamb.dataframe.utils;
    exports net.mahdilamb.dataframe.functions;

    exports net.mahdilamb.stats;
}