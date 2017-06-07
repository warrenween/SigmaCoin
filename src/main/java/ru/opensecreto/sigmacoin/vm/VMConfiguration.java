package ru.opensecreto.sigmacoin.vm;

import static com.google.common.base.Preconditions.checkArgument;

public class VMConfiguration {

    public final int maxCallDepth;

    public VMConfiguration(int maxCallDepth) {
        checkArgument(maxCallDepth > 0);

        this.maxCallDepth = maxCallDepth;
    }

    @Override
    public String toString() {
        return String.format("Max call depth %s.", maxCallDepth);
    }
}
