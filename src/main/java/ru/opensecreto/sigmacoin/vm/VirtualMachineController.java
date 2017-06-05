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

    public StopType execute(byte[] invocationData, Word contractID) {
        if (invocationData.length % WORD_SIZE != 0)
            throw new IllegalArgumentException("invocationData length must be multiple of 32");
        Stack stack = new Stack();

        for (int i = 0; i < invocationData.length / WORD_SIZE; i++) {
            stack.push(new Word(Arrays.copyOfRange(invocationData, i * WORD_SIZE, i * WORD_SIZE + WORD_SIZE)));
        }

        try {
            invoke(stack, contractID);
        } catch (AbortException e) {
            LOGGER.info("Execution of {} was aborted.", contractID, e);
            return StopType.REVERT;
        }
        return StopType.COMMIT;
    }

    public Stack invoke(Stack stack, Word contractID) throws AbortException {
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
