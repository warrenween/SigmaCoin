package ru.opensecreto.sigmacoin.vm;

import ru.opensecreto.sigmacoin.crypto.base.PublicKey;

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
    public final PublicKey publicKey;

    public Account(int type, Memory memory, PublicKey publicKey) {
        this.type = checkNotNull(type);
        this.memory = checkNotNull(memory);
        if (type == USER_CONTROLLED) {
            this.publicKey = checkNotNull(publicKey);
        } else if (type == CODE_CONTROLLED) {
            checkArgument(publicKey == null);
            this.publicKey = null;
        } else throw new IllegalArgumentException();
    }

}
