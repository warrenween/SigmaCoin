package ru.opensecreto.sigmacoin.vm;

public interface ContractManager {

    /**
     * Get contract by it's id.
     *
     * @param contractID contract id
     * @return contract memory for requested id.
     */
    public Memory getContract(ContractID contractID);

}
