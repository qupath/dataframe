package net.mahdilamb.statistics;

import java.util.Iterator;
import java.util.PrimitiveIterator;
import java.util.function.ToDoubleFunction;

/**
 * An abstract stats class that is used to cache values
 */
//TODO min, max, median, percentile, quartile with quickselect
abstract class AbstractStats implements Stats {
    /**
     * Default implementation of Stats object that has an iterable as its source
     *
     * @param <T> the type of the data in the iterable
     */
    static final class OfNumericConvertible<T> extends AbstractStats {
        private final Iterable<? extends T> data;
        private final ToDoubleFunction<T> converter;

        /**
         * Create a Stats object using an iterable and a double extractor function
         *
         * @param data      the input data
         * @param converter the extractor function
         */
        OfNumericConvertible(Iterable<? extends T> data, ToDoubleFunction<T> converter) {
            this.data = data;
            this.converter = converter;
        }

        @Override
        void calculateMin() {
            min = StatUtils.min(data, converter);

        }

        @Override
        void calculateMax() {
            max = StatUtils.max(data, converter);

        }

        @Override
        void calculateMinAndMax() {
            Double max = null;
            Double min = null;
            for (final T d : data) {
                if (d == null) {
                    continue;
                }
                final double f = converter.applyAsDouble(d);
                if (max == null || f > max) {
                    max = f;
                }
                if (min == null || f < min) {
                    min = f;
                }
            }
            if (max == null) {
                throw InsufficientDataException.createForIterable();
            }
            this.max = max;
            this.min = min;
        }

        @Override
        void calculateSum() {
            sum = StatUtils.sum(data, converter);
        }

        @Override
        void calculateCount() {
            count = StatUtils.count(data, converter);
        }

        @Override
        void calculateCountAndSum() {
            sum = 0;
            count = 0;
            for (final T d : data) {
                if (d == null) {
                    continue;
                }
                final double f = converter.applyAsDouble(d);
                sum += f;
                ++count;
            }
        }

        @Override
        public Iterator<Double> iterator() {
            return new PrimitiveIterator.OfDouble() {
                private final Iterator<? extends T> iterator = data.iterator();

                @Override
                public boolean hasNext() {
                    return iterator.hasNext();
                }

                @Override
                public double nextDouble() {
                    return converter.applyAsDouble(iterator.next());
                }
            };
        }
    }

    /**
     * Default implementation for a stats object based on a double array
     */
    static final class OfDoubleArray extends AbstractStats {
        private final double[] data;

        /**
         * Create a stats object using the data supplied
         *
         * @param data the input data
         */
        OfDoubleArray(final double... data) {
            this.data = data;
        }

        @Override
        final synchronized void calculateMin() {
            min = StatUtils.min(data);
        }

        @Override
        final synchronized void calculateMax() {
            max = StatUtils.max(data);
        }

        @Override
        final synchronized void calculateMinAndMax() {
            Double min = null;
            Double max = null;
            for (final double d : data) {
                if (min == null || d < min) {
                    min = d;
                }
                if (max == null || d > max) {
                    max = d;
                }
            }
            if (max == null) {
                throw InsufficientDataException.createForArray();
            }
            this.min = min;
            this.max = max;
        }

        @Override
        final synchronized void calculateSum() {
            sum = StatUtils.sum(data);
        }

        @Override
        final synchronized void calculateCount() {
            count = StatUtils.count(data);
        }

        @Override
        final synchronized void calculateCountAndSum() {

            sum = 0;
            count = 0;
            for (final double d : data) {
                ++count;
                sum += d;
            }
        }

        @Override
        public Iterator<Double> iterator() {

            return new PrimitiveIterator.OfDouble() {
                private int i = 0;

                @Override
                public double nextDouble() {
                    return data[i++];
                }

                @Override
                public boolean hasNext() {
                    return i < data.length;
                }
            };
        }
    }

    private boolean hasCount, hasSum, hasMin, hasMax;
    long count;
    double sum, min, max;

    /**
     * Function used to calculate the min only
     */
    abstract void calculateMin();

    /**
     * Method used to calculate the max only
     */
    abstract void calculateMax();

    /**
     * Method used to calculate the min and max
     */
    abstract void calculateMinAndMax();

    /**
     * Method used to calculate the sum of the data
     */
    abstract void calculateSum();

    /**
     * Method used to calculate the count of the data
     */
    abstract void calculateCount();

    /**
     * Method used to calculate the count and the sum
     */
    abstract void calculateCountAndSum();

    @Override
    public final long getCount() {
        if (!hasCount) {
            calculateCount();
            hasCount = true;
        }
        return count;
    }

    @Override
    public final double getSum() {
        if (!hasSum) {
            calculateSum();
            hasSum = true;
        }
        return sum;
    }

    @Override
    public final double getMean() {
        if (!hasSum && !hasCount) {
            calculateCountAndSum();
            hasSum = hasCount = true;
        } else {
            if (!hasSum) {
                calculateSum();
                hasSum = true;
            } else {
                calculateCount();
                hasCount = true;
            }
        }
        return sum / count;
    }

    @Override
    public final double getMin() {
        if (!hasMin) {
            calculateMin();
            hasMin = true;
        }
        return min;
    }

    @Override
    public final double getMax() {
        if (!hasMax) {
            calculateMax();
            hasMax = true;
        }
        return max;
    }

    @Override
    public final double getRange() {
        if (!hasMin && !hasMax) {
            calculateMinAndMax();
            hasMax = hasMin = true;
        } else {
            if (!hasMin) {
                calculateMin();
                hasMin = true;
            } else {
                calculateMax();
                hasMax = true;
            }
        }
        return max - min;
    }

    protected void invalidate() {
        hasCount = hasSum = hasMin = hasMax = false;

    }
}
