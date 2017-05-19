package ru.opensecreto.sigmacoin.vm;

public interface ContractManager {

    /**
     * Get contract by it's id.
     *
     * @param contractID contract id
     * @return contract memory for requested id.
     */
    public Memory getContract(ContractID contractID);

    /**
     * Check if contract with given contractID exists.
     *
     * @param contractID contract id to check
     * @return true if contract with given contractID exists, false otherwise
     */
    public boolean contractExists(ContractID contractID);

}
