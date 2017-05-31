package ru.opensecreto.sigmacoin.vm;

import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static ru.opensecreto.sigmacoin.vm.Opcodes.*;

public class BytecodeExecutorTest {

    @Test
    public void test_INVOKE_nonExistingContract() {
        ContractManager manager = mock(ContractManager.class);
        when(manager.contractExists(new Word(0))).thenReturn(false);

        VirtualMachineController controller = new VirtualMachineController(manager,
                new VMConfiguration(10, 10));

        Stack result = controller.invoke(new Stack(), new Word(0));

        assertThat(result.getSize()).isEqualTo(2);
        assertThat(result.popCustom(2)).containsExactly(
                new Word(0), new Word(1)
        );
    }

    @Test
    public void test_INVOKE_good() {
        Word idA = new Word(0);
        Memory contractA = new SimpleTestMemory() {{
            set(0, STOP_GOOD);// 0 0x00 (top)
        }};

        ContractManager manager = mock(ContractManager.class);
        when(manager.contractExists(idA)).thenReturn(true);
        when(manager.getContract(idA)).thenReturn(contractA);

        VirtualMachineController controller = new VirtualMachineController(manager,
                new VMConfiguration(10, 10));

        Stack result = controller.invoke(new Stack(), idA);

        assertThat(result.getSize()).isEqualTo(2);
        assertThat(result.popCustom(2)).containsExactly(
                new Word(0), new Word(0)
        );
    }

    @Test
    public void test_INVOKE_bad() {
        Word idA = new Word(0);
        Memory contractA = new SimpleTestMemory() {{
            set(0, STOP_BAD);// 0 0x01 (top)
        }};

        ContractManager manager = mock(ContractManager.class);
        when(manager.contractExists(idA)).thenReturn(true);
        when(manager.getContract(idA)).thenReturn(contractA);

        VirtualMachineController controller = new VirtualMachineController(manager,
                new VMConfiguration(10, 10));

        Stack result = controller.invoke(new Stack(), idA);

        assertThat(result.getSize()).isEqualTo(2);
        assertThat(result.popCustom(2)).containsExactly(
                new Word(0), new Word(0x01)
        );
    }

    @Test
    public void test_INVOKE_returnsCorrectStack() {
        Word idA = new Word(0);
        Memory contractA = new SimpleTestMemory() {{
            set(0, PUSH);
            set(1, new Word(0x1f));// 0x1f (top)
            set(2, DUP);// 0x1f 0x1f (top)
            set(3, DUP);// 0x1f 0x1f 0x1f (top)
            set(4, DUP);// 0x1f 0x1f 0x1f (top)
            set(5, STOP_BAD);// 0x1f 0x1f 0x1f 0x1f 4 1 (top)
        }};

        ContractManager manager = mock(ContractManager.class);
        when(manager.contractExists(idA)).thenReturn(true);
        when(manager.getContract(idA)).thenReturn(contractA);

        VirtualMachineController controller = new VirtualMachineController(manager,
                new VMConfiguration(10, 10));

        Stack result = controller.invoke(new Stack(), idA);

        assertThat(result.getSize()).isEqualTo(6);
        assertThat(result.popCustom(6)).containsExactly(
                new Word(0x1f), new Word(0x1f), new Word(0x1f), new Word(0x1f),
                new Word(4), new Word(1)
        );
    }

    @Test
    public void test_PUSH_POP_DUP() {
        Word idA = new Word(0x00);
        Memory contractA = new SimpleTestMemory() {{
            set(0, PUSH);
            set(1, new Word(0x1f));//0x1f (top)
            set(2, PUSH);
            set(3, new Word(0x56));//0x1f 0x56 (top)
            set(4, DUP);//0x1f 0x56 0x56 (top)
            set(5, POP);//0x1f 0x56 (top)
            set(6, STOP_GOOD);//0x1f 0x56 | 2 0x00 (top)
        }};

        ContractManager manager = mock(ContractManager.class);
        when(manager.contractExists(idA)).thenReturn(true);
        when(manager.getContract(idA)).thenReturn(contractA);

        VirtualMachineController controller = new VirtualMachineController(manager,
                new VMConfiguration(10, 10));

        Stack result = controller.invoke(new Stack(), idA);

        assertThat(result.getSize()).isEqualTo(4);

        assertThat(result.popCustom(4)).containsExactly(
                new Word(0x1f), new Word(0x56), new Word(2), new Word(0x00)
        );
    }

