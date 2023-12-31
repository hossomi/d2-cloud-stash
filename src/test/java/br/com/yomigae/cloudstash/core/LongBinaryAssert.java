package br.com.yomigae.cloudstash.core;

import org.assertj.core.api.AbstractLongAssert;

import java.util.Arrays;

import static com.google.common.base.Strings.padStart;
import static java.lang.Long.toBinaryString;
import static java.lang.String.format;

public class LongBinaryAssert extends AbstractLongAssert<LongBinaryAssert> {

    public LongBinaryAssert(long actual) {
        super(actual, LongBinaryAssert.class);
    }

    public static LongBinaryAssert assertThatBinary(long actual) {
        return new LongBinaryAssert(actual);
    }

    @Override
    public LongBinaryAssert isEqualTo(long expected) {
        int length = getLongest(actual, expected);
        info.overridingErrorMessage(format(
                "%nExpected: <%s>%nActual:   <%s>",
                formatAsBinaryPair(length, expected),
                formatAsBinaryPair(length, actual)));
        return super.isEqualTo(expected);
    }

    private static String formatAsBinaryPair(int length, long value) {
        return format(format("%%%ds | %%s", length), value, padStart(toBinaryString(value), 32, '0'));
    }

    private static int getLongest(long... values) {
        return Arrays.stream(values)
                .mapToInt(l -> String.valueOf(l).length())
                .max()
                .orElse(0);
    }
}
