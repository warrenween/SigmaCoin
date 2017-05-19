package ru.opensecreto.sigmacoin.vm;

/**
 * Note: most significant byte is<b> always at top</b> of stack.
 */
public class Opcodes {

    /**
     * Stop execution. Nothing is reverted.
     */
    public static final byte STOP_BAD = 0x00;

    public static final byte STOP_GOOD = 0x01;

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
    public static final byte INVOKE = 0x02;

    /**
     * Push next byte to stack. Pointer is moved forward by 2.
     */
    public static final byte PUSH = 0x10;


}