    @Test
    public void test_INVOKE_fromCode() {
        Word idA = new Word(0x00);
        Memory contractA = new SimpleTestMemory() {{
            set(0, PUSH);
            set(1, new Word(0x12));// 0x12 (top)
            set(2, PUSH);
            set(3, new Word(0x34));// 0x12 0x34 (top)
            set(4, PUSH);
            set(5, new Word(0x56));// 0x12 0x34 0x56 (top)
            set(6, PUSH);
            set(7, new Word(0x03));// 0x12 0x34 0x56 | 0x03 (top)
            set(8, PUSH);
            set(9, new Word(0x01));// 0x12 0x34 0x56 | 0x03 | 0x01 (top)
            set(10, INVOKE);// 0x12 0x34 0x56 0x56 0x1f 0x05 0x01 (top)
            set(11, STOP_GOOD); // 0x12 0x34 0x56 0x56 0x1f 0x05 0x01 | 0x07 0x00 (top)
        }};

        Word idB = new Word(0x01);
        Memory contractB = new SimpleTestMemory() {{
            // 0x12 0x34 0x56 (top)
            set(0, DUP);// 0x12 0x34 0x56 0x56 (top)
            set(1, PUSH);
            set(2, new Word(0x1f));// 0x12 0x34 0x56 0x56 0x1f (top)
            set(3, STOP_BAD);// 0x12 0x34 0x56 0x56 0x1f | 0x05 | 0x01 (top)
        }};

        ContractManager manager = mock(ContractManager.class);
        when(manager.contractExists(idA)).thenReturn(true);
        when(manager.getContract(idA)).thenReturn(contractA);
        when(manager.contractExists(idB)).thenReturn(true);
        when(manager.getContract(idB)).thenReturn(contractB);

        VirtualMachineController controller = new VirtualMachineController(manager,
                new VMConfiguration(10, 10));

        Stack result = controller.invoke(new Stack(), idA);

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
        Memory contractA = new SimpleTestMemory() {{
            set(0, PUSH);
            set(1, new Word(123456));// 123456 (top)
            set(2, PUSH);
            set(3, new Word(-123456));// 123456 -123456 (top)
            set(4, ADD);// 0 (top)
            set(5, STOP_GOOD);// 0 0x01 0x00 (top)
        }};

        ContractManager manager = mock(ContractManager.class);
        when(manager.contractExists(idA)).thenReturn(true);
        when(manager.getContract(idA)).thenReturn(contractA);

        VirtualMachineController controller = new VirtualMachineController(manager,
                new VMConfiguration(10, 10));

        Stack result = controller.invoke(new Stack(), idA);

        assertThat(result.getSize()).isEqualTo(3);

        assertThat(result.popCustom(3)).containsExactly(
                new Word(0), new Word(1), new Word(0)
        );
    }

    @Test
    public void test_SUB() {
        Word idA = new Word(0x00);
        Memory contractA = new SimpleTestMemory() {{
            set(0, PUSH);
            set(1, new Word(100));// 100 (top)
            set(2, PUSH);
            set(3, new Word(10));// 100 10 (top)
            set(4, SUB);// 90 (top)
            set(5, STOP_GOOD);// 90 0x01 0x00 (top)
        }};

        ContractManager manager = mock(ContractManager.class);
        when(manager.contractExists(idA)).thenReturn(true);
        when(manager.getContract(idA)).thenReturn(contractA);

        VirtualMachineController controller = new VirtualMachineController(manager,
                new VMConfiguration(10, 10));

        Stack result = controller.invoke(new Stack(), idA);

        assertThat(result.getSize()).isEqualTo(3);

        assertThat(result.popCustom(3)).containsExactly(
                new Word(90), new Word(1), new Word(0)
        );
    }

