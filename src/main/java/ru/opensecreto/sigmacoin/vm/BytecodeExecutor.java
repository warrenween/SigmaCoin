package ru.opensecreto.sigmacoin.vm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BytecodeExecutor {

    public static final Logger LOGGER = LoggerFactory.getLogger(BytecodeExecutor.class);

    private final VMConfiguration configuration;
    private final VirtualMachineController controller;

    private final Frame frame;
    private long pointer = 0;

    public BytecodeExecutor(VMConfiguration configuration, Frame frame, VirtualMachineController controller) {
        this.frame = frame;
        this.configuration = configuration;
        this.controller = controller;
    }

    public Stack run() {
        boolean run = true;
        boolean success = true;
        while (run) {
            Word opcode = frame.memory.get(pointer);
            if (opcode.equals(Opcodes.STOP_BAD)) {
                run = false;
                success = false;
            } else if (opcode.equals(Opcodes.STOP_GOOD)) {
                run = false;
            } else if (opcode.equals(Opcodes.INVOKE)) {
                Stack stackInvoke = new Stack(configuration.stackSize);

                Word dataSize = frame.stack.pop();
                if (!dataSize.isInRange(new Word(0), new Word(configuration.stackSize))) {
                    LOGGER.warn("Error executing {} at {}. DataSize parameter exceed stackSize parameter.",
                            frame.contractID, pointer);
                    run = false;
                    success = false;
                } else {
                    Word contractId = frame.stack.pop();
                    stackInvoke.pushCustom(frame.stack.popCustom(dataSize.toInt()));

                    Stack result = controller.invoke(stackInvoke, contractId);
                    frame.stack.pushCustom(result.popCustom(result.getSize()));
                    pointer++;
                }
            } else if (opcode.equals(Opcodes.PUSH)) {
                frame.stack.push(frame.memory.get(pointer + 1));
                pointer += 2;
            } else if (opcode.equals(Opcodes.POP)) {
                frame.stack.pop();
                pointer++;
            } else if (opcode.equals(Opcodes.DUP)) {
                Word data = frame.stack.pop();
                frame.stack.push(data);
                frame.stack.push(data);
                pointer++;
            } else {
                run = false;
                success = false;
                LOGGER.warn("Error while executing {} - unexpected bytecode {} at {}.",
                        frame.contractID, frame.memory.get(pointer), pointer);
            }
        }

        frame.stack.push(new Word(frame.stack.getSize()));

        if (success) {
            frame.stack.push(new Word(0x00));
        } else {
            frame.stack.push(new Word(0x01));
        }

        return frame.stack;
    }
}
