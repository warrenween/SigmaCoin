package org.opensecreto.sigmascript.context.item;

import org.assertj.core.api.Assertions;
import org.opensecreto.sigmascript.Memory;
import org.testng.annotations.Test;

@Test
public class MemoryTest {

    public void testPuttingAndGetting() {
        Memory memory = new Memory();
        memory.put(21L, (byte) 41);
        memory.put(48L, (byte) -21);

        Assertions.assertThat(memory.get(0L))
                .as("Check if non set index returns 0").isEqualTo((byte) 0);
        Assertions.assertThat(memory.get(21L)).isEqualTo((byte) 41);
        Assertions.assertThat(memory.get(48L))
                .as("Test getting negative bytes.").isEqualTo((byte) -21);
    }

    public void testBadArguments() {
        Memory memory = new Memory();
        Assertions.assertThatThrownBy(() -> memory.get(-10));
        Assertions.assertThatThrownBy(() -> memory.put(-10, (byte) 18));
    }

    public void testAssembling() {
        Memory memory = new Memory();
        memory.put(21L, (byte) 41);
        memory.put(48L, (byte) -21);

        Memory mem = Memory.assemble(memory.disassemble());

        Assertions.assertThat(memory.get(21L)).isEqualTo((byte) 41);
        Assertions.assertThat(memory.get(48L))
                .as("Test getting negative bytes.").isEqualTo((byte) -21);
    }

}
