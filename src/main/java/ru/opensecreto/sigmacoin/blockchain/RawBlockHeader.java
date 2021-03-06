package ru.opensecreto.sigmacoin.blockchain;

import org.bouncycastle.crypto.Digest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.opensecreto.sigmacoin.config.BaseConfig;
import ru.opensecreto.sigmacoin.core.DigestProvider;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.Arrays;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class RawBlockHeader {

    private final BigInteger height;
    private final BigInteger timestamp;
    private final BigInteger difficulty;
    private final byte[] parentHash;
    private final byte[] txRootHash;
    private final byte[] stateRootHash;

    public RawBlockHeader(BigInteger height, BigInteger timestamp, BigInteger difficulty, byte[] parentHash,
                          byte[] txRootHash, byte[] stateRootHash) {
        this.height = checkNotNull(height);
        this.timestamp = checkNotNull(timestamp);
        this.difficulty = checkNotNull(difficulty);
        this.parentHash = Arrays.copyOf(checkNotNull(parentHash), parentHash.length);
        this.stateRootHash = Arrays.copyOf(checkNotNull(stateRootHash), stateRootHash.length);
        this.txRootHash = Arrays.copyOf(checkNotNull(txRootHash), txRootHash.length);

        //------
        checkArgument(height.signum() >= 0);
        checkArgument(timestamp.signum() >= 0);
        checkArgument(difficulty.signum() > 0);
        checkArgument(parentHash.length == BaseConfig.DEFAULT_IDENTIFICATOR_LENGTH);
        checkArgument(txRootHash.length == BaseConfig.DEFAULT_IDENTIFICATOR_LENGTH);
        checkArgument(stateRootHash.length == BaseConfig.DEFAULT_IDENTIFICATOR_LENGTH);
    }

    public static final Logger LOGGER = LoggerFactory.getLogger(RawBlockHeader.class);

    public static byte[] encode(RawBlockHeader blockHeader) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DataOutputStream dataOut = new DataOutputStream(out);
        try {
            byte[] heightData = blockHeader.height.toByteArray();
            dataOut.writeInt(heightData.length);
            out.write(heightData);

            byte[] timestampData = blockHeader.timestamp.toByteArray();
            dataOut.writeInt(timestampData.length);
            out.write(timestampData);

            byte[] difficultyData = blockHeader.difficulty.toByteArray();
            dataOut.writeInt(difficultyData.length);
            out.write(difficultyData);

            out.write(blockHeader.parentHash);
            out.write(blockHeader.txRootHash);
            out.write(blockHeader.stateRootHash);

        } catch (IOException e) {
            LOGGER.warn("Something unexpected happened when encoding block header.", e);
            throw new RuntimeException(e);
        }
        return out.toByteArray();
    }

    public static RawBlockHeader decode(byte[] data) {
        ByteBuffer buffer = ByteBuffer.wrap(data);

        byte[] heightData = new byte[buffer.getInt()];
        buffer.get(heightData);
        BigInteger height = new BigInteger(1, heightData);

        byte[] timestampData = new byte[buffer.getInt()];
        buffer.get(timestampData);
        BigInteger timestamp = new BigInteger(timestampData);

        byte[] difficultyData = new byte[buffer.getInt()];
        buffer.get(difficultyData);
        BigInteger difficulty = new BigInteger(difficultyData);

        byte[] parentHash = new byte[BaseConfig.DEFAULT_IDENTIFICATOR_LENGTH];
        buffer.get(parentHash);

        byte[] txRootHash = new byte[BaseConfig.DEFAULT_IDENTIFICATOR_LENGTH];
        buffer.get(txRootHash);

        byte[] stateRootHash = new byte[BaseConfig.DEFAULT_IDENTIFICATOR_LENGTH];
        buffer.get(stateRootHash);

        return new RawBlockHeader(height, timestamp, difficulty, parentHash, txRootHash, stateRootHash);
    }

    public byte[] getHash(DigestProvider provider) {
        byte[] data = encode(this);
        Digest digest = provider.getDigest();
        digest.update(data, 0, data.length);
        byte[] out = new byte[provider.getDigestSize()];
        digest.doFinal(out, 0);
        return out;
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof RawBlockHeader)) {
            return false;
        }
        RawBlockHeader header = (RawBlockHeader) obj;
        return header.height.equals(height) && header.timestamp.equals(timestamp) &&
                header.difficulty.equals(difficulty) && Arrays.equals(header.parentHash, parentHash) &&
                Arrays.equals(header.txRootHash, txRootHash) && Arrays.equals(header.stateRootHash, stateRootHash);
    }

}
