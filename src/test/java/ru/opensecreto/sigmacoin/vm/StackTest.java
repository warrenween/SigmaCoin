package ru.opensecreto.sigmacoin.vm;

import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class StackTest {

    @Test
    public void test_getSize_push_pop() {
        Stack stack = new Stack(10);

        assertThat(stack.getSize()).isEqualTo(0);
        stack.push(new Word(1234));// 1234 (top)
        stack.push(new Word(4321));// 1234 4321 (top)
        assertThat(stack.getSize()).isEqualTo(2);
        assertThat(stack.pop()).isEqualTo(new Word(4321));// 1234 (top)
        assertThat(stack.getSize()).isEqualTo(1);
        stack.push(new Word(6789));// 1234 6789 (top)
        stack.push(new Word(4521));// 1234 6789 4521 (top)
        assertThat(stack.getSize()).isEqualTo(3);
        assertThat(stack.pop()).isEqualTo(new Word(4521));// 1234 6789 (top)
        assertThat(stack.getSize()).isEqualTo(2);
        assertThat(stack.pop()).isEqualTo(new Word(6789));// 1234 (top)
        assertThat(stack.getSize()).isEqualTo(1);
        assertThat(stack.pop()).isEqualTo(new Word(1234));// (top)
        assertThat(stack.getSize()).isEqualTo(0);
    }

    @Test
    public void testBounds() {
        Stack stack = new Stack(2);

        assertThat(stack.getSize()).isEqualTo(0);
        stack.push(new Word(1234));// 1234 (top)
        stack.push(new Word(4321));// 1234 4321 (top)
        assertThat(stack.getSize()).isEqualTo(2);
        assertThat(stack.pop()).isEqualTo(new Word(4321));
        assertThat(stack.pop()).isEqualTo(new Word(1234));
        assertThat(stack.getSize()).isEqualTo(0);
    }

    @Test
    public void testPushCustom() {
        Stack stack = new Stack(4);

        assertThat(stack.getSize()).isEqualTo(0);
        stack.pushCustom(new Word[]{
                new Word(123),
                new Word(321),
                new Word(456),
                new Word(789)
        });
        // 123 321 456 789 (top)
        assertThat(stack.getSize()).isEqualTo(4);
        assertThat(stack.pop()).isEqualTo(new Word(789));// 123 321 456 (top)
        assertThat(stack.getSize()).isEqualTo(3);
        assertThat(stack.pop()).isEqualTo(new Word(456));// 123 321 (top)
        assertThat(stack.getSize()).isEqualTo(2);
        assertThat(stack.pop()).isEqualTo(new Word(321));// 123 (top)
        assertThat(stack.getSize()).isEqualTo(1);
        assertThat(stack.pop()).isEqualTo(new Word(123));// (top)
        assertThat(stack.getSize()).isEqualTo(0);
    }

    @Test
    public void testPopCustom() {
        Stack stack = new Stack(4);

        assertThat(stack.getSize()).isEqualTo(0);
        stack.push(new Word(123));
        stack.push(new Word(456));
        stack.push(new Word(789));
        stack.push(new Word(100));
        assertThat(stack.getSize()).isEqualTo(4);

        assertThat(stack.popCustom(4)).hasSize(4).containsExactly(
                new Word(123), new Word(456), new Word(789), new Word(100)
        );
        assertThat(stack.getSize()).isEqualTo(0);
    }

    @Test
    public void testPushingNull() {
        Stack stack = new Stack(4);

        assertThat(stack.getSize()).isEqualTo(0);
        assertThatThrownBy(() -> stack.push(null)).isInstanceOf(IllegalArgumentException.class);
        assertThat(stack.getSize()).isEqualTo(0);
    }

    @Test
    public void testPopFromEmpty() {
        Stack stack = new Stack(4);

        assertThat(stack.getSize()).isEqualTo(0);
        assertThatThrownBy(stack::pop).isInstanceOf(IllegalStateException.class);
        assertThat(stack.getSize()).isEqualTo(0);
    }

    @Test
    public void testPushingToFull() {
        Stack stack = new Stack(2);

        assertThat(stack.getSize()).isEqualTo(0);
        stack.push(new Word(123));
        stack.push(new Word(321));
        assertThat(stack.getSize()).isEqualTo(2);

        assertThatThrownBy(() -> stack.push(new Word(100))).isInstanceOf(IllegalStateException.class);
        assertThat(stack.getSize()).isEqualTo(2);

        assertThat(stack.pop()).isEqualTo(new Word(321));
        assertThat(stack.pop()).isEqualTo(new Word(123));
        assertThat(stack.getSize()).isEqualTo(0);
    }

}
