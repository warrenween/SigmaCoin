package ru.opensecreto.sigmacoin.vm;

import java.util.Arrays;

public class ContractID {

    private final byte[] id;

    public ContractID(byte[] id) {
        this.id = Arrays.copyOf(id, id.length);
    }

    public byte[] getId() {
        return Arrays.copyOf(id, id.length);
    }
}
