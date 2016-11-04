package org.opensecreto.sigmascript.context.item;

public class ByteContextElement implements ContextElement {

    private byte value;

    public ByteContextElement() {

    }

    public ByteContextElement(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }

    public void setValue(byte value) {
        this.value = value;
    }

    @Override
    public ContextElementType getType() {
        return ContextElementType.BYTE;
    }
}
