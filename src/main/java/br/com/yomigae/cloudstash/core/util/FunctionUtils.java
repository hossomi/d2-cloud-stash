package br.com.yomigae.cloudstash.core.util;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.function.BiConsumer;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FunctionUtils {

    public static final BiConsumer<?, ?> NOOP_BICONSUMER = (a, b) -> {};

    @SuppressWarnings("unchecked")
    public static <T, U> BiConsumer<T, U> noop() {
        return (BiConsumer<T, U>) NOOP_BICONSUMER;
    }
}
