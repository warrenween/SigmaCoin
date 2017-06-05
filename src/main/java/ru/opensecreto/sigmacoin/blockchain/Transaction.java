package ru.opensecreto.sigmacoin.blockchain;

import ru.opensecreto.sigmacoin.crypto.Signers;

import java.util.Arrays;

public class Transaction {

    public final static int CHAINID_SIZE = 4;
    public final static int SOURCE_SIZE = 32;
    public final static int TARGET_SIZE = 32;
    public final static int CULIMIT_SIZE = 16;
    public final static int CUPRICE_SIZE = 32;

    private final byte[] chainID;
    private final long timestamp;
    private final byte[] source;
    private final byte[] target;
    private final byte[] cuLimit;
    private final byte[] cuPrice;
    private final byte[] data;
    private final short sigmethod;
    private final byte[] signature;

    public Transaction(byte[] chainID, long timestamp, byte[] source, byte[] target,
                       byte[] cuLimit, byte[] cuPrice,
                       byte[] data, short sigmethod, byte[] signature)
            throws IllegalArgumentException {
        //chainid verification
        if (chainID == null) throw new IllegalArgumentException("Chain id can not be null");
        if (chainID.length != CHAINID_SIZE) throw new IllegalArgumentException("Invalid chainId field length");

        //source verification
        if (source == null) throw new IllegalArgumentException("Source can not be null");
        if (source.length != SOURCE_SIZE)
            throw new IllegalArgumentException("Invalid source field length.");

        //target verification
        if (target == null) throw new IllegalArgumentException("Target is null.");
        if (target.length != TARGET_SIZE) throw new IllegalArgumentException("Invalid target size");

        //cuLimit verification
        if (cuLimit == null) throw new IllegalArgumentException("CuLimit is null");
        if (cuLimit.length != CULIMIT_SIZE) throw new IllegalArgumentException("CuLimit hash incorrect length");

        //cuPrice verification
        if (cuPrice == null) throw new IllegalArgumentException("CuPrice is null");
        if (cuPrice.length != CULIMIT_SIZE) throw new IllegalArgumentException("CuPrice hash incorrect length");

        //sigmethod verification
        if (!Signers.SIGNERS.containsKey(sigmethod))
            throw new IllegalArgumentException("Unknown signature method " + sigmethod);

        //signature verification
        if (signature == null) throw new IllegalArgumentException("Signature is null");
        if (signature.length != Signers.SIG_SIZES.get(sigmethod))
            throw new IllegalArgumentException("Signature of type " + Signers.SIGNERS_NAMES.get(sigmethod) +
                    " expected to have length of " + Signers.SIG_SIZES.get(sigmethod));


        this.chainID = Arrays.copyOf(chainID, CHAINID_SIZE);
        this.timestamp = timestamp;
        this.source = Arrays.copyOf(source, SOURCE_SIZE);
        this.target = Arrays.copyOf(target, TARGET_SIZE);
        this.cuLimit = Arrays.copyOf(cuLimit, CULIMIT_SIZE);
        this.cuPrice = Arrays.copyOf(cuPrice, CUPRICE_SIZE);
        this.data = Arrays.copyOf(data, data.length);
        this.sigmethod = sigmethod;
        this.signature = Arrays.copyOf(signature, signature.length);
    }
}
