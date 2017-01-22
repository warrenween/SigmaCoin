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

    public void testStack() {
        StorageManager storage = mock(StorageManager.class);
        when(storage.getByte(0)).thenReturn(OP_PUSH);
        when(storage.getByte(1)).thenReturn((byte) 57);
        executor.setStorage(storage);

        executor.storageBreakpoints.add(2L);
        executor.execute();
        assertThat(executor.getStack()).containsExactly((byte) 57);
    }

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

    public void testReturn() {
        StorageManager storage = mock(StorageManager.class);
        when(storage.getByte(0)).thenReturn(OP_PUSH);
        when(storage.getByte(1)).thenReturn((byte) 27);
        when(storage.getByte(2)).thenReturn(OP_RETURN);
        executor.setStorage(storage);

        executor.execute();

        assertThat(executor.getExitCode()).isEqualTo((byte) 27);
    }

}
