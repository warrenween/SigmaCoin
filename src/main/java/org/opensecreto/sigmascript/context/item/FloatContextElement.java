package org.opensecreto.sigmascript.context.item;

public class FloatContextElement implements ContextElement {

    private float value;

    public FloatContextElement() {

    }

    public FloatContextElement(float value) {
        this.value = value;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    @Override
    public ContextElementType getType() {
        return ContextElementType.FLOAT;
    }

}
