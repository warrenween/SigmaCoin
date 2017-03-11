package ru.opensecreto.j2p;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

class PeerDatabase {

    private static final Logger LOGGER = LoggerFactory.getLogger(PeerDatabase.class);

    private final RandomAccessFile db;
    private final Set<Peer> peers = Collections.synchronizedSet(new HashSet<>());
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

    public void loadPeers() throws IOException {
        try {
            synchronized (db) {
                byte[] data = new byte[Peer.PEER_DATA_SIZE];
                while (db.length() - db.getFilePointer() >= Peer.PEER_DATA_SIZE) {
                    db.read(data);
                    peers.add(Peer.deserialize(data));
                }
            }
        } catch (IOException e) {
            LOGGER.error("Error while loading peers.", e);
            throw e;
        }
    }

    public void addPeer(Peer peer) throws IOException {
        synchronized (peers) {
            peers.add(peer);
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
            LOGGER.error("Could not close peer database file");
            throw e;
        }
    }

    public void save() throws IOException {
        byte[] data;
        synchronized (peers) {
            data = new byte[peers.size() * Peer.PEER_DATA_SIZE];
            final int[] offset = {0};
            peers.forEach(peer -> {
                byte[] temp = peer.serialize();
                System.arraycopy(temp, 0, data, offset[0], Peer.PEER_DATA_SIZE);
                offset[0] += Peer.PEER_DATA_SIZE;
            });
        }
        synchronized (db) {
            try {
                checkOpen();
                db.setLength(0);
                db.seek(0);
                db.write(data);
            } catch (IOException e) {
                LOGGER.error("Error while saving peer list.", e);
                throw e;
            }
        }
    }

    /**
     * @see Collections#synchronizedSet(Set)
     */
    public Set<Peer> getPeers() {
        return peers;
    }

}
