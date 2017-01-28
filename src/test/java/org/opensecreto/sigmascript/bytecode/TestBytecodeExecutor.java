package org.opensecreto.sigmascript.bytecode;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.opensecreto.sigmascript.bytecode.Opcodes.*;

@Test
public class TestBytecodeExecutor {

    private DebuggableBytecodeExecutor executor = new DebuggableBytecodeExecutor();
    private StorageManager storage;

    @BeforeMethod
    public void prepare() {
        executor.reset();
        storage = mock(StorageManager.class);
        executor.setStorage(storage);
    }

    @Test(timeOut = 10 * 1000)
    public void testStack() {
        when(storage.getByte(0)).thenReturn(OP_PUSH);
        when(storage.getByte(1)).thenReturn((byte) 0x47);

        executor.execute();
        assertThat(executor.getStack()).containsExactly((byte) 0x47);
    }

    @Test(timeOut = 10 * 1000)
    public void testSwitchModeToMemory() {
        when(storage.getByte(0)).thenReturn(OP_MODE_M);

        executor.execute();

        assertThat(executor.getMode()).isEqualTo(true);
        assertThat(executor.getMode()).isTrue();
    }

    @Test(timeOut = 10 * 1000)
    public void testPop() {
        when(storage.getByte(0)).thenReturn(OP_PUSH);
        when(storage.getByte(1)).thenReturn((byte) 27);
        when(storage.getByte(2)).thenReturn(OP_POP);

        executor.execute();

        assertThat(executor.getStack()).isEmpty();
    }

    @Test(timeOut = 10 * 1000)
    public void testOP_MEM_PUT() {
        when(storage.getByte(0)).thenReturn(OP_PUSH);
        when(storage.getByte(1)).thenReturn((byte) 0xff);
        when(storage.getByte(2)).thenReturn(OP_PUSH);
        when(storage.getByte(3)).thenReturn((byte) 0xff);
        when(storage.getByte(4)).thenReturn(OP_PUSH);
        when(storage.getByte(5)).thenReturn((byte) 0xff);
        when(storage.getByte(6)).thenReturn(OP_PUSH);
        when(storage.getByte(7)).thenReturn((byte) 0x57);
        when(storage.getByte(8)).thenReturn(OP_MEM_PUT);

        executor.execute();

        assertThat(executor.getMemory().get(0xffffff)).isEqualTo((byte) 0x57);
    }

    public void testPoppingEmptyStack() {
        when(storage.getByte(0)).thenReturn(OP_POP);

        assertThatThrownBy(() -> executor.execute());
    }

    public void testOP_SET_POINTER() {
        when(storage.getByte(0)).thenReturn(OP_PUSH);
        when(storage.getByte(1)).thenReturn((byte) 0x24);
        when(storage.getByte(2)).thenReturn(OP_PUSH);
        when(storage.getByte(3)).thenReturn((byte) 0xda);
        when(storage.getByte(4)).thenReturn(OP_PUSH);
        when(storage.getByte(5)).thenReturn((byte) 0xc0);
        when(storage.getByte(6)).thenReturn(OP_PUSH);
        when(storage.getByte(7)).thenReturn((byte) 0x17);
        when(storage.getByte(8)).thenReturn(OP_PUSH);
        when(storage.getByte(9)).thenReturn((byte) 0);
        when(storage.getByte(10)).thenReturn(OP_PUSH);
        when(storage.getByte(11)).thenReturn((byte) 0);
        when(storage.getByte(12)).thenReturn(OP_PUSH);
        when(storage.getByte(13)).thenReturn((byte) 0);
        when(storage.getByte(14)).thenReturn(OP_PUSH);
        when(storage.getByte(15)).thenReturn((byte) 0);
        when(storage.getByte(16)).thenReturn(OP_SET_POINTER);

        when(storage.getByte(0x24dac017L)).thenReturn(OP_PUSH);
        when(storage.getByte(0x24dac018L)).thenReturn((byte) 0x15);

        executor.execute();

        assertThat(executor.getStack()).containsExactly(new byte[]{0x24, (byte) 0xda, (byte) 0xc0, 0x17});
    }

}
