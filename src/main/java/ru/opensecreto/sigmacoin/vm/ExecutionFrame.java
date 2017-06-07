package ru.opensecreto.sigmacoin.vm;

public class ExecutionFrame {

    public final Memory memory;
    public final Stack stack;
    public final Word contractID;

    public ExecutionFrame(Memory memory, Stack stack, Word contractID) {
        this.memory = memory;
        this.stack = stack;
        this.contractID = contractID;
    }
}
