package org.opensecreto.sigmascript.context.item;

import org.opensecreto.sigmascript.DebuggableBytecodeExecutor;
import org.opensecreto.sigmascript.StorageManager;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.opensecreto.sigmascript.Opcodes.*;

@Test
public class TestBytecodeExecutor {

    private DebuggableBytecodeExecutor executor = new DebuggableBytecodeExecutor();
    private StorageManager storage;

    @BeforeMethod
    public void prepare() {
        executor.reset();
        storage = mock(StorageManager.class, withSettings().defaultAnswer(invocation -> (byte) 0));
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
        when(storage.getByte(0)).thenReturn(OP_JUMP_M);

        executor.execute();

        assertThat(executor.getPointer()).isEqualTo(0L);
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

        assertThat(executor.getMemory()[0xffffff]).isEqualTo((byte) 0x57);
    }

}
