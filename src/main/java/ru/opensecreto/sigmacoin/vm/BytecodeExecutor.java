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
        stopBad();
    }

    private void opcode_STOP_GOOD() {
        stopGood();
    }

    private void opcode_STOP_REVERT() {
        stopException();
    }

    private void stopGood() {
        run = false;
        stopType = StopType.GOOD;
    }

    private void stopBad() {
        run = false;
        stopType = StopType.BAD;
    }

    private ResultFrame invoke(String opcodeName) {
        Word contractId;
        if (executionFrame.stack.getSize() > 0) {
            contractId = executionFrame.stack.pop();
        } else {
            LOGGER.warn("Error executing {} at {}. Can not {}. Can not pop contractID.",
                    executionFrame.contractID, pointer, opcodeName);
            stopException();
            return null;
        }

        Word dataSize;
        if (executionFrame.stack.getSize() > 0) {
            dataSize = executionFrame.stack.pop();
        } else {
            LOGGER.warn("Error executing {} at {}. Can not {}. Can not pop dataSize.",
                    executionFrame.contractID, pointer, opcodeName);
            stopException();
            return null;
        }

        if ((new Word(executionFrame.stack.getSize()).compareTo(dataSize) < 0) | dataSize.isNegative()) {
            LOGGER.warn("Error executing {} at {}. Can not {}. Stack.size is less than dataSize.",
                    executionFrame.contractID, pointer, opcodeName);
            executionFrame.stack.popCustom(executionFrame.stack.getSize());
            stopException();
            return null;
        }

        Stack stackInvoke = new Stack();

        stackInvoke.pushCustom(executionFrame.stack.popCustom(dataSize.toInt()));
        return controller.invoke(stackInvoke, contractId);
    }

    private void opcode_INVOKE() {
        ResultFrame result = invoke("INVOKE");

        if (result == null) return;

        int stackSize = result.stack.getSize();
        executionFrame.stack.pushCustom(result.stack.popCustom(result.stack.getSize()));
        executionFrame.stack.push(new Word(stackSize));

        switch (result.stopType) {
            case ERROR:
                stopError();
                return;

            case EXCEPTION:
                stopException();
                break;

            case GOOD:
                executionFrame.stack.push(Word.WORD_0);
                break;

            case BAD:
                executionFrame.stack.push(Word.WORD_1);
                break;

            default:
                throw new IllegalStateException("Unexpected stop type " + result.stopType.toString());
        }
        pointer++;
    }

    private void opcode_INVOKE_CATCH() {
        ResultFrame result = invoke("INVOKE");

        if (result == null) return;

        int stackSize = result.stack.getSize();
        executionFrame.stack.pushCustom(result.stack.popCustom(result.stack.getSize()));
        executionFrame.stack.push(new Word(stackSize));

        switch (result.stopType) {
            case ERROR:
                stopError();
                return;

            case EXCEPTION:
                executionFrame.stack.push(Word.WORD_2);
                break;

            case GOOD:
                executionFrame.stack.push(Word.WORD_0);
                break;

            case BAD:
                executionFrame.stack.push(Word.WORD_1);
                break;

            default:
                throw new IllegalStateException("Unexpected stop type " + result.stopType.toString());
        }
        pointer++;
    }

    private void stopException() {
        run = false;
        stopType = StopType.EXCEPTION;
    }

    private void stopError() {
        run = false;
        stopType = StopType.ERROR;
    }

    private void opcode_PUSH() {
        executionFrame.stack.push(executionFrame.memory.get(pointer + 1));
        pointer += 2;
    }

    private void opcode_POP() {
        if (executionFrame.stack.getSize() < 1) {
            LOGGER.warn("Error executing {} at {}. Can not POP. Can not pop dataSize.");
            stopException();
            return;
        }

        executionFrame.stack.pop();
        pointer++;
    }

    private void opcode_SWAP() {
        if (executionFrame.stack.getSize() < 1) {
            LOGGER.warn("Error executing {} at {}. Can not SWAP. Can not pop first value - stack is empty.");
            stopException();
            return;
        }
        Word a = executionFrame.stack.pop();

        if (executionFrame.stack.getSize() < 1) {
            LOGGER.warn("Error executing {} at {}. Can not SWAP. Can not pop second value - stack is empty.");
            stopException();
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
            if (opcode.equals(Opcodes.THROW)) {
                opcode_STOP_REVERT();
            } else if (opcode.equals(Opcodes.STOP_BAD)) {
                opcode_STOP_BAD();
            } else if (opcode.equals(Opcodes.STOP_GOOD)) {
                opcode_STOP_GOOD();
            } else if (opcode.equals(Opcodes.INVOKE)) {
                opcode_INVOKE();
            } else if (opcode.equals(Opcodes.INVOKE_CATCH)) {
                opcode_INVOKE_CATCH();
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
                stopException();
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
            stopException();
            return;
        }
        Word A = executionFrame.stack.pop();

        if (executionFrame.stack.getSize() < 1) {
            LOGGER.warn("Error while executing {} at {}. Can not GET. Stack is empty.",
                    executionFrame.contractID, pointer);
            stopException();
            return;
        }
        Word B = executionFrame.stack.pop();

        if (A.isNegative()) {
            LOGGER.warn("Error while executing {} at {}. Can not GET. Position is negative.",
                    executionFrame.contractID, pointer);
            stopException();
            return;
        }

        executionFrame.memory.set(A.toInt(), B);
        pointer++;
    }

    private void opcode_GET() {
        if (executionFrame.stack.getSize() < 1) {
            LOGGER.warn("Error while executing {} at {}. Can not GET. Stack is empty.",
                    executionFrame.contractID, pointer);
            stopException();
            return;
        }
        Word A = executionFrame.stack.pop();
        if (A.isNegative()) {
            LOGGER.warn("Error while executing {} at {}. Can not GET. Position is negative.",
                    executionFrame.contractID, pointer);
            stopException();
            return;
        }

        executionFrame.stack.push(executionFrame.memory.get(A.toInt()));
        pointer++;
    }

    private void opcode_MOD() {
        if (executionFrame.stack.getSize() < 1) {
            LOGGER.warn("Error executing {} at {}. Can not DIV. Can not pop A.",
                    executionFrame.contractID, pointer);
            stopException();
            return;
        }
        Word A = executionFrame.stack.pop();

        if (executionFrame.stack.getSize() < 1) {
            LOGGER.warn("Error executing {} at {}. Can not DIV. Can not pop B.",
                    executionFrame.contractID, pointer);
            stopException();
            return;
        }
        Word B = executionFrame.stack.pop();

        if (A.equals(Word.WORD_0)) {
            LOGGER.warn("Error executing {} at {}. Can not DIV. Division by zero.",
                    executionFrame.contractID, pointer);
            stopException();
            return;
        }

        executionFrame.stack.push(B.mod(A));
        pointer++;
    }

    private void opcode_DIV() {
        if (executionFrame.stack.getSize() < 1) {
            LOGGER.warn("Error executing {} at {}. Can not DIV. Can not pop A.",
                    executionFrame.contractID, pointer);
            stopException();
            return;
        }
        Word A = executionFrame.stack.pop();

        if (executionFrame.stack.getSize() < 1) {
            LOGGER.warn("Error executing {} at {}. Can not DIV. Can not pop B.",
                    executionFrame.contractID, pointer);
            stopException();
            return;
        }
        Word B = executionFrame.stack.pop();

        if (A.equals(Word.WORD_0)) {
            LOGGER.warn("Error executing {} at {}. Can not DIV. Division by zero.",
                    executionFrame.contractID, pointer);
            stopException();
            return;
        }

        executionFrame.stack.push(B.div(A));
        pointer++;
    }

    private void opcode_SUB() {
        if (executionFrame.stack.getSize() < 1) {
            LOGGER.warn("Error executing {} at {}. Can not SUB. Can not pop A.",
                    executionFrame.contractID, pointer);
            stopException();
            return;
        }
        Word A = executionFrame.stack.pop();

        if (executionFrame.stack.getSize() < 1) {
            LOGGER.warn("Error executing {} at {}. Can not SUB. Can not pop B.",
                    executionFrame.contractID, pointer);
            stopException();
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
            stopException();
            return;
        }
        Word a = executionFrame.stack.pop();

        if (executionFrame.stack.getSize() < 1) {
            LOGGER.warn("Error executing {} at {}. Can not SUM. Can not pop second value - stack is empty.",
                    executionFrame.contractID, pointer);
            stopException();
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
            stopException();
            return;
        }

        Word a = executionFrame.stack.pop();
        executionFrame.stack.push(a);
        executionFrame.stack.push(a);
        pointer++;
    }
}
