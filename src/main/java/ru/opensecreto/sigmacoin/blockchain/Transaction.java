package ru.opensecreto.sigmacoin.blockchain;

import ru.opensecreto.sigmacoin.crypto.Signers;
import ru.opensecreto.sigmacoin.vm.ContractAddress;

import java.math.BigInteger;
import java.util.Arrays;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class Transaction {

    private final BigInteger chainID;
    private final BigInteger timestamp;
    private final ContractAddress target;
    private final BigInteger cuLimit;
    private final BigInteger cuPrice;
    private final BigInteger sigmethod;
    private final byte[] signature;
    private final byte[] data;


    public Transaction(BigInteger chainID, BigInteger timestamp, ContractAddress target,
                       BigInteger cuLimit, BigInteger cuPrice, BigInteger sigmethod, byte[] signature,
                       byte[] data)
            throws IllegalArgumentException, NullPointerException {
        this.chainID = checkNotNull(chainID);
        this.timestamp = checkNotNull(timestamp);
        this.target = checkNotNull(target);
        this.cuLimit = checkNotNull(cuLimit);
        this.cuPrice = checkNotNull(cuPrice);
        this.sigmethod = checkNotNull(sigmethod);
        this.signature = Arrays.copyOf(checkNotNull(signature), signature.length);
        this.data = Arrays.copyOf(checkNotNull(data), data.length);

        checkArgument(chainID.signum() >= 0, "ChainID can not be negative");
        checkArgument(timestamp.signum() >= 0, "Timestamp can not be negative");
        checkArgument(cuLimit.signum() >= 0, "CU limit can not be negative");
        checkArgument(cuPrice.signum() >= 0, "CU price can not be negative");
        checkArgument(Signers.SIGNERS.containsKey(sigmethod),
                "Unknown signature method " + sigmethod);
        checkArgument(signature.length != Signers.SIG_SIZES.get(sigmethod),
                "Signature of type " + Signers.SIGNERS_NAMES.get(sigmethod) +
                        " expected to have length of " + Signers.SIG_SIZES.get(sigmethod));
    }

}
