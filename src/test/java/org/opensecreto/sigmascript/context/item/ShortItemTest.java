package org.opensecreto.sigmascript.context.item;

import org.assertj.core.api.Assertions;
import org.opensecreto.sigmascipt.context.item.ContextItemType;
import org.opensecreto.sigmascipt.context.item.ShortItem;
import org.testng.annotations.Test;

import java.util.Random;

public class ShortItemTest {

    private static Random random = new Random();

    @Test
    public void testType() {
        Assertions.assertThat(new ShortItem().getType()).isEqualTo(ContextItemType.SHORT);
    }

    @Test
    public void testSetAndGet() {
        short a = (short) random.nextInt(Short.MAX_VALUE + 1);;
        short b = (short) random.nextInt(Short.MAX_VALUE + 1);;

        ShortItem item = new ShortItem(a);
        Assertions.assertThat(item.getValue()).isEqualTo(a);

        item.setValue(b);
        Assertions.assertThat(item.getValue()).isEqualTo(b);
    }

}
