package org.opensecreto.sigmascript.context.item;

import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;

public class BooleanContextElementTest {

    @Test
    public void testType() {
        Assertions.assertThat(new BooleanContextElement().getType()).isEqualTo(ContextElementType.BOOLEAN);
    }

    @Test
    public void testSetAndGet() {
        boolean a = true;
        boolean b = false;

        BooleanContextElement item = new BooleanContextElement(a);
        Assertions.assertThat(item.getValue()).isEqualTo(a);

        item.setValue(b);
        Assertions.assertThat(item.getValue()).isEqualTo(b);

        a = false;
        b = true;

        item = new BooleanContextElement(a);
        Assertions.assertThat(item.getValue()).isEqualTo(a);

        item.setValue(b);
        Assertions.assertThat(item.getValue()).isEqualTo(b);
    }

}
