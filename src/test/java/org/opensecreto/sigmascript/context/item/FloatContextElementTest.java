package org.opensecreto.sigmascript.context.item;

import org.assertj.core.api.Assertions;
import org.opensecreto.sigmascipt.context.item.ContextElementType;
import org.opensecreto.sigmascipt.context.item.FloatContextElement;
import org.testng.annotations.Test;

import java.util.Random;

public class FloatContextElementTest {

    private static Random random = new Random();

    @Test
    public void testType() {
        Assertions.assertThat(new FloatContextElement().getType()).isEqualTo(ContextElementType.FLOAT);
    }

    @Test
    public void testSetAndGet() {
        long a = random.nextLong();
        long b = random.nextLong();

        FloatContextElement item = new FloatContextElement(a);
        Assertions.assertThat(item.getValue()).isEqualTo(a);

        item.setValue(b);
        Assertions.assertThat(item.getValue()).isEqualTo(b);
    }


}
