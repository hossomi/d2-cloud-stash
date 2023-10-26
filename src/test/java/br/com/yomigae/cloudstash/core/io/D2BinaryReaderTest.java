package br.com.yomigae.cloudstash.core.io;

import br.com.yomigae.cloudstash.core.parser.D2DataException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static br.com.yomigae.cloudstash.core.io.D2BinaryReader.SIZEOF_INT;
import static br.com.yomigae.cloudstash.core.io.LongBinaryAssert.assertThatBinary;
import static br.com.yomigae.cloudstash.core.util.Utils.binaryToLong;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class D2BinaryReaderTest {

    public static final byte[] DATA = {
            0x68, 0x65, 0x6C, 0x6C, 0x6F, 0x20, 0x77, 0x6F,
            0x72, 0x6C, 0x64, 0x20, 0x6F, 0x66, 0x20, 0x64,
            0x69, 0x61, 0x62, 0x6C, 0x6F, 0x20, 0x69, 0x69
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
    void setByteIndex() {
        assertThat(reader.setByteIndex(10).readByte()).isEqualTo((byte) 0x64);
    }

    @Test
    void findWorks() {
        reader.find(new byte[]{0x77, 0x6F});
        assertThat(reader.byteIndex()).isEqualTo(6);
    }

    @Test
    void findThrowsIfNotFound() {
        assertThatThrownBy(() -> reader.find(new byte[]{0x12, 0x34, 0x56}))
                .isInstanceOf(D2DataException.class);
    }

    @Test
    void readBitsWorks() {
        reader = new D2BinaryReader(new byte[]{0b01010101});
        assertThat(reader.read(5)).isEqualTo(0b00010101);
        assertThat(reader.bitIndex()).isEqualTo(5);
    }

    @ParameterizedTest
    @CsvSource(textBlock = """
            0,  1,  0
            0,  5,  1000
            0,  8,  01101000
            0,  10, 01 01101000
            0,  16, 01100101 01101000
            0,  20, 1100 01100101 01101000
            0,  32, 01101100 01101100 01100101 01101000
            3,  1,  1
            3,  5,  01101
            3,  8,  10101101
            3,  13, 01100 10101101
            3,  16, 10001100 10101101
            3,  21, 011011 0001100 10101101
            3,  32, 11101101 10001101 10001100 10101101
            10, 1,  1
            10, 5,  11001
            10, 8,  00011001
            10, 13, 11011 00011001
            10, 16, 00011011 00011001
            10, 21, 11011 00011011 00011001
            10, 32, 00011011 11011011 00011011 00011001
            1,  8,  10110100
            2,  8,  01011010
            7,  8,  11001010
            8,  8,  01100101
            9,  8,  00110010
            10, 8,  00011001
            """)
    void readBitsWorks(int start, int size, String expect) {
        assertThatBinary(reader.skip(start).read(size)).isEqualTo(binaryToLong(expect));
        assertThat(reader.bitIndex()).isEqualTo(start + size);
    }

    @Test
    void readBitsMultipleTimesWorks() {
        assertThatBinary(reader.read(5)).isEqualTo(binaryToLong("01000"));
        assertThatBinary(reader.read(12)).isEqualTo(binaryToLong("0011 00101011"));
        assertThatBinary(reader.skip(18).read(3)).isEqualTo(binaryToLong("101"));
    }

    @Test
    void readBitsSingleBitWorks() {
        assertThatBinary(reader.read(1)).isEqualTo(0);
    }

    @Test
    void readBitsLastBitWorks() {
        D2BinaryReader d2BinaryReader = reader.skip(DATA.length * 8 - 1);
        assertThatBinary(d2BinaryReader.read(1)).isEqualTo(0);
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
        assertThat(reader.bitIndex()).isEqualTo(40);
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
