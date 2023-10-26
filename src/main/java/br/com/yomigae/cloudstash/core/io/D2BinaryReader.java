package br.com.yomigae.cloudstash.core.io;

import br.com.yomigae.cloudstash.core.parser.D2DataException;
import br.com.yomigae.cloudstash.core.util.ByteUtils;
import com.google.common.primitives.Bytes;

import java.util.HexFormat;

import static br.com.yomigae.cloudstash.core.util.ByteUtils.flipBits;
import static br.com.yomigae.cloudstash.core.util.ByteUtils.transform;
import static java.lang.Math.floorDiv;
import static java.lang.Math.floorMod;
import static java.lang.String.format;

public class D2BinaryReader {
    static final int SIZEOF_INT = 4;

    private final byte[] data;
    @Deprecated
    private int bit = 0;
    private int byteIndex = 0;
    private int bitIndex = 0;

    public D2BinaryReader(byte[] data) {
        // Store bit-flipped bytes TODO: Why?
        this.data = transform(data, ByteUtils::flipBits);
    }

    public int checksum(int checksumAddress) {
        int checksum = 0;
        for (int i = 0; i < data.length; i++) {
            // Set the stored data checksum to zero while calculating it. Otherwise, expand data into int.
            int value = (i < checksumAddress || i >= checksumAddress + SIZEOF_INT)
                    ? 0x000000ff & flipBits(data[i])
                    : 0;

            // Not sure why but this is how the checksum is.
            checksum = (checksum << 1) + value + (checksum < 0 ? 1 : 0);
        }
        return checksum;
    }

    public int bits() {
        return data.length * 8;
    }

    public int bytes() {
        return data.length;
    }

    public int bitsRemaining() {
        return data.length * 8 - bit;
    }

    public int bitIndex() {
        return byteIndex * 8 + bitIndex;
    }

    public int byteIndex() {
        return byteIndex;
    }

    public D2BinaryReader setBitIndex(int index) {
        if (index > bits()) {
            throw new D2ReaderException(format(
                    "End of data setting bit index to %d (%d total)",
                    index, bits()));
        }
        this.byteIndex = floorDiv(index, 8);
        this.bitIndex = floorMod(index, 8);
        return this;
    }

    public D2BinaryReader setByteIndex(int index) {
        return setBitIndex(index * 8);
    }

    public D2BinaryReader skip(int bits) {
        return setBitIndex(bitIndex() + bits);
    }

    public D2BinaryReader skipBytes(int bytes) {
        return skip(bytes * 8);
    }

    public D2BinaryReader find(byte[] target) {
        int index = Bytes.indexOf(this.data, transform(target, ByteUtils::flipBits));
        if (index == -1) {
            throw new D2DataException(format(
                    "Could not find 0x%s in data",
                    HexFormat.of().formatHex(target)));
        }
        return setByteIndex(index);
    }

    private byte read() {
        if (byteIndex >= this.data.length) {
            throw new D2ReaderException("End of data reading bit");
        }

        // Mask for desired bit
        int mask = 0x80 >> bitIndex;
        // Get bit from data
        int bit = (this.data[byteIndex] & mask);
        // Shift to first bit
        bit >>= 7 - bitIndex;

        bitIndex++;
        if (bitIndex >= 8) {
            bitIndex = 0;
            byteIndex++;
        }
        return (byte) (bit & 0xff);
    }

    public long read(int bits) {
        if (bits > Integer.SIZE) {
            throw new D2ReaderException(format(
                    "Cannot read more than %d bits (want %d bits)",
                    Integer.SIZE, bits));
        }

        // Use long as a buffer to fit a 4-bytes unsigned int
        long buffer = 0;
        for (int b = 0; b < bits; b++) {
            // Read bit and shift to correct position
            buffer |= (long) read() << b;
        }

        return buffer;
    }

    public byte readByte() {
        long data = read(8);
        return (byte) (data & 0xFF);
    }

    public int readInt() {
        return (int) (read(32) & 0xFFFFFFFFL);
    }

    public short readShort() {
        return (short) (read(16) & 0x0000FFFFL);
    }

    public String readString(int size) {
        StringBuilder string = new StringBuilder(size);

        for (int i = 0; i < size; i++) {
            char c = (char) readByte();
            if (c == 0) {break;}
            string.append(c);
        }

        skipBytes(size - string.length());
        return string.toString();
    }

}
