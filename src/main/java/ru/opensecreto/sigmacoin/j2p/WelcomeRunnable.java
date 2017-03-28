package ru.opensecreto.sigmacoin.j2p;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
        ExecutorService executorService = Executors.newCachedThreadPool();
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(welcomePort, 10);

            while (!Thread.currentThread().isInterrupted()) {
                Socket socket = serverSocket.accept();
                LOGGER.debug("Accepted connection from {}", socket.getRemoteSocketAddress());
                controller.registerPeer(new Peer(
                        ((InetSocketAddress) socket.getRemoteSocketAddress()).getAddress(),
                        ((InetSocketAddress) socket.getRemoteSocketAddress()).getPort(),
                        System.currentTimeMillis(),
                        0
                ));
                executorService.submit(new ConnectionHandler(socket, controller));
            }
        } catch (IOException e) {
            LOGGER.error("Exception was thrown while waiting for connections.", e);
        } finally {
            try {
                if (serverSocket != null) serverSocket.close();
                LOGGER.debug("Successfully closed server socket {}.", serverSocket);
            } catch (IOException e) {
                LOGGER.error("Could not close serverSocket");
            }
        }

        executorService.shutdownNow();
        LOGGER.debug("Executor service was shut down successfully.");
    }

}
