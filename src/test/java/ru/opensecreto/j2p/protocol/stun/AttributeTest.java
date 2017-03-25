package ru.opensecreto.j2p.protocol.stun;

import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;
import ru.opensecreto.j2p.protocols.stun.Attribute;

public class AttributeTest {

    @Test
    public void testPadding() {
        Attribute attribute = new Attribute(Attribute.MAPPED_ADDRESS);

        attribute.data = new byte[1];
        Assertions.assertThat(attribute.encode()).hasSize(8);

        attribute.data = new byte[2];
        Assertions.assertThat(attribute.encode()).hasSize(8);

        attribute.data = new byte[3];
        Assertions.assertThat(attribute.encode()).hasSize(8);

        attribute.data = new byte[4];
        Assertions.assertThat(attribute.encode()).hasSize(8);
    }

}
