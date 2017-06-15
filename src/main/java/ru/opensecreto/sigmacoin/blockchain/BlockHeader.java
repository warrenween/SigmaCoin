package ru.opensecreto.sigmacoin.blockchain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.opensecreto.sigmacoin.config.BaseConfig;
import ru.opensecreto.sigmacoin.config.CoinConfig;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class BlockHeader {

    private final BigInteger height;
    private final BigInteger timestamp;
    private final BigInteger difficulty;
    private final byte[] parentHash;
    private final byte[] txRootHash;
    private final byte[] stateRootHash;
    private final int[] pow;

    public BlockHeader(BigInteger height, BigInteger timestamp, BigInteger difficulty, byte[] parentHash,
                       byte[] txRootHash, int[] pow, byte[] stateRootHash) {
        this.height = checkNotNull(height);
        this.timestamp = checkNotNull(timestamp);
        this.difficulty = checkNotNull(difficulty);
        this.parentHash = Arrays.copyOf(checkNotNull(parentHash), parentHash.length);
        this.stateRootHash = Arrays.copyOf(checkNotNull(stateRootHash), stateRootHash.length);
        this.txRootHash = Arrays.copyOf(checkNotNull(txRootHash), txRootHash.length);

        this.pow = checkNotNull(pow);

        //------
        checkArgument(height.signum() >= 0);
        checkArgument(timestamp.signum() >= 0);
        checkArgument(difficulty.signum() > 0);
        checkArgument(parentHash.length == BaseConfig.DEFAULT_IDENTIFICATOR_LENGTH);
        checkArgument(txRootHash.length == BaseConfig.DEFAULT_IDENTIFICATOR_LENGTH);
        checkArgument(stateRootHash.length == BaseConfig.DEFAULT_IDENTIFICATOR_LENGTH);
        checkArgument(pow.length >= 2);
    }

    public static final Logger LOGGER = LoggerFactory.getLogger(BlockHeader.class);

    public static byte[] encode(BlockHeader blockHeader) {
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

            for (int i : blockHeader.pow) {
                dataOut.writeInt(i);
            }

        } catch (IOException e) {
            LOGGER.warn("Something unexpected happened when encoding block header.", e);
            throw new RuntimeException(e);
        }
        return out.toByteArray();
    }

}
