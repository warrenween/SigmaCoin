package ru.opensecreto.sigmacoin.network;

import jetbrains.exodus.bindings.ComparableBinding;
import jetbrains.exodus.util.LightOutputStream;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.DatatypeConverter;
import java.io.ByteArrayInputStream;
import java.net.*;
import java.nio.ByteBuffer;

public class Peer implements Comparable {

    public static final Logger LOGGER = LoggerFactory.getLogger(Peer.class);
    public final static int PEER_DATA_SIZE = 37;

    public final InetSocketAddress socketAddress;
    public long lastSeen;
    public long unbanTime;

    public Peer(InetSocketAddress socketAddress, long lastSeen, long unbanTime) {
        this.socketAddress = socketAddress;
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
        return new Peer(new InetSocketAddress(address, port), lastSeen, unbanTime);
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
        if (socketAddress.getAddress() instanceof Inet4Address) {
            buffer.put((byte) 4);
            buffer.put(socketAddress.getAddress().getAddress());
            //fill space with additional bytes (space is 16(ipv6), but ipv4 is only 4 bytes)
            buffer.put(new byte[12]);
        } else if (socketAddress.getAddress() instanceof Inet6Address) {
            buffer.put((byte) 6);
            buffer.put(socketAddress.getAddress().getAddress());
        } else {
            LOGGER.warn("Unknown type of InetAddress. Address: {}", socketAddress);
            buffer.put(new byte[16]);
        }
        buffer.putInt(socketAddress.getPort());
        buffer.putLong(lastSeen);
        buffer.putLong(unbanTime);
        return buffer.array();
    }

    @Override
    public boolean equals(Object obj) {
        return (obj != null) && (obj instanceof Peer) &&
                (((Peer) obj).socketAddress.equals(socketAddress));
    }

    @Override
    public String toString() {
        return socketAddress.toString() + ':' + socketAddress.getPort();
    }

    @Override
    public int compareTo(@NotNull Object o) {
        return 0;
    }

    public static class PeerComparableBinding extends ComparableBinding {

        @Override
        public Comparable readObject(@NotNull ByteArrayInputStream stream) {
            byte[] buf = new byte[stream.available()];
            stream.read(buf, 0, stream.available());
            return deserialize(buf);
        }

        @Override
        public void writeObject(@NotNull LightOutputStream output, @NotNull Comparable object) {
            output.write(((Peer) object).serialize());
        }
    }
}
