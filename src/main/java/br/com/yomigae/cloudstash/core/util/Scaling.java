package br.com.yomigae.cloudstash.core.util;

import java.util.SortedSet;

import static java.util.stream.IntStream.range;

public interface Scaling {

    double apply(int x);

    int unapply(double value);

    record Linear(int base, double increment) implements Scaling {

        @Override
        public double apply(int x) {
            return base + increment * x;
        }

        @Override
        public int unapply(double value) {
            return (int) Math.floor((value - base) / increment);
        }

        @Override
        public String toString() {
            return "%d+%3.3fx".formatted(base, increment);
        }
    }

    record Breakpoint(BreakpointMap<? extends Scaling> breakpoints) implements Scaling {

        @Override
        public double apply(int x) {
            int bp = breakpoints.breakpoint(x);
            return (int) breakpoints.get(x).apply(x - bp);
        }

        @Override
        public int unapply(double value) {
            int bp = breakpoint(value);
            return breakpoints.get(bp).unapply(value) + bp;
        }

        private int breakpoint(double value) {
            SortedSet<Integer> breakpoints = this.breakpoints.breakpoints();
            if (breakpoints.isEmpty()) {
                throw new IllegalStateException("No breakpoints registered");
            }

            int breakpoint = breakpoints.first();
            for (int bp : breakpoints) {
                if (apply(bp) > value) {
                    if (apply(bp - 1) < value) {
                        // If f(bp) is higher but f(bp-1) is lower, value is not in any breakpoint range
                        throw new IllegalArgumentException("No breakpoint yields value %.3f".formatted(value));
                    }
                    // If f(bp) is higher, use previous breakpoint (already set)
                    break;
                }
                breakpoint = bp;
            }


            return breakpoint;
        }

        @Override
        public String toString() {
            return breakpoints.toString();
        }
    }

    record Experience(int offset, int increment) implements Scaling {

        @Override
        public double apply(int x) {
            x += offset;
            return increment * x * x * (x + 1);
        }

        @Override
        public int unapply(double value) {
            // The formula is too complex to reverse. But experience range is very constrained, so we can do a scan.
            return -1 + range(-100, 100)
                    .filter(x -> apply(x) > value)
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("No offset yields value %.3f".formatted(value)));
        }

        @Override
        public String toString() {
            return "%d*L^2*(L+1)".formatted(increment);
        }
    }
}
