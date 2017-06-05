package ru.opensecreto.sigmacoin.vm;

/**
 * Note: most significant byte is<b> always at top</b> of stack.
 */
public class Opcodes {

    /**
     * Immediately stops execution. All Changes are reverted
     */
    public static final Word STOP_REVERT = new Word(0x00);

    /**
     * <ol>
     * <li>Stack.size is pushed</li>
     * <li>0x01 is pushed</li>
     * <li>Stop execution</li>
     * <li>Returned stack to caller</li>
     * </ol>
     */
    public static final Word STOP_BAD = new Word(0x01);

    /**
     * <ol>
     * <li>Stack.size is pushed</li>
     * <li>0x00 is pushed</li>
     * <li>Stop execution</li>
     * <li>Return stack to caller</li>
     * </ol>
     */
    public static final Word STOP_GOOD = new Word(0x02);

    /**
     * <ol>
     * <li>If stack.size > 0 contractID is popped. STOP_REVERT executed otherwise</li>
     * <li>If stack.size > 0 dataSize is popped. STOP_REVERT executed otherwise</li>
     * <li>
     * If stack.size < dataSize or dataSize is negative, all words are removed from stack and STOP_REVERT executed.
     * Otherwise dataSize words are moved to new stack. Top word will still be at top of stack.
     * Contract with contractId is invoked with given array of words.
     * Execution result stack is moved to stack.
     * </li>
     * <li>Pointer is increased by one.</li>
     * </ol>
     */
    public static final Word INVOKE = new Word(0x03);

    /**
     * <ol>
     * <li>Push word from memory with index (pointer+1).</li>
     * <li>Pointer is increased by 2.</li>
     * </ol>
     */
    public static final Word PUSH = new Word(0x10);

    /**
     * <ol>
     * <li>If stack.size > 0 then one word is removed from top of stack. Otherwise STOP_REVERT is executed</li>
     * <li>Pointer is increased by 1</li>
     * </ol>
     */
    public static final Word POP = new Word(0x11);

    /**
     * <ol>
     * <li>If stack.size > 0 then A is popped. A is pushed. A is pushed. Else STOP_REVERT executed</li>
     * <li>Pointer is increased by 1</li>
     * </ol>
     */
    public static final Word DUP = new Word(0x12);

    /**
     * <ol>
     * <li>If stack.size > 0 pop A. Else STOP_REVERT is executed</li>
     * <li>If stack.size > 0 pop B. Else STOP_REVERT is executed</li>
     * <li>Push A</li>
     * <li>Push B</li>
     * <li>Pointer is increased by 1</li>
     * </ol>
     */
    public static final Word SWAP = new Word(0x13);

    /**
     * <ol>
     * <li>If stack.size > 0 pop A. Else STOP_REVERT is executed</li>
     * <li>If stack.size > 0 pop B. Else STOP_REVERT is executed</li>
     * <li>Push (A+B)</li>
     * <li>Pointer is increased by 1</li>
     * </ol>
     */
    public static final Word ADD = new Word(0x20);

    /**
     * <ol>
     * <li>If stack.size > 0 pop A. Else STOP_REVERT is executed</li>
     * <li>If stack.size >0 pop B. Else STOP_REVERT is executed</li>
     * <li>Push word (B-A) back</li>
     * <li>Pointer is increased by 1</li>
     * </ol>
     */
    public static final Word SUB = new Word(0x21);

    /**
     * <ol>
     * <li>If stack.size > 0 pop A. Else STOP_REVERT is executed</li>
     * <li>If stack.size > 0 pop B. Else STOP_REVERT is executed</li>
     * <li>If (A=0) STOP_REVERT. Else (b div a) is pushed to stack</li>
     * </ol>
     */
    public static final Word DIV = new Word(0x22);

    /**
     * <ol>
     * <li>If stack.size > 0 pop A. Else STOP_REVERT is executed</li>
     * <li>If stack.size > 0 pop B. Else STOP_REVERT is executed</li>
     * <li>If (A=0) STOP_REVERT. Else (b mod a) is pushed to stack</li>
     * </ol>
     */
    public static final Word MOD = new Word(0x23);

    /**
     * <ol>
     * <li>If stack.size > 0 pop A. Else STOP_REVERT is executed.</li>
     * <li>If A<0 STOP_REVERT is executed.</li>
     * <li>If memory has word with index A word with index A is pushed to stack. Else 0x00 is pushed to stack.</li>
     * <li>Pointer is increased by 1</li>
     * </ol>
     */
    public static final Word GET = new Word(0x30);

    /**
     * <ol>
     * <li>If stack.size > 0 pop A. Else STOP_REVERT is executed.</li>
     * <li>If stack.size > 0 pop B. Else STOP_REVERT is executed.</li>
     * <li>If A<0 STOP_REVERT is executed.</li>
     * <li>Word B is put to memory with index A</li>
     * <li>Pointer is increased by 1</li>
     * </ol>
     */
    public static final Word PUT = new Word(0x31);
}
