package ru.opensecreto.j2p;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class WelcomeRunnable implements Runnable {

    private final static Logger LOGGER = LoggerFactory.getLogger(WelcomeRunnable.class);
    private final int welcomePort;
    private final Controller controller;
    private final ConnectionHandler handler;

    public WelcomeRunnable(int welcomePort, Controller controller, ConnectionHandler handler) {
        this.welcomePort = welcomePort;
        this.controller = controller;
        this.handler = handler;
    }

    @Override
    public void run() {
        ThreadGroup connectionGroup = new ThreadGroup("peer connection");
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(welcomePort);
        } catch (IOException e) {
            LOGGER.error("Could not setup ServerSocket", e);
            Thread.currentThread().interrupt();
        }
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Socket socket = serverSocket.accept();
                handler.handle(socket, controller);
            } catch (IOException e) {
                LOGGER.error("Exception was thrown while waiting for connections.", e);
                Thread.currentThread().interrupt();
            }
        }
    }

}
