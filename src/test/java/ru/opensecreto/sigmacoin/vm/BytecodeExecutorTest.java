package ru.opensecreto.sigmacoin.vm;

import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BytecodeExecutorTest {

    @Test
    public void test_INVOKE_nonExistingContract() {
        ContractManager manager = mock(ContractManager.class);
        when(manager.contractExists(new Word(0))).thenReturn(false);

        VirtualMachineController controller = new VirtualMachineController(manager,
                new VMConfiguration(10, 10, 10));

        Stack result = controller.invoke(new Stack(10), new Word(0));

        assertThat(result.getSize()).isEqualTo(2);
        assertThat(result.popCustom(2)).containsExactly(
                new Word(0), new Word(1)
        );
    }

    @Test
    public void test_INVOKE_good() {
        Word idA = new Word(0);
        Memory contractA = mock(Memory.class);
        when(contractA.get(0)).thenReturn(Opcodes.STOP_GOOD);// 0 0x00 (top)

        ContractManager manager = mock(ContractManager.class);
        when(manager.contractExists(idA)).thenReturn(true);
        when(manager.getContract(idA)).thenReturn(contractA);

        VirtualMachineController controller = new VirtualMachineController(manager,
                new VMConfiguration(10, 10, 10));

        Stack result = controller.invoke(new Stack(10), idA);

        assertThat(result.getSize()).isEqualTo(2);
        assertThat(result.popCustom(2)).containsExactly(
                new Word(0), new Word(0)
        );
    }

    @Test
    public void test_INVOKE_bad() {
        Word idA = new Word(0);
        Memory contractA = mock(Memory.class);
        when(contractA.get(0)).thenReturn(Opcodes.STOP_BAD);// 0 0x01 (top)

        ContractManager manager = mock(ContractManager.class);
        when(manager.contractExists(idA)).thenReturn(true);
        when(manager.getContract(idA)).thenReturn(contractA);

        VirtualMachineController controller = new VirtualMachineController(manager,
                new VMConfiguration(10, 10, 10));

        Stack result = controller.invoke(new Stack(10), idA);

        assertThat(result.getSize()).isEqualTo(2);
        assertThat(result.popCustom(2)).containsExactly(
                new Word(0), new Word(0x01)
        );
    }

    @Test
    public void test_INVOKE_returnsCorrectStack() {
        Word idA = new Word(0);
        Memory contractA = mock(Memory.class);

        when(contractA.get(0)).thenReturn(Opcodes.PUSH);
        when(contractA.get(1)).thenReturn(new Word(0x1f));// 0x1f (top)
        when(contractA.get(2)).thenReturn(Opcodes.DUP);// 0x1f 0x1f (top)
        when(contractA.get(3)).thenReturn(Opcodes.DUP);// 0x1f 0x1f 0x1f (top)
        when(contractA.get(4)).thenReturn(Opcodes.DUP);// 0x1f 0x1f 0x1f (top)
        when(contractA.get(5)).thenReturn(Opcodes.STOP_BAD);// 0x1f 0x1f 0x1f 0x1f 4 1 (top)

        ContractManager manager = mock(ContractManager.class);
        when(manager.contractExists(idA)).thenReturn(true);
        when(manager.getContract(idA)).thenReturn(contractA);

        VirtualMachineController controller = new VirtualMachineController(manager,
                new VMConfiguration(10, 10, 10));

        Stack result = controller.invoke(new Stack(10), idA);

        assertThat(result.getSize()).isEqualTo(6);
        assertThat(result.popCustom(6)).containsExactly(
                new Word(0x1f), new Word(0x1f), new Word(0x1f), new Word(0x1f),
                new Word(4), new Word(1)
        );
    }

    @Test
    public void test_PUSH_POP_DUP() {
        Word idA = new Word(0x00);
        Memory contractA = mock(Memory.class);
        when(contractA.get(0)).thenReturn(Opcodes.PUSH);
        when(contractA.get(1)).thenReturn(new Word(0x1f));//0x1f (top)
        when(contractA.get(2)).thenReturn(Opcodes.PUSH);
        when(contractA.get(3)).thenReturn(new Word(0x56));//0x1f 0x56 (top)
        when(contractA.get(4)).thenReturn(Opcodes.DUP);//0x1f 0x56 0x56 (top)
        when(contractA.get(5)).thenReturn(Opcodes.POP);//0x1f 0x56 (top)
        when(contractA.get(6)).thenReturn(Opcodes.STOP_GOOD);//0x1f 0x56 | 2 0x00 (top)

        ContractManager manager = mock(ContractManager.class);
        when(manager.contractExists(idA)).thenReturn(true);
        when(manager.getContract(idA)).thenReturn(contractA);

        VirtualMachineController controller = new VirtualMachineController(manager,
                new VMConfiguration(10, 10, 10));

        Stack result = controller.invoke(new Stack(10), idA);

        assertThat(result.getSize()).isEqualTo(4);

        assertThat(result.popCustom(4)).containsExactly(
                new Word(0x1f), new Word(0x56), new Word(2), new Word(0x00)
        );
    }

    @Test
    public void test_INVOKE_fromCode() {
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
    }

    @Test
    public void test_ADD() {
        Word idA = new Word(0x00);
        Memory contractA = mock(Memory.class);
        when(contractA.get(0)).thenReturn(Opcodes.PUSH);
        when(contractA.get(1)).thenReturn(new Word(123456));// 123456 (top)
        when(contractA.get(2)).thenReturn(Opcodes.PUSH);
        when(contractA.get(3)).thenReturn(new Word(-123456));// 123456 -123456 (top)
        when(contractA.get(4)).thenReturn(Opcodes.ADD);// 0 (top)
        when(contractA.get(5)).thenReturn(Opcodes.STOP_GOOD);// 0 0x01 0x00 (top)

        ContractManager manager = mock(ContractManager.class);
        when(manager.contractExists(idA)).thenReturn(true);
        when(manager.getContract(idA)).thenReturn(contractA);

        VirtualMachineController controller = new VirtualMachineController(manager,
                new VMConfiguration(10, 10, 10));

        Stack result = controller.invoke(new Stack(10), idA);

        assertThat(result.getSize()).isEqualTo(3);

        assertThat(result.popCustom(3)).containsExactly(
                new Word(0), new Word(1), new Word(0)
        );
    }

    @Test
    public void test_SUB() {
        Word idA = new Word(0x00);
        Memory contractA = mock(Memory.class);
        when(contractA.get(0)).thenReturn(Opcodes.PUSH);
        when(contractA.get(1)).thenReturn(new Word(100));// 100 (top)
        when(contractA.get(2)).thenReturn(Opcodes.PUSH);
        when(contractA.get(3)).thenReturn(new Word(10));// 100 10 (top)
        when(contractA.get(4)).thenReturn(Opcodes.SUB);// 90 (top)
        when(contractA.get(5)).thenReturn(Opcodes.STOP_GOOD);// 90 0x01 0x00 (top)

        ContractManager manager = mock(ContractManager.class);
        when(manager.contractExists(idA)).thenReturn(true);
        when(manager.getContract(idA)).thenReturn(contractA);

        VirtualMachineController controller = new VirtualMachineController(manager,
                new VMConfiguration(10, 10, 10));

        Stack result = controller.invoke(new Stack(10), idA);

        assertThat(result.getSize()).isEqualTo(3);

        assertThat(result.popCustom(3)).containsExactly(
                new Word(90), new Word(1), new Word(0)
        );
    }

    @Test
    public void test_DIV() {
        Word idA = new Word(0x00);
        Memory contractA = mock(Memory.class);
        when(contractA.get(0)).thenReturn(Opcodes.PUSH);
        when(contractA.get(1)).thenReturn(new Word(23));// 23 (top)
        when(contractA.get(2)).thenReturn(Opcodes.PUSH);
        when(contractA.get(3)).thenReturn(new Word(12));// 23 12 (top)
        when(contractA.get(4)).thenReturn(Opcodes.DIV);// 1 (top)
        when(contractA.get(5)).thenReturn(Opcodes.STOP_GOOD);// 1 0x01 0x00 (top)

        ContractManager manager = mock(ContractManager.class);
        when(manager.contractExists(idA)).thenReturn(true);
        when(manager.getContract(idA)).thenReturn(contractA);

        VirtualMachineController controller = new VirtualMachineController(manager,
                new VMConfiguration(10, 10, 10));

        Stack result = controller.invoke(new Stack(10), idA);

        assertThat(result.getSize()).isEqualTo(3);

        assertThat(result.popCustom(3)).containsExactly(
                new Word(1), new Word(1), new Word(0)
        );
    }

    @Test
    public void test_MOD() {
        Word idA = new Word(0x00);
        Memory contractA = mock(Memory.class);
        when(contractA.get(0)).thenReturn(Opcodes.PUSH);
        when(contractA.get(1)).thenReturn(new Word(23));// 23 (top)
        when(contractA.get(2)).thenReturn(Opcodes.PUSH);
        when(contractA.get(3)).thenReturn(new Word(12));// 23 12 (top)
        when(contractA.get(4)).thenReturn(Opcodes.MOD);// 11 (top)
        when(contractA.get(5)).thenReturn(Opcodes.STOP_GOOD);// 11 0x01 0x00 (top)

        ContractManager manager = mock(ContractManager.class);
        when(manager.contractExists(idA)).thenReturn(true);
        when(manager.getContract(idA)).thenReturn(contractA);

        VirtualMachineController controller = new VirtualMachineController(manager,
                new VMConfiguration(10, 10, 10));

        Stack result = controller.invoke(new Stack(10), idA);

        assertThat(result.getSize()).isEqualTo(3);

        assertThat(result.popCustom(3)).containsExactly(
                new Word(11), new Word(1), new Word(0)
        );
    }

    @Test
    public void test_DIV_byZero() {
        Word idA = new Word(0x00);
        Memory contractA = mock(Memory.class);
        when(contractA.get(0)).thenReturn(Opcodes.PUSH);
        when(contractA.get(1)).thenReturn(new Word(23));// 23 (top)
        when(contractA.get(2)).thenReturn(Opcodes.PUSH);
        when(contractA.get(3)).thenReturn(new Word(0));// 23 0 (top)
        when(contractA.get(4)).thenReturn(Opcodes.DIV);// 0x00 0x01 (top)

        ContractManager manager = mock(ContractManager.class);
        when(manager.contractExists(idA)).thenReturn(true);
        when(manager.getContract(idA)).thenReturn(contractA);

        VirtualMachineController controller = new VirtualMachineController(manager,
                new VMConfiguration(10, 10, 10));

        Stack result = controller.invoke(new Stack(10), idA);

        assertThat(result.getSize()).isEqualTo(2);

        assertThat(result.popCustom(2)).containsExactly(
                new Word(0), new Word(1)
        );
    }

    @Test
    public void test_POP_fromEmpty() {
        Word idA = new Word(0x00);
        Memory contractA = mock(Memory.class);
        when(contractA.get(0)).thenReturn(Opcodes.POP);//0 0x01 (top)

        ContractManager manager = mock(ContractManager.class);
        when(manager.contractExists(idA)).thenReturn(true);
        when(manager.getContract(idA)).thenReturn(contractA);

        VirtualMachineController controller = new VirtualMachineController(manager,
                new VMConfiguration(10, 10, 10));

        Stack result = controller.invoke(new Stack(10), idA);

        assertThat(result.getSize()).isEqualTo(2);

        assertThat(result.popCustom(2)).containsExactly(
                new Word(0), new Word(1)
        );
    }

    @Test
    public void test_DUP_fromEmpty() {
        Word idA = new Word(0x00);
        Memory contractA = mock(Memory.class);
        when(contractA.get(0)).thenReturn(Opcodes.DUP);//0 0x01 (top)

        ContractManager manager = mock(ContractManager.class);
        when(manager.contractExists(idA)).thenReturn(true);
        when(manager.getContract(idA)).thenReturn(contractA);

        VirtualMachineController controller = new VirtualMachineController(manager,
                new VMConfiguration(10, 10, 10));

        Stack result = controller.invoke(new Stack(10), idA);

        assertThat(result.getSize()).isEqualTo(2);

        assertThat(result.popCustom(2)).containsExactly(
                new Word(0), new Word(1)
        );
    }

    @Test
    public void test_SWAP_noValues() {
        Word idA = new Word(0x00);
        Memory contractA = mock(Memory.class);
        when(contractA.get(0)).thenReturn(Opcodes.SWAP);//0 0x01 (top)

        ContractManager manager = mock(ContractManager.class);
        when(manager.contractExists(idA)).thenReturn(true);
        when(manager.getContract(idA)).thenReturn(contractA);

        VirtualMachineController controller = new VirtualMachineController(manager,
                new VMConfiguration(10, 10, 10));

        Stack result = controller.invoke(new Stack(10), idA);

        assertThat(result.getSize()).isEqualTo(2);

        assertThat(result.popCustom(2)).containsExactly(
                new Word(0), new Word(1)
        );
    }

    @Test
    public void test_SWAP_oneValue() {
        Word idA = new Word(0x00);
        Memory contractA = mock(Memory.class);
        when(contractA.get(0)).thenReturn(Opcodes.PUSH);
        when(contractA.get(1)).thenReturn(new Word(12));// 12 (top)
        when(contractA.get(2)).thenReturn(Opcodes.SWAP);//0 0x01 (top)

        ContractManager manager = mock(ContractManager.class);
        when(manager.contractExists(idA)).thenReturn(true);
        when(manager.getContract(idA)).thenReturn(contractA);

        VirtualMachineController controller = new VirtualMachineController(manager,
                new VMConfiguration(10, 10, 10));

        Stack result = controller.invoke(new Stack(10), idA);

        assertThat(result.getSize()).isEqualTo(2);

        assertThat(result.popCustom(2)).containsExactly(
                new Word(0), new Word(1)
        );
    }

    @Test
    public void test_ADD_noValues() {
        Word idA = new Word(0x00);
        Memory contractA = mock(Memory.class);
        when(contractA.get(0)).thenReturn(Opcodes.ADD);//0 0x01 (top)

        ContractManager manager = mock(ContractManager.class);
        when(manager.contractExists(idA)).thenReturn(true);
        when(manager.getContract(idA)).thenReturn(contractA);

        VirtualMachineController controller = new VirtualMachineController(manager,
                new VMConfiguration(10, 10, 10));

        Stack result = controller.invoke(new Stack(10), idA);

        assertThat(result.getSize()).isEqualTo(2);

        assertThat(result.popCustom(2)).containsExactly(
                new Word(0), new Word(1)
        );
    }

    @Test
    public void test_ADD_oneValue() {
        Word idA = new Word(0x00);
        Memory contractA = mock(Memory.class);
        when(contractA.get(0)).thenReturn(Opcodes.PUSH);
        when(contractA.get(1)).thenReturn(new Word(12));// 12 (top)
        when(contractA.get(2)).thenReturn(Opcodes.SWAP);//0 0x01 (top)

        ContractManager manager = mock(ContractManager.class);
        when(manager.contractExists(idA)).thenReturn(true);
        when(manager.getContract(idA)).thenReturn(contractA);

        VirtualMachineController controller = new VirtualMachineController(manager,
                new VMConfiguration(10, 10, 10));

        Stack result = controller.invoke(new Stack(10), idA);

        assertThat(result.getSize()).isEqualTo(2);

        assertThat(result.popCustom(2)).containsExactly(
                new Word(0), new Word(1)
        );
    }

    @Test
    public void test_MOD_byZero() {
        Word idA = new Word(0x00);
        Memory contractA = mock(Memory.class);
        when(contractA.get(0)).thenReturn(Opcodes.PUSH);
        when(contractA.get(1)).thenReturn(new Word(23));// 23 (top)
        when(contractA.get(2)).thenReturn(Opcodes.PUSH);
        when(contractA.get(3)).thenReturn(new Word(0));// 23 0 (top)
        when(contractA.get(4)).thenReturn(Opcodes.MOD);// 0 1 (top)

        ContractManager manager = mock(ContractManager.class);
        when(manager.contractExists(idA)).thenReturn(true);
        when(manager.getContract(idA)).thenReturn(contractA);

        VirtualMachineController controller = new VirtualMachineController(manager,
                new VMConfiguration(10, 10, 10));

        Stack result = controller.invoke(new Stack(10), idA);

        assertThat(result.getSize()).isEqualTo(2);

        assertThat(result.popCustom(2)).containsExactly(
                new Word(0), new Word(1)
        );
    }

    @Test
    public void test_SWAP() {
        Word idA = new Word(0x00);
        Memory contractA = mock(Memory.class);
        when(contractA.get(0)).thenReturn(Opcodes.PUSH);
        when(contractA.get(1)).thenReturn(new Word(23));// 23 (top)
        when(contractA.get(2)).thenReturn(Opcodes.PUSH);
        when(contractA.get(3)).thenReturn(new Word(43));// 23 43 (top)
        when(contractA.get(4)).thenReturn(Opcodes.SWAP);// 43 23 (top)
        when(contractA.get(5)).thenReturn(Opcodes.STOP_GOOD);// 43 23 2 0 (top)

        ContractManager manager = mock(ContractManager.class);
        when(manager.contractExists(idA)).thenReturn(true);
        when(manager.getContract(idA)).thenReturn(contractA);

        VirtualMachineController controller = new VirtualMachineController(manager,
                new VMConfiguration(10, 10, 10));

        Stack result = controller.invoke(new Stack(10), idA);

        assertThat(result.getSize()).isEqualTo(4);

        assertThat(result.popCustom(4)).containsExactly(
                new Word(43), new Word(23), new Word(2), new Word(0)
        );
    }

}
