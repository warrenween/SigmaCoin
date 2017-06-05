package ru.opensecreto.sigmacoin.vm;

public class ResultFrame {

    public final Stack result;
    public final StopType stopType;

    public ResultFrame(Stack result, StopType stopType) {
        this.result = result;
        this.stopType = stopType;
    }
}
