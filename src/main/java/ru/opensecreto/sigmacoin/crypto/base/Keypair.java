package ru.opensecreto.sigmacoin.crypto.base;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class Keypair {

    private final PublicKey publicKey;
    private final PrivateKey privateKey;

    public Keypair(PublicKey publicKey, PrivateKey privateKey) throws NullPointerException, IllegalArgumentException {
        this.publicKey = checkNotNull(publicKey);
        this.privateKey = checkNotNull(privateKey);
        checkArgument(privateKey.getMethod() == publicKey.getMethod(),
                "Private and public keys has different methods.");
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

}
