package ru.opensecreto.sigmacoin.por.work;

import com.google.common.primitives.Ints;
import org.bouncycastle.crypto.Digest;
import ru.opensecreto.sigmacoin.core.DigestProvider;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class Verifier {

    private final DigestProvider chunkProvider;
    private final DigestProvider hashProvider;
    private final int n;

    public Verifier(DigestProvider chunkProvider, DigestProvider hashProvider, int n) {
        this.chunkProvider = checkNotNull(chunkProvider);
        this.hashProvider = checkNotNull(hashProvider);
        this.n = n;

        checkNotNull(chunkProvider.getDigest());
        checkNotNull(hashProvider.getDigest());
        checkArgument(n > 1);
    }

    public boolean verify(byte[] data, byte[] target, int[] solution) {
        checkNotNull(data);
        checkNotNull(target);
        checkNotNull(solution);

        if (solution.length != n) return false;

        //check values are in ascending order (unsigned)
        long tmp[] = new long[n];
        for (int i = 0; i < solution.length; i++) {
            tmp[i] = solution[i] & 0xffffffffL;
        }
        long previous = tmp[0];
        for (int i = 1; i < tmp.length; i++) {
            if (previous >= tmp[i]) return false;
        }

        //xoring
        Digest chunkDigest = chunkProvider.getDigest();
        byte[] chunkResult = new byte[chunkDigest.getDigestSize()];
        byte[] chunkTmp = new byte[chunkDigest.getDigestSize()];
        for (int i = 0; i < n; i++) {
            chunkDigest.reset();
            chunkDigest.update(Ints.toByteArray(solution[0]), 0, Integer.BYTES);
            chunkDigest.update(data, 0, data.length);
            chunkDigest.doFinal(chunkTmp, 0);
            for (int j = 0; j < chunkTmp.length; j++) {
                chunkResult[j] ^= chunkTmp[j];
            }
        }

        //checking xor is 0
        for (byte b : chunkResult) {
            if (b != 0) return false;
        }

        //checking final hash
        Digest hashDigest = hashProvider.getDigest();
        hashDigest.update(data, 0, data.length);
        for (int aSolution : solution) {
            hashDigest.update(Ints.toByteArray(aSolution), 0, Integer.BYTES);
        }
        byte[] hash = new byte[hashDigest.getDigestSize()];
        hashDigest.doFinal(hash, 0);

        for (int i = 0; i < hash.length; i++) {
            if ((hash[i] & 0xff) > (target[i] & 0xff)) {
                return false;
            }
            if ((hash[i] & 0xff) < (target[i] & 0xff)) {
                return true;
            }
        }
        return true;
    }
}
