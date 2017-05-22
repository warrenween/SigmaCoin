package ru.opensecreto.sigmacoin.vm;

import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;

import static org.mockito.Mockito.mock;

public class VirtualMachineControllerTest {

    @Test
    public void testExecutionCorrectStack() {
        ContractManager manager = mock(ContractManager.class);

        VirtualMachineController controller = new VirtualMachineController(
                manager,
                new VMConfiguration(10, 2, 3, 10)
        ) {
            @Override
            public Stack invoke(Stack stack, ContractID contractID) {
                Assertions.assertThat(stack.popShort()).isEqualTo((short) 0x1234);
                return null;
            }
        };

        controller.execute(new byte[]{0x12, 0x34}, new ContractID(new byte[]{0x00, 0x00}));

    }

}
