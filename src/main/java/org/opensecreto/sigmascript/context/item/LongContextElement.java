package org.opensecreto.sigmascript.context.item;

public class LongContextElement implements ContextElement {

    private long value;

    public LongContextElement() {

    }

    public LongContextElement(long value) {
        this.value = value;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    @Override
    public ContextElementType getType() {
        return ContextElementType.LONG;
    }

}
