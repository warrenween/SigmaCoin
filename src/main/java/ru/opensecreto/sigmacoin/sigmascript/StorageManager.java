package ru.opensecreto.sigmacoin.sigmascript;

public interface StorageManager {

    public byte getByte(long index);

    public void setByte(long index, byte value);

}
