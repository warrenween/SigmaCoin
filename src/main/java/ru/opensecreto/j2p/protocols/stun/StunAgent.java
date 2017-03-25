package ru.opensecreto.j2p.protocols.stun;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.*;
import java.util.concurrent.Callable;

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
            socket.connect(address);

            byte[] mData = message.getData();
            DatagramPacket packet = new DatagramPacket(mData, mData.length);

            socket.send(packet);

            DatagramPacket result = new DatagramPacket(new byte[576], 576);

            int count = 0;
            while (count <= retryCount) {
                try {
                    socket.receive(result);
                } catch (SocketTimeoutException e) {
                    count++;
                    LOGGER.warn("Could not recieve STUN response", e);
                    if (count >= retryCount) {
                        LOGGER.error("Retry count exceeded.");
                        return null;
                    }
                }

            }
        } catch (Exception e) {
            LOGGER.error("Error while sending stun message.");
        } finally {
            if (socket != null) socket.close();
        }
        return null;
    }
}