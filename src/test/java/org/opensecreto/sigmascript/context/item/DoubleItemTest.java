package org.opensecreto.sigmascript.context.item;

import org.assertj.core.api.Assertions;
import org.opensecreto.sigmascipt.context.item.ContextItemType;
import org.opensecreto.sigmascipt.context.item.DoubleItem;
import org.testng.annotations.Test;

import java.util.Random;

public class DoubleItemTest {

    private static Random random = new Random();

    @Test
    public void testType() {
        Assertions.assertThat(new DoubleItem().getType()).isEqualTo(ContextItemType.DOUBLE);
    }

    @Test
    public void testSetAndGet() {
        double a = random.nextDouble();
        double b = random.nextDouble();

        DoubleItem item = new DoubleItem(a);
        Assertions.assertThat(item.getValue()).isEqualTo(a);

        item.setValue(b);
        Assertions.assertThat(item.getValue()).isEqualTo(b);
    }

}
