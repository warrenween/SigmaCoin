package ru.opensecreto.j2p.protocols.stun;

import java.nio.ByteBuffer;

public class Attribute {

    public static final short MAPPED_ADDRESS = 0x0001;

    public byte[] data;
    public final short type;

    public Attribute(short type) {
        this.type = type;
    }

    public byte[] encode() {
        if (data.length > 65536) {
            throw new IllegalStateException("data array is too big");
        }
        ByteBuffer buf;
        if (data.length % 4 == 0) {
            buf = ByteBuffer.allocate(4 + data.length);
        } else {
            buf = ByteBuffer.allocate(4 + data.length + (4 - data.length % 4));
        }

        buf.putShort(type);
        buf.putShort((short) data.length);
        buf.put(data);
        return buf.array();
    }

}
