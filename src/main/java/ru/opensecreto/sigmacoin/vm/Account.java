package ru.opensecreto.sigmacoin.vm;

import ru.opensecreto.sigmacoin.crypto.base.PublicKey;

import java.math.BigInteger;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class Account {

    /**
     * User controlled accounts are same as code controlled accounts but can be invoked from transaction.
     * To invoke code transaction must be signed with corresponding public key.
     */
    public static final int USER_CONTROLLED = 0;
    /**
     * Contract that can be invoked only from another contract.
     */
    public static final int CODE_CONTROLLED = 1;

    public final int type;
    public final Memory memory;
    private PublicKey publicKey;
    private BigInteger balance;
    public final AccountAddress accountAddress;

    public Account(int type, Memory memory, PublicKey publicKey, BigInteger balance, AccountAddress accountAddress) {
        this.type = checkNotNull(type);
        this.memory = checkNotNull(memory);
        this.balance = checkNotNull(balance);
        if (type == USER_CONTROLLED) {
            this.publicKey = checkNotNull(publicKey);
        } else if (type == CODE_CONTROLLED) {
            checkArgument(publicKey == null);
            this.publicKey = null;
        } else throw new IllegalArgumentException();
        this.accountAddress = checkNotNull(accountAddress);
    }

    public BigInteger getBalance() {
        return balance;
    }

    public void setBalance(BigInteger balance) {
        this.balance = checkNotNull(balance);
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = checkNotNull(publicKey);
    }
}
