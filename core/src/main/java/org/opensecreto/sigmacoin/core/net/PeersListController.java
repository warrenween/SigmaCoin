package org.opensecreto.sigmacoin.core.net;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class PeersListController implements Iterable<Peer> {

    private RandomAccessFile peers;

    public PeersListController(File dataFolder) throws IOException {
        File peersFile = new File(dataFolder.getPath() + "/peers.dat");
        if (!peersFile.exists()) {
            if (!peersFile.createNewFile()) {
                throw new RuntimeException("Could not create file " + peersFile.getCanonicalPath());
            }
        }
        peers = new RandomAccessFile(peersFile, "rwd");
    }

    public void addPeer(Peer peer) throws IOException {
        peers.seek(peers.length());
        peers.write(Peer.deconstructIntoBytes(peer));
    }

    public long peerCount() throws IOException {
        return peers.length() / Peer.DeconstructedBytesArrayLength;
    }

    public Peer getPeer(long index) throws IOException {
        if (index > peerCount()) {
            throw new IllegalArgumentException("Index can not be larger than peer count.");
        }
        peers.seek(index * Peer.DeconstructedBytesArrayLength);
        byte[] peerBytes = new byte[Peer.DeconstructedBytesArrayLength];
        peers.read(peerBytes, 0, Peer.DeconstructedBytesArrayLength);

        return Peer.constructFromBytes(peerBytes);
    }

    @Override
    public Iterator<Peer> iterator() {
        return new PeerListIterator(this);
    }

    public void close() throws IOException {
        peers.close();
    }

    public static class PeerListIterator implements Iterator<Peer> {

        private long index;
        private PeersListController controller;

        public PeerListIterator(PeersListController controller) {
            this.controller = controller;
        }

        @Override
        public boolean hasNext() {
            try {
                return index < controller.peerCount();
            } catch (IOException e) {
                return false;
            }
        }

        @Override
        public Peer next() {
            try {
                if (index >= controller.peerCount()) {
                    throw new NoSuchElementException();
                }
                Peer result = controller.getPeer(index);
                index++;
                return result;
            } catch (IOException e) {
                NoSuchElementException exception = new NoSuchElementException();
                exception.initCause(e);
                throw exception;
            }
        }
    }

}
