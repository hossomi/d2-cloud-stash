package br.com.yomigae.cloudstash.core.io;

import br.com.yomigae.cloudstash.core.util.ByteUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static br.com.yomigae.cloudstash.core.util.ByteUtils.flipBytes;
import static java.lang.Math.ceilDiv;
import static java.lang.Math.floorDiv;
import static java.lang.String.format;

public class D2BinaryReader {
    static final int SIZEOF_INT = 4;

    private final byte[] data;
    private final ByteBuffer intBuffer = ByteBuffer.allocate(SIZEOF_INT);
    private final ByteBuffer charBuffer = ByteBuffer.allocate(64);

    private int bit = 0;

    public D2BinaryReader(byte[] data) {
        this.data = data;
        intBuffer.order(ByteOrder.LITTLE_ENDIAN);
    }

    public int checksum(int checksumAddress) {
        int checksum = 0;
        for (int i = 0; i < data.length; i++) {
            /*
             * Expand byte to int keeping the bits, and not the value. For example, we want:
             * 0xAA (-86) -> 0x000000AA (170)
             * and not:
             * 0xAA (-86) -> 0xFFFFFFAA (-86)
             */
            int value = 0x000000FF & data[i];

            // Set the stored data checksum to zero while calculating it.
            if (i >= checksumAddress && i < checksumAddress + SIZEOF_INT) {
                value = 0;
            }

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

    public int remaining() {
        return data.length * 8 - bit;
    }

    public int bitPos() {
        return bit;
    }

    public int bytePos() {
        return floorDiv(bit, 8);
    }

    public D2BinaryReader setBitPos(int pos) {
        if (pos > bits()) {
            throw new D2ReaderException(format(
                    "End of data setting bit position to %d (%d total)",
                    pos, bits()));
        }
        this.bit = pos;
        return this;
    }

    public D2BinaryReader setBytePos(int pos) {
        return setBitPos(pos * 8);
    }

    public D2BinaryReader skip(int bits) {
        if (remaining() < bits) {
            throw new D2ReaderException(format(
                    "End of data skipping %d bits (%d remaining)",
                    bits, remaining()));
        }
        this.bit += bits;
        return this;
    }

    public D2BinaryReader skipBytes(int bytes) {
        return skip(bytes * 8);
    }

    public long read(int bits) {
        if (remaining() < bits) {
            throw new D2ReaderException(format(
                    "End of data reading %d bits (%d remaining)",
                    bits, remaining()));
        }

        if (bits > Integer.SIZE) {
            throw new D2ReaderException(format(
                    "Cannot read more than %d bits (want %d bits)",
                    Integer.SIZE, bits));
        }

        int start = bitPos();
        int end = start + bits;

        // Use long as a buffer to fit a 4-bytes unsigned int
        long buffer = 0;

        // Copy to buffer all necessary bytes in the same order
        // Use end-1 because it is not inclusive
        for (int i = floorDiv(start, 8); i <= floorDiv(end - 1, 8); i++) {
            buffer = (buffer << 8) + (0xFF & data[i]);
        }

        // Chop extra bits to the right
        buffer >>= ceilDiv(end, 8) * 8L - bits - start;
        // Apply mask to Chop extra bits to the left
        buffer &= 0xFFFFFFFFL >>> (Integer.SIZE - bits);
        // Move cursor to next bit
        setBitPos(end);

        return buffer;
    }

    @Deprecated
    public void read(byte[] buffer) {
        read(buffer, buffer.length);
    }

    @Deprecated
    public void read(byte[] buffer, int size) {
        if (data.length - bit < size) {
            throw new D2ReaderException(format(
                    "End of data reading %d bytes (%d remaining)",
                    size, data.length - bit));
        }

        if (buffer.length < size) {
            throw new D2ReaderException(format(
                    "Buffer too small (want %d bytes, buffer has %d)",
                    size, buffer.length));
        }

        System.arraycopy(data, bit, buffer, 0, size);
        bit += size;
    }

    public byte readByte() {
        long data = read(8);
        return (byte) (data & 0xFF);
    }

    public int readInt() {
        return (int) (flipBytes(read(32), 4) & 0xFFFFFFFFL);
    }

    public short readShort() {
        return (short) (flipBytes(read(16), 2) & 0x0000FFFFL);
    }

    public String readString(int size) {
        StringBuilder string = new StringBuilder(size);

        for (int i = 0; i < size; i++) {
            char c = (char) readByte();
            if (c == 0) break;
            string.append(c);
        }

        skipBytes(size - string.length());
        return string.toString();
    }

}
