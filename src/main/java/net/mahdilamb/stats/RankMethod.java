package net.mahdilamb.stats;

/**
 * Enum for rank method for ranking data
 */
public enum RankMethod {
    /**
     * Each value is given an index based on the sort
     */
    DENSE((i, f, l) -> i),
    /**
     * The returned rank is the order the value appeared
     */
    ORDINAL(null),
    /**
     * Returns the maximum element when ties appear
     */
    MAX((i, f, l) -> l),
    /**
     * Returns the first index when ties appear
     */
    MIN((i, f, l) -> f + 1),
    /**
     * Returns the mid-point where ties exist
     */
    AVERAGE((i, f, l) -> (l + f + 1) * .5);

    @FunctionalInterface
    interface RankComputer {
        /**
         * Compute the rank based on the first and last index of a run an the the iteration order
         *
         * @param id    the iteration order (i.e. the current index of the run)
         * @param first the start of the run
         * @param last  the end of the run
         * @return the rank based on the given information
         */
        double computeRank(int id, int first, int last);
    }

    final RankComputer ranker;

    RankMethod(RankComputer ranker) {
        this.ranker = ranker;
    }

    /**
     * @param method the name of the method
     * @return the rank method from its name
     */
    public static RankMethod getRankMethod(final String method) {
        switch (method.toLowerCase()) {
            case "average":
                return AVERAGE;
            case "dense":
                return DENSE;
            case "ordinal":
                return ORDINAL;
            case "max":
                return MAX;
            case "min":
                return MIN;
            default:
                throw new UnsupportedOperationException("Could not find method called " + method);
        }
    }
}
