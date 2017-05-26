package ru.opensecreto.sigmacoin.vm;

import com.google.common.primitives.Shorts;

import javax.xml.bind.DatatypeConverter;
import java.util.Arrays;

/**
 * Word is 32 byte (256 bits) signed value. Most significant
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

    public byte[] getData() {
        return Arrays.copyOf(data, 32);
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
