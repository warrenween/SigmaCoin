package ru.opensecreto.sigmacoin.network.protocol.stun;

import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;
import ru.opensecreto.sigmacoin.network.protocols.stun.Attribute;
import ru.opensecreto.sigmacoin.network.protocols.stun.AttributeParser;

import javax.xml.bind.DatatypeConverter;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import static javax.xml.bind.DatatypeConverter.parseHexBinary;

public class AttributeParserTest {

    @Test
    public void testMAPPED_ADDRESS() throws UnknownHostException {
        Assertions.assertThat(AttributeParser.parseMAPPED_ADDRESS(new Attribute(
                Attribute.MAPPED_ADDRESS, parseHexBinary("000104d27b00ff20")
        ))).isEqualTo(new InetSocketAddress("123.0.255.32", 1234));
        Assertions.assertThat(AttributeParser.parseMAPPED_ADDRESS(new Attribute(
                Attribute.MAPPED_ADDRESS, parseHexBinary("000204d220010db885a3000000008a2e03707334")
        ))).isEqualTo(new InetSocketAddress("2001:0db8:85a3:0000:0000:8a2e:0370:7334", 1234));
    }

    @Test
    public void testXOR_MAPPED_ADDRESS() throws UnknownHostException {
        Assertions.assertThat(AttributeParser.parseXOR_MAPPED_ADDRESS(
                new Attribute(
                        Attribute.XOR_MAPPED_ADDRESS, parseHexBinary("0001DD707EA5E1A5")
                ),
                parseHexBinary("000000000000000000000000")
        )).isEqualTo(new InetSocketAddress("95.183.69.231", 64610));
    }

}
