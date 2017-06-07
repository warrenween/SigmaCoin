package ru.opensecreto.sigmacoin.vm;

public class ResultFrame {

    public final Stack stack;
    public final StopType stopType;

    public ResultFrame(Stack stack, StopType stopType) {
        this.stack = stack;
        this.stopType = stopType;
    }
}
