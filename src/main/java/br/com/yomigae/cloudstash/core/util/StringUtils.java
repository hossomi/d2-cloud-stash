package br.com.yomigae.cloudstash.core.util;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.List;

import static com.google.common.base.Strings.repeat;

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
}
