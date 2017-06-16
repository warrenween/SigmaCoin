package ru.opensecreto.sigmacoin.vm;

import static com.google.common.base.Preconditions.checkNotNull;

public class ExecutionFrame {

    public final Memory memory;
    public final Stack stack;
    public final AccountAddress accountAddress;

    public ExecutionFrame(Memory memory, Stack stack, AccountAddress accountAddress) {
        this.memory = checkNotNull(memory);
        this.stack = checkNotNull(stack);
        this.accountAddress = checkNotNull(accountAddress);
    }
}
