package ru.opensecreto.sigmacoin.vm;

import com.google.common.primitives.Ints;
import com.google.common.primitives.Shorts;

import javax.xml.bind.DatatypeConverter;
import java.util.Arrays;

/**
 * Word is 32 byte (256 bits) signed value. Most significant bit as at the begining of array.
 * Word is immutable. All operations return new word.
 */
public final class Word {

    public static final int WORD_SIZE = 32;

    private final byte[] data = new byte[WORD_SIZE];

    public Word() {
    }

    public Word(byte[] data) {
        if (data.length != 32) throw new IllegalArgumentException("data length must be 32");
        System.arraycopy(data, 0, this.data, 0, WORD_SIZE);
    }

    public Word(byte value) {
        data[31] = value;
    }

    public Word(short value) {
        System.arraycopy(Shorts.toByteArray(value), 0, data, 30, 2);
    }

    public Word(int value) {
        System.arraycopy(Ints.toByteArray(value), 0, data, 28, 4);
    }

    public byte[] getData() {
        return Arrays.copyOf(data, 32);
    }

    public Word sum(Word value) {
        byte[] result = new byte[32];
        int carry = 0;
        for (int i = WORD_SIZE - 1; i >= 0; i--) {
            int tmp = (data[i] & 0xff) + (value.data[i] & 0xff) + carry;
            result[i] = (byte) (tmp & 0xff);
            carry = (tmp & 0xff00) >> 8;
        }
        return new Word(result);
    }

    /**
     * Subtract value from this
     *
     * @param value value to subtract
     * @return
     */
    public Word subtract(Word value) {
        byte[] buf = Arrays.copyOf(value.data, WORD_SIZE);
        for (int i = 0; i < buf.length; i++) {
            buf[i] ^= (byte) 0xff;
        }
        return sum(new Word(buf));
    }

    public boolean isNegative() {
        return (data[0] >>> 8) == 1;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj != null) && (obj instanceof Word) && Arrays.equals(((Word) obj).data, data);
    }

    @Override
    public String toString() {
        return "0x" + DatatypeConverter.printHexBinary(data);
    }
}
