package ru.opensecreto.sigmacoin.blockchain;

import org.bouncycastle.crypto.Digest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.opensecreto.sigmacoin.core.DigestProvider;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class FullBlockHeader {

    private final RawBlockHeader rawBlockHeader;
    private final int[] pow;

    public FullBlockHeader(RawBlockHeader rawBlockHeader, int[] pow) {
        this.rawBlockHeader = checkNotNull(rawBlockHeader);
        this.pow = checkNotNull(pow);

        checkArgument(pow.length >= 2);
    }

    public static final Logger LOGGER = LoggerFactory.getLogger(FullBlockHeader.class);

    public static byte[] encode(FullBlockHeader fullBlockHeader) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DataOutputStream dataOut = new DataOutputStream(out);
        try {
            byte[] data = RawBlockHeader.encode(fullBlockHeader.rawBlockHeader);
            dataOut.writeInt(data.length);
            out.write(data, 0, data.length);

            dataOut.writeInt(fullBlockHeader.pow.length);
            for (int i : fullBlockHeader.pow) {
                dataOut.writeInt(i);
            }
        } catch (IOException e) {
            LOGGER.warn("Something unexpected happened when encoding block header.", e);
            throw new RuntimeException(e);
        }
        return out.toByteArray();
    }

    public static FullBlockHeader decode(byte[] data) {
        ByteBuffer buffer = ByteBuffer.wrap(data);

        byte[] rawBlockHeaderData = new byte[buffer.getInt()];
        buffer.get(rawBlockHeaderData);
        RawBlockHeader rawBlockHeader = RawBlockHeader.decode(rawBlockHeaderData);

        int[] pow = new int[buffer.getInt()];
        for (int i = 0; i < pow.length; i++) {
            pow[i] = buffer.getInt();
        }
        return new FullBlockHeader(rawBlockHeader, pow);
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof FullBlockHeader)) {
            return false;
        }
        FullBlockHeader header = (FullBlockHeader) obj;
        return header.rawBlockHeader.equals(rawBlockHeader) && Arrays.equals(header.pow, pow);

    }

    public byte[] getHash(DigestProvider provider) {
        byte[] data = encode(this);
        Digest digest = provider.getDigest();
        digest.update(data, 0, data.length);
        byte[] out = new byte[provider.getDigestSize()];
        digest.doFinal(out, 0);
        return out;
    }
}
