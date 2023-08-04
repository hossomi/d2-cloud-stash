package br.com.yomigae.cloudstash.core.util;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static br.com.yomigae.cloudstash.core.io.LongBinaryAssert.assertThatBinary;
import static br.com.yomigae.cloudstash.core.util.ByteUtils.flipBits;
import static br.com.yomigae.cloudstash.core.util.ByteUtils.flipBytes;
import static br.com.yomigae.cloudstash.core.util.Utils.binaryStringToLong;

class ByteUtilsTest {

    @ParameterizedTest
    @CsvSource(textBlock = """
            01100101 01101100 01101100 01101111, 1,  1
            01100101 01101100 01101100 01101111, 5,  11110
            01100101 01101100 01101100 01101111, 8,  11110110
            01100101 01101100 01101100 01101111, 10, 11110110 00
            01100101 01101100 01101100 01101111, 16, 11110110 00110110
            01100101 01101100 01101100 01101111, 20, 11110110 00110110 0011
            01100101 01101100 01101100 01101111, 32, 11110110 00110110 00110110 10100110
            """)
    void flipBitsWorks(String data, int bits, String expected) {
        assertThatBinary(flipBits(binaryStringToLong(data), bits)).isEqualTo(binaryStringToLong(expected));
    }

    @ParameterizedTest
    @CsvSource(textBlock = """
            0x656C6C67, 1,  0x67
            0x656C6C67, 2,  0x676C
            0x656C6C67, 3,  0x676C6C
            0x656C6C67, 4,  0x676C6C65
            """)
    void flipBytesWorks(long data, int bits, long expected) {
        assertThatBinary(flipBytes(data, bits)).isEqualTo(expected);
    }
}