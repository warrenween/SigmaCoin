package org.opensecreto.sigmascript.context.item;

import org.opensecreto.sigmascript.DebuggableBytecodeExecutor;
import org.opensecreto.sigmascript.StorageManager;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.opensecreto.sigmascript.Opcodes.*;

@Test
public class TestBytecodeExecutor {

    private DebuggableBytecodeExecutor executor = new DebuggableBytecodeExecutor();

    @BeforeMethod
    public void prepare() {
        executor.reset();
    }

    @Test(timeOut = 10*1000)
    public void testStack() {
        StorageManager storage = mock(StorageManager.class);
        when(storage.getByte(0)).thenReturn(OP_PUSH);
        when(storage.getByte(1)).thenReturn((byte) 57);
        executor.setStorage(storage);

        executor.storageBreakpoints.add(2L);
        executor.execute();
        assertThat(executor.getStack()).containsExactly((byte) 57);
    }

    @Test(timeOut = 10*1000)
    public void testSwitchModeToMemory() {
        StorageManager storage = mock(StorageManager.class);
        when(storage.getByte(0)).thenReturn(OP_JUMP_M);
        executor.setStorage(storage);

        executor.memoryBreakpoints.add(0L);
        executor.storageBreakpoints.add(2L);
        executor.execute();

        assertThat(executor.getPointer()).isEqualTo(0L);
        assertThat(executor.getMode()).isTrue();
    }

    @Test(timeOut = 10*1000)
    public void testPop() {
        StorageManager storage = mock(StorageManager.class);
        when(storage.getByte(0)).thenReturn(OP_PUSH);
        when(storage.getByte(1)).thenReturn((byte) 27);
        when(storage.getByte(2)).thenReturn(OP_POP);
        executor.setStorage(storage);

        executor.execute();

        assertThat(executor.getStack()).isEmpty();
    }

    @Test(timeOut = 10*1000)
    public void testOP_MEM_PUT() {
        StorageManager storage = mock(StorageManager.class);
        when(storage.getByte(0)).thenReturn(OP_PUSH);
        when(storage.getByte(1)).thenReturn((byte) 0xff);
        when(storage.getByte(2)).thenReturn(OP_PUSH);
        when(storage.getByte(3)).thenReturn((byte) 0xff);
        when(storage.getByte(4)).thenReturn(OP_PUSH);
        when(storage.getByte(5)).thenReturn((byte) 0xff);
        when(storage.getByte(6)).thenReturn(OP_PUSH);
        when(storage.getByte(7)).thenReturn((byte) 0x57);
        when(storage.getByte(8)).thenReturn(OP_MEM_PUT);
        executor.setStorage(storage);

        executor.storageBreakpoints.add(9L);
        executor.execute();

        assertThat(executor.getMemory()[0xffffff]).isEqualTo((byte) 0x57);
    }

}
