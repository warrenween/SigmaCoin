package ru.opensecreto.sigmacoin.vm;

import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BytecodeExecutorTest {

    @Test
    public void testInvokeNonExisting() {
        ContractManager manager = mock(ContractManager.class);
        when(manager.contractExists(new ContractID(new byte[]{0}))).thenReturn(false);

        VirtualMachineController controller = new VirtualMachineController(manager,
                new VMConfiguration(10, 1, 10, 10));

        Stack result = controller.invoke(new Stack(0), new ContractID(new byte[]{0}));

        assertThat(result.pop()).isEqualTo((byte) 0x01);
        assertThat(result.popShort()).isEqualTo((short) 0);
        assertThat(result.getSize()).isEqualTo(0);
    }

    @Test
    public void testInvokeGood() {
        ContractID idA = new ContractID(new byte[]{0});
        Memory contractA = mock(Memory.class);
        when(contractA.get(0)).thenReturn(Opcodes.STOP_GOOD);

        ContractManager manager = mock(ContractManager.class);
        when(manager.contractExists(idA)).thenReturn(true);
        when(manager.getContract(idA)).thenReturn(contractA);

        VirtualMachineController controller = new VirtualMachineController(manager,
                new VMConfiguration(10, 1, 10, 10));

        Stack result = controller.invoke(new Stack(0), idA);

        assertThat(result.pop()).isEqualTo((byte) 0x00);
        assertThat(result.popShort()).isEqualTo((short) 0);
    }

    @Test
    public void testInvokeBad() {
        ContractID idA = new ContractID(new byte[]{0});
        Memory contractA = mock(Memory.class);
        when(contractA.get(0)).thenReturn(Opcodes.STOP_BAD);

        ContractManager manager = mock(ContractManager.class);
        when(manager.contractExists(idA)).thenReturn(true);
        when(manager.getContract(idA)).thenReturn(contractA);

        VirtualMachineController controller = new VirtualMachineController(manager,
                new VMConfiguration(10, 1, 10, 10));

        Stack result = controller.invoke(new Stack(0), idA);

        assertThat(result.pop()).isEqualTo((byte) 0x01);
        assertThat(result.popShort()).isEqualTo((short) 0);
    }
}
