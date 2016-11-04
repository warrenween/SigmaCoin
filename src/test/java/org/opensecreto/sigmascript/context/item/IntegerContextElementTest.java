package org.opensecreto.sigmascript.context.item;

import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;

import java.util.Random;

public class IntegerContextElementTest {

    private static Random random = new Random();

    @Test
    public void testType() {
        Assertions.assertThat(new IntegerContextElement().getType()).isEqualTo(ContextElementType.INTEGER);
    }

    @Test
    public void testSetAndGet() {
        int a = random.nextInt();
        int b = random.nextInt();

        IntegerContextElement item = new IntegerContextElement(a);
        Assertions.assertThat(item.getValue()).isEqualTo(a);

        item.setValue(b);
        Assertions.assertThat(item.getValue()).isEqualTo(b);
    }

}
