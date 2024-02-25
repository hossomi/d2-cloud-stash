package br.com.yomigae.cloudstash.core.util;

import com.google.common.collect.Streams;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.EnumMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FunctionUtils {

    public static final BiConsumer<?, ?> NOOP_BICONSUMER = (a, b) -> { };

    @SuppressWarnings("unchecked")
    public static <T, U> BiConsumer<T, U> noop() {
        return (BiConsumer<T, U>) NOOP_BICONSUMER;
    }

    public static <T, R> R map(T in, Function<T, R> function) {
        return function.apply(in);
    }

    public static <T> void accept(T in, Consumer<T> consumer) {
        consumer.accept(in);
    }

    public static <K extends Enum<K>, V> EnumMap<K, V> toEnumMap(Class<K> type, Iterable<K> keys, Function<K, V> values) {
        return Streams.stream(keys).collect(
                () -> new EnumMap<>(type),
                (m, k) -> m.put(k, values.apply(k)),
                EnumMap::putAll);
    }
}
