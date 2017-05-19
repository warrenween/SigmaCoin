package ru.opensecreto.sigmacoin.vm;

import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;
import com.google.common.primitives.Shorts;

import java.util.Arrays;

public class Stack {

    private final byte[] stack;
    private int size = 0;

    public Stack(int stackSize) {
        this.stack = new byte[stackSize];
    }

    public void push(byte value) {
        if (size == stack.length) throw new IllegalStateException("Can not push. Stack is full.");
        stack[size] = value;
        size++;
    }

    public byte pop() {
        if (size == 0) throw new IllegalStateException("Nothing to pop. Stack is empty.");
        size--;
        return stack[size];
    }

    public int getSize() {
        return size;
    }

    public void pushInt(int value) {
        byte[] data = Ints.toByteArray(value);
        for (int i = data.length - 1; i >= 0; i--) {
            push(data[i]);
        }
    }

    public int popInt() {
        if (getSize() < 4)
            throw new IllegalStateException(
                    "Can not pop int - not enough bytes. Available " + getSize() + " bytes, required 4 bytes");
        return Ints.fromBytes(pop(), pop(), pop(), pop());
    }

    public byte[] getStack() {
        return Arrays.copyOf(stack, size);
    }

    public void pushShort(short value) {
        byte[] data = Shorts.toByteArray(value);
        for (int i = data.length - 1; i >= 0; i--) {
            push(data[i]);
        }
    }

    public short popShort() {
        if (getSize() < 2)
            throw new IllegalStateException(
                    "Can not pop short - not enough bytes. Available " + getSize() + " bytes, required 2 bytes");
        return Shorts.fromBytes(pop(), pop());
    }

    public void pushLong(long value) {
        byte[] data = Longs.toByteArray(value);
        for (int i = data.length - 1; i >= 0; i--) {
            push(data[i]);
        }
    }

    public long popLong() {
        if (getSize() < 8)
            throw new IllegalStateException(
                    "Can not pop long - not enough bytes. Available " + getSize() + " bytes, required 8 bytes");
        return Longs.fromBytes(pop(), pop(), pop(), pop(), pop(), pop(), pop(), pop());
    }
}
