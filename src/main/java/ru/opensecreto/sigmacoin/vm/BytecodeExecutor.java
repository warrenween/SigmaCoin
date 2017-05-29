package ru.opensecreto.sigmacoin.vm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BytecodeExecutor {

    public static final Logger LOGGER = LoggerFactory.getLogger(BytecodeExecutor.class);

    private final VMConfiguration configuration;
    private final VirtualMachineController controller;

    private final Frame frame;
    private long pointer = 0;
    private boolean run = true;
    private boolean success = true;

    public BytecodeExecutor(VMConfiguration configuration, Frame frame, VirtualMachineController controller) {
        this.frame = frame;
        this.configuration = configuration;
        this.controller = controller;
    }

    private void opcode_STOP_BAD() {
        fail();
    }

    private void opcode_STOP_GOOD() {
        stop();
    }

    private void stop() {
        run = false;
    }

    private void opcode_INVOKE() {
        Word contractId;
        if (frame.stack.getSize() > 0) {
            contractId = frame.stack.pop();
        } else {
            LOGGER.warn("Error executing {} at {}. Can not INVOKE. Can not pop contractID.");
            fail();
            return;
        }

        Word dataSize;
        if (frame.stack.getSize() > 0) {
            dataSize = frame.stack.pop();
        } else {
            LOGGER.warn("Error executing {} at {}. Can not INVOKE. Can not pop dataSize.");
            fail();
            return;
        }

        if ((new Word(frame.stack.getSize()).compareTo(dataSize) < 0) | dataSize.isNegative()) {
            LOGGER.warn("Error executing {} at {}. Can not INVOKE. Stack.size is less than dataSize.");
            frame.stack.popCustom(frame.stack.getSize());
            fail();
            return;
        }

        Stack stackInvoke = new Stack(configuration.stackSize);

        stackInvoke.pushCustom(frame.stack.popCustom(dataSize.toInt()));
        Stack result = controller.invoke(stackInvoke, contractId);
        frame.stack.pushCustom(result.popCustom(result.getSize()));
        pointer++;
    }

    private void fail() {
        run = false;
        success = false;
    }

    private void opcode_PUSH() {
        frame.stack.push(frame.memory.get(pointer + 1));
        pointer += 2;
    }

    private void opcode_POP() {
        if (frame.stack.getSize() < 1) {
            LOGGER.warn("Error executing {} at {}. Can not POP. Can not pop dataSize.");
            fail();
            return;
        }

        frame.stack.pop();
        pointer++;
    }

    private void opcode_SWAP() {
        if (frame.stack.getSize() < 1) {
            LOGGER.warn("Error executing {} at {}. Can not SWAP. Can not pop first value - stack is empty.");
            fail();
            return;
        }
        Word a = frame.stack.pop();

        if (frame.stack.getSize() < 1) {
            LOGGER.warn("Error executing {} at {}. Can not SWAP. Can not pop second value - stack is empty.");
            fail();
            return;
        }
        Word b = frame.stack.pop();

        frame.stack.push(a);
        frame.stack.push(b);
        pointer++;
    }

    public Stack run() {
        while (run) {
            Word opcode = frame.memory.get(pointer);
            if (opcode.equals(Opcodes.STOP_BAD)) {
                opcode_STOP_BAD();
            } else if (opcode.equals(Opcodes.STOP_GOOD)) {
                opcode_STOP_GOOD();
            } else if (opcode.equals(Opcodes.INVOKE)) {
                opcode_INVOKE();
            } else if (opcode.equals(Opcodes.PUSH)) {
                opcode_PUSH();
            } else if (opcode.equals(Opcodes.POP)) {
                opcode_POP();
            } else if (opcode.equals(Opcodes.DUP)) {
                opcode_DUP();
            } else if (opcode.equals(Opcodes.SWAP)) {
                opcode_SWAP();
            } else if (opcode.equals(Opcodes.ADD)) {
                opcode_ADD();
            } else if (opcode.equals(Opcodes.SUB)) {
                opcode_SUM();
            } else if (opcode.equals(Opcodes.DIV)) {
                opcode_DIV();
            } else if (opcode.equals(Opcodes.MOD)) {
                opcode_MOD();
            } else {
                fail();
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

    private void opcode_MOD() {
        if (frame.stack.getSize() < 2) {
            LOGGER.warn("Error executing {} at {}. Can not MOD. Stack size is less than 2",
                    frame.contractID, pointer);
            fail();
        } else {
            Word a = frame.stack.pop();
            Word b = frame.stack.pop();

            if (a.equals(Word.WORD_0)) {
                LOGGER.warn("Error executing {} at {}. Can not MOD. Division by zero.",
                        frame.contractID, pointer);
                fail();
            } else {
                frame.stack.push(b.mod(a));
                pointer++;
            }
        }
    }

    private void opcode_DIV() {
        if (frame.stack.getSize() < 2) {
            LOGGER.warn("Error executing {} at {}. Can not DIV. Stack size is less than 2",
                    frame.contractID, pointer);
            fail();
        } else {
            Word a = frame.stack.pop();
            Word b = frame.stack.pop();

            if (a.equals(Word.WORD_0)) {
                LOGGER.warn("Error executing {} at {}. Can not DIV. Division by zero.",
                        frame.contractID, pointer);
                fail();
            } else {
                frame.stack.push(b.div(a));
                pointer++;
            }
        }
    }

    private void opcode_SUM() {
        if (frame.stack.getSize() < 2) {
            LOGGER.warn("Error executing {} at {}. Can not SUB. Stack size is less than 2",
                    frame.contractID, pointer);
            fail();
        } else {
            Word a = frame.stack.pop();
            Word b = frame.stack.pop();
            Word result = b.subtract(a);
            frame.stack.push(result);
            pointer++;
        }
    }

    private void opcode_ADD() {
        if (frame.stack.getSize() < 1) {
            LOGGER.warn("Error executing {} at {}. Can not SUM. Can not pop first value - stack is empty.",
                    frame.contractID, pointer);
            fail();
            return;
        }
        Word a = frame.stack.pop();

        if (frame.stack.getSize() < 1) {
            LOGGER.warn("Error executing {} at {}. Can not SUM. Can not pop second value - stack is empty.",
                    frame.contractID, pointer);
            fail();
            return;
        }
        Word b = frame.stack.pop();

        frame.stack.push(a.sum(b));
        pointer++;
    }

    private void opcode_DUP() {
        if (frame.stack.getSize() < 1) {
            LOGGER.warn("Error executing {} at {}. Can not DUP. Stack is empty.",
                    frame.contractID, pointer);
            fail();
            return;
        }

        Word a = frame.stack.pop();
        frame.stack.push(a);
        frame.stack.push(a);
        pointer++;
    }
}
