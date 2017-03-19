package org.opensecreto.sigmascript;

public class Stack {

    protected byte[] stack;
    protected int stackSize = 0;

    public Stack(int size) {
        stack = new byte[size];
    }

    public void push(byte aByte) {
        if (stackSize >= stack.length) {
            throw new IllegalStateException("Can not push. Maximum size reached");
        }
        stack[stackSize] = aByte;
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
        if (index >= stackSize) {
            throw new IllegalArgumentException("Can not get byte. Index is too big,");
        }
        return stack[stackSize - 1 - index];
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
        System.arraycopy(stack, 0, result, 0, stackSize);
        return result;
    }

    public void reset() {
        stackSize = 0;
    }

    public int getSize() {
        return stackSize;
    }
}
