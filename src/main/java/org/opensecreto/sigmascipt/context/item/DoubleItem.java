package org.opensecreto.sigmascipt.context.item;

public class DoubleItem implements ContextItem {

    private double value;

    public DoubleItem() {

    }

    public DoubleItem(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public ContextItemType getType() {
        return ContextItemType.DOUBLE;
    }

}
