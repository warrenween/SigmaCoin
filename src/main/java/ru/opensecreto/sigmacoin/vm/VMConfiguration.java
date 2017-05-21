package ru.opensecreto.sigmacoin.vm;

public class VMConfiguration {

    public final int frameMaxStackSize;
    public final int contractIdLength;
    public final int maxCallDepth;
    public final int memoryMaxSize;

    public VMConfiguration(int frameMaxStackSize, int contractIdLength, int maxCallDepth, int memoryMaxSize) {
        if (maxCallDepth <= 0) throw new IllegalArgumentException("maxCallDepth must be >= 1");
        if (contractIdLength <= 0) throw new IllegalArgumentException("contractIdLength must be >= 1");
        if (frameMaxStackSize <= 0) throw new IllegalArgumentException("frameMaxStack must be >= 1");
        if (memoryMaxSize < 1) throw new IllegalArgumentException("memoryMaxSize must be >= 1");

        this.frameMaxStackSize = frameMaxStackSize;
        this.contractIdLength = contractIdLength;
        this.maxCallDepth = maxCallDepth;
        this.memoryMaxSize = memoryMaxSize;
    }
}
