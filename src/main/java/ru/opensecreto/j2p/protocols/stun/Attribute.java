package ru.opensecreto.j2p.protocols.stun;

import java.nio.ByteBuffer;

public class Attribute {

    public static final short MAPPED_ADDRESS = 0x0001;

    public byte[] data;
    public final short type;

    public Attribute(short type) {
        this.type = type;
    }

    public int getSize() {
        if (data.length % 4 == 0) {
            return 4 + data.length;
        } else {
            return 4 + data.length + (4 - data.length % 4);
        }
    }

    public byte[] encode() {
        if (data.length > 65536) {
            throw new IllegalStateException("data array is too big");
        }
        ByteBuffer buf = ByteBuffer.allocate(getSize());

        buf.putShort(type);
        buf.putShort((short) data.length);
        buf.put(data);
        return buf.array();
    }

}
