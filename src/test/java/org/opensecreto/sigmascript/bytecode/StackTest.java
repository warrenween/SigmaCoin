package org.opensecreto.sigmascript.bytecode;

import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Test
public class StackTest {

    public void testPoppingFromEmpty() {
        Stack stack = new Stack();
        assertThatThrownBy(stack::pop);
    }

    public void testPuttingAndGetting() {
        Stack stack = new Stack();
        stack.push((byte) 1);
        stack.push((byte) 2);
        stack.push((byte) 3);
        stack.push((byte) 4);
        assertThat(stack.get(0)).isEqualTo((byte) 4);
        assertThat(stack.get(1)).isEqualTo((byte) 3);
        assertThat(stack.get(2)).isEqualTo((byte) 2);
        assertThat(stack.get(3)).isEqualTo((byte) 1);
    }

    public void testGettingStack() {
        Stack stack = new Stack();
        stack.push((byte) 1);
        stack.push((byte) 2);
        stack.push((byte) 3);
        stack.push((byte) 4);
        assertThat(stack.getStack()).hasSize(4).containsExactly(new byte[]{1, 2, 3, 4});
    }

    public void testPopping() {
        Stack stack = new Stack();
        assertThatThrownBy(stack::pop);

        stack.push((byte) 4);
        stack.push((byte) 8);
        stack.pop();
        assertThat(stack.getStack()).hasSize(1).containsExactly(new byte[]{4});
    }

    public void testGettingTooBigIndex() {
        Stack stack = new Stack();
        stack.push((byte) 2);
        assertThatThrownBy(() -> stack.get(2));
    }

}
