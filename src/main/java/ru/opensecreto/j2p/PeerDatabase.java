package ru.opensecreto.j2p;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

class PeerDatabase implements Cloneable {

    private static final Logger LOGGER = LoggerFactory.getLogger(PeerDatabase.class);

    private final RandomAccessFile db;
    private final File dbFile;
    private boolean opened;

    public PeerDatabase(File dbFile) throws IOException {
        this.dbFile = dbFile;
        if (!dbFile.isFile()) {
            try {
                dbFile.createNewFile();
            } catch (IOException e) {
                LOGGER.error("Could not create file {}", dbFile, e);
                throw e;
            }
        }
        try {
            db = new RandomAccessFile(dbFile, "rw");
        } catch (IOException e) {
            LOGGER.error("Could not open peer database file.", e);
            throw e;
        }
        opened = true;
    }

    public void addPeer(Peer peer) throws IOException {
        byte[] data = peer.serialize();
        checkOpen();
        synchronized (db) {
            try {
                db.seek(db.length());
                db.write(data);
            } catch (IOException e) {
                LOGGER.error("Error while reading from peer database file. {}", db, e);
                throw e;
            }
        }
    }

    private void checkOpen() {
        if (!opened) throw new IllegalStateException("Peer database file is not opened");
    }

    public void close() throws IOException {
        opened = false;
        try {
            synchronized (db) {
                db.close();
            }
        } catch (IOException e) {
            LOGGER.error("Could not close peer databse file");
            throw e;
        }
    }

}
