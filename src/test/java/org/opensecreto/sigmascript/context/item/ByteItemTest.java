package org.opensecreto.sigmascript.context.item;

import org.assertj.core.api.Assertions;
import org.opensecreto.sigmascipt.context.item.ByteItem;
import org.opensecreto.sigmascipt.context.item.ContextItemType;
import org.testng.annotations.Test;

import java.util.Random;

public class ByteItemTest {

    private static Random random = new Random();

    @Test
    public void testType() {
        Assertions.assertThat(new ByteItem().getType()).isEqualTo(ContextItemType.BYTE);
    }

    @Test
    public void testSetAndGet() {
        byte[] initBytes = new byte[2];
        random.nextBytes(initBytes);

        ByteItem item = new ByteItem(initBytes[0]);
        Assertions.assertThat(item.getByte()).isEqualTo(initBytes[0]);

        item.setValue(initBytes[1]);
        Assertions.assertThat(item.getByte()).isEqualTo(initBytes[1]);
    }

}
