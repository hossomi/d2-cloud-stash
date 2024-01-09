package br.com.yomigae.cloudstash.core.util;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.function.BiFunction;
import java.util.function.Supplier;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ValidationUtils {

    public static <T, E extends Exception> T throwOnNull(T value, Supplier<E> onMissing) throws E {
        if (value == null) {
            throw onMissing.get();
        }
        return value;
    }

    public static <E extends Exception> int checkValueLessThan(int value, int max, BiFunction<Integer, Integer, E> onFailure) throws E {
        if (value > max) { throw onFailure.apply(value, max); }
        return value;
    }
}
