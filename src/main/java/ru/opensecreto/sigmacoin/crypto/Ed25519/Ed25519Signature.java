package ru.opensecreto.sigmacoin.crypto.Ed25519;

import com.google.common.base.Preconditions;
import ru.opensecreto.sigmacoin.crypto.interfaces.Signature;

import java.util.Arrays;

public class Ed25519Signature implements Signature {

    private final byte[] signature;

    public Ed25519Signature(byte[] signature) throws NullPointerException {
        this.signature = Arrays.copyOf(Preconditions.checkNotNull(signature), signature.length);
    }

    @Override
    public byte[] encode() {
        return Arrays.copyOf(signature, signature.length);
    }
}
