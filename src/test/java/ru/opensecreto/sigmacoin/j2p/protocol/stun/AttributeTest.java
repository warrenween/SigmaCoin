package ru.opensecreto.sigmacoin.j2p.protocol.stun;

import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;
import ru.opensecreto.sigmacoin.j2p.protocols.stun.Attribute;

import java.util.Arrays;
import java.util.List;

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

    @Test
    public void testEncoding() {
        List<Attribute> attributes = Arrays.asList(
                new Attribute(Attribute.MAPPED_ADDRESS, new byte[]{16, 28}),
                new Attribute(Attribute.USERNAME, new byte[]{9, 8, 7, 11, 13})
        );

        Assertions.assertThat(Attribute.decode(Attribute.encodeAll(attributes))).containsExactlyElementsOf(attributes);
    }
}
