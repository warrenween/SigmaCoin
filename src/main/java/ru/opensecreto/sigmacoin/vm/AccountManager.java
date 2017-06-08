package ru.opensecreto.sigmacoin.vm;

public interface AccountManager {

    public Account getAccount(Word contractID);


    /**
     * Check if contract with given contractID exists.
     *
     * @param contractID contract id to check
     * @return true if contract with given contractID exists, false otherwise
     */
    public boolean accountExists(Word contractID);

}
