package org.opensecreto.sigmascript.context.item;

import org.assertj.core.api.Assertions;
import org.opensecreto.sigmascipt.context.item.ContextElementType;
import org.opensecreto.sigmascipt.context.item.DoubleContextElement;
import org.testng.annotations.Test;

import java.util.Random;

public class DoubleContextElementTest {

    private static Random random = new Random();

    @Test
    public void testType() {
        Assertions.assertThat(new DoubleContextElement().getType()).isEqualTo(ContextElementType.DOUBLE);
    }

    @Test
    public void testSetAndGet() {
        double a = random.nextDouble();
        double b = random.nextDouble();

        DoubleContextElement item = new DoubleContextElement(a);
        Assertions.assertThat(item.getValue()).isEqualTo(a);

        item.setValue(b);
        Assertions.assertThat(item.getValue()).isEqualTo(b);
    }

}
