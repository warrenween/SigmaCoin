package ru.opensecreto.sigmacoin.vm;

public class Frame {

    public final Memory memory;
    public final Stack stack;
    public final ContractID contractID;

    public Frame(Memory memory, Stack stack, ContractID contractID) {
        this.memory = memory;
        this.stack = stack;
        this.contractID = contractID;
    }
}
