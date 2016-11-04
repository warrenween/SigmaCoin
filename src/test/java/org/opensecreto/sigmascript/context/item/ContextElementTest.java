package org.opensecreto.sigmascript.context.item;

import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;

import java.util.ArrayList;

import static org.opensecreto.sigmascript.context.item.ContextElementType.*;
import static org.opensecreto.sigmascript.context.item.ContextElementType.ARRAY;

public class ContextElementTest {

    @Test
    public void testDefaultValue() {
        ContextElement objectElement = new ContextElement(OBJECT);
        Assertions.assertThat(objectElement.type).isEqualTo(OBJECT);
        Assertions.assertThat(objectElement.getValue("value")).isNull();

        ContextElement byteElement = new ContextElement(BYTE);
        Assertions.assertThat(byteElement.type).isEqualTo(BYTE);
        Assertions.assertThat(byteElement.getValue("value")).isEqualTo(new Byte((byte) 0));

        ContextElement shortElement = new ContextElement(SHORT);
        Assertions.assertThat(shortElement.type).isEqualTo(SHORT);
        Assertions.assertThat(shortElement.getValue("value")).isEqualTo(new Short((short) 0));

        ContextElement integerElement = new ContextElement(INTEGER);
        Assertions.assertThat(integerElement.type).isEqualTo(INTEGER);
        Assertions.assertThat(integerElement.getValue("value")).isEqualTo(new Integer(0));

        ContextElement longElement = new ContextElement(LONG);
        Assertions.assertThat(longElement.type).isEqualTo(LONG);
        Assertions.assertThat(longElement.getValue("value")).isEqualTo(new Long(0L));

        ContextElement floatElement = new ContextElement(FLOAT);
        Assertions.assertThat(floatElement.type).isEqualTo(FLOAT);
        Assertions.assertThat(floatElement.getValue("value")).isEqualTo(new Float(0L));

        ContextElement doubleElement = new ContextElement(DOUBLE);
        Assertions.assertThat(doubleElement.type).isEqualTo(DOUBLE);
        Assertions.assertThat(doubleElement.getValue("value")).isEqualTo(new Double(0d));

        ContextElement booleanElement = new ContextElement(BOOLEAN);
        Assertions.assertThat(booleanElement.type).isEqualTo(BOOLEAN);
        Assertions.assertThat(booleanElement.getValue("value")).isEqualTo(new Boolean(false));

        ContextElement nullElement = new ContextElement(NULL);
        Assertions.assertThat(nullElement.type).isEqualTo(NULL);
        Assertions.assertThat(nullElement.getValue("value")).isNull();

        ContextElement arrayElement = new ContextElement(ARRAY);
        Assertions.assertThat(arrayElement.type).isEqualTo(ARRAY);
        Assertions.assertThat(arrayElement.getValue("value")).isInstanceOf(ArrayList.class);
        Assertions.assertThat((ArrayList) arrayElement.getValue("value")).isEmpty();
    }


}
