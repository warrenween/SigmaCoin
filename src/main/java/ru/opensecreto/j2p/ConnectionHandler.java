package ru.opensecreto.j2p;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.opensecreto.j2p.commandhandlers.ConnectionInterruptHandler;
import ru.opensecreto.j2p.commandhandlers.GetPeersCommandHandler;
import ru.opensecreto.j2p.commandhandlers.PeerInfoHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

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
        InputStream in = null;
        OutputStream out = null;
        try {
            in = socket.getInputStream();
            out = socket.getOutputStream();
        } catch (IOException e) {
            LOGGER.error("Could not create streams", e);
            try {
                socket.close();
            } catch (IOException e1) {
                LOGGER.error("Could not close socket.", e);
            }
            Thread.currentThread().interrupt();
        }
        try {
            while (!Thread.currentThread().isInterrupted() && socket.isConnected() && !socket.isClosed()
                    && in != null && out != null) {
                int val = in.read();

                if (val == -1) {
                    LOGGER.warn("Reached end of input stream. Disconnecting from {}.", socket.getRemoteSocketAddress());
                    socket.close();
                    Thread.currentThread().interrupt();
                } else if (!COMMAND_HANDLER_LIST.containsKey(((byte) val))) {
                    //peer must send valid commands
                    //if no, we will disconnect
                    LOGGER.warn("Peer {} sent incorrect command. Disconnecting.", socket.getRemoteSocketAddress());
                    socket.close();
                    Thread.currentThread().interrupt();
                } else {
                    COMMAND_HANDLER_LIST.get(((byte) val)).handle(socket, in, out, controller);
                }
            }
        } catch (IOException e) {
            LOGGER.warn("Error while operating with socket", e);
            Thread.currentThread().interrupt();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                LOGGER.error("Could not close socket.", e);
            }
        }
    }
}
