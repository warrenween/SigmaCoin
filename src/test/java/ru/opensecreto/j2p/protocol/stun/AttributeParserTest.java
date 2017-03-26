package ru.opensecreto.j2p.protocol.stun;

import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;
import ru.opensecreto.j2p.protocols.stun.Attribute;
import ru.opensecreto.j2p.protocols.stun.AttributeParser;

import javax.xml.bind.DatatypeConverter;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

public class AttributeParserTest {

    @Test
    public void testMAPPED_ADDRESS() throws UnknownHostException {
        Assertions.assertThat(AttributeParser.parseMAPPED_ADDRESS(new Attribute(
                Attribute.MAPPED_ADDRESS, DatatypeConverter.parseHexBinary("000104d27b00ff20")
        ))).isEqualTo(new InetSocketAddress("123.0.255.32", 1234));
    }

}
