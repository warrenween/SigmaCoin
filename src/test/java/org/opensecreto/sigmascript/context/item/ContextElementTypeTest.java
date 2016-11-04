package org.opensecreto.sigmascript.context.item;

import org.assertj.core.api.Assertions;
import org.opensecreto.sigmascript.exceptions.IllegalOperationException;
import org.testng.annotations.Test;

import static org.opensecreto.sigmascript.context.item.ContextElementType.*;

public class ContextElementTypeTest {

    @Test
    public void testNormalization() throws IllegalOperationException {
        Assertions.assertThatThrownBy(() ->
                normalize(OBJECT, BYTE)
        ).isInstanceOf(IllegalOperationException.class);

        Assertions.assertThatThrownBy(() ->
                normalize(BYTE, OBJECT)
        ).isInstanceOf(IllegalOperationException.class);

        Assertions.assertThatThrownBy(() ->
                normalize(OBJECT, OBJECT)
        ).isInstanceOf(IllegalOperationException.class);

        Assertions.assertThat(normalize(BYTE, BYTE)).isEqualTo(BYTE);
        Assertions.assertThat(normalize(BYTE, SHORT)).isEqualTo(SHORT);
        Assertions.assertThat(normalize(BYTE, INTEGER)).isEqualTo(INTEGER);
        Assertions.assertThat(normalize(BYTE, LONG)).isEqualTo(LONG);
        Assertions.assertThat(normalize(BYTE, FLOAT)).isEqualTo(FLOAT);
        Assertions.assertThat(normalize(BYTE, DOUBLE)).isEqualTo(DOUBLE);
        Assertions.assertThat(normalize(BYTE, BOOLEAN)).isEqualTo(BOOLEAN);

        Assertions.assertThat(normalize(SHORT, BYTE)).isEqualTo(SHORT);
        Assertions.assertThat(normalize(SHORT, SHORT)).isEqualTo(SHORT);
        Assertions.assertThat(normalize(SHORT, INTEGER)).isEqualTo(INTEGER);
        Assertions.assertThat(normalize(SHORT, LONG)).isEqualTo(LONG);
        Assertions.assertThat(normalize(SHORT, FLOAT)).isEqualTo(FLOAT);
        Assertions.assertThat(normalize(SHORT, DOUBLE)).isEqualTo(DOUBLE);
        Assertions.assertThat(normalize(SHORT, BOOLEAN)).isEqualTo(BOOLEAN);

        Assertions.assertThat(normalize(INTEGER, BYTE)).isEqualTo(INTEGER);
        Assertions.assertThat(normalize(INTEGER, SHORT)).isEqualTo(INTEGER);
        Assertions.assertThat(normalize(INTEGER, INTEGER)).isEqualTo(INTEGER);
        Assertions.assertThat(normalize(INTEGER, LONG)).isEqualTo(LONG);
        Assertions.assertThat(normalize(INTEGER, FLOAT)).isEqualTo(FLOAT);
        Assertions.assertThat(normalize(INTEGER, DOUBLE)).isEqualTo(DOUBLE);
        Assertions.assertThat(normalize(INTEGER, BOOLEAN)).isEqualTo(BOOLEAN);

        Assertions.assertThat(normalize(LONG, BYTE)).isEqualTo(LONG);
        Assertions.assertThat(normalize(LONG, SHORT)).isEqualTo(LONG);
        Assertions.assertThat(normalize(LONG, INTEGER)).isEqualTo(LONG);
        Assertions.assertThat(normalize(LONG, LONG)).isEqualTo(LONG);
        Assertions.assertThat(normalize(LONG, FLOAT)).isEqualTo(FLOAT);
        Assertions.assertThat(normalize(LONG, DOUBLE)).isEqualTo(DOUBLE);
        Assertions.assertThat(normalize(LONG, BOOLEAN)).isEqualTo(BOOLEAN);

        Assertions.assertThat(normalize(FLOAT, BYTE)).isEqualTo(FLOAT);
        Assertions.assertThat(normalize(FLOAT, SHORT)).isEqualTo(FLOAT);
        Assertions.assertThat(normalize(FLOAT, INTEGER)).isEqualTo(FLOAT);
        Assertions.assertThat(normalize(FLOAT, LONG)).isEqualTo(FLOAT);
        Assertions.assertThat(normalize(FLOAT, FLOAT)).isEqualTo(FLOAT);
        Assertions.assertThat(normalize(FLOAT, DOUBLE)).isEqualTo(DOUBLE);
        Assertions.assertThat(normalize(FLOAT, BOOLEAN)).isEqualTo(BOOLEAN);

        Assertions.assertThat(normalize(DOUBLE, BYTE)).isEqualTo(DOUBLE);
        Assertions.assertThat(normalize(DOUBLE, SHORT)).isEqualTo(DOUBLE);
        Assertions.assertThat(normalize(DOUBLE, INTEGER)).isEqualTo(DOUBLE);
        Assertions.assertThat(normalize(DOUBLE, LONG)).isEqualTo(DOUBLE);
        Assertions.assertThat(normalize(DOUBLE, FLOAT)).isEqualTo(DOUBLE);
        Assertions.assertThat(normalize(DOUBLE, DOUBLE)).isEqualTo(DOUBLE);
        Assertions.assertThat(normalize(DOUBLE, BOOLEAN)).isEqualTo(BOOLEAN);

        Assertions.assertThat(normalize(BOOLEAN, BYTE)).isEqualTo(BOOLEAN);
        Assertions.assertThat(normalize(BOOLEAN, SHORT)).isEqualTo(BOOLEAN);
        Assertions.assertThat(normalize(BOOLEAN, INTEGER)).isEqualTo(BOOLEAN);
        Assertions.assertThat(normalize(BOOLEAN, LONG)).isEqualTo(BOOLEAN);
        Assertions.assertThat(normalize(BOOLEAN, FLOAT)).isEqualTo(BOOLEAN);
        Assertions.assertThat(normalize(BOOLEAN, DOUBLE)).isEqualTo(BOOLEAN);
        Assertions.assertThat(normalize(BOOLEAN, BOOLEAN)).isEqualTo(BOOLEAN);
    }

}
