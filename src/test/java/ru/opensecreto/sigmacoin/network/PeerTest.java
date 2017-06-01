package ru.opensecreto.sigmacoin.network;

import org.testng.annotations.Test;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import static org.assertj.core.api.Assertions.assertThat;

public class PeerTest {

    @Test
    public void testSerialization() throws UnknownHostException {
        Peer peer4 = new Peer(new InetSocketAddress(InetAddress.getByName("127.0.0.1"), 128), 256, 512);
        Peer peer4Ser = Peer.deserialize(peer4.serialize());

        assertThat(peer4Ser.socketAddress.getAddress()).isEqualTo(InetAddress.getByName("127.0.0.1"));
        assertThat(peer4Ser.socketAddress.getPort()).isEqualTo(128);
        assertThat(peer4Ser.lastSeen).isEqualTo(256);
        assertThat(peer4Ser.unbanTime).isEqualTo(512);

        Peer peer6 = new Peer(new InetSocketAddress(InetAddress.getByName("FEDC:BA98:7654:3210:FEDC:BA98:7654:3210"), 128),
                256, 512);
        Peer peer6Ser = Peer.deserialize(peer6.serialize());

        assertThat(peer6Ser.socketAddress.getAddress()).isEqualTo(InetAddress.getByName("FEDC:BA98:7654:3210:FEDC:BA98:7654:3210"));
        assertThat(peer6Ser.socketAddress.getPort()).isEqualTo(128);
        assertThat(peer6Ser.lastSeen).isEqualTo(256);
        assertThat(peer6Ser.unbanTime).isEqualTo(512);
    }

}
