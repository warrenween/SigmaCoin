package ru.opensecreto.j2p;

import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class PeerTest {

    @Test
    public void testSerialization() throws UnknownHostException {
        Peer peer4 = new Peer(InetAddress.getByName("127.0.0.1"), 128, 256, 512);
        Peer peer4Ser = Peer.deserialize(peer4.serialize());

        Assertions.assertThat(peer4Ser.address).isEqualTo(InetAddress.getByName("127.0.0.1"));
        Assertions.assertThat(peer4Ser.port).isEqualTo(128);
        Assertions.assertThat(peer4Ser.lastSeen).isEqualTo(256);
        Assertions.assertThat(peer4Ser.unbanTime).isEqualTo(512);

        Peer peer6 = new Peer(InetAddress.getByName("FEDC:BA98:7654:3210:FEDC:BA98:7654:3210"),
                128, 256, 512);
        Peer peer6Ser = Peer.deserialize(peer6.serialize());

        Assertions.assertThat(peer6Ser.address).isEqualTo(InetAddress.getByName("FEDC:BA98:7654:3210:FEDC:BA98:7654:3210"));
        Assertions.assertThat(peer6Ser.port).isEqualTo(128);
        Assertions.assertThat(peer6Ser.lastSeen).isEqualTo(256);
        Assertions.assertThat(peer6Ser.unbanTime).isEqualTo(512);
    }

}
