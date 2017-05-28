package ru.opensecreto.sigmacoin.vm;

/**
 * Note: most significant byte is<b> always at top</b> of stack.
 */
public class Opcodes {

    /**
     * Stop execution. Nothing is reverted.
     */
    public static final Word STOP_BAD = new Word(0x00);

    public static final Word STOP_GOOD = new Word(0x01);

    /**
     * Invoke other contract id.
     * <p>
     * Call parameters:
     * <ul>
     * <li><b>BOTTOM</b>:call data</li>
     * <li>contract id  </li>
     * <li><b>TOP</b>:2 byte int - call data length </li>
     * </ul>
     * <p>
     * All parameters are removed from stack. Contract with given contract id is invoked.
     * Result is pushed to stack - top of result stack will be at top of stack!
     * 2 bytes int is pushed to stack - length of result data.
     * If invoked contract was invoked successfully 0x00 byte is pushed at top of stack, 0x01 otherwise.
     */
    public static final Word INVOKE = new Word(0x02);

    /**
     * Push next byte to stack. Pointer is moved forward by 2.
     */
    public static final Word PUSH = new Word(0x10);

    /**
     * Remove one byte from top of stack.
     */
    public static final Word POP = new Word(0x11);

    public static final Word DUP = new Word(0x12);

    /**
     * Pop 2 words. Sum them and push back to stack.
     */
    public static final Word ADD = new Word(0x20);

    /**
     * Pop word a. Pop word b. Push word b-a back.
     */
    public static final Word SUB = new Word(0x21);

}