    @Test
    public void test_SUB_noValues() {
        Word idA = new Word(0x00);
        Memory contractA = new SimpleTestMemory() {{
            set(0, SUB);
        }};

        ContractManager manager = mock(ContractManager.class);
        when(manager.contractExists(idA)).thenReturn(true);
        when(manager.getContract(idA)).thenReturn(contractA);

        VirtualMachineController controller = new VirtualMachineController(manager,
                new VMConfiguration(10, 10));

        Stack result = controller.invoke(new Stack(), idA);

        assertThat(result.getSize()).isEqualTo(2);

        assertThat(result.popCustom(2)).containsExactly(
                new Word(0), new Word(1)
        );
    }

    @Test
    public void test_SUB_oneValues() {
        Word idA = new Word(0x00);
        Memory contractA = new SimpleTestMemory() {{
            set(0, PUSH);
            set(1, new Word(100));// 100 (top)
            set(2, SUB);// 0 0x01 (top)
        }};

        ContractManager manager = mock(ContractManager.class);
        when(manager.contractExists(idA)).thenReturn(true);
        when(manager.getContract(idA)).thenReturn(contractA);

        VirtualMachineController controller = new VirtualMachineController(manager,
                new VMConfiguration(10, 10));

        Stack result = controller.invoke(new Stack(), idA);

        assertThat(result.getSize()).isEqualTo(2);

        assertThat(result.popCustom(2)).containsExactly(
                new Word(0), new Word(0x01)
        );
    }

    @Test
    public void test_DIV() {
        Word idA = new Word(0x00);
        Memory contractA = new SimpleTestMemory() {{
            set(0, PUSH);
            set(1, new Word(23));// 23 (top)
            set(2, PUSH);
            set(3, new Word(12));// 23 12 (top)
            set(4, DIV);// 1 (top)
            set(5, STOP_GOOD);// 1 0x01 0x00 (top)
        }};

        ContractManager manager = mock(ContractManager.class);
        when(manager.contractExists(idA)).thenReturn(true);
        when(manager.getContract(idA)).thenReturn(contractA);

        VirtualMachineController controller = new VirtualMachineController(manager,
                new VMConfiguration(10, 10));

        Stack result = controller.invoke(new Stack(), idA);

        assertThat(result.getSize()).isEqualTo(3);

        assertThat(result.popCustom(3)).containsExactly(
                new Word(1), new Word(1), new Word(0)
        );
    }

    @Test
    public void test_DIV_noValues() {
        Word idA = new Word(0x00);
        Memory contractA = new SimpleTestMemory() {{
            set(0, DIV);// 0 0x01 (top)
        }};

        ContractManager manager = mock(ContractManager.class);
        when(manager.contractExists(idA)).thenReturn(true);
        when(manager.getContract(idA)).thenReturn(contractA);

        VirtualMachineController controller = new VirtualMachineController(manager,
                new VMConfiguration(10, 10));

        Stack result = controller.invoke(new Stack(), idA);

        assertThat(result.getSize()).isEqualTo(2);

        assertThat(result.popCustom(2)).containsExactly(
                new Word(0), new Word(0x01)
        );
    }

    @Test
    public void test_DIV_oneValue() {
        Word idA = new Word(0x00);
        Memory contractA = new SimpleTestMemory() {{
            set(0, PUSH);
            set(1, new Word(23));// 23 (top)
            set(2, DIV);// 0 0x01 (top)
        }};

        ContractManager manager = mock(ContractManager.class);
        when(manager.contractExists(idA)).thenReturn(true);
        when(manager.getContract(idA)).thenReturn(contractA);

        VirtualMachineController controller = new VirtualMachineController(manager,
                new VMConfiguration(10, 10));

        Stack result = controller.invoke(new Stack(), idA);

        assertThat(result.getSize()).isEqualTo(2);

        assertThat(result.popCustom(2)).containsExactly(
                new Word(0), new Word(0x01)
        );
    }

    @Test
    public void test_MOD_noValues() {
        Word idA = new Word(0x00);
        Memory contractA = new SimpleTestMemory() {{
            set(0, MOD);// 0 0x01 (top)
        }};

        ContractManager manager = mock(ContractManager.class);
        when(manager.contractExists(idA)).thenReturn(true);
        when(manager.getContract(idA)).thenReturn(contractA);

        VirtualMachineController controller = new VirtualMachineController(manager,
                new VMConfiguration(10, 10));

        Stack result = controller.invoke(new Stack(), idA);

        assertThat(result.getSize()).isEqualTo(2);

        assertThat(result.popCustom(2)).containsExactly(
                new Word(0), new Word(0x01)
        );
    }

