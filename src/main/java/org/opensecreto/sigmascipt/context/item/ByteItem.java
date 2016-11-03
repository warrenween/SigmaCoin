package org.opensecreto.sigmascipt.context.item;

public class ByteItem implements ContextItem {

    private byte value;

    public ByteItem() {

    }

    public ByteItem(byte value) {
        this.value = value;
    }

    public byte getByte() {
        return value;
    }

    public void setValue(byte value) {
        this.value = value;
    }

    @Override
    public ContextItemType getType() {
        return ContextItemType.BYTE;
    }
}
