package ru.opensecreto.j2p;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class Controller {

    private static final Logger LOGGER = LoggerFactory.getLogger(Controller.class);

    private final File databaseFile;
    private PeerDatabase database;

    public Controller(File databaseFile) throws IOException {
        this.databaseFile = databaseFile;
        try {
            database = new PeerDatabase(databaseFile);
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
