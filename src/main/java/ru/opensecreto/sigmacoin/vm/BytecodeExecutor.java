package ru.opensecreto.sigmacoin.vm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.DatatypeConverter;

public class BytecodeExecutor {

    public static final Logger LOGGER = LoggerFactory.getLogger(BytecodeExecutor.class);

    private final VMConfiguration configuration;

    private final Frame frame;
    private long pointer = 0;

    public BytecodeExecutor(VMConfiguration configuration, Frame frame, VirtualMachineController controller) {
        this.frame = frame;
        this.configuration = configuration;
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


                case Opcodes.INVOKE:


                default:
                    run = false;
                    success = false;
                    LOGGER.warn("Error while executing {} - unexpected bytecode 0x{} at {}.",
                            frame.contractID, DatatypeConverter.printHexBinary(new byte[]{frame.memory.get(pointer)}), pointer);
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
