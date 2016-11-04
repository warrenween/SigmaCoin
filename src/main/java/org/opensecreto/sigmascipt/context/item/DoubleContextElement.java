package org.opensecreto.sigmascipt.context.item;

public class DoubleContextElement implements ContextElement {

    private double value;

    public DoubleContextElement() {

    }

    public DoubleContextElement(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public ContextElementType getType() {
        return ContextElementType.DOUBLE;
    }

}
