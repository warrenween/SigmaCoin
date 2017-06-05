package ru.opensecreto.sigmacoin.vm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BytecodeExecutor {

    public static final Logger LOGGER = LoggerFactory.getLogger(BytecodeExecutor.class);

    private final VMConfiguration configuration;
    private final VirtualMachineController controller;

    private final ExecutionFrame executionFrame;
    private int pointer = 0;
    private boolean run = true;
    private StopType stopType;

    public BytecodeExecutor(VMConfiguration configuration, ExecutionFrame executionFrame, VirtualMachineController controller) {
        this.executionFrame = executionFrame;
        this.configuration = configuration;
        this.controller = controller;
    }

    private void opcode_STOP_BAD() {
        stop_BAD();
    }

    private void opcode_STOP_GOOD() {
        stop_GOOD();
    }

    private void opcode_STOP_REVERT() {
        stop_REVERT();
    }

    private void stop_GOOD() {
        run = false;
        stopType = StopType.GOOD;
    }

    private void stop_BAD() {
        run = false;
        stopType = StopType.BAD;
    }

    private void opcode_INVOKE() {
        Word contractId;
        if (executionFrame.stack.getSize() > 0) {
            contractId = executionFrame.stack.pop();
        } else {
            LOGGER.warn("Error executing {} at {}. Can not INVOKE. Can not pop contractID.");
            stop_REVERT();
            return;
        }

        Word dataSize;
        if (executionFrame.stack.getSize() > 0) {
            dataSize = executionFrame.stack.pop();
        } else {
            LOGGER.warn("Error executing {} at {}. Can not INVOKE. Can not pop dataSize.");
            stop_REVERT();
            return;
        }

        if ((new Word(executionFrame.stack.getSize()).compareTo(dataSize) < 0) | dataSize.isNegative()) {
            LOGGER.warn("Error executing {} at {}. Can not INVOKE. Stack.size is less than dataSize.");
            executionFrame.stack.popCustom(executionFrame.stack.getSize());
            stop_REVERT();
            return;
        }

        Stack stackInvoke = new Stack();

        stackInvoke.pushCustom(executionFrame.stack.popCustom(dataSize.toInt()));
        ResultFrame result = controller.invoke(stackInvoke, contractId);
        switch (result.stopType) {
            case REVERT:

        }
        executionFrame.stack.pushCustom(result.stack.popCustom(result.stack.getSize()));
        executionFrame.stack.push(new Word(result.stack.getSize()));
        pointer++;
    }

    private void stop_REVERT() {
        run = false;
        stopType = StopType.REVERT;
    }

    private void opcode_PUSH() {
        executionFrame.stack.push(executionFrame.memory.get(pointer + 1));
        pointer += 2;
    }

    private void opcode_POP() {
        if (executionFrame.stack.getSize() < 1) {
            LOGGER.warn("Error executing {} at {}. Can not POP. Can not pop dataSize.");
            stop_REVERT();
            return;
        }

        executionFrame.stack.pop();
        pointer++;
    }

    private void opcode_SWAP() {
        if (executionFrame.stack.getSize() < 1) {
            LOGGER.warn("Error executing {} at {}. Can not SWAP. Can not pop first value - stack is empty.");
            stop_REVERT();
            return;
        }
        Word a = executionFrame.stack.pop();

        if (executionFrame.stack.getSize() < 1) {
            LOGGER.warn("Error executing {} at {}. Can not SWAP. Can not pop second value - stack is empty.");
            stop_REVERT();
            return;
        }
        Word b = executionFrame.stack.pop();

        executionFrame.stack.push(a);
        executionFrame.stack.push(b);
        pointer++;
    }

    public ResultFrame run() {
        while (run) {
            Word opcode = executionFrame.memory.get(pointer);
            if (opcode.equals(Opcodes.STOP_REVERT)) {
                opcode_STOP_REVERT();
            } else if (opcode.equals(Opcodes.STOP_BAD)) {
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
                opcode_SUB();
            } else if (opcode.equals(Opcodes.DIV)) {
                opcode_DIV();
            } else if (opcode.equals(Opcodes.MOD)) {
                opcode_MOD();
            } else if (opcode.equals(Opcodes.GET)) {
                opcode_GET();
            } else if (opcode.equals(Opcodes.PUT)) {
                opcode_PUT();
            } else {
                stop_REVERT();
                LOGGER.warn("Error while executing {} - unexpected bytecode {} at {}.",
                        executionFrame.contractID, executionFrame.memory.get(pointer), pointer);
            }
        }

        return new ResultFrame(executionFrame.stack, stopType);
    }

    private void opcode_PUT() {
        if (executionFrame.stack.getSize() < 1) {
            LOGGER.warn("Error while executing {} at {}. Can not GET. Stack is empty.",
                    executionFrame.contractID, pointer);
            stop_REVERT();
            return;
        }
        Word A = executionFrame.stack.pop();

        if (executionFrame.stack.getSize() < 1) {
            LOGGER.warn("Error while executing {} at {}. Can not GET. Stack is empty.",
                    executionFrame.contractID, pointer);
            stop_REVERT();
            return;
        }
        Word B = executionFrame.stack.pop();

        if (A.isNegative()) {
            LOGGER.warn("Error while executing {} at {}. Can not GET. Position is negative.",
                    executionFrame.contractID, pointer);
            stop_REVERT();
            return;
        }

        executionFrame.memory.set(A.toInt(), B);
        pointer++;
    }

    private void opcode_GET() {
        if (executionFrame.stack.getSize() < 1) {
            LOGGER.warn("Error while executing {} at {}. Can not GET. Stack is empty.",
                    executionFrame.contractID, pointer);
            stop_REVERT();
            return;
        }
        Word A = executionFrame.stack.pop();
        if (A.isNegative()) {
            LOGGER.warn("Error while executing {} at {}. Can not GET. Position is negative.",
                    executionFrame.contractID, pointer);
            stop_REVERT();
            return;
        }

        executionFrame.stack.push(executionFrame.memory.get(A.toInt()));
        pointer++;
    }

    private void opcode_MOD() {
        if (executionFrame.stack.getSize() < 1) {
            LOGGER.warn("Error executing {} at {}. Can not DIV. Can not pop A.",
                    executionFrame.contractID, pointer);
            stop_REVERT();
            return;
        }
        Word A = executionFrame.stack.pop();

        if (executionFrame.stack.getSize() < 1) {
            LOGGER.warn("Error executing {} at {}. Can not DIV. Can not pop B.",
                    executionFrame.contractID, pointer);
            stop_REVERT();
            return;
        }
        Word B = executionFrame.stack.pop();

        if (A.equals(Word.WORD_0)) {
            LOGGER.warn("Error executing {} at {}. Can not DIV. Division by zero.",
                    executionFrame.contractID, pointer);
            stop_REVERT();
            return;
        }

        executionFrame.stack.push(B.mod(A));
        pointer++;
    }

    private void opcode_DIV() {
        if (executionFrame.stack.getSize() < 1) {
            LOGGER.warn("Error executing {} at {}. Can not DIV. Can not pop A.",
                    executionFrame.contractID, pointer);
            stop_REVERT();
            return;
        }
        Word A = executionFrame.stack.pop();

        if (executionFrame.stack.getSize() < 1) {
            LOGGER.warn("Error executing {} at {}. Can not DIV. Can not pop B.",
                    executionFrame.contractID, pointer);
            stop_REVERT();
            return;
        }
        Word B = executionFrame.stack.pop();

        if (A.equals(Word.WORD_0)) {
            LOGGER.warn("Error executing {} at {}. Can not DIV. Division by zero.",
                    executionFrame.contractID, pointer);
            stop_REVERT();
            return;
        }

        executionFrame.stack.push(B.div(A));
        pointer++;
    }

    private void opcode_SUB() {
        if (executionFrame.stack.getSize() < 1) {
            LOGGER.warn("Error executing {} at {}. Can not SUB. Can not pop A.",
                    executionFrame.contractID, pointer);
            stop_REVERT();
            return;
        }
        Word A = executionFrame.stack.pop();

        if (executionFrame.stack.getSize() < 1) {
            LOGGER.warn("Error executing {} at {}. Can not SUB. Can not pop B.",
                    executionFrame.contractID, pointer);
            stop_REVERT();
            return;
        }
        Word B = executionFrame.stack.pop();

        Word result = B.subtract(A);
        executionFrame.stack.push(result);
        pointer++;

    }

    private void opcode_ADD() {
        if (executionFrame.stack.getSize() < 1) {
            LOGGER.warn("Error executing {} at {}. Can not SUM. Can not pop first value - stack is empty.",
                    executionFrame.contractID, pointer);
            stop_REVERT();
            return;
        }
        Word a = executionFrame.stack.pop();

        if (executionFrame.stack.getSize() < 1) {
            LOGGER.warn("Error executing {} at {}. Can not SUM. Can not pop second value - stack is empty.",
                    executionFrame.contractID, pointer);
            stop_REVERT();
            return;
        }
        Word b = executionFrame.stack.pop();

        executionFrame.stack.push(a.sum(b));
        pointer++;
    }

    private void opcode_DUP() {
        if (executionFrame.stack.getSize() < 1) {
            LOGGER.warn("Error executing {} at {}. Can not DUP. Stack is empty.",
                    executionFrame.contractID, pointer);
            stop_REVERT();
            return;
        }

        Word a = executionFrame.stack.pop();
        executionFrame.stack.push(a);
        executionFrame.stack.push(a);
        pointer++;
    }
}
