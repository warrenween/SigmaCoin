package ru.opensecreto.j2p.protocols.stun;

public class MessageMethod {

    //           ** ***            ***  ****
    public static final byte[] BINDING =
            {0b0000_0000, (byte) 0b0000_0001};
    public static final byte[] MASK =
            {0b0011_1110, (byte) 0b1110_1111};

}
