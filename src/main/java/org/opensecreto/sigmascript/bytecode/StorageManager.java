package org.opensecreto.sigmascript.bytecode;

public interface StorageManager {

    public byte getByte(long index);

    public void setByte(long index, byte value);

}
