package br.com.yomigae.cloudstash.core.util;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.google.common.base.Strings.repeat;
import static java.lang.Math.ceil;
import static java.lang.Math.floor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class StringUtils {

    public enum Divider {
        DOUBLE, SINGLE, BAR
    }

    public static String checkbox(String label, boolean value) {
        return "[%c] %s".formatted(value ? 'x' : ' ', label);
    }

    public static String list(String header, List<String> items) {
        StringBuilder string = new StringBuilder(header);
        items.forEach(item -> string
                .append("\n- ")
                .append(item.replaceAll("\n", "\n  ")));
        return string.toString();
    }

    public static String divider(Divider style) {
        return switch (style) {
            case DOUBLE -> repeat("=", 50);
            case SINGLE -> repeat("-", 50);
            case BAR -> '[' + repeat("/", 48) + "]";
        };
    }

    @RequiredArgsConstructor
    public static class Padder {
        private final String text;
        private char left = ' ';
        private char right = ' ';

        public Padder() {
            this("");
        }

        public Padder both(char c) {
            return left(c).right(c);
        }

        public Padder left(char c) {
            this.left = c;
            return this;
        }

        public Padder right(char c) {
            this.right = c;
            return this;
        }

        public String pad(int left, int right) {
            return String.valueOf(this.left).repeat(left) + text + String.valueOf(this.right).repeat(right);
        }

        public String align(int width, double position) {
            int actual = width - text.length();
            return pad((int) floor(actual * position), (int) ceil(actual * (1 - position)));
        }

        @Override
        public String toString() {
            return text;
        }
    }
}
