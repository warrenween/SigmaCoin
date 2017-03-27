package ru.opensecreto.j2p;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.RunnableFuture;

public class Controller {

    private static final Logger LOGGER = LoggerFactory.getLogger(Controller.class);

    private final File peerDatabaseFile;
    private final int port;
    private PeerDatabase database;
    private InetAddress address;
    private final ExecutorService executorService;
    private Future peerWelcomerFuture;

    public Controller(File peerDatabaseFile) throws IOException {
        this(peerDatabaseFile, WelcomeRunnable.DEFAULT_PORT);
    }

    public Controller(File peerDatabaseFile, int port) throws IOException {
        this.peerDatabaseFile = peerDatabaseFile;
        try {
            database = new PeerDatabase(peerDatabaseFile);
            database.loadPeers();
        } catch (IOException e) {
            LOGGER.error("Could not initialize peer database.", e);
            throw e;
        }
        this.port = port;

        executorService = Executors.newSingleThreadExecutor();
        peerWelcomerFuture = executorService.submit(new WelcomeRunnable(port, this));
    }


    private void close() throws IOException {
        try {
            database.close();
        } catch (IOException e) {
            LOGGER.error("Error while closing peer database.", e);
            throw e;
        }
        executorService.shutdown();
    }

    public void updateExternalAddress(InetAddress newAddress) {
        this.address = newAddress;
    }

}
