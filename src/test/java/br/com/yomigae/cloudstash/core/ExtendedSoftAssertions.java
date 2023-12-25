package br.com.yomigae.cloudstash.core;

import org.assertj.core.api.SoftAssertions;

public class ExtendedSoftAssertions  extends SoftAssertions {
    public LongBinaryAssert assertThatBinary(Long value) {
        return proxy(LongBinaryAssert.class, long.class, value);
    }
}
