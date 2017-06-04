package ru.opensecreto.sigmacoin.blockchain;

public class Block {

    private final byte[] version = new byte[4];
    private final long timestamp;
    private final byte[] parentHash = new byte[32];
    private final byte[] metaData = new byte[16];
    private final int[] pow;

    public Block(long timestamp, int[] pow) {
        this.timestamp = timestamp;
        this.pow = pow;
    }
}
