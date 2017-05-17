package ru.opensecreto.sigmacoin.vm;

/**
 * Controls execution of bytecode. Creates frames, handles invocations.
 */
public class VirtualMachineController {

    private final Memory mainContract;
    private final ContractManager contractManager;

    public VirtualMachineController(Memory mainContract, ContractManager contractManager) {
        this.mainContract = mainContract;
        this.contractManager = contractManager;
    }

    public void execute() {

    }

    public void invoke() {

    }
}
