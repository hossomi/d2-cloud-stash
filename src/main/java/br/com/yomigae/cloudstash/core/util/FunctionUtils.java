package br.com.yomigae.cloudstash.core.util;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.function.BiConsumer;
import java.util.function.Function;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FunctionUtils {

    public static final BiConsumer<?, ?> NOOP_BICONSUMER = (a, b) -> {};

    @SuppressWarnings("unchecked")
    public static <T, U> BiConsumer<T, U> noop() {
        return (BiConsumer<T, U>) NOOP_BICONSUMER;
    }

    public static <T, R> R with(T in, Function<T, R> mapper) {
        return mapper.apply(in);
    }
}
