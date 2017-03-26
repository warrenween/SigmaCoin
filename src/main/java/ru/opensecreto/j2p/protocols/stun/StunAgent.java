package ru.opensecreto.j2p.protocols.stun;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.*;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class StunAgent implements Callable<StunMessage> {

    private final static Logger LOGGER = LoggerFactory.getLogger(StunAgent.class);
    private final InetSocketAddress address;
    private final StunMessage message;
    public static final int DEFAULT_RETRY = 5;
    private final int retryCount;

    public StunAgent(InetSocketAddress address, StunMessage message) {
        this(address, message, DEFAULT_RETRY);
    }

    public StunAgent(InetSocketAddress address, StunMessage message, int retryCount) {
        this.address = address;
        this.message = message;
        this.retryCount = retryCount;
    }

    @Override
    public StunMessage call() throws Exception {
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket();
            socket.setSoTimeout(10000);

            byte[] mData = message.getStunMessage();
            DatagramPacket packet = new DatagramPacket(mData, mData.length, address);

            socket.send(packet);
            LOGGER.trace("Send packet to {}.", address);

            DatagramPacket result = new DatagramPacket(new byte[576], 576);

            int count = 0;
            while (count <= retryCount) {
                try {
                    socket.receive(result);
                    LOGGER.trace("Received packet from {}.", socket.getRemoteSocketAddress());
                } catch (SocketTimeoutException e) {
                    count++;
                    LOGGER.warn("Could not receive STUN response", e);
                    if (count >= retryCount) {
                        LOGGER.error("Retry count exceeded.");
                        return null;
                    }
                    TimeUnit.SECONDS.sleep(5);
                }

            }
        } catch (Exception e) {
            LOGGER.error("Error while sending stun message.", e);
        } finally {
            if (socket != null) socket.close();
        }
        return null;
    }
}