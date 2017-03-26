package ru.opensecreto.j2p.protocols.stun;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

public class AttributeParser {

    public static InetSocketAddress parseMAPPED_ADDRESS(Attribute attribute) throws UnknownHostException {
        if (attribute.type != Attribute.MAPPED_ADDRESS) {
            throw new IllegalArgumentException("Attribute has wrong type");
        }
        ByteBuffer buf = ByteBuffer.wrap(attribute.data);
        buf.position(1);
        byte family = buf.get();
        short port = buf.getShort();
        InetAddress address;
        if (family == 0x01) {
            byte[] addressBytes = new byte[4];
            buf.get(addressBytes);
            return new InetSocketAddress(InetAddress.getByAddress(addressBytes), port);
        } else if (family == 0x02) {
            byte[] addressBytes = new byte[16];
            buf.get(addressBytes);
            return new InetSocketAddress(InetAddress.getByAddress(addressBytes), port);
        } else {
            throw new IllegalStateException("bad attribute");
        }
    }

}
