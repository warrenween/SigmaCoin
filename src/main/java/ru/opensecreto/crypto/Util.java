package ru.opensecreto.crypto;

public class Util {

    public static byte[] bigToLittleEndian(byte[] in) {
        byte[] out = new byte[in.length];

        for (int i = 0; i < in.length; i++) {
            out[i] = bigToLittleEndian(in[in.length - i - 1]);
        }
        return out;
    }

    public static byte bigToLittleEndian(byte in) {
        return (byte) (
                ((in & 0b1) << 7) |
                        ((in & 0b10) << 5) |
                        ((in & 0b100) << 3) |
                        ((in & 0b1000) << 1) |
                        ((in & 0b10000) >> 1) |
                        ((in & 0b100000) >> 3) |
                        ((in & 0b1000000) >> 5) |
                        ((in & 0b10000000) >> 7)
        );
    }

}
