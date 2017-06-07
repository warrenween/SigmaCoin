package ru.opensecreto.sigmacoin.vm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

import static ru.opensecreto.sigmacoin.vm.Word.WORD_SIZE;

/**
 * Controls execution of bytecode. Creates frames, handles invocations.
 */
public class VirtualMachineController {

    private final static Logger LOGGER = LoggerFactory.getLogger(VirtualMachineController.class);

    private final ContractManager contractManager;
    private final VMConfiguration configuration;

    private int currentCallStackDepth = 0;

    public VirtualMachineController(ContractManager contractManager, VMConfiguration configuration) {
        this.contractManager = contractManager;
        this.configuration = configuration;
    }

    public StopType execute(byte[] invokationData, ContractAddress contractAddress) {
        return execute(invokationData, contractAddress.id);
    }

    public StopType execute(byte[] invocationData, Word contractID) {
        if (invocationData.length % WORD_SIZE != 0)
            throw new IllegalArgumentException("invocationData length must be multiple of 32");
        Stack stack = new Stack();

        for (int i = 0; i < invocationData.length / WORD_SIZE; i++) {
            stack.push(new Word(Arrays.copyOfRange(invocationData, i * WORD_SIZE, i * WORD_SIZE + WORD_SIZE)));
        }

        return invoke(stack, contractID).stopType;
    }

    public ResultFrame invoke(Stack stack, Word contractID) {
        if ((!contractManager.contractExists(contractID)) | (currentCallStackDepth == configuration.maxCallDepth)) {
            return new ResultFrame(new Stack(), StopType.BAD);
        } else {
            ExecutionFrame executionFrame = new ExecutionFrame(contractManager.getContract(contractID), stack, contractID);
            BytecodeExecutor executor = new BytecodeExecutor(configuration, executionFrame, this);

            currentCallStackDepth++;
            ResultFrame result = executor.run();
            currentCallStackDepth--;

            return result;
        }
    }
}
