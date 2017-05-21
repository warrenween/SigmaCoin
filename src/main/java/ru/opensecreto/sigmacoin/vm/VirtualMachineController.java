package ru.opensecreto.sigmacoin.vm;

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

    public void execute(byte[] invocationData, ContractID contractID) {
        Stack stack = new Stack(configuration.stackSize);
        stack.pushCustom(invocationData);
        invoke(stack, contractID);
    }

    public Stack invoke(Stack stack, ContractID contractID) {
        if (!contractManager.contractExists(contractID)) {
            Stack resultStack = new Stack(configuration.stackSize);
            resultStack.pushShort((short) 0);
            resultStack.push((byte) 0x01);
            return resultStack;
        }

        if (currentCallStackDepth == configuration.maxCallDepth) {
            Stack resultStack = new Stack(configuration.stackSize);
            resultStack.pushShort((short) 0);
            resultStack.push((byte) 0x01);
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
