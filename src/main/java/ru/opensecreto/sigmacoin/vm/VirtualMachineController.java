package ru.opensecreto.sigmacoin.vm;

/**
 * Controls execution of bytecode. Creates frames, handles invocations.
 */
public class VirtualMachineController {

    private final Memory mainContract;
    private final ContractManager contractManager;
    private final int stackSize;

    public VirtualMachineController(Memory mainContract, ContractManager contractManager, int stackSize) {
        this.mainContract = mainContract;
        this.contractManager = contractManager;
        this.stackSize = stackSize;
    }

    public void execute(byte[] invocationData, ContractID contractID) {
        Stack stack = new Stack(stackSize);
        for (int i = 0; i < invocationData.length; i++) {
            stack.push(invocationData[i]);
        }
        invoke(stack, contractID);
    }

    public Stack invoke(Stack stack, ContractID contractID) {
        Frame frame = new Frame(contractManager.getContract(contractID), stack, contractID);

        BytecodeExecutor executor = new BytecodeExecutor(frame, this);
        return executor.run();
    }
}
