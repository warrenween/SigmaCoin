package ru.opensecreto.sigmacoin.vm;

import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class StackTest {

    @Test
    public void test() {
        Stack stack = new Stack(2);
        assertThat(stack.getSize()).isEqualTo(0);
        stack.push((byte) 2);
        assertThat(stack.getSize()).isEqualTo(1);
        assertThat(stack.pop()).isEqualTo((byte) 2);
        assertThat(stack.getSize()).isEqualTo(0);
    }

    @Test
    public void testExceptions() {
        Stack stack = new Stack(1);
        stack.push((byte) 1);
        assertThatThrownBy(() -> stack.push((byte) 1))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    public void testInt() {
        Stack stack = new Stack(4);
        stack.pushInt(0x12345678);
        assertThat(stack.popInt()).inHexadecimal().isEqualTo(0x12345678);
        assertThatThrownBy(stack::popInt).isInstanceOf(IllegalStateException.class);
    }

    @Test
    public void testGetStack() {
        Stack stack = new Stack(8);
        stack.pushInt(0x12345678);
        assertThat(stack.getStack()).inHexadecimal().hasSize(4)
                .containsOnly((byte) 0x12, (byte) 0x34, (byte) 0x56, (byte) 0x78);
    }

    @Test
    public void testShort() {
        Stack stack = new Stack(2);
        stack.pushShort((short) 0x1234);
        assertThat(stack.popShort()).inHexadecimal().isEqualTo(((short) 0x1234));
        assertThatThrownBy(stack::popShort).isInstanceOf(IllegalStateException.class);
    }

    @Test
    public void testLong() {
        Stack stack = new Stack(8);
        stack.pushLong(0x123456789abcdef1L);
        assertThat(stack.popLong()).inHexadecimal().isEqualTo(0x123456789abcdef1L);
        assertThatThrownBy(stack::popLong).isInstanceOf(IllegalStateException.class);
    }

    @Test
    public void testPopCustom() {
        Stack stack = new Stack(3);
        stack.push((byte) 1);
        stack.push((byte) 3);
        stack.push((byte) 4);

        assertThat(stack.popCustom(3)).containsExactly((byte) 4, (byte) 3, (byte) 1);
    }
}
