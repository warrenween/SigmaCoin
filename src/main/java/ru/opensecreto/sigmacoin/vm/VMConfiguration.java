package ru.opensecreto.sigmacoin.vm;

public class VMConfiguration {

    public final int frameMaxStackSize;
    public final int contractIdLength;
    public final int maxCallDepth;

    public VMConfiguration(int frameMaxStackSize, int contractIdLength, int maxCallDepth) {
        this.frameMaxStackSize = frameMaxStackSize;
        this.contractIdLength = contractIdLength;
        this.maxCallDepth = maxCallDepth;
    }
}
