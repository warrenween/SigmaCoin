package ru.opensecreto.j2p;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.opensecreto.j2p.commandhandlers.ConnectionInterruptHandler;
import ru.opensecreto.j2p.commandhandlers.GetPeersCommandHandler;
import ru.opensecreto.j2p.commandhandlers.PeerInfoHandler;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ConnectionHandler implements Runnable {

    private static final Map<Byte, CommandHandler> COMMAND_HANDLER_LIST = new HashMap<Byte, CommandHandler>() {{
        put((byte) 0, new ConnectionInterruptHandler());
        put((byte) 1, new PeerInfoHandler());
        put((byte) 2, new GetPeersCommandHandler());
    }};

    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionHandler.class);

    private final Socket socket;
    private final Controller controller;

    public ConnectionHandler(Socket socket, Controller controller) {
        this.socket = socket;
        this.controller = controller;
    }

    @Override
    public void run() {
        try {
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            while (!Thread.currentThread().isInterrupted() && socket.isConnected() && !socket.isClosed()) {
                while (in.available() > 0) {
                    byte val = in.readByte();
                    if (!COMMAND_HANDLER_LIST.containsKey(val)) {
                        //peer must send valid commands
                        //if no, we will disconnect
                        LOGGER.warn("Peer {} sent incorrect command. Disconnecting.", socket.getRemoteSocketAddress());
                        socket.close();
                        Thread.currentThread().interrupt();
                    } else {
                        COMMAND_HANDLER_LIST.get(val).handle(socket, in, out, controller);
                    }
                }
                TimeUnit.MILLISECONDS.sleep(500);
            }
        } catch (EOFException e) {
            LOGGER.warn("Reached end of stream. Disconnecting from {}.", socket.getRemoteSocketAddress(), e);
        } catch (IOException e) {
            LOGGER.warn("Error while operating with socket.", e);
        } catch (InterruptedException e) {
            LOGGER.warn("Thread was interrupted", e);
        } finally {
            try {
                socket.close();
                LOGGER.debug("Closed socket {}.", socket);
            } catch (IOException e) {
                LOGGER.warn("Could not close socket.", e);
            }
        }
    }
}
