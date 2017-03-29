package ru.opensecreto.sigmacoin.network.protocols.stun;

import java.util.Arrays;

/**
 * Defined by RFC5389, p 10-11
 * <p>
 * Values are already encoded to structure of message.
 */
public enum MessageClass {

    REQUEST, SUCCESS, FAILURE, INDICATION;

    //type bits
    //                 *       *
    public static final byte[] REQUEST_BYTES =
            {0b0000_0000, 0b0000_0000};
    public static final byte[] SUCCESS_BYTES =
            {0b0000_0001, 0b0000_0000};
    public static final byte[] FAILURE_BYTES =
            {0b0000_0001, 0b0001_0000};
    public static final byte[] INDICATION_BYTES =
            {0b0000_0000, 0b0001_0000};
    public static final byte[] MASK =
            {0b0000_0001, 0b0001_0000};

    public static byte[] convert(MessageClass messageClass) {
        if (messageClass == MessageClass.REQUEST) return MessageClass.REQUEST_BYTES;
        else if (messageClass == MessageClass.SUCCESS) return MessageClass.SUCCESS_BYTES;
        else if (messageClass == MessageClass.FAILURE) return MessageClass.FAILURE_BYTES;
        else if (messageClass == MessageClass.INDICATION) return MessageClass.INDICATION_BYTES;
        else throw new IllegalArgumentException("bad messageClass");
    }

    public static MessageClass convert(byte[] classData) {
        if (Arrays.equals(classData , MessageClass.REQUEST_BYTES)) return MessageClass.REQUEST;
        else if (Arrays.equals(classData , MessageClass.SUCCESS_BYTES)) return MessageClass.SUCCESS;
        else if (Arrays.equals(classData , MessageClass.FAILURE_BYTES)) return MessageClass.FAILURE;
        else if (Arrays.equals(classData , MessageClass.INDICATION_BYTES)) return MessageClass.INDICATION;
        else throw new IllegalArgumentException("bad classData");
    }

}
