package ru.opensecreto.sigmacoin.vm;

public interface Memory {

    public Word get(long pointer);

    public Word put(long pointer, byte data);

}
