package ru.opensecreto.j2p.protocols.stun;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Attribute {

    public static final short MAPPED_ADDRESS = 0x0001;
    public static final short USERNAME = 0x0006;
    public static final short MESSAGE_INTEGRITY = 0x0008;
    public static final short ERROR_CODE = 0x0009;
    public static final short UNKNOWN_ATTRIBUTES = 0x000A;
    public static final short REALM = 0x0014;
    public static final short NONCE = 0x0015;
    public static final short XOR_MAPPED_ADDRESS = 0x0020;
    public static final short SOFTWARE = (short) 0x8022;

    public byte[] data;
    public final short type;

    public Attribute(short type) {
        this.type = type;
    }

    public Attribute(short type, byte[] data) {
        this.data = data;
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

    @Override
    public boolean equals(Object obj) {
        return (obj != null) && (obj instanceof Attribute) && Arrays.equals(((Attribute) obj).data, data);
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

    public static List<Attribute> decode(byte[] data) {
        ByteBuffer buf = ByteBuffer.wrap(data);
        List<Attribute> attributes = new ArrayList<>();
        while (buf.hasRemaining()) {
            short type = buf.getShort();
            int length = buf.getShort();
            byte[] attrData = new byte[length];
            buf.get(attrData);

            if (buf.position() % 4 != 0) {
                buf.position(buf.position() + 4 - (buf.position() % 4));
            }

            Attribute attribute = new Attribute(type);
            attribute.data = attrData;
            attributes.add(attribute);
        }
        return attributes;
    }

}
