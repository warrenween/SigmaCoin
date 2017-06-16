package ru.opensecreto.sigmacoin.vm;

public interface AccountManager {

    public Account getAccount(AccountAddress accountAddress);


    /**
     * Check if contract with given contractID exists.
     *
     * @param accountAddress contract id to check
     * @return true if contract with given contractID exists, false otherwise
     */
    public boolean accountExists(AccountAddress accountAddress);

}
