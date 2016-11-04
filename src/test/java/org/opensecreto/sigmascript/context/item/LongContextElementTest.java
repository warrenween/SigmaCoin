package org.opensecreto.sigmascript.context.item;

import org.assertj.core.api.Assertions;
import org.opensecreto.sigmascipt.context.item.ContextElementType;
import org.opensecreto.sigmascipt.context.item.LongContextElement;
import org.testng.annotations.Test;

import java.util.Random;

public class LongContextElementTest {

    private static Random random = new Random();

    @Test
    public void testType() {
        Assertions.assertThat(new LongContextElement().getType()).isEqualTo(ContextElementType.LONG);
    }

    @Test
    public void testSetAndGet() {
        long a = random.nextInt();
        long b = random.nextInt();

        LongContextElement item = new LongContextElement(a);
        Assertions.assertThat(item.getValue()).isEqualTo(a);

        item.setValue(b);
        Assertions.assertThat(item.getValue()).isEqualTo(b);
    }


}
