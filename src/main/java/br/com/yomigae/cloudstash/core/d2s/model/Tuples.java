package br.com.yomigae.cloudstash.core.d2s.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class Tuples {

    @Data
    @AllArgsConstructor
    public static class Alternate<T> {
        private T primary;
        private T secondary;
        private Selection active;

        public enum Selection {
            PRIMARY, SECONDARY
        }

        public T get() {
            return get(active);
        }

        public T get(Selection selection) {
            return switch (selection) {
                case PRIMARY -> primary;
                case SECONDARY -> secondary;
            };
        }
    }

    @Data
    @AllArgsConstructor
    public static class Dual<T> {
        private T left;
        private T right;
    }

    @Data
    @AllArgsConstructor
    public static class Gauge<T> {
        private T current;
        private T max;
    }

    public static <T> Alternate<T> alternate(T primary, T secondary, Alternate.Selection active) {
        return new Alternate<>(primary, secondary, active);
    }

    public static <T> Dual<T> dual(T left, T right) {
        return new Dual<>(left, right);
    }

    public static <T> Gauge<T> gauge(T current, T max) {
        return new Gauge<>(current, max);
    }
}
