package org.opensecreto.sigmascipt.context.item;

public class ShortItem implements ContextItem {

    private short value;

    public ShortItem() {

    }

    public ShortItem(short value) {
        this.value = value;
    }

    public short getValue() {
        return value;
    }

    public void setValue(short value) {
        this.value = value;
    }

    @Override
    public ContextItemType getType() {
        return ContextItemType.SHORT;
    }

}
