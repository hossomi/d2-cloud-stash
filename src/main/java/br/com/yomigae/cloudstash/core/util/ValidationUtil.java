package br.com.yomigae.cloudstash.core.util;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.function.Supplier;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ValidationUtil {

    public static <T, E extends Exception> T throwOnNull(T value, Supplier<E> onMissing) throws E {
        if (value == null) {
            throw onMissing.get();
        }
        return value;
    }
}
