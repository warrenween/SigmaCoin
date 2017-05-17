package ru.opensecreto.sigmacoin.vm;

import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;

public class StackTest {

    @Test
    public void test() {
        Stack stack = new Stack(2);
        Assertions.assertThat(stack.getSize()).isEqualTo(0);
        stack.push((byte) 2);
        Assertions.assertThat(stack.getSize()).isEqualTo(1);
        Assertions.assertThat(stack.pop()).isEqualTo((byte) 2);
        Assertions.assertThat(stack.getSize()).isEqualTo(0);
    }

    @Test
    public void testExceptions() {
        Stack stack = new Stack(1);
        stack.push((byte) 1);
        Assertions.assertThatThrownBy(() -> stack.push((byte) 1))
                .isInstanceOf(IllegalStateException.class);
    }

}
