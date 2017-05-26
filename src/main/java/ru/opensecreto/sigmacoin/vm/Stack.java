package ru.opensecreto.sigmacoin.vm;

public class Stack {

    private final Word[] stack;
    private int size = 0;

    public Stack(int stackSize) {
        this.stack = new Word[stackSize];
    }

    /**
     * Push one word to top of stack.
     *
     * @param word word to be pushed.
     * @throws IllegalStateException if stack size has reached maximum
     */
    public void push(Word word) throws IllegalStateException {
        if (size == stack.length) throw new IllegalStateException("Can not push. Stack is full.");
        stack[size] = word;
        size++;
    }

    /**
     * Removes one word from top of stack and returns it as the result.
     *
     * @return one word from stack
     * @throws IllegalStateException if stack is empty
     */
    public Word pop() throws IllegalStateException {
        if (size == 0) throw new IllegalStateException("Nothing to pop. Stack is empty.");
        size--;
        return stack[size];
    }

    /**
     * Pop specified count of words and returns them as array.
     * Word from top of stack will be at the end of array.
     *
     * @param count how many words to pop
     * @return array of words. Word from top of stack will be at the end of array.
     */
    public Word[] popCustom(int count) {
        Word[] result = new Word[count];
        for (int i = count; i > 0; i--) {
            result[i] = pop();
        }
        return result;
    }

    public void pushCustom(Word[] words) {
        for (int i = 0; i < words.length; i++) {
            push(words[i]);
        }
    }

    /**
     * @return amount of bytes this stack contains now
     */
    public int getSize() {
        return size;
    }

}
