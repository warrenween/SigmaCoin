package ru.opensecreto.sigmacoin.blockchain;

import ru.opensecreto.sigmacoin.crypto.base.Signature;
import ru.opensecreto.sigmacoin.vm.AccountAddress;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.Arrays;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class Transaction {

    private final BigInteger chainID;
    private final BigInteger timestamp;
    private final AccountAddress target;
    private final BigInteger cuLimit;
    private final BigInteger cuPrice;
    private final Signature signature;
    private final byte[] data;

    public Transaction(BigInteger chainID, BigInteger timestamp, AccountAddress target,
                       BigInteger cuLimit, BigInteger cuPrice, Signature signature, byte[] data)
            throws IllegalArgumentException, NullPointerException {
        this.chainID = checkNotNull(chainID);
        this.timestamp = checkNotNull(timestamp);
        this.target = checkNotNull(target);
        this.cuLimit = checkNotNull(cuLimit);
        this.cuPrice = checkNotNull(cuPrice);
        this.signature = checkNotNull(signature);
        this.data = Arrays.copyOf(checkNotNull(data), data.length);

        checkArgument(chainID.signum() >= 0, "ChainID can not be negative");
        checkArgument(timestamp.signum() >= 0, "Timestamp can not be negative");
        checkArgument(cuLimit.signum() >= 0, "CU limit can not be negative");
        checkArgument(cuPrice.signum() >= 0, "CU price can not be negative");
    }

    public static byte[] encode(Transaction tx)
            throws NullPointerException {
        checkNotNull(tx);
        ByteBuffer buf = ByteBuffer.allocate(
                Integer.BYTES + bigIntBytes(tx.chainID) +
                        Integer.BYTES + bigIntBytes(tx.timestamp) +
                        AccountAddress.BYTES +//target
                        Integer.BYTES + bigIntBytes(tx.cuLimit) +
                        Integer.BYTES + bigIntBytes(tx.cuPrice) +
                        Integer.BYTES + tx.signature.encode().length +
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

        buf.putInt(tx.signature.encode().length);
        buf.put(tx.signature.encode());

        buf.putInt(tx.data.length);
        buf.put(tx.data);

        return buf.array();
    }

    public static Transaction decode(byte[] data)
            throws NullPointerException {
        ByteBuffer buf = ByteBuffer.wrap(checkNotNull(data));

        byte[] chainIdArr = new byte[buf.getInt()];
        buf.get(chainIdArr);
        BigInteger chainId = new BigInteger(chainIdArr);

        byte[] timestampArr = new byte[buf.getInt()];
        buf.get(timestampArr);
        BigInteger timestamp = new BigInteger(timestampArr);

        byte[] targetArr = new byte[AccountAddress.BYTES];
        buf.get(targetArr);
        AccountAddress target = new AccountAddress(targetArr);

        byte[] cuLimitArr = new byte[buf.getInt()];
        buf.get(cuLimitArr);
        BigInteger cuLimit = new BigInteger(cuLimitArr);

        byte[] cuPriceArr = new byte[buf.getInt()];
        buf.get(cuPriceArr);
        BigInteger cuPrice = new BigInteger(cuPriceArr);

        byte[] signatureData = new byte[buf.getInt()];
        buf.get(signatureData);
        Signature signature = new Signature(signatureData);

        byte[] txData = new byte[buf.getInt()];
        buf.get(txData);

        return new Transaction(chainId, timestamp, target, cuLimit, cuPrice, signature, txData);
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
        return o.chainID.equals(chainID) && o.timestamp.equals(timestamp) && o.target.equals(target) &&
                o.cuLimit.equals(cuLimit) && o.cuPrice.equals(cuPrice) &&
                o.signature.equals(signature) && Arrays.equals(o.data, data);
    }

    public BigInteger getChainID() {
        return chainID;
    }

    public BigInteger getTimestamp() {
        return timestamp;
    }

    public AccountAddress getTarget() {
        return target;
    }

    public BigInteger getCuLimit() {
        return cuLimit;
    }

    public BigInteger getCuPrice() {
        return cuPrice;
    }

    public Signature getSignature() {
        return signature;
    }

    public byte[] getData() {
        return Arrays.copyOf(data, data.length);
    }
}
