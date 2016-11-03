package org.opensecreto.sigmascript.context.item;

import org.assertj.core.api.Assertions;
import org.opensecreto.sigmascipt.context.item.ContextItemType;
import org.opensecreto.sigmascipt.context.item.IntegerItem;
import org.testng.annotations.Test;

import java.util.Random;

public class IntegerItemTest {

    private static Random random = new Random();

    @Test
    public void testType() {
        Assertions.assertThat(new IntegerItem().getType()).isEqualTo(ContextItemType.INTEGER);
    }

    @Test
    public void testSetAndGet() {
        int a = random.nextInt();
        int b = random.nextInt();

        IntegerItem item = new IntegerItem(a);
        Assertions.assertThat(item.getValue()).isEqualTo(a);

        item.setValue(b);
        Assertions.assertThat(item.getValue()).isEqualTo(b);
    }

}
