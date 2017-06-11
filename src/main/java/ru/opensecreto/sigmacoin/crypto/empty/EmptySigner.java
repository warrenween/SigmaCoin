package ru.opensecreto.sigmacoin.crypto.empty;

import ru.opensecreto.sigmacoin.crypto.base.PrivateKey;
import ru.opensecreto.sigmacoin.crypto.base.PublicKey;
import ru.opensecreto.sigmacoin.crypto.base.Signature;
import ru.opensecreto.sigmacoin.crypto.base.Signer;

import static com.google.common.base.Preconditions.checkNotNull;

public class EmptySigner implements Signer {

    public static final int EMPTYSIGNER_ID = 0;

    @Override
    public Signature sign(byte[] message, PrivateKey privateKey) {
        checkNotNull(message);
        checkNotNull(privateKey);
        return new Signature(0, new byte[0]);
    }

    @Override
    public boolean verify(byte[] message, Signature signature, PublicKey publicKey) {
        checkNotNull(message);
        checkNotNull(signature);
        checkNotNull(publicKey);
        return true;
    }
}
