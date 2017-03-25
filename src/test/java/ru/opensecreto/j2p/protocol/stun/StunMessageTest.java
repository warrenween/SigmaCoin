package ru.opensecreto.j2p.protocol.stun;

import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;
import ru.opensecreto.j2p.protocols.stun.MessageClass;
import ru.opensecreto.j2p.protocols.stun.MessageMethod;
import ru.opensecreto.j2p.protocols.stun.StunMessage;

import javax.xml.bind.DatatypeConverter;

import static javax.xml.bind.DatatypeConverter.parseHexBinary;
import static org.assertj.core.api.Assertions.assertThat;

public class StunMessageTest {

    @Test
    public void testCreatingMessage() {
        assertThat(new StunMessage(
                MessageClass.REQUEST_BYTES,
                MessageMethod.BINDING_BYTES,
                parseHexBinary("64744968693676426f393366"),
                parseHexBinary("802f0016687474703a2f2f6c" +
                        "6f63616c686f73743a333030302f0000")
        ).getStunMessage()).inHexadecimal().containsExactly(
                parseHexBinary("0001001c2112a4426474496869367642" +
                        "6f393366802f0016687474703a2f2f6c6f63616c686f73743a333030302f0000")
        );
        assertThat(new StunMessage(
                MessageClass.REQUEST,
                MessageMethod.BINDING,
                parseHexBinary("64744968693676426f393366"),
                parseHexBinary("802f0016687474703a2f2f6c" +
                        "6f63616c686f73743a333030302f0000")
        ).getStunMessage()).inHexadecimal().containsExactly(
                parseHexBinary("0001001c2112a4426474496869367642" +
                        "6f393366802f0016687474703a2f2f6c6f63616c686f73743a333030302f0000")
        );
    }

    @Test
    public void testCreatingFromBytes() {
        assertThat(new StunMessage(parseHexBinary(
                "0001001c2112a44264744968693676426f393366802f0016687474703a2f2f6c6f63616c686f73743a333030302f0000"
        )).getStunMessage()).inHexadecimal().containsExactly(parseHexBinary(
                "0001001c2112a44264744968693676426f393366802f0016687474703a2f2f6c6f63616c686f73743a333030302f0000"
        ));
    }

    @Test
    public void testGettingData() {
        assertThat(new StunMessage(parseHexBinary(
                "0001001c2112a44264744968693676426f393366802f0016687474703a2f2f6c6f63616c686f73743a333030302f0000"
        )).getMessageClass()).isEqualTo(MessageClass.REQUEST);
        assertThat(new StunMessage(parseHexBinary(
                "0001001c2112a44264744968693676426f393366802f0016687474703a2f2f6c6f63616c686f73743a333030302f0000"
        )).getMessageMethod()).isEqualTo(MessageMethod.BINDING);
        assertThat(new StunMessage(parseHexBinary(
                "0001001c2112a44264744968693676426f393366802f0016687474703a2f2f6c6f63616c686f73743a333030302f0000"
        )).getData()).isEqualTo(parseHexBinary(
                "802f0016687474703a2f2f6c6f63616c686f73743a333030302f0000"
        ));
    }

}
