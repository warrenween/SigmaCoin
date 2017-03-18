package org.opensecreto.sigmascript.oop;

import org.opensecreto.sigmascript.bytecode.BytecodeExecutor;
import org.opensecreto.sigmascript.bytecode.Stack;
import org.opensecreto.sigmascript.bytecode.StorageManager;

public class SMethod {

    private final MethodExecutor methodExecutor = new MethodExecutor();

    public SMethod(StorageManager storage, long initPointer) {
        methodExecutor.setStorage(storage);
        methodExecutor.init(initPointer);
    }

    public Stack invoke(Stack stack) {
        return methodExecutor.invoke(stack);
    }

    private static class MethodExecutor extends BytecodeExecutor {

        void init(long initPointer) {
            pointer = initPointer;
            stack = new Stack();
            execute();
            reset();
        }

        Stack invoke(Stack stack) {
            this.stack = stack;
            execute();
            reset();
            return stack;
        }

        @Override
        public void reset() {
            pointer = 0;
            modeMemory = true;
            finished = false;
            run = true;
        }
    }
}
