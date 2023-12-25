package br.com.yomigae.cloudstash.core.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static br.com.yomigae.cloudstash.core.LongBinaryAssert.assertThatBinary;
import static br.com.yomigae.cloudstash.core.util.ByteUtils.flipBits;
import static br.com.yomigae.cloudstash.core.util.ByteUtils.flipBytes;
import static br.com.yomigae.cloudstash.core.util.Utils.binaryToLong;

class ByteUtilsTest {

    @ParameterizedTest
    @CsvSource(textBlock = """
            01000100 01101001 01100001 01101100 01101111, 1,  1
            01000100 01101001 01100001 01101100 01101111, 5,  11110
            01000100 01101001 01100001 01101100 01101111, 8,  11110110
            01000100 01101001 01100001 01101100 01101111, 10, 11110110 00
            01000100 01101001 01100001 01101100 01101111, 16, 11110110 00110110
            01000100 01101001 01100001 01101100 01101111, 20, 11110110 00110110 1000
            01000100 01101001 01100001 01101100 01101111, 32, 11110110 00110110 10000110 10010110
            01000100 01101001 01100001 01101100 01101111, 40, 11110110 00110110 10000110 10010110 00100010
            """)
    void flipBitsWorks(String data, int bits, String expected) {
        assertThatBinary(flipBits(binaryToLong(data), bits)).isEqualTo(binaryToLong(expected));
    }

    @Test
    void flipBitsWorksForByte() {
        assertThatBinary(flipBits((byte) 0b01101111)).isEqualTo((byte) 0b11110110);
    }

    @ParameterizedTest
    @CsvSource(textBlock = """
            0x656c6c67, 1,  0x67
            0x656c6c67, 2,  0x676c
            0x656c6c67, 3,  0x676c6c
            0x656c6c67, 4,  0x676c6c65
            """)
    void flipBytesWorks(long data, int bits, long expected) {
        assertThatBinary(flipBytes(data, bits)).isEqualTo(expected);
    }
}
