package ru.opensecreto.sigmacoin.j2p;

import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;

public class TestPeerDatabase {

    @Test
    public void testAddingPeers() throws IOException {
        File dbFile = new File("testAddingPeers.db");
        PeerDatabase db = new PeerDatabase(dbFile);

        Peer peer1 = new Peer(InetAddress.getByName("127.0.0.1"), 1234, 128, 256);
        Peer peer2 = new Peer(InetAddress.getByName("1080::8:800:200C:417A"), 1234, 128, 256);

        db.addPeer(peer1);
        db.addPeer(peer2);
        db.save();
        db.close();

        PeerDatabase db2 = new PeerDatabase(dbFile);
        db2.loadPeers();

        Assertions.assertThat(db2.getPeers()).containsOnly(peer1, peer2);
    }

}
