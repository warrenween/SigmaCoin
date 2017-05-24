package ru.opensecreto.sigmacoin.powcba.storage;

import ru.opensecreto.sigmacoin.crypto.Ed25519.Ed25519PublicKey;

import java.util.Arrays;

public class Shard {

    private final long[] ids;
    private final byte[] seed;
    public final Ed25519PublicKey publicKey;

    public Shard(long[] ids, byte[] seed, Ed25519PublicKey publicKey) {
        this.ids = ids;
        this.seed = seed;
        this.publicKey = publicKey;
    }

    public long[] getIds() {
        return Arrays.copyOf(ids, ids.length);
    }

    public byte[] getSeed() {
        return Arrays.copyOf(seed, seed.length);
    }
}
