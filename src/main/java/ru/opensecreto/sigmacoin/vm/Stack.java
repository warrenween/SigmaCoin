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

    /**
     * Push one byte to top of stack. Pushed byte will be at top of stack.
     *
     * @param value byte to be pushed.
     * @throws IllegalStateException if stack size has reached maximum
     */
    public void push(byte value) throws IllegalStateException {
        if (size == stack.length) throw new IllegalStateException("Can not push. Stack is full.");
        stack[size] = value;
        size++;
    }

    /**
     * Removes one byte from top of stack and returns it as the result.
     *
     * @return one byte from stack
     * @throws IllegalStateException if stack is empty
     */
    public byte pop() throws IllegalStateException {
        if (size == 0) throw new IllegalStateException("Nothing to pop. Stack is empty.");
        size--;
        return stack[size];
    }

    /**
     * @return amount of bytes this stack contains now
     */
    public int getSize() {
        return size;
    }

    /**
     * Interpret given value as 4 bytes array and {@link Stack#pushCustom(byte[])} it.
     * Most significant byte of given value will be at top of stack.
     *
     * @param value integer to push to this stack.
     */
    public void pushInt(int value) {
        pushCustom(Ints.toByteArray(value));
    }

    /**
     * Pops 4 bytes and interprets them as int.
     * Byte from top of stack will be the most significant byte of result.
     *
     * @return 4 bytes integer.
     */
    public int popInt() {
        return Ints.fromByteArray(popCustom(Integer.BYTES));
    }

    /**
     * Interpret given value as 2 bytes array and {@link Stack#pushCustom(byte[])} it.
     * Most significant byte of given value will be at top of stack.
     *
     * @param value short value to push to this stack.
     */
    public void pushShort(short value) {
        pushCustom(Shorts.toByteArray(value));
    }

    /**
     * Pops 2 bytes and interprets them as short.
     * Byte from top of stack will be the most significant byte of result.
     *
     * @return 4 bytes integer.
     */
    public short popShort() {
        return Shorts.fromByteArray(popCustom(Short.BYTES));
    }

    /**
     * Interpret given value as 8 bytes array and {@link Stack#pushCustom(byte[])} it.
     * Most significant byte of given value will be at top of stack.
     *
     * @param value long value to push to this stack.
     */
    public void pushLong(long value) {
        pushCustom(Longs.toByteArray(value));
    }

    /**
     * Pops 8 bytes and interprets them as int.
     * Byte from top of stack will be the most significant byte of result.
     *
     * @return long integer.
     */
    public long popLong() {
        return Longs.fromByteArray(popCustom(Long.BYTES));
    }

    /**
     * Pops specified amount of bytes from this stack. Byte from top of stack will have index 0.
     *
     * @param size amount of bytes to pop.
     * @return array of popped bytes. Byte from top of stack will have index 0.
     * @throws IllegalStateException    if stack contains less bytes than required
     * @throws IllegalArgumentException if size is negative
     */
    public byte[] popCustom(int size) throws IllegalStateException, IllegalArgumentException {
        if (size < 0) throw new IllegalArgumentException("size can not be negative");
        if (getSize() < size) throw new IllegalStateException("Not enough bytes to pop. " + "" +
                "Available " + getSize() + " bytes, required " + size + " bytes");
        byte[] data = new byte[size];
        for (int i = 0; i < data.length; i++) {
            data[i] = pop();
        }
        return data;
    }

    /**
     * Pushes given array of bytes to stack. Byte with index 0 will be at top of stack.
     *
     * @param data array of bytes to push
     * @throws IllegalArgumentException if array is null
     */
    public void pushCustom(byte[] data) throws IllegalArgumentException {
        if (data == null) throw new IllegalArgumentException("array can not be null");
        for (int i = data.length - 1; i >= 0; i--) {
            push(data[i]);
        }
    }

}
