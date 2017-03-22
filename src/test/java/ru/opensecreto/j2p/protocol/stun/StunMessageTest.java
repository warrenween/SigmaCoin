package ru.opensecreto.j2p.protocol.stun;

import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;
import ru.opensecreto.j2p.protocols.stun.MessageClass;
import ru.opensecreto.j2p.protocols.stun.MessageMethod;
import ru.opensecreto.j2p.protocols.stun.StunMessage;

import javax.xml.bind.DatatypeConverter;

public class StunMessageTest {

    @Test
    public void testCreatingMessage() {
        Assertions.assertThat(new StunMessage(
                MessageClass.REQUEST,
                MessageMethod.BINDING,
                DatatypeConverter.parseHexBinary("64744968693676426f393366"),
                DatatypeConverter.parseHexBinary("802f0016687474703a2f2f6c" +
                        "6f63616c686f73743a333030302f0000")
        ).getStunMessage()).inHexadecimal().containsExactly(
                DatatypeConverter.parseHexBinary("0001001c2112a4426474496869367642" +
                        "6f393366802f0016687474703a2f2f6c6f63616c686f73743a333030302f0000")
        );
    }

}
