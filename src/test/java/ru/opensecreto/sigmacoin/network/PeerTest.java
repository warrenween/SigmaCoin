package ru.opensecreto.sigmacoin.network;

import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;

import static org.assertj.core.api.Assertions.assertThat;

public class PeerTest {

    @Test
    public void testSerialization() throws UnknownHostException {
        Peer peer4 = new Peer(InetAddress.getByName("127.0.0.1"), 128, 256, 512);
        Peer peer4Ser = Peer.deserialize(peer4.serialize());

        assertThat(peer4Ser.address).isEqualTo(InetAddress.getByName("127.0.0.1"));
        assertThat(peer4Ser.port).isEqualTo(128);
        assertThat(peer4Ser.lastSeen).isEqualTo(256);
        assertThat(peer4Ser.unbanTime).isEqualTo(512);

        Peer peer6 = new Peer(InetAddress.getByName("FEDC:BA98:7654:3210:FEDC:BA98:7654:3210"),
                128, 256, 512);
        Peer peer6Ser = Peer.deserialize(peer6.serialize());

        assertThat(peer6Ser.address).isEqualTo(InetAddress.getByName("FEDC:BA98:7654:3210:FEDC:BA98:7654:3210"));
        assertThat(peer6Ser.port).isEqualTo(128);
        assertThat(peer6Ser.lastSeen).isEqualTo(256);
        assertThat(peer6Ser.unbanTime).isEqualTo(512);
    }

}
