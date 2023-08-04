package br.com.yomigae.cloudstash.core.util;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ByteUtils {

    public static long flipBytes(long data, int bytes) {
        long flip = 0;
        while (bytes-- > 0) {
            flip = (flip << 8) + (data & 0xFF);
            data >>= 8;
        }
        return flip;
    }

    public static long flipBits(long data, int bits) {
        long flip = 0;
        while (bits-- > 0) {
            flip = (flip << 1) + (data & 0x1);
            data >>= 1;
        }
        return flip;
    }
}
