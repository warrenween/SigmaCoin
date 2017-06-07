package ru.opensecreto.sigmacoin.blockchain;

import ru.opensecreto.sigmacoin.crypto.Signers;

import java.util.Arrays;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class Transaction {

    public final static int CHAINID_SIZE = 4;
    public final static int SOURCE_SIZE = 32;
    public final static int TARGET_SIZE = 32;

    private final byte[] chainID;
    private final long timestamp;
    private final byte[] source;
    private final byte[] target;
    private final byte[] cuLimit;
    private final byte[] cuPrice;
    private final short sigmethod;
    private final byte[] signature;
    private final byte[] data;


    public Transaction(byte[] chainID, long timestamp, byte[] source, byte[] target,
                       byte[] cuLimit, byte[] cuPrice, short sigmethod, byte[] signature,
                       byte[] data)
            throws IllegalArgumentException, NullPointerException {
        //chainid verification
        checkNotNull(chainID);
        checkNotNull(source);
        checkNotNull(target);
        checkNotNull(cuLimit);
        checkNotNull(cuPrice);
        checkNotNull(signature);
        checkNotNull(data);

        checkArgument(chainID.length != CHAINID_SIZE, "Invalid chainId field length");
        checkArgument(source.length != SOURCE_SIZE, "Invalid source field length.");
        checkArgument(target.length != TARGET_SIZE, "Invalid target size");
        checkArgument(Signers.SIGNERS.containsKey(sigmethod), "Unknown signature method " + sigmethod);
        checkArgument(signature.length != Signers.SIG_SIZES.get(sigmethod),
                "Signature of type " + Signers.SIGNERS_NAMES.get(sigmethod) +
                        " expected to have length of " + Signers.SIG_SIZES.get(sigmethod));

        this.chainID = Arrays.copyOf(chainID, CHAINID_SIZE);
        this.timestamp = timestamp;
        this.source = Arrays.copyOf(source, SOURCE_SIZE);
        this.target = Arrays.copyOf(target, TARGET_SIZE);
        this.cuLimit = Arrays.copyOf(cuLimit, cuLimit.length);
        this.cuPrice = Arrays.copyOf(cuPrice, cuPrice.length);
        this.sigmethod = sigmethod;
        this.signature = Arrays.copyOf(signature, signature.length);
        this.data = Arrays.copyOf(data, data.length);
    }

}
