package org.opensecreto.sigmascript.context.item;

public class ShortContextElement implements ContextElement {

    private short value;

    public ShortContextElement() {

    }

    public ShortContextElement(short value) {
        this.value = value;
    }

    public short getValue() {
        return value;
    }

    public void setValue(short value) {
        this.value = value;
    }

    @Override
    public ContextElementType getType() {
        return ContextElementType.SHORT;
    }

}
