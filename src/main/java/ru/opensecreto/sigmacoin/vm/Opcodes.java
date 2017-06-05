package ru.opensecreto.sigmacoin.vm;

/**
 * Note: most significant byte is<b> always at top</b> of stack.
 */
public class Opcodes {

    /**
     * Current stack and EXCEPTION flag are returned to caller
     */
    public static final Word THROW = new Word(0x00);

    /**
     * <ol>
     * <li>Current stack and BAD flag are returned to caller</li>
     * </ol>
     */
    public static final Word STOP_BAD = new Word(0x01);

    /**
     * <ol>
     * <li>Current stack and GOOD flag are returned to caller</li>
     * </ol>
     */
    public static final Word STOP_GOOD = new Word(0x02);

    /**
     * <ol>
     * <li>If stack.size > 0 contractID is popped. Else THROW.</li>
     * <li>If stack.size > 0 dataSize is popped. Else THROW.</li>
     * <li>
     * If stack.size < dataSize or dataSize is negative, stack is cleared and THROW executed.
     * Else dataSize words are moved to new stack. Top word will be at top of new stack.
     * <li>If contract with given contractId do not exist than reuslt is empty stack and BAD flag</li>
     *<li> Contract with contractId is invoked with given array of words.</li>
     * </li>
     * <li>Result stack is pushed to current stack.</li>
     * <li>
     * If flag is EXCEPTION current stack is returned to caller with EXCEPTION flag.
     * If flag is GOOD resultStack.size and 0x00 is pushed to current stack.
     * If flag is BAD resultStack.size and 0x01 is pushed to current stack.
     * </li>
     * <li>Pointer is increased by 1.</li>
     * </ol>
     */
    public static final Word INVOKE = new Word(0x03);

    /**
     * <ol>
     * <li>Push word from memory to stack with index (pointer+1).</li>
     * <li>Pointer is increased by 2.</li>
     * </ol>
     */
    public static final Word PUSH = new Word(0x10);

    /**
     * <ol>
     * <li>If stack.size > 0 then one word is removed from top of stack. Else exception is thrown.</li>
     * <li>Pointer is increased by 1</li>
     * </ol>
     */
    public static final Word POP = new Word(0x11);

    /**
     * <ol>
     * <li>If stack.size > 0 then A is popped. A is pushed. A is pushed. Else exception is thrown.</li>
     * <li>Pointer is increased by 1</li>
     * </ol>
     */
    public static final Word DUP = new Word(0x12);

    /**
     * <ol>
     * <li>If stack.size > 0 then pop A. Else exception is thrown.</li>
     * <li>If stack.size > 0 then pop B. Else exception is thrown.</li>
     * <li>Push A</li>
     * <li>Push B</li>
     * <li>Pointer is increased by 1</li>
     * </ol>
     */
    public static final Word SWAP = new Word(0x13);

    /**
     * <ol>
     * <li>If stack.size > 0 pop A. Else exception is thrown.</li>
     * <li>If stack.size > 0 pop B. Else exception is thrown.</li>
     * <li>Push (A+B)</li>
     * <li>Pointer is increased by 1</li>
     * </ol>
     */
    public static final Word ADD = new Word(0x20);

    /**
     * <ol>
     * <li>If stack.size > 0 then pop A. Else exception is thrown.</li>
     * <li>If stack.size > 0 then pop B. Else exception is thrown.</li>
     * <li>Push word (B-A) back</li>
     * <li>Pointer is increased by 1</li>
     * </ol>
     */
    public static final Word SUB = new Word(0x21);

    /**
     * <ol>
     * <li>If stack.size > 0 then pop A. Else exception is thrown.</li>
     * <li>If stack.size > 0 then pop B. Else exception is thrown.</li>
     * <li>If (A=0) STOP_REVERT. Else (b div a) is pushed to stack</li>
     * </ol>
     */
    public static final Word DIV = new Word(0x22);

    /**
     * <ol>
     * <li>If stack.size > 0 then pop A. Else exception is thrown.</li>
     * <li>If stack.size > 0 then pop B. Else exception is thrown.</li>
     * <li>If (A=0) STOP_REVERT. Else (b mod a) is pushed to stack</li>
     * </ol>
     */
    public static final Word MOD = new Word(0x23);

    /**
     * <ol>
     * <li>If stack.size > 0 then pop A. Else exception is thrown.</li>
     * <li>If A < 0 then exception is thrown.</li>
     * <li>Word from memory with index A is pushed to stack. Default value is 0x00.</li>
     * <li>Pointer is increased by 1.</li>
     * </ol>
     */
    public static final Word GET = new Word(0x30);

    /**
     * <ol>
     * <li>If stack.size > 0 then pop A. Else exception is thrown.</li>
     * <li>If stack.size > 0 then pop B. Else exception is thrown.</li>
     * <li>If A < 0 exception is thrown</li>
     * <li>Word B is put to memory with index A.</li>
     * <li>Pointer is increased by 1.</li>
     * </ol>
     */
    public static final Word PUT = new Word(0x31);
}
