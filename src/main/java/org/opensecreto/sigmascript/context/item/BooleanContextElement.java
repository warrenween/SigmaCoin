package org.opensecreto.sigmascript.context.item;

public class BooleanContextElement implements ContextElement {

    private boolean value;

    public BooleanContextElement() {

    }

    public BooleanContextElement(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }

    @Override
    public ContextElementType getType() {
        return ContextElementType.BOOLEAN;
    }
}
