package ru.opensecreto.sigmacoin.vm;

import static com.google.common.base.Preconditions.checkNotNull;

public class ExecutionFrame {

    public final Memory memory;
    public final Stack stack;
    public final Word contractID;

    public ExecutionFrame(Memory memory, Stack stack, Word contractID) {
        this.memory = checkNotNull(memory);
        this.stack = checkNotNull(stack);
        this.contractID = checkNotNull(contractID);
    }
}
