package org.opensecreto.sigmascript;

import javax.xml.bind.DatatypeConverter;

import static org.opensecreto.sigmascript.Opcodes.*;

public class BytecodeExecutor {

    protected Stack stack = new Stack(Config.MAX_STACK);
    protected byte[] memory = new byte[Config.MAX_MEMORY];
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

    protected final void process() {
        byte opcode = next();
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
                    throw new ExecutionException("OP_SET_POINTER requires 4 byte address in stack. Now " + stack.getSize());
                }
                pointer = ((stack.get(0) & 0xff) << 16) |
                        ((stack.get(1) & 0xff) << 8) |
                        (stack.get(2) & 0xff);
                break;
            case OP_MEM_PUT:
                if (stack.getSize() < 4) {
                    throw new IllegalStateException("To perform OP_MEM_PUT at least stack must contain at least 4 bytes");
                }
                byte value = stack.get(0);
                int index = ((stack.get(1) & 0xff) << 16) |
                        ((stack.get(2) & 0xff) << 8) |
                        (stack.get(3) & 0xff);
                memory[index] = value;
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

    public byte[] getStack() {
        return stack.getStack();
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
            if (pointer >= Config.MAX_MEMORY) {
                throw new ExecutionException("Invalid memory address " + pointer + ". "
                        + "Max " + (Config.MAX_MEMORY - 1) + ".");
            }
            result = memory[(int) pointer];
        } else {
            if (pointer >= Config.STORAGE_MAX_SIZE) {
                throw new ExecutionException("Invalid storage address " + pointer + ". "
                        + "Max " + (Config.STORAGE_MAX_SIZE - 1) + ".");
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