    @Test
    public void test_MOD_oneValue() {
        Word idA = new Word(0x00);
        Memory contractA = new SimpleTestMemory() {{
            set(0, PUSH);
            set(1, new Word(23));// 23 (top)
            set(2, MOD);// 0 0x01 (top)
        }};

        ContractManager manager = mock(ContractManager.class);
        when(manager.contractExists(idA)).thenReturn(true);
        when(manager.getContract(idA)).thenReturn(contractA);

        VirtualMachineController controller = new VirtualMachineController(manager,
                new VMConfiguration(10, 10));

        Stack result = controller.invoke(new Stack(), idA);

        assertThat(result.getSize()).isEqualTo(2);

        assertThat(result.popCustom(2)).containsExactly(
                new Word(0), new Word(0x01)
        );
    }

    @Test
    public void test_MOD() {
        Word idA = new Word(0x00);
        Memory contractA = new SimpleTestMemory() {{
            set(0, PUSH);
            set(1, new Word(23));// 23 (top)
            set(2, PUSH);
            set(3, new Word(12));// 23 12 (top)
            set(4, MOD);// 11 (top)
            set(5, STOP_GOOD);// 11 0x01 0x00 (top)
        }};

        ContractManager manager = mock(ContractManager.class);
        when(manager.contractExists(idA)).thenReturn(true);
        when(manager.getContract(idA)).thenReturn(contractA);

        VirtualMachineController controller = new VirtualMachineController(manager,
                new VMConfiguration(10, 10));

        Stack result = controller.invoke(new Stack(), idA);

        assertThat(result.getSize()).isEqualTo(3);

        assertThat(result.popCustom(3)).containsExactly(
                new Word(11), new Word(1), new Word(0)
        );
    }

    @Test
    public void test_DIV_byZero() {
        Word idA = new Word(0x00);
        Memory contractA = new SimpleTestMemory() {{
            set(0, PUSH);
            set(1, new Word(23));// 23 (top)
            set(2, PUSH);
            set(3, new Word(0));// 23 0 (top)
            set(4, DIV);// 0x00 0x01 (top)
        }};

        ContractManager manager = mock(ContractManager.class);
        when(manager.contractExists(idA)).thenReturn(true);
        when(manager.getContract(idA)).thenReturn(contractA);

        VirtualMachineController controller = new VirtualMachineController(manager,
                new VMConfiguration(10, 10));

        Stack result = controller.invoke(new Stack(), idA);

        assertThat(result.getSize()).isEqualTo(2);

        assertThat(result.popCustom(2)).containsExactly(
                new Word(0), new Word(1)
        );
    }

    @Test
    public void test_POP_fromEmpty() {
        Word idA = new Word(0x00);
        Memory contractA = new SimpleTestMemory() {{
            set(0, POP);//0 0x01 (top)
        }};

        ContractManager manager = mock(ContractManager.class);
        when(manager.contractExists(idA)).thenReturn(true);
        when(manager.getContract(idA)).thenReturn(contractA);

        VirtualMachineController controller = new VirtualMachineController(manager,
                new VMConfiguration(10, 10));

        Stack result = controller.invoke(new Stack(), idA);

        assertThat(result.getSize()).isEqualTo(2);

        assertThat(result.popCustom(2)).containsExactly(
                new Word(0), new Word(1)
        );
    }

    @Test
    public void test_DUP_fromEmpty() {
        Word idA = new Word(0x00);
        Memory contractA = new SimpleTestMemory() {{
            set(0, DUP);//0 0x01 (top)
        }};

        ContractManager manager = mock(ContractManager.class);
        when(manager.contractExists(idA)).thenReturn(true);
        when(manager.getContract(idA)).thenReturn(contractA);

        VirtualMachineController controller = new VirtualMachineController(manager,
                new VMConfiguration(10, 10));

        Stack result = controller.invoke(new Stack(), idA);

        assertThat(result.getSize()).isEqualTo(2);

        assertThat(result.popCustom(2)).containsExactly(
                new Word(0), new Word(1)
        );
    }

