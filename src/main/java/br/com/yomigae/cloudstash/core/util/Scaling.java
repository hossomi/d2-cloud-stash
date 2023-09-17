package br.com.yomigae.cloudstash.core.util;

import java.util.stream.Collectors;

public interface Scaling {

    double get(int offset);

    record Linear(int base, double increment) implements Scaling {
        @Override
        public double get(int offset) {
            return base + increment * offset;
        }

        @Override
        public String toString() {
            return "%d+%3.3fx".formatted(base, increment);
        }
    }

    record Breakpoint(BreakpointMap<? extends Scaling> breakpoints) implements Scaling {
        @Override
        public double get(int offset) {
            int bp = breakpoints.breakpoint(offset);
            return (int) breakpoints.get(offset).get(offset - bp);
        }

        @Override
        public String toString() {
            return "[" + breakpoints.breakpoints().entrySet().stream()
                    .map(e -> "%d: %s".formatted(e.getKey(), e.getValue()))
                    .collect(Collectors.joining(", ")) + "]";
        }
    }

    record Experience(int increment) implements Scaling {

        @Override
        public double get(int offset) {
            return increment * offset * offset * (offset + 1);
        }
    }
}
