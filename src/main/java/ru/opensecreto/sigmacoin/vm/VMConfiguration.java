package ru.opensecreto.sigmacoin.vm;

public class VMConfiguration {

    public final int stackSize;
    public final int contractIdLength;
    public final int maxCallDepth;
    public final int memorySize;

    public VMConfiguration(int stackSize, int contractIdLength, int maxCallDepth, int memorySize) {
        if (maxCallDepth <= 0) throw new IllegalArgumentException("maxCallDepth must be >= 1");
        if (contractIdLength <= 0) throw new IllegalArgumentException("contractIdLength must be >= 1");
        if (stackSize <= 0) throw new IllegalArgumentException("frameMaxStack must be >= 1");
        if (memorySize < 1) throw new IllegalArgumentException("memorySize must be >= 1");

        this.stackSize = stackSize;
        this.contractIdLength = contractIdLength;
        this.maxCallDepth = maxCallDepth;
        this.memorySize = memorySize;
    }
}
