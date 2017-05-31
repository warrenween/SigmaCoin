package ru.opensecreto.sigmacoin.vm;

public class VMConfiguration {

    public final int maxCallDepth;

    public VMConfiguration(int maxCallDepth) {
        if (maxCallDepth <= 0) throw new IllegalArgumentException("maxCallDepth must be >= 1");

        this.maxCallDepth = maxCallDepth;
    }

    @Override
    public String toString() {
        return String.format("Max call depth %s.", maxCallDepth);
    }
}