    @Test
    public void test_SWAP_noValues() {
        Word idA = new Word(0x00);
        Memory contractA = new SimpleTestMemory() {{
            set(0, SWAP);//0 0x01 (top)
        }};

        ContractManager manager = mock(ContractManager.class);
        when(manager.contractExists(idA)).thenReturn(true);
        when(manager.getContract(idA)).thenReturn(contractA);

        VirtualMachineController controller = new VirtualMachineController(manager,
                new VMConfiguration(10, 10));

        Stack result = controller.invoke(new Stack(), idA);

        assertThat(result.getSize()).isEqualTo(2);

        assertThat(result.popCustom(2)).containsExactly(
                new Word(0), new Word(1)
        );
    }

    @Test
    public void test_SWAP_oneValue() {
        Word idA = new Word(0x00);
        Memory contractA = new SimpleTestMemory() {{
            set(0, PUSH);
            set(1, new Word(12));// 12 (top)
            set(2, SWAP);//0 0x01 (top)
        }};

        ContractManager manager = mock(ContractManager.class);
        when(manager.contractExists(idA)).thenReturn(true);
        when(manager.getContract(idA)).thenReturn(contractA);

        VirtualMachineController controller = new VirtualMachineController(manager,
                new VMConfiguration(10, 10));

        Stack result = controller.invoke(new Stack(), idA);

        assertThat(result.getSize()).isEqualTo(2);

        assertThat(result.popCustom(2)).containsExactly(
                new Word(0), new Word(1)
        );
    }

    @Test
    public void test_ADD_noValues() {
        Word idA = new Word(0x00);
        Memory contractA = new SimpleTestMemory() {{
            set(0, ADD);//0 0x01 (top)
        }};

        ContractManager manager = mock(ContractManager.class);
        when(manager.contractExists(idA)).thenReturn(true);
        when(manager.getContract(idA)).thenReturn(contractA);

        VirtualMachineController controller = new VirtualMachineController(manager,
                new VMConfiguration(10, 10));

        Stack result = controller.invoke(new Stack(), idA);

        assertThat(result.getSize()).isEqualTo(2);

        assertThat(result.popCustom(2)).containsExactly(
                new Word(0), new Word(1)
        );
    }

    @Test
    public void test_ADD_oneValue() {
        Word idA = new Word(0x00);
        Memory contractA = new SimpleTestMemory() {{
            set(0, PUSH);
            set(1, new Word(12));// 12 (top)
            set(2, SWAP);//0 0x01 (top)
        }};

        ContractManager manager = mock(ContractManager.class);
        when(manager.contractExists(idA)).thenReturn(true);
        when(manager.getContract(idA)).thenReturn(contractA);

        VirtualMachineController controller = new VirtualMachineController(manager,
                new VMConfiguration(10, 10));

        Stack result = controller.invoke(new Stack(), idA);

        assertThat(result.getSize()).isEqualTo(2);

        assertThat(result.popCustom(2)).containsExactly(
                new Word(0), new Word(1)
        );
    }

    @Test
    public void test_MOD_byZero() {
        Word idA = new Word(0x00);
        Memory contractA = new SimpleTestMemory() {{
            set(0, PUSH);
            set(1, new Word(23));// 23 (top)
            set(2, PUSH);
            set(3, new Word(0));// 23 0 (top)
            set(4, MOD);// 0 1 (top)
        }};

        ContractManager manager = mock(ContractManager.class);
        when(manager.contractExists(idA)).thenReturn(true);
        when(manager.getContract(idA)).thenReturn(contractA);

        VirtualMachineController controller = new VirtualMachineController(manager,
                new VMConfiguration(10, 10));

        Stack result = controller.invoke(new Stack(), idA);

        assertThat(result.getSize()).isEqualTo(2);

        assertThat(result.popCustom(2)).containsExactly(
                new Word(0), new Word(1)
        );
    }

