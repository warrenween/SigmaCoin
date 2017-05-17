package ru.opensecreto.sigmacoin.vm;

public class BytecodeExecutor {

    private final Frame frame;
    private long pointer = 0;

    public BytecodeExecutor(Frame frame, VirtualMachineController controller) {
        this.frame = frame;
    }

    public Stack run() {
        boolean run = true;
        boolean success = true;
        while (run) {
            switch (frame.memory.get(pointer)) {
                case Opcodes.STOP_BAD:
                    run = false;
                    success = false;
                    break;


                case Opcodes.STOP_GOOD:
                    run = false;
                    break;


                default:
                    run = false;
                    success = false;
            }
        }

        if (success) {
            frame.stack.push((byte) 0x00);
        } else {
            frame.stack.push((byte) 0x01);
        }

        return frame.stack;
    }
}
