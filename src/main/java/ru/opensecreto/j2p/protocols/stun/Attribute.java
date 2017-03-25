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

    public void encode(byte[] out, int offset) {
        if (data.length > 65536) {
            throw new IllegalStateException("data array is too big");
        }
        ByteBuffer buf = ByteBuffer.wrap(out, offset, getSize());

        buf.putShort(type);
        buf.putShort((short) data.length);
        buf.put(data);
    }

    public static byte[] encodeAll(Iterable<Attribute> attributes) {
        int dataSize = 0;
        for (Attribute attribute : attributes) {
            dataSize += attribute.getSize();
        }

        int offset = 0;
        byte[] data = new byte[dataSize];
        for (Attribute attribute : attributes) {
            attribute.encode(data, offset);
            offset += attribute.getSize();
        }
        return data;
    }

}
