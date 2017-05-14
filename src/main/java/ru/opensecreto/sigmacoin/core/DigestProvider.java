package ru.opensecreto.sigmacoin.core;

import org.bouncycastle.crypto.Digest;

public interface DigestProvider {

    public Digest getDigest();

}
