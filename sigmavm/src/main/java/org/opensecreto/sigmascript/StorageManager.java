package org.opensecreto.sigmascript;

public interface StorageManager {

    public byte getByte(long index);

    public void setByte(long index, byte value);

}
