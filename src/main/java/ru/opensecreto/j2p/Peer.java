package ru.opensecreto.j2p;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.DatatypeConverter;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

class Peer {

    public static final Logger LOGGER = LoggerFactory.getLogger(Peer.class);
    public final static int PEER_DATA_SIZE = 37;

    public final InetAddress address;
    public int port;
    public long lastSeen;
    public long unbanTime;

    public Peer(InetAddress address, int port, long lastSeen, long unbanTime) {
        this.address = address;
        this.port = port;
        this.lastSeen = lastSeen;
        this.unbanTime = unbanTime;
    }

    public static Peer deserialize(byte[] data) {
        if (data.length != PEER_DATA_SIZE) {
            LOGGER.warn("Could not deserialize peer data. Expected {} bytes. Got {}. Data: {}",
                    PEER_DATA_SIZE, data.length, DatatypeConverter.printHexBinary(data));
            throw new IllegalArgumentException();
        }
        ByteBuffer buffer = ByteBuffer.wrap(data);
        byte addressType = buffer.get();
        InetAddress address = null;
        if (addressType == 4) {
            byte[] addr = new byte[4];
            buffer.get(addr);
            buffer.get(new byte[12]);
            try {
                address = InetAddress.getByAddress(addr);
            } catch (UnknownHostException e) {
                LOGGER.warn("Could not deserialize peer address.", e);
            }
        } else if (addressType == 6) {
            byte[] addr = new byte[16];
            buffer.get(addr);
            try {
                address = InetAddress.getByAddress(addr);
            } catch (UnknownHostException e) {
                LOGGER.warn("Could not deserialize peer address.", e);
            }
        }
        int port = buffer.getInt();
        long lastSeen = buffer.getLong();
        long unbanTime = buffer.getLong();
        return new Peer(address, port, lastSeen, unbanTime);
    }

    /**
     * Format:
     * 1 byte - address type
     * 16 bytes - ip address (compatible with ipv6)
     * 4 bytes - port
     * 8 bytes - last seen
     * 8 bytes - unban time
     */
    public byte[] serialize() {
        ByteBuffer buffer = ByteBuffer.allocate(PEER_DATA_SIZE);
        if (address instanceof Inet4Address) {
            buffer.put((byte) 4);
            buffer.put(address.getAddress());
            //fill space with additional bytes (space is 16(ipv6), but ipv4 is only 4 bytes)
            buffer.put(new byte[12]);
        } else if (address instanceof Inet6Address) {
            buffer.put((byte) 6);
            buffer.put(address.getAddress());
        } else {
            LOGGER.warn("Unknown type of InetAddress. Address: {}", address);
            buffer.put(new byte[16]);
        }
        buffer.putInt(port);
        buffer.putLong(lastSeen);
        buffer.putLong(unbanTime);
        return buffer.array();
    }
}
