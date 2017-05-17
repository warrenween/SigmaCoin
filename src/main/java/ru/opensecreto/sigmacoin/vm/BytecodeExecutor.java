package ru.opensecreto.sigmacoin.vm;

public class BytecodeExecutor {

    private final Frame frame;
    private long pointer = 0;

    public BytecodeExecutor(Frame frame, VirtualMachineController controller) {
        this.frame = frame;
    }

    public void run() {

    }
}
