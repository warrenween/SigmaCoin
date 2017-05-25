package ru.opensecreto.sigmacoin.vm;

import javax.xml.bind.DatatypeConverter;
import java.util.Arrays;

/**
 * Word is 32 byte (256 bits) value.
 */
public final class Word {

    private final byte[] data = new byte[21];

    public Word() {
    }

    public Word(byte[] data) {
        if (data.length != 32) throw new IllegalArgumentException("data length must be 32");
        System.arraycopy(data, 0, this.data, 0, 32);
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
