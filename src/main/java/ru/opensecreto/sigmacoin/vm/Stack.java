package ru.opensecreto.sigmacoin.vm;

import java.util.LinkedList;
import java.util.List;

public class Stack {

    private final List<Word> stack;
    private int size = 0;

    public Stack() {
        this.stack = new LinkedList<>();
    }

    /**
     * Push one word to top of stack.
     *
     * @param word word to be pushed.
     * @throws IllegalStateException if stack size has reached maximum
     */
    public void push(Word word) throws IllegalStateException {
        if (word == null) throw new IllegalArgumentException("word can not be null");
        stack.add(word);
        size++;
    }

    /**
     * Removes one word from top of stack and returns it as the stack.
     *
     * @return one word from stack
     * @throws IllegalStateException if stack is empty
     */
    public Word pop() throws IllegalStateException {
        if (size == 0) throw new IllegalStateException("Nothing to pop. Stack is empty.");
        size--;
        return stack.remove(size);
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
        for (int i = count - 1; i >= 0; i--) {
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
