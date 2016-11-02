package org.opensecreto.sigmacoin.core.net;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

public class Peer {

    /**
     * Size of array, returned by {@link Peer#deconstructIntoBytes(Peer)}
     * and used by {@link Peer#constructFromBytes(byte[])}
     */
    public static final int DeconstructedBytesArrayLength = 29;
    private int port;
    private InetAddress address;
    private long lastSeen;

    public Peer(byte[] address, int port, long lastSeen) throws UnknownHostException {
        this.address = InetAddress.getByAddress(address);
        this.port = port;
        this.lastSeen = lastSeen;
    }

    public static Peer constructFromBytes(byte[] bytes) throws UnknownHostException {
        if (bytes.length != 29) {
            throw new IllegalArgumentException("Wrong bytes length " + bytes.length + ". Length must be 29.");
        }

        boolean ipv4 = bytes[0] == 0;
        byte[] address;
        if (ipv4) {
            address = new byte[4];
            System.arraycopy(bytes, 1, address, 0, 4);
        } else {
            address = new byte[16];
            System.arraycopy(bytes, 1, address, 0, 16);
        }
        ByteBuffer portBuffer = ByteBuffer.allocate(4).put(bytes, 17, 4);
        portBuffer.position(0);
        int port = portBuffer.getInt();
        ByteBuffer lastSeenBuffer = ByteBuffer.allocate(8).put(bytes, 21, 8);
        lastSeenBuffer.position(0);
        long lastSeen = lastSeenBuffer.getLong();

        return new Peer(address, port, lastSeen);
    }

    public static byte[] deconstructIntoBytes(Peer peer) {
        byte[] result = new byte[29];

        byte[] address = peer.getAddress().getAddress();
        boolean ipv4;
        if (address.length == 4) {
            ipv4 = true;
        } else if (address.length == 16) {
            ipv4 = false;
        } else {
            throw new RuntimeException("Invalid address length " + address.length);
        }

        if (ipv4) {
            byte[] ipv4Address = address;
            address = new byte[16];
            System.arraycopy(ipv4Address, 0, address, 0, 4);
        }

        result[0] = ipv4 ? (byte) 0 : (byte) 1;
        System.arraycopy(address, 0, result, 1, 16);
        System.arraycopy(ByteBuffer.allocate(4).putInt(peer.port).array(), 0, result, 17, 4);
        System.arraycopy(ByteBuffer.allocate(8).putLong(peer.lastSeen).array(), 0, result, 21, 8);
        return result;
    }

    public InetAddress getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }

    public long getLastSeen() {
        return lastSeen;
    }

    public void updateLastSeen(long newTime) {
        if (newTime > lastSeen) {
            lastSeen = newTime;
        }
    }

}
