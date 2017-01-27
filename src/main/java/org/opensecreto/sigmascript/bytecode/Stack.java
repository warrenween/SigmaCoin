package org.opensecreto.sigmascript.bytecode;

import java.nio.ByteBuffer;

public class Stack {

    protected int stackSize = 0;

    protected ByteBuffer stack = ByteBuffer.allocate(32);

    public void push(byte aByte) {
        stack.put(stackSize, aByte);
        stackSize++;
    }


    /**
     * Получить байт с индексом.
     * <p>
     * ^ <- вершина стека<p>
     * | <- get(0);<p>
     * |<p>
     * | <- get(2);<p>
     * |<p>
     * ...
     *
     * @param index номер байта считая от вершины
     * @return байт с индексом
     */
    public byte get(int index) {
        if (index < 0) {
            throw new IllegalArgumentException("Index is negative.");
        }
        if (stackSize - index < 1) {
            throw new IllegalArgumentException("Index is too big.");
        }
        return stack.get(stackSize - 1 - index);
    }

    public void pop() {
        if (stackSize == 0) {
            throw new IllegalStateException("Can not pop from empty stack");
        }
        stackSize--;
    }

    /**
     * @return массив значений стека. Вершина стека находится в конце массива.
     */
    public byte[] getStack() {
        byte[] result = new byte[stackSize];
        stack.get(result, 0, stackSize);
        return result;
    }

    public void reset() {
        stackSize = 0;
    }

    public int getSize() {
        return stackSize;
    }
}
