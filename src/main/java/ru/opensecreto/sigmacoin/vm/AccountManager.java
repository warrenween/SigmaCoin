package ru.opensecreto.sigmacoin.vm;

public interface AccountManager {

    public Account getAccount(AccountAddress accountAddress);

    /**
     * @throws IllegalStateException if previous update was not submitted or cancelled.
     */
    public void startUpdate() throws IllegalStateException;

    public void updateAccount(Account account);

    public void submit();

    public void cancel();

    /**
     * Check if contract with given contractID exists.
     *
     * @param accountAddress contract id to check
     * @return true if contract with given contractID exists, false otherwise
     */
    public boolean accountExists(AccountAddress accountAddress);

}
