package ru.opensecreto.j2p;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.opensecreto.j2p.commandhandlers.ConnectionInterruptHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ConnectionHandler implements Runnable {

    private static final Map<Short, CommandHandler> COMMAND_HANDLER_LIST = new HashMap<Short, CommandHandler>() {{
        put((short) 0, new ConnectionInterruptHandler());
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
        InputStream in;
        OutputStream out;
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
        while (!Thread.currentThread().isInterrupted() && socket.isConnected() && !socket.isClosed()) {

        }
    }
}
