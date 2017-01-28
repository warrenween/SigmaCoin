package org.opensecreto.sigmascript.bytecode;

import com.google.common.primitives.Longs;

import javax.xml.bind.DatatypeConverter;

import static org.opensecreto.sigmascript.bytecode.Opcodes.*;

public class BytecodeExecutor {

    protected Stack stack = new Stack();
    protected Memory memory = new Memory();
    protected StorageManager storage;

    protected boolean run = true;
    protected boolean finished = false;

    /**
     * false - executing bytecode in storage
     * true - executing bytecode in memory.
     */
    boolean modeMemory = false;
    /**
     * Указатель на команду, которая выполнится при <b>следующей</b> итерации.
     */
    long pointer = 0;

    public BytecodeExecutor(StorageManager storage) {
        this.storage = storage;
    }

    public BytecodeExecutor() {
    }

    protected final void processOpcode(byte opcode) {
        switch (opcode) {
            case OP_STOP:
                run = false;
                finished = true;
                break;
            case OP_MODE_M:
                switchMode(true);
                break;
            case OP_MODE_S:
                switchMode(false);
                break;
            case OP_PUSH:
                stack.push(next());
                break;
            case OP_POP:
                stack.pop();
                break;
            case OP_SET_POINTER:
                if (stack.getSize() < 4) {
                    throw new ExecutionException("OP_SET_POINTER requires 8 byte address in stack. Now " + stack.getSize());
                }
                pointer = Longs.fromBytes(stack.get(0), stack.get(1), stack.get(2), stack.get(3),
                        stack.get(4), stack.get(5), stack.get(6), stack.get(7));
                break;
            case OP_MEM_PUT:
                if (stack.getSize() < 9) {
                    throw new IllegalStateException("To perform OP_MEM_PUT at least stack must contain at least 9 bytes");
                }
                byte value = stack.get(0);
                memory.put(Longs.fromBytes(
                        stack.get(1), stack.get(2), stack.get(3), stack.get(4),
                        stack.get(5), stack.get(6), stack.get(7), stack.get(8)
                ), value);
                break;
            default:
                throw new ExecutionException(
                        "Unknown opcode " + DatatypeConverter.printHexBinary(new byte[]{opcode})
                );
        }
    }

    public boolean isFinished() {
        return finished;
    }

    public void setStorage(StorageManager storage) {
        this.storage = storage;
    }

    public void reset() {
        stack.reset();
        pointer = 0;
        modeMemory = false;
        finished = false;
        run = true;
        memory = new Memory();
    }

    public byte[] getStack() {
        return stack.getStack();
    }

    public Memory getMemory() {
        return memory;
    }

    public void execute() {
        if (storage == null) {
            throw new NullPointerException("storage is null");
        }
        while (run && !finished) {
            processOpcode(next());
        }
    }

    protected byte next() {
        byte result;
        if (modeMemory) {
            if (pointer < 0) {
                throw new ExecutionException("Pointer is negative");
            }
            result = memory.get(pointer);
        } else {
            if (pointer < 0) {
                throw new ExecutionException("Pointer is negative.");
            }
            result = storage.getByte(pointer);
        }
        pointer++;
        return result;
    }

    protected void switchMode(boolean memoryMode) {
        pointer = 0;
        modeMemory = memoryMode;
    }

}
