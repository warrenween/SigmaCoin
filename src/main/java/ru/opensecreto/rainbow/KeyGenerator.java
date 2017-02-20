package ru.opensecreto.rainbow;

/**
 * Rainbow signature scheme key generator.
 * <p>
 * See: https://pdfs.semanticscholar.org/7977/afcdb8ec9c420935f7a1f8212c303f0ca7fb.pdf
 * <p>
 * https://eprint.iacr.org/2010/437.pdf
 */
public class KeyGenerator {

    private final int[][] K;

    private final int[] vi;
    private final int[] oi;
    private final int u;
    private final int n;
    private final int[][] S;


    public KeyGenerator(int Kdim, int[] vi) {
        K = new int[Kdim][Kdim];

        if (vi[0] <= 0) {
            throw new IllegalArgumentException("v1 must be greater than 0");
        }
        int v0 = 0;
        for (int i = 0; i < vi.length; i++) {
            if (vi[i] <= v0) {
                throw new IllegalArgumentException("v" + i + " must be grater than v" + (i - 1));
            }
            v0 = vi[i];
        }

        this.vi = vi;
        u = vi.length;
        n = vi[vi.length - 1];

        S = new int[u][n];
        for (int l = 0; l < S.length; l++) {
            for (int i = 0; i < vi[l]; i++) {
                S[l][i] = i + 1;
            }
        }

        oi = new int[vi.length - 1];
        for (int i = 0; i < oi.length; i++) {
            oi[i] = vi[i + 1] - vi[i];
        }
    }

    public int[] getVi() {
        return vi;
    }

    public int[] getOi() {
        return oi;
    }

    public int getU() {
        return u;
    }

    public int getN() {
        return n;
    }

    public int[][] getS() {
        return S;
    }
}
