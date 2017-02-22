package ru.opensecreto.crypto;

import org.bouncycastle.crypto.digests.SHA512Digest;

public class Ed25519SHA512 implements BaseSigner {

    private SHA512Digest digest = new SHA512Digest();

    @Override
    public byte[] sign(byte[] message, byte[] privKey) {
        return new byte[0];
    }

    @Override
    public boolean verify(byte[] message, byte[] signature, byte[] pubKey) {
        return false;
    }
}
