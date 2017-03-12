package ru.opensecreto.j2p;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;

public class Controller {

    public static final int DEFAULT_PORT = 23456;
    private static final Logger LOGGER = LoggerFactory.getLogger(Controller.class);
    private final File peerDatabaseFile;
    private final int port;
    private PeerDatabase database;
    private InetAddress address;

    public Controller(File peerDatabaseFile, ConnectionHandler handler) throws IOException {
        this(peerDatabaseFile, DEFAULT_PORT, handler, true);
    }

    public Controller(File peerDatabaseFile, int port, ConnectionHandler handler, boolean daemon) throws IOException {
        this.peerDatabaseFile = peerDatabaseFile;
        try {
            database = new PeerDatabase(peerDatabaseFile);
            database.loadPeers();
        } catch (IOException e) {
            LOGGER.error("Could not initialize peer database.", e);
            throw e;
        }
        this.port = port;

        WelcomeRunnable welcome = new WelcomeRunnable(port, this, handler);
        Thread welcomeThread = new Thread(welcome);
        welcomeThread.setDaemon(daemon);
        welcomeThread.setName("PeersWelcomer");
        welcomeThread.start();
    }


    private void close() throws IOException {
        try {
            database.close();
        } catch (IOException e) {
            LOGGER.error("Error while closing peer database.", e);
            throw e;
        }
    }

    public void updateExternalAddress(InetAddress newAddress) {
        this.address = newAddress;
    }

}
