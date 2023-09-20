package br.com.yomigae.cloudstash.core.io;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static br.com.yomigae.cloudstash.core.io.D2BinaryReader.SIZEOF_INT;
import static br.com.yomigae.cloudstash.core.io.LongBinaryAssert.assertThatBinary;
import static br.com.yomigae.cloudstash.core.util.Utils.binaryStringToLong;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class D2BinaryReaderTest {

    public static final byte[] DATA = {
            0x68, 0x65, 0x6C, 0x6C, 0x6F, 0x20, 0x77, 0x6F, // 01101000 01100101 01101100 01101100 01101111 00100000 01110111 01101111
            0x72, 0x6C, 0x64, 0x20, 0x6F, 0x66, 0x20, 0x64, // 01100010 01101100 01100100 00100000 01101111 01100110 00100000 01100100
            0x69, 0x61, 0x62, 0x6C, 0x6F, 0x20, 0x69, 0x69  // 01101001 01100001 01100010 01101100 01101111 00100000 01101001 01101001
    };

    private D2BinaryReader reader;

    @BeforeEach
    void setup() {
        reader = new D2BinaryReader(DATA);
    }

    @Test
    void bitsWorks() {
        assertThat(reader.bits()).isEqualTo(DATA.length * 8);
    }

    @Test
    void bytesWorks() {
        assertThat(reader.bytes()).isEqualTo(DATA.length);
    }

    @Test
    void checksumWorks() {
        reader = new D2BinaryReader(new byte[]{0x00, 0x01, 0x02, 0x04, 0x08, 0x10, 0x20, 0x40});
        assertThat(reader.checksum(0)).isEqualTo(256);
    }

    @Test
    void checksumWorksWithChecksumAtEndOfData() {
        reader = new D2BinaryReader(new byte[]{0x00, 0x01, 0x02, 0x04, 0x08, 0x10, 0x20, 0x40});
        assertThat(reader.checksum(4)).isEqualTo(192);
    }

    @Test
    void setCursorWorks() {
        assertThat(reader.setBytePos(10).readByte()).isEqualTo((byte) 0x64);
    }

    @Test
    void readWorks() {
        reader = new D2BinaryReader(new byte[]{0b01010101});
        assertThat(reader.read(5)).isEqualTo(0b00001010);
        assertThat(reader.bitPos()).isEqualTo(5);
    }

    // First 8 bytes of data:
    //              01101000 01100101 01101100 01101100 01101111 00100000 01110111 01101111
    @ParameterizedTest
    @CsvSource(textBlock = """
            0,  1,  0
            0,  5,  01101
            0,  8,  01101000
            0,  10, 01101000 01
            0,  16, 01101000 01100101
            0,  20, 01101000 01100101 0110
            0,  32, 01101000 01100101 01101100 01101100
            1,  8,   1101000 0
            2,  8,    101000 01
            7,  8,         0 0110010
            8,  8,           01100101
            9,  8,            1100101 0
            10, 8,             100101 01
            3,  1,     0
            3,  5,     01000
            3,  8,     01000 011
            3,  13,    01000 01100101
            3,  16,    01000 01100101 011
            3,  21,    01000 01100101 01101100
            3,  32,    01000 01100101 01101100 01101100 011
            10, 1,             1
            10, 5,             10010
            10, 8,             100101 01
            10, 13,            100101 0110110
            10, 16,            100101 01101100 01
            10, 21,            100101 01101100 0110110
            10, 32,            100101 01101100 01101100 01101111 00
            """)
    void readWorks(int start, int size, String expect) {
        assertThatBinary(reader.skip(start).read(size)).isEqualTo(binaryStringToLong(expect));
        assertThat(reader.bitPos()).isEqualTo(start + size);
    }

    // First 8 bytes of data:
    // 01101000 01100101 01101100 01101100 01101111 00100000 01110111 01101111
    @Test
    void readMultipleTimesWorks() {
        assertThatBinary(reader.read(5)).isEqualTo(binaryStringToLong("01101"));
        assertThatBinary(reader.read(12)).isEqualTo(binaryStringToLong("000 01100101 0"));
        assertThatBinary(reader.skip(8).read(3)).isEqualTo(binaryStringToLong("110"));
    }

    @Test
    void readSingleBitWorks() {
        assertThatBinary(reader.read(1)).isEqualTo(0);
    }

    @Test
    void readLastBitWorks() {
        assertThatBinary(reader.skip(DATA.length * 8 - 1).read(1)).isEqualTo(1);
    }

    @Test
    void readByteWorks() {
        assertThat(reader.readByte()).isEqualTo((byte) 0x68);
    }

    @Test
    void readByteMultipleTimesWorks() {
        assertThat(reader.readByte()).isEqualTo((byte) 0x68);
        assertThat(reader.readByte()).isEqualTo((byte) 0x65);
        assertThat(reader.readByte()).isEqualTo((byte) 0x6C);
    }

    @Test
    void readByteAfterSkipWorks() {
        assertThat(reader.skipBytes(8).readByte()).isEqualTo((byte) 0x72);
    }

    @Test
    void readByteAtEndOfDataWorks() {
        assertThat(reader.skipBytes(DATA.length - 1).readByte()).isEqualTo((byte) 0x69);
    }

    @Test
    void readBytePastEndOfDataThrows() {
        assertThatThrownBy(() -> reader.skipBytes(DATA.length).readByte())
                .isInstanceOf(D2ReaderException.class);
    }

    @Test
    void readIntWorks() {
        assertThat(reader.readInt()).isEqualTo(0x6C6C6568);
    }

    @Test
    void readIntMultipleTimesWorks() {
        assertThat(reader.readInt()).isEqualTo(0x6C6C6568);
        assertThat(reader.readInt()).isEqualTo(0x6F77206F);
        assertThat(reader.readInt()).isEqualTo(0x20646C72);
    }

    @Test
    void readIntAfterSkipWorks() {
        assertThat(reader.skipBytes(8).readInt()).isEqualTo(0x20646C72);
    }

    @Test
    void readIntAtEndOfDataWorks() {
        assertThat(reader.skipBytes(DATA.length - SIZEOF_INT).readInt()).isEqualTo(0x6969206F);
    }

    @Test
    void readIntPastEndOfDataThrows() {
        assertThatThrownBy(() -> reader.skipBytes(DATA.length).readInt())
                .isInstanceOf(D2ReaderException.class);
    }

    @Test
    void readStringWorks() {
        assertThat(reader.readString(5)).isEqualTo("hello");
        assertThat(reader.bitPos()).isEqualTo(40);
    }

    @Test
    void readStringMultipleTimesWorks() {
        assertThat(reader.readString(6)).isEqualTo("hello ");
        assertThat(reader.readString(6)).isEqualTo("world ");
        assertThat(reader.readString(3)).isEqualTo("of ");
    }

    @Test
    void readStringAfterSkipWorks() {
        assertThat(reader.skipBytes(15).readString(9)).isEqualTo("diablo ii");
    }

    @Test
    void readStringPastEndOfDataThrows() {
        assertThatThrownBy(() -> reader.skipBytes(DATA.length).readString(5))
                .isInstanceOf(D2ReaderException.class);
    }
}
