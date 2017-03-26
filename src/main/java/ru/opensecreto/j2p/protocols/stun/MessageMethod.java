package ru.opensecreto.j2p.protocols.stun;

import java.util.Arrays;

public enum MessageMethod {

    BINDING;

    //           ** ***            ***  ****
    public static final byte[] BINDING_BYTES =
            {0b0000_0000, (byte) 0b0000_0001};
    public static final byte[] MASK =
            {0b0011_1110, (byte) 0b1110_1111};

    public static byte[] convert(MessageMethod method) throws IllegalArgumentException {
        if (method == MessageMethod.BINDING) return MessageMethod.BINDING_BYTES;
        else throw new IllegalArgumentException("bad messageMethod");
    }

    public static MessageMethod convert(byte[] methodData) {
        if (Arrays.equals(methodData, MessageMethod.BINDING_BYTES)) return MessageMethod.BINDING;
        else throw new IllegalArgumentException("bad methodData");
    }
}
