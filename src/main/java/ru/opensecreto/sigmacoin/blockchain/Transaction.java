package ru.opensecreto.sigmacoin.blockchain;

import ru.opensecreto.sigmacoin.crypto.Signers;
import ru.opensecreto.sigmacoin.vm.ContractAddress;

import java.math.BigInteger;
import java.nio.ByteBuffer;
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
        checkArgument(sigmethod.signum() >= 0, "Sigmethod can not be negative");
        checkArgument(Signers.SIGNERS.containsKey(sigmethod),
                "Unknown signature method " + sigmethod);
        checkArgument(signature.length == Signers.SIG_SIZES.get(sigmethod),
                "Signature of type " + Signers.SIGNERS_NAMES.get(sigmethod) +
                        " expected to have length of " + Signers.SIG_SIZES.get(sigmethod));
    }

    public static byte[] encode(Transaction tx) {
        ByteBuffer buf = ByteBuffer.allocate(
                Integer.BYTES + bigIntBytes(tx.chainID) +
                        Integer.BYTES + bigIntBytes(tx.timestamp) +
                        ContractAddress.BYTES +//target
                        Integer.BYTES + bigIntBytes(tx.cuLimit) +
                        Integer.BYTES + bigIntBytes(tx.cuPrice) +
                        Integer.BYTES + bigIntBytes(tx.sigmethod) +
                        Integer.BYTES + tx.signature.length +
                        Integer.BYTES + tx.data.length
        );

        byte[] chainIdArr = tx.chainID.toByteArray();
        buf.putInt(chainIdArr.length);
        buf.put(chainIdArr);

        byte[] timestampArr = tx.timestamp.toByteArray();
        buf.putInt(timestampArr.length);
        buf.put(timestampArr);

        buf.put(tx.target.id.getData());

        byte[] cuLimitArr = tx.cuLimit.toByteArray();
        buf.putInt(cuLimitArr.length);
        buf.put(cuLimitArr);

        byte[] cuPriceArr = tx.cuPrice.toByteArray();
        buf.putInt(cuPriceArr.length);
        buf.put(cuPriceArr);

        byte[] sigmethodArr = tx.sigmethod.toByteArray();
        buf.putInt(sigmethodArr.length);
        buf.put(sigmethodArr);

        buf.putInt(tx.signature.length);
        buf.put(tx.signature);

        buf.putInt(tx.data.length);
        buf.put(tx.data);

        return buf.array();
    }

    public static Transaction decode(byte[] data) {
        ByteBuffer buf = ByteBuffer.wrap(data);

        byte[] chainIdArr = new byte[buf.getInt()];
        buf.get(chainIdArr);
        BigInteger chainId = new BigInteger(chainIdArr);

        byte[] timestampArr = new byte[buf.getInt()];
        buf.get(timestampArr);
        BigInteger timestamp = new BigInteger(timestampArr);

        byte[] targetArr = new byte[ContractAddress.BYTES];
        buf.get(targetArr);
        ContractAddress target = new ContractAddress(targetArr);

        byte[] cuLimitArr = new byte[buf.getInt()];
        buf.get(cuLimitArr);
        BigInteger cuLimit = new BigInteger(cuLimitArr);

        byte[] cuPriceArr = new byte[buf.getInt()];
        buf.get(cuPriceArr);
        BigInteger cuPrice = new BigInteger(cuPriceArr);

        byte[] sigmethodArr = new byte[buf.getInt()];
        buf.get(sigmethodArr);
        BigInteger sigmethod = new BigInteger(sigmethodArr);

        byte[] signature = new byte[buf.getInt()];
        buf.get(signature);

        byte[] txData = new byte[buf.getInt()];
        buf.get(txData);

        return new Transaction(chainId, timestamp, target, cuLimit, cuPrice, sigmethod, signature, txData);
    }

    private static int bigIntBytes(BigInteger val) {
        return val.bitLength() / 8 + 1;
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) | !(obj instanceof Transaction)) {
            return false;
        }
        Transaction o = (Transaction) obj;
        return (o.chainID.equals(chainID)) && (o.timestamp.equals(timestamp)) && (o.target.equals(target)) &&
                (o.cuLimit.equals(cuLimit)) && (o.cuPrice.equals(cuPrice)) && (o.sigmethod.equals(sigmethod)) &&
                Arrays.equals(o.signature, signature) && Arrays.equals(o.data, data);
    }
}
