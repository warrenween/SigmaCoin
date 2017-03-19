package org.opensecreto.sigmascript;

import java.util.ArrayList;
import java.util.List;

public class DebuggableBytecodeExecutor extends BytecodeExecutor {

    public List<Long> memoryBreakpoints = new ArrayList<>();
    public List<Long> storageBreakpoints = new ArrayList<>();

    @Override
    public void execute() {
        if (storage == null) {
            throw new NullPointerException("storage is null");
        }
        while (run && !finished) {
            if (modeMemory) {
                if (!memoryBreakpoints.contains(pointer)) {
                    processOpcode(next());
                } else {
                    return;
                }
            } else {
                if (!storageBreakpoints.contains(pointer)) {
                    processOpcode(next());
                } else {
                    return;
                }
            }
        }
    }

    public long getPointer() {
        return pointer;
    }

    public boolean getMode() {
        return modeMemory;
    }
}
