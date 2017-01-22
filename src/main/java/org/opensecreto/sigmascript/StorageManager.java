package org.opensecreto.sigmascript;

import org.joou.UByte;

public interface StorageManager {

    public byte getByte(long index);

    public void setByte(long index, UByte value);

}
