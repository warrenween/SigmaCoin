package ru.opensecreto.sigmacoin.vm;

public class VMConfiguration {

    public final int maxCallDepth;
    public final int memorySize;

    public VMConfiguration( int maxCallDepth, int memorySize) {
        if (maxCallDepth <= 0) throw new IllegalArgumentException("maxCallDepth must be >= 1");
        if (memorySize < 1) throw new IllegalArgumentException("memorySize must be >= 1");

        this.maxCallDepth = maxCallDepth;
        this.memorySize = memorySize;
    }

    @Override
    public String toString() {
        return String.format("Max call depth %s.%nMemory size %s.%n",
                maxCallDepth,  memorySize);
    }
}
