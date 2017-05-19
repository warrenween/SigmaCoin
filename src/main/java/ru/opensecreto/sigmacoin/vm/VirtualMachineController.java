package ru.opensecreto.sigmacoin.vm;

/**
 * Controls execution of bytecode. Creates frames, handles invocations.
 */
public class VirtualMachineController {

    private final Memory mainContract;
    private final ContractManager contractManager;
    private final VMConfiguration configuration;

    private int currentCallStackDepth;

    public VirtualMachineController(Memory mainContract, ContractManager contractManager, VMConfiguration configuration) {
        this.mainContract = mainContract;
        this.contractManager = contractManager;
        this.configuration = configuration;
    }

    public void execute(byte[] invocationData, ContractID contractID) {
        Stack stack = new Stack(configuration.frameMaxStackSize);
        for (int i = 0; i < invocationData.length; i++) {
            stack.push(invocationData[i]);
        }
        invoke(stack, contractID);
    }

    public Stack invoke(Stack stack, ContractID contractID) {
        if (!contractManager.contractExists(contractID)) {
            stack.push((byte) 0x01);
            return stack;
        }

        Frame frame = new Frame(contractManager.getContract(contractID), stack, contractID);

        BytecodeExecutor executor = new BytecodeExecutor(configuration, frame, this);
        currentCallStackDepth++;

        if (currentCallStackDepth == configuration.maxCallDepth) {
            Stack resultStack = new Stack(configuration.frameMaxStackSize);
            stack.pushShort((short) 0);
            stack.push((byte) 0x01);
            return stack;
        } else {
            Stack result = executor.run();
            currentCallStackDepth--;
            return result;
        }
    }
}
