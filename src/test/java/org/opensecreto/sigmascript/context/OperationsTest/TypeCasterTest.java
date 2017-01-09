package org.opensecreto.sigmascript.context.OperationsTest;

import org.assertj.core.api.Assertions;
import org.opensecreto.sigmascript.context.item.ContextElement;
import org.opensecreto.sigmascript.context.item.ContextElementType;
import org.opensecreto.sigmascript.exceptions.CastException;
import org.testng.annotations.Test;

import static org.opensecreto.sigmascript.context.operations.TypeCaster.cast;

public class TypeCasterTest {

    @Test
    public void testCasting() throws CastException {
        ContextElement byteElement = new ContextElement(ContextElementType.BYTE);
        Assertions.assertThat(cast(byteElement, ContextElementType.BYTE).getValue("value"))
                .isInstanceOf(Byte.class);
        Assertions.assertThat(cast(byteElement, ContextElementType.SHORT).getValue("value"))
                .isInstanceOf(Short.class);
        Assertions.assertThat(cast(byteElement, ContextElementType.INTEGER).getValue("value"))
                .isInstanceOf(Integer.class);
        Assertions.assertThat(cast(byteElement, ContextElementType.LONG).getValue("value"))
                .isInstanceOf(Long.class);
        Assertions.assertThat(cast(byteElement, ContextElementType.FLOAT).getValue("value"))
                .isInstanceOf(Float.class);
        Assertions.assertThat(cast(byteElement, ContextElementType.DOUBLE).getValue("value"))
                .isInstanceOf(Double.class);
        Assertions.assertThat(cast(byteElement, ContextElementType.BOOLEAN).getValue("value"))
                .isInstanceOf(Boolean.class);

        ContextElement shortElement = new ContextElement(ContextElementType.SHORT);
        Assertions.assertThat(cast(shortElement, ContextElementType.BYTE).getValue("value"))
                .isInstanceOf(Byte.class);
        Assertions.assertThat(cast(shortElement, ContextElementType.SHORT).getValue("value"))
                .isInstanceOf(Short.class);
        Assertions.assertThat(cast(shortElement, ContextElementType.INTEGER).getValue("value"))
                .isInstanceOf(Integer.class);
        Assertions.assertThat(cast(shortElement, ContextElementType.LONG).getValue("value"))
                .isInstanceOf(Long.class);
        Assertions.assertThat(cast(shortElement, ContextElementType.FLOAT).getValue("value"))
                .isInstanceOf(Float.class);
        Assertions.assertThat(cast(shortElement, ContextElementType.DOUBLE).getValue("value"))
                .isInstanceOf(Double.class);
        Assertions.assertThat(cast(shortElement, ContextElementType.BOOLEAN).getValue("value"))
                .isInstanceOf(Boolean.class);


        ContextElement integerElement = new ContextElement(ContextElementType.INTEGER);
        Assertions.assertThat(cast(integerElement, ContextElementType.BYTE).getValue("value"))
                .isInstanceOf(Byte.class);
        Assertions.assertThat(cast(integerElement, ContextElementType.SHORT).getValue("value"))
                .isInstanceOf(Short.class);
        Assertions.assertThat(cast(integerElement, ContextElementType.INTEGER).getValue("value"))
                .isInstanceOf(Integer.class);
        Assertions.assertThat(cast(integerElement, ContextElementType.LONG).getValue("value"))
                .isInstanceOf(Long.class);
        Assertions.assertThat(cast(integerElement, ContextElementType.FLOAT).getValue("value"))
                .isInstanceOf(Float.class);
        Assertions.assertThat(cast(integerElement, ContextElementType.DOUBLE).getValue("value"))
                .isInstanceOf(Double.class);
        Assertions.assertThat(cast(integerElement, ContextElementType.BOOLEAN).getValue("value"))
                .isInstanceOf(Boolean.class);

        ContextElement longElement = new ContextElement(ContextElementType.LONG);
        Assertions.assertThat(cast(longElement, ContextElementType.BYTE).getValue("value"))
                .isInstanceOf(Byte.class);
        Assertions.assertThat(cast(longElement, ContextElementType.SHORT).getValue("value"))
                .isInstanceOf(Short.class);
        Assertions.assertThat(cast(longElement, ContextElementType.INTEGER).getValue("value"))
                .isInstanceOf(Integer.class);
        Assertions.assertThat(cast(longElement, ContextElementType.LONG).getValue("value"))
                .isInstanceOf(Long.class);
        Assertions.assertThat(cast(longElement, ContextElementType.FLOAT).getValue("value"))
                .isInstanceOf(Float.class);
        Assertions.assertThat(cast(longElement, ContextElementType.DOUBLE).getValue("value"))
                .isInstanceOf(Double.class);
        Assertions.assertThat(cast(longElement, ContextElementType.BOOLEAN).getValue("value"))
                .isInstanceOf(Boolean.class);

        ContextElement floatElement = new ContextElement(ContextElementType.FLOAT);
        Assertions.assertThat(cast(floatElement, ContextElementType.BYTE).getValue("value"))
                .isInstanceOf(Byte.class);
        Assertions.assertThat(cast(floatElement, ContextElementType.SHORT).getValue("value"))
                .isInstanceOf(Short.class);
        Assertions.assertThat(cast(floatElement, ContextElementType.INTEGER).getValue("value"))
                .isInstanceOf(Integer.class);
        Assertions.assertThat(cast(floatElement, ContextElementType.LONG).getValue("value"))
                .isInstanceOf(Long.class);
        Assertions.assertThat(cast(floatElement, ContextElementType.FLOAT).getValue("value"))
                .isInstanceOf(Float.class);
        Assertions.assertThat(cast(floatElement, ContextElementType.DOUBLE).getValue("value"))
                .isInstanceOf(Double.class);
        Assertions.assertThat(cast(floatElement, ContextElementType.BOOLEAN).getValue("value"))
                .isInstanceOf(Boolean.class);

        ContextElement doubleElement = new ContextElement(ContextElementType.DOUBLE);
        Assertions.assertThat(cast(doubleElement, ContextElementType.BYTE).getValue("value"))
                .isInstanceOf(Byte.class);
        Assertions.assertThat(cast(doubleElement, ContextElementType.SHORT).getValue("value"))
                .isInstanceOf(Short.class);
        Assertions.assertThat(cast(doubleElement, ContextElementType.INTEGER).getValue("value"))
                .isInstanceOf(Integer.class);
        Assertions.assertThat(cast(doubleElement, ContextElementType.LONG).getValue("value"))
                .isInstanceOf(Long.class);
        Assertions.assertThat(cast(doubleElement, ContextElementType.FLOAT).getValue("value"))
                .isInstanceOf(Float.class);
        Assertions.assertThat(cast(doubleElement, ContextElementType.DOUBLE).getValue("value"))
                .isInstanceOf(Double.class);
        Assertions.assertThat(cast(doubleElement, ContextElementType.BOOLEAN).getValue("value"))
                .isInstanceOf(Boolean.class);

        ContextElement booleanElement = new ContextElement(ContextElementType.BOOLEAN);
        Assertions.assertThat(cast(booleanElement, ContextElementType.BYTE).getValue("value"))
                .isInstanceOf(Byte.class);
        Assertions.assertThat(cast(booleanElement, ContextElementType.SHORT).getValue("value"))
                .isInstanceOf(Short.class);
        Assertions.assertThat(cast(booleanElement, ContextElementType.INTEGER).getValue("value"))
                .isInstanceOf(Integer.class);
        Assertions.assertThat(cast(booleanElement, ContextElementType.LONG).getValue("value"))
                .isInstanceOf(Long.class);
        Assertions.assertThat(cast(booleanElement, ContextElementType.FLOAT).getValue("value"))
                .isInstanceOf(Float.class);
        Assertions.assertThat(cast(booleanElement, ContextElementType.DOUBLE).getValue("value"))
                .isInstanceOf(Double.class);
        Assertions.assertThat(cast(booleanElement, ContextElementType.BOOLEAN).getValue("value"))
                .isInstanceOf(Boolean.class);
    }

}
