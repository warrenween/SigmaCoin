package org.opensecreto.sigmascript.context.item;

import org.assertj.core.api.Assertions;
import org.opensecreto.sigmascipt.context.item.ByteContextElement;
import org.opensecreto.sigmascipt.context.item.ContextElementType;
import org.testng.annotations.Test;

import java.util.Random;

public class ByteContextElementTest {

    private static Random random = new Random();

    @Test
    public void testType() {
        Assertions.assertThat(new ByteContextElement().getType()).isEqualTo(ContextElementType.BYTE);
    }

    @Test
    public void testSetAndGet() {
        byte[] initBytes = new byte[2];
        random.nextBytes(initBytes);

        ByteContextElement item = new ByteContextElement(initBytes[0]);
        Assertions.assertThat(item.getValue()).isEqualTo(initBytes[0]);

        item.setValue(initBytes[1]);
        Assertions.assertThat(item.getValue()).isEqualTo(initBytes[1]);
    }

}
