package ru.opensecreto.sigmacoin.vm;

public class Frame {

    public final Memory memory;
    public final Stack stack;

    public Frame(Memory memory, Stack stack) {
        this.memory = memory;
        this.stack = stack;
    }
}
