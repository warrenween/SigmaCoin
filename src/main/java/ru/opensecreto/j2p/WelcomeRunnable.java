package ru.opensecreto.j2p;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class WelcomeRunnable implements Runnable {

    public static final int DEFAULT_PORT = 23456;
    private final static Logger LOGGER = LoggerFactory.getLogger(WelcomeRunnable.class);
    private final int welcomePort;
    private final Controller controller;

    public WelcomeRunnable(int welcomePort, Controller controller) {
        this.welcomePort = welcomePort;
        this.controller = controller;
    }

    @Override
    public void run() {
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(welcomePort, 10);
        } catch (IOException e) {
            LOGGER.error("Could not setup ServerSocket. Accepting incoming connections is not possible.", e);
            Thread.currentThread().interrupt();
            return;
        }
        ExecutorService executorService = Executors.newCachedThreadPool();
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Socket socket = serverSocket.accept();
                LOGGER.debug("Accepted connection from {}", socket.getRemoteSocketAddress());
                executorService.submit(new ConnectionHandler(socket, controller));
            } catch (IOException e) {
                LOGGER.error("Exception was thrown while waiting for connections.", e);
                Thread.currentThread().interrupt();
                return;
            }
        }

        LOGGER.debug("Shutting down executor service.");
        executorService.shutdownNow();

        try {
            LOGGER.debug("Closing server socket");
            serverSocket.close();
        } catch (IOException e) {
            LOGGER.error("Could not close serverSocket");
        }
    }

}
