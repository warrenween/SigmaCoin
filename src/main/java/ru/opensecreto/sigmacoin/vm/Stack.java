package ru.opensecreto.sigmacoin.vm;

public class Stack {

    private final byte[] stack;
    private int size = 0;

    public Stack(int stackSize) {
        this.stack = new byte[stackSize];
    }

    public void push(byte value) {
        if (size==stack.length) throw new IllegalStateException("Can not push. Stack is full.");
        stack[size] = value;
        size++;
    }

    public byte pop() {
        if (size==0) throw new IllegalStateException("Nothing to pop. Stack is empty.");
        return stack[size-1];
    }

    public int getSize() {
        return size;
    }
}
