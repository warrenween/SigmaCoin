package org.opensecreto.sigmascipt.context.item;

public class IntegerItem implements ContextItem {

    private int value;

    public IntegerItem() {

    }

    public IntegerItem(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public ContextItemType getType() {
        return ContextItemType.INTEGER;
    }

}
