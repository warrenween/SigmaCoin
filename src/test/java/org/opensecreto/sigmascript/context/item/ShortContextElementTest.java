package org.opensecreto.sigmascript.context.item;

import org.assertj.core.api.Assertions;
import org.opensecreto.sigmascipt.context.item.ContextElementType;
import org.opensecreto.sigmascipt.context.item.ShortContextElement;
import org.testng.annotations.Test;

import java.util.Random;

public class ShortContextElementTest {

    private static Random random = new Random();

    @Test
    public void testType() {
        Assertions.assertThat(new ShortContextElement().getType()).isEqualTo(ContextElementType.SHORT);
    }

    @Test
    public void testSetAndGet() {
        short a = (short) random.nextInt(Short.MAX_VALUE + 1);;
        short b = (short) random.nextInt(Short.MAX_VALUE + 1);;

        ShortContextElement item = new ShortContextElement(a);
        Assertions.assertThat(item.getValue()).isEqualTo(a);

        item.setValue(b);
        Assertions.assertThat(item.getValue()).isEqualTo(b);
    }

}
