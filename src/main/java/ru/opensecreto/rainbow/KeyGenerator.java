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

    private final int[] v;
    private final int[] o;
    private final int u;
    private final int n;
    private final int[][] S;


    public KeyGenerator(int Kdim, int[] v) {
        K = new int[Kdim][Kdim];

        if (v[0] <= 0) {
            throw new IllegalArgumentException("v1 must be greater than 0");
        }
        int v0 = 0;
        for (int i = 0; i < v.length; i++) {
            if (v[i] <= v0) {
                throw new IllegalArgumentException("v" + i + " must be grater than v" + (i - 1));
            }
            v0 = v[i];
        }

        this.v = v;
        u = v.length;
        n = v[v.length - 1];

        S = new int[u][n];
        for (int l = 0; l < S.length; l++) {
            for (int i = 0; i < v[l]; i++) {
                S[l][i] = i + 1;
            }
        }

        o = new int[v.length - 1];
        for (int i = 0; i < o.length; i++) {
            o[i] = v[i + 1] - v[i];
        }
    }

    public int[] getv() {
        return v;
    }

    public int[] geto() {
        return o;
    }

    public int getu() {
        return u;
    }

    public int getn() {
        return n;
    }

    public int[][] getS() {
        return S;
    }
}
