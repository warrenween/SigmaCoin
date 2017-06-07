package ru.opensecreto.sigmacoin.vm;

import static com.google.common.base.Preconditions.checkNotNull;

public class ResultFrame {

    public final Stack stack;
    public final StopType stopType;

    public ResultFrame(Stack stack, StopType stopType)throws NullPointerException {
        this.stack = checkNotNull(stack);
        this.stopType = checkNotNull(stopType);
    }
}
