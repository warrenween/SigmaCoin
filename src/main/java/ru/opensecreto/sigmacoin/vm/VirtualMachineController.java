package ru.opensecreto.sigmacoin.vm;

import java.util.Arrays;

import static ru.opensecreto.sigmacoin.vm.Word.WORD_SIZE;

/**
 * Controls execution of bytecode. Creates frames, handles invocations.
 */
public class VirtualMachineController {

    private final ContractManager contractManager;
    private final VMConfiguration configuration;

    private int currentCallStackDepth = 0;

    public VirtualMachineController(ContractManager contractManager, VMConfiguration configuration) {
        this.contractManager = contractManager;
        this.configuration = configuration;
    }

    public void execute(byte[] invocationData, Word contractID) {
        if (invocationData.length % WORD_SIZE != 0)
            throw new IllegalArgumentException("invocationData length must be multiple of 32");
        Stack stack = new Stack();

        for (int i = 0; i < invocationData.length / WORD_SIZE; i++) {
            stack.push(new Word(Arrays.copyOfRange(invocationData, i * WORD_SIZE, i * WORD_SIZE + WORD_SIZE)));
        }

        invoke(stack, contractID);
    }

    public Stack invoke(Stack stack, Word contractID) {
        if ((!contractManager.contractExists(contractID)) | (currentCallStackDepth == configuration.maxCallDepth)) {
            Stack resultStack = new Stack();
            resultStack.push(new Word((short) 0));
            resultStack.push(new Word((byte) 0x01));
            return resultStack;
        } else {
            Frame frame = new Frame(contractManager.getContract(contractID), stack, contractID);
            BytecodeExecutor executor = new BytecodeExecutor(configuration, frame, this);

            currentCallStackDepth++;
            Stack result = executor.run();
            currentCallStackDepth--;

            return result;
        }
    }
}
