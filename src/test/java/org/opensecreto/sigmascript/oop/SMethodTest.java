package org.opensecreto.sigmascript.oop;

import org.assertj.core.api.Assertions;
import org.mockito.Mockito;
import org.opensecreto.sigmascript.bytecode.Stack;
import org.opensecreto.sigmascript.bytecode.StorageManager;
import org.testng.annotations.Test;

import static org.mockito.Mockito.when;
import static org.opensecreto.sigmascript.bytecode.Opcodes.*;

@Test
public class SMethodTest {

    public void test() {
        final long startIndex = 64;

        StorageManager manager = Mockito.mock(StorageManager.class);
        when(manager.getByte(startIndex + 1)).thenReturn(OP_PUSH);
        when(manager.getByte(startIndex + 2)).thenReturn((byte) 0x00);
        when(manager.getByte(startIndex + 3)).thenReturn(OP_PUSH);
        when(manager.getByte(startIndex + 4)).thenReturn((byte) 0x00);
        when(manager.getByte(startIndex + 5)).thenReturn(OP_PUSH);
        when(manager.getByte(startIndex + 6)).thenReturn((byte) 0x00);
        when(manager.getByte(startIndex + 7)).thenReturn(OP_PUSH);
        when(manager.getByte(startIndex + 8)).thenReturn((byte) 0x00);
        when(manager.getByte(startIndex + 9)).thenReturn(OP_PUSH);
        when(manager.getByte(startIndex + 10)).thenReturn((byte) 0x00);
        when(manager.getByte(startIndex + 11)).thenReturn(OP_PUSH);
        when(manager.getByte(startIndex + 12)).thenReturn((byte) 0x00);
        when(manager.getByte(startIndex + 13)).thenReturn(OP_PUSH);
        when(manager.getByte(startIndex + 14)).thenReturn((byte) 0x00);
        when(manager.getByte(startIndex + 15)).thenReturn(OP_PUSH);
        when(manager.getByte(startIndex + 16)).thenReturn((byte) 0x00);
        when(manager.getByte(startIndex + 17)).thenReturn(OP_PUSH);
        when(manager.getByte(startIndex + 18)).thenReturn(OP_PUSH); //<- put this to memory at 0
        when(manager.getByte(startIndex + 19)).thenReturn(OP_MEM_PUT);
        when(manager.getByte(startIndex + 20)).thenReturn(OP_POP);
        when(manager.getByte(startIndex + 21)).thenReturn(OP_POP);
        when(manager.getByte(startIndex + 22)).thenReturn(OP_POP);
        when(manager.getByte(startIndex + 23)).thenReturn(OP_POP);
        when(manager.getByte(startIndex + 24)).thenReturn(OP_POP);
        when(manager.getByte(startIndex + 25)).thenReturn(OP_POP);
        when(manager.getByte(startIndex + 26)).thenReturn(OP_POP);
        when(manager.getByte(startIndex + 27)).thenReturn(OP_POP);
        when(manager.getByte(startIndex + 28)).thenReturn(OP_POP);//<- clean stack
        when(manager.getByte(startIndex + 29)).thenReturn(OP_PUSH);
        when(manager.getByte(startIndex + 30)).thenReturn((byte) 0x01);
        when(manager.getByte(startIndex + 31)).thenReturn(OP_PUSH);
        when(manager.getByte(startIndex + 32)).thenReturn((byte) 0x00);
        when(manager.getByte(startIndex + 33)).thenReturn(OP_PUSH);
        when(manager.getByte(startIndex + 34)).thenReturn((byte) 0x00);
        when(manager.getByte(startIndex + 35)).thenReturn(OP_PUSH);
        when(manager.getByte(startIndex + 36)).thenReturn((byte) 0x00);
        when(manager.getByte(startIndex + 37)).thenReturn(OP_PUSH);
        when(manager.getByte(startIndex + 38)).thenReturn((byte) 0x00);
        when(manager.getByte(startIndex + 39)).thenReturn(OP_PUSH);
        when(manager.getByte(startIndex + 40)).thenReturn((byte) 0x00);
        when(manager.getByte(startIndex + 41)).thenReturn(OP_PUSH);
        when(manager.getByte(startIndex + 42)).thenReturn((byte) 0x00);
        when(manager.getByte(startIndex + 43)).thenReturn(OP_PUSH);
        when(manager.getByte(startIndex + 44)).thenReturn((byte) 0x00);
        when(manager.getByte(startIndex + 45)).thenReturn(OP_PUSH);
        when(manager.getByte(startIndex + 46)).thenReturn((byte) 0x32); //<- put this to memory at 1
        when(manager.getByte(startIndex + 47)).thenReturn(OP_MEM_PUT);

        SMethod method = new SMethod(manager, 65);
        Stack stack = method.invoke(new Stack());
        Assertions.assertThat(stack.getStack()).containsExactly((byte) 0x32);
    }

}
