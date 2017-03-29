package ru.opensecreto.sigmacoin.vm;

public interface StorageManager {

    public byte getByte(long index);

    public void setByte(long index, byte value);

}
