package ru.opensecreto.j2p.protocols.stun;

/**
 * Defined by RFC5389, p 10-11
 * <p>
 * Values are already encoded to structure of message.
 */
public class MessageClass {

    //type bits
    //                 *       *
    public static final byte[] REQUEST =
            {0b0000_0000, 0b0000_0000};
    public static final byte[] SUCCESS =
            {0b0000_0001, 0b0000_0000};
    public static final byte[] FAILURE =
            {0b0000_0001, 0b0001_0000};
    public static final byte[] INDICATION =
            {0b0000_0000, 0b0001_0000};
    public static final byte[] MASK =
            {0b0000_0001, 0b0001_0000};

}
