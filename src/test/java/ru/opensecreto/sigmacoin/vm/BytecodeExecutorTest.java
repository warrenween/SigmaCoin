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
                new VMConfiguration(10, 10, 10));

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
                new VMConfiguration(10, 10, 10));

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
                new VMConfiguration(10, 10, 10));

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
                new VMConfiguration(10, 10, 10));

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
                new VMConfiguration(10, 10, 10));

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
        Word idA = new Word(0x00);
        Memory contractA = mock(Memory.class);
        when(contractA.get(0)).thenReturn(Opcodes.PUSH);
        when(contractA.get(1)).thenReturn(new Word(0x12));// 0x12 (top)
        when(contractA.get(2)).thenReturn(Opcodes.PUSH);
        when(contractA.get(3)).thenReturn(new Word(0x34));// 0x12 0x34 (top)
        when(contractA.get(4)).thenReturn(Opcodes.PUSH);
        when(contractA.get(5)).thenReturn(new Word(0x56));// 0x12 0x34 0x56 (top)
        when(contractA.get(6)).thenReturn(Opcodes.PUSH);
        when(contractA.get(7)).thenReturn(new Word(0x03));// 0x12 0x34 0x56 | 0x03 (top)
        when(contractA.get(8)).thenReturn(Opcodes.PUSH);
        when(contractA.get(9)).thenReturn(new Word(0x01));// 0x12 0x34 0x56 | 0x03 | 0x01 (top)
        when(contractA.get(10)).thenReturn(Opcodes.INVOKE);// 0x12 0x34 0x56 0x56 0x1f 0x05 0x01 (top)
        when(contractA.get(11)).thenReturn(Opcodes.STOP_GOOD); // 0x12 0x34 0x56 0x56 0x1f 0x05 0x01 | 0x07 0x00 (top)

        Word idB = new Word(0x01);
        Memory contractB = mock(Memory.class);
        // 0x12 0x34 0x56 (top)
        when(contractB.get(0)).thenReturn(Opcodes.DUP);// 0x12 0x34 0x56 0x56 (top)
        when(contractB.get(1)).thenReturn(Opcodes.PUSH);
        when(contractB.get(2)).thenReturn(new Word(0x1f));// 0x12 0x34 0x56 0x56 0x1f (top)
        when(contractB.get(3)).thenReturn(Opcodes.STOP_BAD);// 0x12 0x34 0x56 0x56 0x1f | 0x05 | 0x01 (top)

        ContractManager manager = mock(ContractManager.class);
        when(manager.contractExists(idA)).thenReturn(true);
        when(manager.getContract(idA)).thenReturn(contractA);
        when(manager.contractExists(idB)).thenReturn(true);
        when(manager.getContract(idB)).thenReturn(contractB);

        VirtualMachineController controller = new VirtualMachineController(manager,
                new VMConfiguration(20, 10, 10));

        Stack result = controller.invoke(new Stack(20), idA);

        assertThat(result.getSize()).isEqualTo(9);
        assertThat(result.popCustom(9)).containsExactly(
                new Word(0x12),
                new Word(0x34),
                new Word(0x56),
                new Word(0x56),
                new Word(0x1f),
                new Word(0x05),
                new Word(0x01),
                new Word(0x07),
                new Word(0x00)
        );

        assertThat(result.getSize()).isEqualTo(0);
    }

    @Test
    public void test_ADD() {
        Word idA = new Word(0x00);
        Memory contractA = mock(Memory.class);
        when(contractA.get(0)).thenReturn(Opcodes.PUSH);
        when(contractA.get(1)).thenReturn(new Word(123456));//123456 (top)
        when(contractA.get(2)).thenReturn(Opcodes.PUSH);
        when(contractA.get(3)).thenReturn(new Word(-123456));//123456 -123456 (top)
        when(contractA.get(4)).thenReturn(Opcodes.ADD);//0(top)
        when(contractA.get(6)).thenReturn(Opcodes.STOP_GOOD);// 0 0x01 0x00 (top)

        ContractManager manager = mock(ContractManager.class);
        when(manager.contractExists(idA)).thenReturn(true);
        when(manager.getContract(idA)).thenReturn(contractA);

        VirtualMachineController controller = new VirtualMachineController(manager,
                new VMConfiguration(10, 10, 10));

        Stack result = controller.invoke(new Stack(10), idA);

        assertThat(result.getSize()).isEqualTo(3);

        assertThat(result.pop()).isEqualTo(new Word(0x00));
        assertThat(result.pop()).isEqualTo(new Word(0x01));
        assertThat(result.pop()).isEqualTo(new Word(0));

        assertThat(result.getSize()).isEqualTo(0);
    }
    
}
