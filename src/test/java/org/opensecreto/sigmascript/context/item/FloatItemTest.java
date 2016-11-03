package org.opensecreto.sigmascript.context.item;

import org.assertj.core.api.Assertions;
import org.opensecreto.sigmascipt.context.item.ContextItemType;
import org.opensecreto.sigmascipt.context.item.FloatItem;
import org.testng.annotations.Test;

import java.util.Random;

public class FloatItemTest {

    private static Random random = new Random();

    @Test
    public void testType() {
        Assertions.assertThat(new FloatItem().getType()).isEqualTo(ContextItemType.FLOAT);
    }

    @Test
    public void testSetAndGet() {
        long a = random.nextLong();
        long b = random.nextLong();

        FloatItem item = new FloatItem(a);
        Assertions.assertThat(item.getValue()).isEqualTo(a);

        item.setValue(b);
        Assertions.assertThat(item.getValue()).isEqualTo(b);
    }


}
