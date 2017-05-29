package ru.opensecreto.sigmacoin.vm;

/**
 * Note: most significant byte is<b> always at top</b> of stack.
 */
public class Opcodes {

    /**
     * Stack.size is pushed.<br>
     * 0x01 is pushed.<br>
     * Execution is stopped. Stack is returned to calling contract.
     */
    public static final Word STOP_BAD = new Word(0x00);

    /**
     * Stack.size is pushed.<br>
     * 0x00 is pushed.<br>
     * Execution is stopped. Stack is returned to calling contract.
     */
    public static final Word STOP_GOOD = new Word(0x01);

    /**
     * If stack.size > 0 contractID is popped. STOP_BAD executed otherwise.<br>
     * If stack.size > 0 dataSize is popped. STOP_BAD executed otherwise.<br>
     * If stack.size < dataSize or dataSize is negative, all words are removed from stack and STOP_BAD executed.
     * Otherwise dataSize words are moved to new stack. Top word will still be at top of stack.
     * Contract with contractId is invoked with given array of words.
     * Execution result stack is moved to stack.<br>
     * Pointer is increased by one.
     */
    public static final Word INVOKE = new Word(0x02);

    /**
     * Push (pointer+1) word to stack. <br>
     * Pointer is increased forward by 2.
     */
    public static final Word PUSH = new Word(0x10);

    /**
     * If stack.size > 0 then one word is removed from top of stack. Otherwise STOP_BAD is executed.
     * Pointer is increased by 1;
     */
    public static final Word POP = new Word(0x11);

    /**
     * If stack.size > 0 then A is popped. A is pushed. A is pushed. Else STOP_BAD executed.<br>
     * Pointer is increased by 1.
     */
    public static final Word DUP = new Word(0x12);

    /**
     * Pop a. Pop b. Push a. Push b.
     */
    public static final Word SWAP = new Word(0x13);

    /**
     * Pop 2 words. Sum them and push back to stack.
     */
    public static final Word ADD = new Word(0x20);

    /**
     * Pop word a. Pop word b. Push word b-a back.
     */
    public static final Word SUB = new Word(0x21);

    /**
     * Pop a. Pop b. If a==0 execution fails, (b div a) pushed to stack otherwise.
     */
    public static final Word DIV = new Word(0x22);

    /**
     * Pop a. Pop b. If a==0 execution fails, (b mod a) pushed to stack otherwise.
     */
    public static final Word MOD = new Word(0x23);
}
