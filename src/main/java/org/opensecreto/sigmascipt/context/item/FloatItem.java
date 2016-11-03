package org.opensecreto.sigmascipt.context.item;

public class FloatItem implements ContextItem {

    private float value;

    public FloatItem() {

    }

    public FloatItem(float value) {
        this.value = value;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    @Override
    public ContextItemType getType() {
        return ContextItemType.FLOAT;
    }

}
