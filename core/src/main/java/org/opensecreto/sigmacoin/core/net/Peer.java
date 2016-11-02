package org.opensecreto.sigmacoin.core.net;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Peer {

    private int port;
    private InetAddress address;
    private long lastSeen;

    public Peer(byte[] address, int port, long lastSeen) throws UnknownHostException {
        this.address = InetAddress.getByAddress(address);
        this.port = port;
        this.lastSeen = lastSeen;
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
