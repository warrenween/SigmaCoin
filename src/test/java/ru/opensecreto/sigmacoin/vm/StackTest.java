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

    @Test
    public void testInt() {
        Stack stack = new Stack(4);
        stack.pushInt(0x12345678);
        Assertions.assertThat(stack.popInt()).inHexadecimal().isEqualTo(0x12345678);
        Assertions.assertThatThrownBy(stack::popInt).isInstanceOf(IllegalStateException.class);
    }

    @Test
    public void testGetStack() {
        Stack stack = new Stack(8);
        stack.pushInt(0x12345678);
        Assertions.assertThat(stack.getStack()).inHexadecimal().hasSize(4)
                .containsOnly((byte) 0x12, (byte) 0x34, (byte) 0x56, (byte) 0x78);
    }

    @Test
    public void testShort() {
        Stack stack = new Stack(2);
        stack.pushShort((short) 0x1234);
        Assertions.assertThat(stack.popShort()).inHexadecimal().isEqualTo(((short) 0x1234));
        Assertions.assertThatThrownBy(stack::popShort).isInstanceOf(IllegalStateException.class);
    }

    @Test
    public void testLong() {
        Stack stack = new Stack(8);
        stack.pushLong(0x123456789abcdef1L);
        Assertions.assertThat(stack.popLong()).inHexadecimal().isEqualTo(0x123456789abcdef1L);
        Assertions.assertThatThrownBy(stack::popLong).isInstanceOf(IllegalStateException.class);
    }
}
