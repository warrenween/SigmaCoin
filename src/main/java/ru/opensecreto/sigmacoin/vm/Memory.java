package ru.opensecreto.sigmacoin.vm;

public interface Memory {

    public byte get(long pointer);

    public byte put(long pointer, byte data);

}
