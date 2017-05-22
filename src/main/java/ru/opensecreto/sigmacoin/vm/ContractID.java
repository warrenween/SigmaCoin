package ru.opensecreto.sigmacoin.vm;

import javax.xml.bind.DatatypeConverter;
import java.util.Arrays;

/**
 * Most significant byte is in the beginning of array, least significant is at the end of array.
 */
public class ContractID {

    private final byte[] id;

    public ContractID(byte[] id) {
        this.id = Arrays.copyOf(id, id.length);
    }

    public byte[] getId() {
        return Arrays.copyOf(id, id.length);
    }

    @Override
    public String toString() {
        return "0x" + DatatypeConverter.printHexBinary(id);
    }

    @Override
    public boolean equals(Object obj) {
        return (obj != null) && (obj instanceof ContractID) && (Arrays.equals(((ContractID) obj).id, id));
    }
}
