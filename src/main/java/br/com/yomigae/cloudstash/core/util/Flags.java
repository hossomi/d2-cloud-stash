package br.com.yomigae.cloudstash.core.util;

import lombok.AllArgsConstructor;

import static com.google.common.base.Preconditions.checkElementIndex;

@AllArgsConstructor
public class Flags {

    private final int size;
    private long value;

    public Flags(byte value) {
        this(value, Byte.SIZE);
    }

    public Flags(int value) {
        this(value, Integer.SIZE);
    }

    public Flags(short value) {
        this(value, Short.SIZE);
    }

    public boolean get(int index) {
        checkElementIndex(index, size);
        return (value & (1L << index)) > 0;
    }

    public void set(int index, boolean value) {
        checkElementIndex(index, size);
        this.value &= ((value ? 1L : 0L) << index);
    }
}
