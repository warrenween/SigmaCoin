package ru.opensecreto.sigmacoin.j2p;

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

    private final File peerDatabaseFile;
    private final int port;
    private PeerDatabase database;
    private InetAddress address;
    private final ExecutorService executorService;
    private Future peerWelcomerFuture;

    public final String software;
    public final int version;

    public Controller(File peerDatabaseFile, String software, int version) throws IOException {
        this(peerDatabaseFile, WelcomeRunnable.DEFAULT_PORT, software, version);
    }

    public Controller(File peerDatabaseFile, int port, String software, int version) throws IOException {
        this.software = software;
        this.version = version;

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


    public void stop() {
        LOGGER.info("Stopping controller.");
        try {
            database.save();
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
