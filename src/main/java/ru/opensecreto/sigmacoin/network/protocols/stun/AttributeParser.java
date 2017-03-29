package ru.opensecreto.sigmacoin.network.protocols.stun;

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
        int port = buf.getShort() & 0xffff;
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

    public static InetSocketAddress parseXOR_MAPPED_ADDRESS(Attribute attribute, byte[] id) throws UnknownHostException {
        if (attribute.type != Attribute.XOR_MAPPED_ADDRESS) {
            throw new IllegalArgumentException("Attribute has wrong type");
        }
        ByteBuffer buf = ByteBuffer.wrap(attribute.data);
        buf.position(1);
        byte family = buf.get();
        int port = buf.getShort() & 0xffff;
        port ^= ((StunMessage.MAGIC_COOKIE[0] & 0xff) << 8) | (StunMessage.MAGIC_COOKIE[1] & 0xff);
        InetAddress address;
        if (family == 0x01) {
            byte[] addressBytes = new byte[4];
            buf.get(addressBytes);
            addressBytes[0] = (byte) (addressBytes[0] ^ StunMessage.MAGIC_COOKIE[0]);
            addressBytes[1] = (byte) (addressBytes[1] ^ StunMessage.MAGIC_COOKIE[1]);
            addressBytes[2] = (byte) (addressBytes[2] ^ StunMessage.MAGIC_COOKIE[2]);
            addressBytes[3] = (byte) (addressBytes[3] ^ StunMessage.MAGIC_COOKIE[3]);
            return new InetSocketAddress(InetAddress.getByAddress(addressBytes), port);
        } else if (family == 0x02) {
            byte[] addressBytes = new byte[16];
            buf.get(addressBytes);

            if (id.length != 12) throw new IllegalArgumentException("id length must be 12 bytes");
            for (int i = 0; i < StunMessage.MAGIC_COOKIE.length; i++) {
                addressBytes[i] ^= StunMessage.MAGIC_COOKIE[i];
            }
            for (int i = 0; i < id.length; i++) {
                addressBytes[4 + i] ^= id[i];
            }

            return new InetSocketAddress(InetAddress.getByAddress(addressBytes), port);
        } else {
            throw new IllegalStateException("bad attribute");
        }
    }

}
