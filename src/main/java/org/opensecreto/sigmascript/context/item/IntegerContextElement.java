package org.opensecreto.sigmascript.context.item;

public class IntegerContextElement implements ContextElement {

    private int value;

    public IntegerContextElement() {

    }

    public IntegerContextElement(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public ContextElementType getType() {
        return ContextElementType.INTEGER;
    }

}