    @Test
    public void test_SWAP() {
        Word idA = new Word(0x00);
        Memory contractA = new SimpleTestMemory() {{
            set(0, PUSH);
            set(1, new Word(23));// 23 (top)
            set(2, PUSH);
            set(3, new Word(43));// 23 43 (top)
            set(4, SWAP);// 43 23 (top)
            set(5, STOP_GOOD);// 43 23 2 0 (top)
        }};

        ContractManager manager = mock(ContractManager.class);
        when(manager.contractExists(idA)).thenReturn(true);
        when(manager.getContract(idA)).thenReturn(contractA);

        VirtualMachineController controller = new VirtualMachineController(manager,
                new VMConfiguration(10, 10));

        Stack result = controller.invoke(new Stack(), idA);

        assertThat(result.getSize()).isEqualTo(4);

        assertThat(result.popCustom(4)).containsExactly(
                new Word(43), new Word(23), new Word(2), new Word(0)
        );
    }

    @Test
    public void test_GET() {
        Word idA = new Word(0x00);
        Memory contractA = new SimpleTestMemory() {{
            set(0, PUSH);
            set(1, new Word(23));// 23 (top)
            set(2, GET);// 1234 (top)
            set(3, STOP_GOOD);// 1234 1 0 (top)

            set(23, new Word(1234));
        }};

        ContractManager manager = mock(ContractManager.class);
        when(manager.contractExists(idA)).thenReturn(true);
        when(manager.getContract(idA)).thenReturn(contractA);

        VirtualMachineController controller = new VirtualMachineController(manager,
                new VMConfiguration(10, 10));

        Stack result = controller.invoke(new Stack(), idA);

        assertThat(result.getSize()).isEqualTo(3);

        assertThat(result.popCustom(3)).containsExactly(
                new Word(1234), new Word(1), new Word(0)
        );
    }

    @Test
    public void test_GET_noValues() {
        Word idA = new Word(0x00);
        Memory contractA = new SimpleTestMemory() {{
            set(0, GET);// 0 0x01 (top)
            set(1, STOP_GOOD);// never executed
        }};

        ContractManager manager = mock(ContractManager.class);
        when(manager.contractExists(idA)).thenReturn(true);
        when(manager.getContract(idA)).thenReturn(contractA);

        VirtualMachineController controller = new VirtualMachineController(manager,
                new VMConfiguration(10, 10));

        Stack result = controller.invoke(new Stack(), idA);

        assertThat(result.getSize()).isEqualTo(3);

        assertThat(result.popCustom(3)).containsExactly(
                new Word(0), new Word(0x01)
        );
    }

    @Test
    public void test_GET_negativeValue() {
        Word idA = new Word(0x00);
        Memory contractA = new SimpleTestMemory() {{
            set(0, PUSH);
            set(1, new Word(-23));
            set(2, GET);// 0 0x01 (top)
            set(3, STOP_GOOD);// never executed
        }};

        ContractManager manager = mock(ContractManager.class);
        when(manager.contractExists(idA)).thenReturn(true);
        when(manager.getContract(idA)).thenReturn(contractA);

        VirtualMachineController controller = new VirtualMachineController(manager,
                new VMConfiguration(10, 10));

        Stack result = controller.invoke(new Stack(), idA);

        assertThat(result.getSize()).isEqualTo(2);

        assertThat(result.popCustom(2)).containsExactly(
                new Word(0), new Word(0x01)
        );
    }

    @Test
    public void test_GET_bigValue() {
        Word idA = new Word(0x00);
        Memory contractA = new SimpleTestMemory() {{
            set(0, PUSH);
            set(1, new Word(99999999));
            set(2, GET);// 0 0x01 (top)
            set(3, STOP_GOOD);// never executed
        }};

        ContractManager manager = mock(ContractManager.class);
        when(manager.contractExists(idA)).thenReturn(true);
        when(manager.getContract(idA)).thenReturn(contractA);

        VirtualMachineController controller = new VirtualMachineController(manager,
                new VMConfiguration(10, 10));

        Stack result = controller.invoke(new Stack(), idA);

        assertThat(result.getSize()).isEqualTo(2);

        assertThat(result.popCustom(2)).containsExactly(
                new Word(0), new Word(0x01)
        );
    }

}
