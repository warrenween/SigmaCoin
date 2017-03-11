package ru.opensecreto.j2p;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class Controller {

    private static final Logger LOGGER = LoggerFactory.getLogger(Controller.class);
    public static final int DEFAULT_PORT = 23456;

    private final File peerDatabaseFile;
    private PeerDatabase database;

    public Controller(File peerDatabaseFile) throws IOException {
        this.peerDatabaseFile = peerDatabaseFile;
        try {
            database = new PeerDatabase(peerDatabaseFile);
            database.loadPeers();
        } catch (IOException e) {
            LOGGER.error("Could not initialize peer database.", e);
            throw e;
        }
    }


    private void close() throws IOException {
        try {
            database.close();
        } catch (IOException e) {
            LOGGER.error("Error while closing peer database.", e);
            throw e;
        }
    }

}
