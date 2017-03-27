package ru.opensecreto.j2p;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ConnectionHandler implements Runnable {

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
