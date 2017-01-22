package org.opensecreto.sigmascript;

import javax.xml.bind.DatatypeConverter;

import static org.opensecreto.sigmascript.Opcodes.*;

public class BytecodeExecutor {

    protected byte[] stack = new byte[Config.MAX_STACK];
    protected byte[] memory = new byte[Config.MAX_MEMORY];
    protected StorageManager storage;
    protected int stackSize = 0;

    protected boolean run = true;
    protected boolean finished = false;
    protected byte exitCode;


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

    protected final void process() {
        byte opcode = next();
        switch (opcode) {
            case OP_JUMP_M:
                switchMode(true);
                break;
            case OP_JUMP_S:
                switchMode(false);
                break;
            case OP_PUSH:
                stackPush(next());
                break;
            case OP_POP:
                stackPop();
                break;
            case OP_RETURN:
                if (stackSize == 0) {
                    exitCode = 0;
                } else {
                    exitCode = stack[stackSize - 1];
                }
                run = false;
                finished = true;
            default:
                throw new InvalidOpcodeException(
                        "Unknown opcode " + DatatypeConverter.printHexBinary(new byte[]{opcode})
                );
        }
    }

    public boolean isFinished() {
        return finished;
    }

    public byte getExitCode() {
        return exitCode;
    }

    public void setStorage(StorageManager storage) {
        this.storage = storage;
    }

    public void reset() {
        stackSize = 0;
        for (int i = 0; i < memory.length; i++) {
            memory[i] = 0;
        }
    }

    protected void jump(long newPointer) {
        if (newPointer < 0) {
            throw new IndexOutOfBoundsException("Pointer can not be negative");
        }
        if (modeMemory) {
            if (newPointer > Config.MAX_MEMORY) {
                throw new IndexOutOfBoundsException("New pointer " +
                        newPointer +
                        " is more than available memory " +
                        Config.MAX_MEMORY);
            }
            pointer = newPointer;
        } else {
            if (newPointer > 4294967295L) {
                throw new IndexOutOfBoundsException("New pointer is larger than max");
            }
            pointer = newPointer;
        }
    }

    protected void jumpRel(long offset) {
        jump(pointer + offset);
    }

    protected void stackPush(byte aByte) {
        stack[stackSize] = aByte;
        stackSize++;
    }

    protected void stackPop() {
        stackSize--;
    }

    public byte[] getStack() {
        byte[] result = new byte[stackSize];
        System.arraycopy(stack, 0, result, 0, stackSize);
        return result;
    }

    public byte[] getMemory() {
        return memory;
    }

    public void execute() {
        if (storage == null) {
            throw new NullPointerException("storage is null");
        }
        while (run && !finished) {
            process();
        }
    }

    protected byte next() {
        byte result;
        if (modeMemory) {
            result = memory[(int) pointer];
        } else {
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
