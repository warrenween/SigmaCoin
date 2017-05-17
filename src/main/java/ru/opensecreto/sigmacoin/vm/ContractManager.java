package ru.opensecreto.sigmacoin.vm;

public interface ContractManager {

    /**
     * Get contract by it's id.
     *
     * @param id contract id
     * @return contract memory for requested id.
     */
    public Memory getContract(byte[] id);

}
