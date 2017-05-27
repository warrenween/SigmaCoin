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
                new VMConfiguration(10, 3, 10)
        ) {
            @Override
            public Stack invoke(Stack stack, Word contractID) {
                Assertions.assertThat(stack.getSize()).isEqualTo(2);
                Assertions.assertThat(stack.pop()).isEqualTo(new Word(0x9876));
                Assertions.assertThat(stack.pop()).isEqualTo(new Word(0x1234));
                Assertions.assertThat(stack.getSize()).isEqualTo(0);
                return null;
            }
        };

        Word a = new Word(0x1234);//stack bottom
        Word b = new Word(0x9876);//stack top

        byte[] data = new byte[Word.WORD_SIZE * 2];
        System.arraycopy(a.getData(), 0, data, 0, Word.WORD_SIZE);
        System.arraycopy(b.getData(), 0, data, Word.WORD_SIZE, Word.WORD_SIZE);

        controller.execute(data, new Word(0));
    }

}
