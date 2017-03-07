package ru.opensecreto.j2p;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

class PeerDatabase implements Cloneable {

    private static final Logger LOGGER = LoggerFactory.getLogger(PeerDatabase.class);

    private RandomAccessFile db;
    private File dbFile;
    private boolean opened;

    public PeerDatabase(File dbFile) {
        this.dbFile = dbFile;
    }

    public void open() throws IOException {
        if (!dbFile.isFile()) {
            LOGGER.error("Could not open peer database. {} is not file", dbFile);
            throw new RuntimeException(dbFile + " is not file");
        }
        try {
            db = new RandomAccessFile(dbFile, "rw");
        } catch (IOException e) {
            LOGGER.error("Could not open peer database file.", e);
            throw e;
        }
        opened = true;
    }

    public void close() throws IOException {
        opened = false;
        try {
            db.close();
        } catch (IOException e) {
            LOGGER.error("Could not close peer databse file");
            throw e;
        }
    }

}
