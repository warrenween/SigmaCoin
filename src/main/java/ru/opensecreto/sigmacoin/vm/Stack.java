package ru.opensecreto.sigmacoin.vm;

import java.util.LinkedList;
import java.util.List;

import static com.google.common.base.Preconditions.*;

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
    public void push(Word word) throws NullPointerException, IllegalStateException {
        checkNotNull(word);
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
        checkState(size > 0, "Stack is empty.");
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
        checkArgument(count >= 0);
        Word[] result = new Word[count];
        for (int i = count - 1; i >= 0; i--) {
            result[i] = pop();
        }
        return result;
    }

    public void pushCustom(Word[] words) {
        for (Word word : words) {
            push(word);
        }
    }

    /**
     * @return amount of bytes this stack contains now
     */
    public int getSize() {
        return size;
    }

}
