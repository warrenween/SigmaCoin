package ru.opensecreto.sigmacoin.network;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Controller {

    private static final Logger LOGGER = LoggerFactory.getLogger(Controller.class);
    public final String software;
    public final int version;
    private final File peerDatabaseFile;
    private final int port;
    private final ExecutorService executorService;
    private PeerDatabase database;
    private InetAddress address;
    private Future peerWelcomerFuture;

    public Controller(File peerDatabaseDir, String software, int version) throws IOException {
        this(peerDatabaseDir, WelcomeRunnable.DEFAULT_PORT, software, version);
    }

    public Controller(File peerDatabaseDir, int port, String software, int version) throws IOException {
        this.software = software;
        this.version = version;

        this.peerDatabaseFile = peerDatabaseDir;
        try {
            database = new PeerDatabase(peerDatabaseDir);
        } catch (IOException e) {
            LOGGER.error("Could not initialize peer database.", e);
            throw e;
        }
        this.port = port;

        executorService = Executors.newSingleThreadExecutor();
        peerWelcomerFuture = executorService.submit(new WelcomeRunnable(port, this));
    }

    public void stop() {
        LOGGER.info("Stopping controller.");
        try {
            database.close();
        } catch (IOException e) {
            LOGGER.error("Error while closing peer database.", e);
        }
        executorService.shutdownNow();
    }

    public void registerPeer(Peer peer) {
        try {
            database.addPeer(peer);
        } catch (IOException e) {
            LOGGER.warn("Could not add peer to database.", e);
        }
    }

    public PeerDatabase getPeersDatabase() {
        return database;
    }

    public void updateExternalAddress(InetAddress newAddress) {
        this.address = newAddress;
    }

}
