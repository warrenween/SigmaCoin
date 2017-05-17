package ru.opensecreto.sigmacoin.vm;

import javax.xml.bind.DatatypeConverter;
import java.util.Arrays;

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
}
