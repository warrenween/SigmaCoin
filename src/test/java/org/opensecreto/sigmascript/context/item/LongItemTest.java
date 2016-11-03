package org.opensecreto.sigmascript.context.item;

import org.assertj.core.api.Assertions;
import org.opensecreto.sigmascipt.context.item.ContextItemType;
import org.opensecreto.sigmascipt.context.item.LongItem;
import org.testng.annotations.Test;

import java.util.Random;

public class LongItemTest {

    private static Random random = new Random();

    @Test
    public void testType() {
        Assertions.assertThat(new LongItem().getType()).isEqualTo(ContextItemType.LONG);
    }

    @Test
    public void testSetAndGet() {
        long a = random.nextInt();
        long b = random.nextInt();

        LongItem item = new LongItem(a);
        Assertions.assertThat(item.getValue()).isEqualTo(a);

        item.setValue(b);
        Assertions.assertThat(item.getValue()).isEqualTo(b);
    }


}
