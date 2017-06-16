package ru.opensecreto.sigmacoin.vm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static ru.opensecreto.sigmacoin.vm.Word.WORD_SIZE;

/**
 * Controls execution of bytecode. Creates frames, handles invocations.
 */
public class VirtualMachineController {

    private final static Logger LOGGER = LoggerFactory.getLogger(VirtualMachineController.class);

    private final AccountManager accountManager;
    private final VMConfiguration configuration;

    private int currentCallStackDepth = 0;

    public VirtualMachineController(AccountManager accountManager, VMConfiguration configuration) {
        this.accountManager = checkNotNull(accountManager);
        this.configuration = checkNotNull(configuration);
    }

    public StopType execute(byte[] invocationData, AccountAddress accountAddress)
            throws IllegalArgumentException {
        checkNotNull(invocationData);
        checkNotNull(accountAddress);
        checkArgument(invocationData.length % WORD_SIZE == 0);
        Stack stack = new Stack();

        for (int i = 0; i < invocationData.length / WORD_SIZE; i++) {
            stack.push(new Word(Arrays.copyOfRange(invocationData, i * WORD_SIZE, i * WORD_SIZE + WORD_SIZE)));
        }

        return invoke(stack, accountAddress).stopType;
    }

    public ResultFrame invoke(Stack stack, AccountAddress accountAddress) {
        checkNotNull(stack);
        checkNotNull(accountAddress);
        if ((!accountManager.accountExists(accountAddress)) | (currentCallStackDepth == configuration.maxCallDepth)) {
            return new ResultFrame(new Stack(), StopType.BAD);
        } else {
            ExecutionFrame executionFrame = new ExecutionFrame(accountManager.getAccount(accountAddress).memory, stack, accountAddress);
            BytecodeExecutor executor = new BytecodeExecutor(configuration, executionFrame, this);

            currentCallStackDepth++;
            ResultFrame result = executor.run();
            currentCallStackDepth--;

            return result;
        }
    }
}
