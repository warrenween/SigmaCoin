package org.opensecreto.sigmascript.context.item;

import org.assertj.core.api.Assertions;

import static org.opensecreto.sigmascript.context.item.ContextElement.getElementByType;

public class ContextElementTest {

    public void testGetElementByType() {
        // TODO: 04.11.2016 add testing getting object
        Assertions.assertThat(getElementByType(ContextElementType.BYTE)).isInstanceOf(ByteContextElement.class);
        Assertions.assertThat(getElementByType(ContextElementType.SHORT)).isInstanceOf(ShortContextElement.class);
        Assertions.assertThat(getElementByType(ContextElementType.INTEGER)).isInstanceOf(IntegerContextElement.class);
        Assertions.assertThat(getElementByType(ContextElementType.LONG)).isInstanceOf(LongContextElement.class);
        Assertions.assertThat(getElementByType(ContextElementType.FLOAT)).isInstanceOf(FloatContextElement.class);
        Assertions.assertThat(getElementByType(ContextElementType.DOUBLE)).isInstanceOf(DoubleContextElement.class);
        Assertions.assertThat(getElementByType(ContextElementType.BOOLEAN)).isInstanceOf(BooleanContextElement.class);
    }

}
