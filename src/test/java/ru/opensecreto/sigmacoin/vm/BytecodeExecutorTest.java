package ru.opensecreto.sigmacoin.vm;

import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BytecodeExecutorTest {

    @Test
    public void testInvokeNonExisting() {
        ContractManager manager = mock(ContractManager.class);
        when(manager.contractExists(new Word(0))).thenReturn(false);

        VirtualMachineController controller = new VirtualMachineController(manager,
                new VMConfiguration(10, 1, 10, 10));

        Stack result = controller.invoke(new Stack(10), new Word(0));

        assertThat(result.pop()).isEqualTo(new Word(1));
        assertThat(result.pop()).isEqualTo(new Word(0));

        assertThat(result.getSize()).isEqualTo(0);
    }

    @Test
    public void testInvokeGood() {
        Word idA = new Word(0);
        Memory contractA = mock(Memory.class);
        when(contractA.get(0)).thenReturn(Opcodes.STOP_GOOD);

        ContractManager manager = mock(ContractManager.class);
        when(manager.contractExists(idA)).thenReturn(true);
        when(manager.getContract(idA)).thenReturn(contractA);

        VirtualMachineController controller = new VirtualMachineController(manager,
                new VMConfiguration(10, 1, 10, 10));

        Stack result = controller.invoke(new Stack(10), idA);

        assertThat(result.pop()).isEqualTo(new Word(0));
        assertThat(result.pop()).isEqualTo(new Word(0));

        assertThat(result.getSize()).isEqualTo(0);
    }

    @Test
    public void testInvokeBad() {
        Word idA = new Word(0);
        Memory contractA = mock(Memory.class);
        when(contractA.get(0)).thenReturn(Opcodes.STOP_BAD);

        ContractManager manager = mock(ContractManager.class);
        when(manager.contractExists(idA)).thenReturn(true);
        when(manager.getContract(idA)).thenReturn(contractA);

        VirtualMachineController controller = new VirtualMachineController(manager,
                new VMConfiguration(10, 1, 10, 10));

        Stack result = controller.invoke(new Stack(10), idA);

        assertThat(result.pop()).isEqualTo(new Word(1));
        assertThat(result.pop()).isEqualTo(new Word(0));

        assertThat(result.getSize()).isEqualTo(0);
    }

    @Test
    public void testInvokeResultPUSH_DUP() {
        Word idA = new Word(0);
        Memory contractA = mock(Memory.class);
        when(contractA.get(0)).thenReturn(Opcodes.PUSH);
        when(contractA.get(1)).thenReturn(new Word(0x1f));
        when(contractA.get(2)).thenReturn(Opcodes.DUP);
        when(contractA.get(3)).thenReturn(Opcodes.DUP);
        when(contractA.get(4)).thenReturn(Opcodes.DUP);
        when(contractA.get(5)).thenReturn(Opcodes.STOP_BAD);

        ContractManager manager = mock(ContractManager.class);
        when(manager.contractExists(idA)).thenReturn(true);
        when(manager.getContract(idA)).thenReturn(contractA);

        VirtualMachineController controller = new VirtualMachineController(manager,
                new VMConfiguration(10, 1, 10, 10));

        Stack result = controller.invoke(new Stack(10), idA);

        assertThat(result.pop()).isEqualTo(new Word(1));
        assertThat(result.pop()).isEqualTo(new Word(4));
        assertThat(result.pop()).isEqualTo(new Word(0x1f));
        assertThat(result.pop()).isEqualTo(new Word(0x1f));
        assertThat(result.pop()).isEqualTo(new Word(0x1f));
        assertThat(result.pop()).isEqualTo(new Word(0x1f));

        assertThat(result.getSize()).isEqualTo(0);
    }

    @Test
    public void testPUSH_POP_DUP() {
        Word idA = new Word(0x00);
        Memory contractA = mock(Memory.class);
        when(contractA.get(0)).thenReturn(Opcodes.PUSH);
        when(contractA.get(1)).thenReturn(new Word(0x1f));//0x1f (top)
        when(contractA.get(2)).thenReturn(Opcodes.PUSH);
        when(contractA.get(3)).thenReturn(new Word(0x56));//0x1f 0x56 (top)
        when(contractA.get(4)).thenReturn(Opcodes.DUP);//0x1f 0x56 0x56 (top)
        when(contractA.get(5)).thenReturn(Opcodes.POP);//0x1f 0x56 (top)
        when(contractA.get(6)).thenReturn(Opcodes.STOP_GOOD);//0x1f 0x56 | 0x02 0x00 (top)

        ContractManager manager = mock(ContractManager.class);
        when(manager.contractExists(idA)).thenReturn(true);
        when(manager.getContract(idA)).thenReturn(contractA);

        VirtualMachineController controller = new VirtualMachineController(manager,
                new VMConfiguration(10, 1, 10, 10));

        Stack result = controller.invoke(new Stack(10), idA);

        assertThat(result.getSize()).isEqualTo(4);

        assertThat(result.pop()).isEqualTo(new Word(0x00));
        assertThat(result.pop()).isEqualTo(new Word(0x02));
        assertThat(result.pop()).isEqualTo(new Word(0x56));
        assertThat(result.pop()).isEqualTo(new Word(0x1f));

        assertThat(result.getSize()).isEqualTo(0);
    }

    @Test
    public void testInvokeFromCode() {
        Word idA = new Word(0);
        Memory contractA = mock(Memory.class);
        when(contractA.get(0)).thenReturn(Opcodes.PUSH);//data
        when(contractA.get(1)).thenReturn(new Word(0xab));
        when(contractA.get(2)).thenReturn(Opcodes.PUSH);//contract id
        when(contractA.get(3)).thenReturn(new Word(0x01));
        when(contractA.get(4)).thenReturn(Opcodes.PUSH);//data length
        when(contractA.get(5)).thenReturn(new Word(0x01));
        when(contractA.get(6)).thenReturn(Opcodes.INVOKE);//invoking | result: (top) 0x01 0x03 0x1f 0xab 0xab
        when(contractA.get(7)).thenReturn(Opcodes.STOP_GOOD); //adds 0x00 0x06 *result*

        Word idB = new Word(0x01);
        Memory contractB = mock(Memory.class);
        //stack has: 0xab
        when(contractB.get(0)).thenReturn(Opcodes.DUP);
        when(contractB.get(1)).thenReturn(Opcodes.PUSH);
        when(contractB.get(2)).thenReturn(new Word(0x1f));
        when(contractB.get(3)).thenReturn(Opcodes.STOP_BAD);
        //returns: (top) 0x01 0x03 || 0x1f 0xab 0xab

        ContractManager manager = mock(ContractManager.class);
        when(manager.contractExists(idA)).thenReturn(true);
        when(manager.getContract(idA)).thenReturn(contractA);
        when(manager.contractExists(idB)).thenReturn(true);
        when(manager.getContract(idB)).thenReturn(contractB);

        VirtualMachineController controller = new VirtualMachineController(manager,
                new VMConfiguration(20, 1, 10, 10));

        Stack result = controller.invoke(new Stack(20), idA);

        assertThat(result.getSize()).isEqualTo(7);
        assertThat(result.popCustom(7)).containsExactly(
                new Word(0xab),
                new Word(0xab),
                new Word(0x1f),
                new Word(0x03),
                new Word(0x01),
                new Word(0x06),
                new Word(0x00)
        );

        assertThat(result.getSize()).isEqualTo(0);
    }
}
