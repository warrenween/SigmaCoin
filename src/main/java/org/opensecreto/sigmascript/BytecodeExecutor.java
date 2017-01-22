package org.opensecreto.sigmascript;

import javax.xml.bind.DatatypeConverter;

import static org.opensecreto.sigmascript.Opcodes.*;

public class BytecodeExecutor {

    protected final byte[] stack = new byte[Config.MAX_STACK];
    protected final byte[] memory = new byte[Config.MAX_MEMORY];
    protected final StorageManager storage;
    protected int stackSize = 0;
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

    public byte execute(StorageManager storage) {
        while (true) {
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
                    next();
                    break;
                case OP_POP:
                    stackPop();
                    break;
                case OP_RETURN:
                    return next();
                default:
                    throw new InvalidOpcodeException(
                            "Unknown opcode " + DatatypeConverter.printHexBinary(new byte[]{opcode})
                    );
            }
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
