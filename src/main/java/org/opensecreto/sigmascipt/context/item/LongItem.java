package org.opensecreto.sigmascipt.context.item;

public class LongItem implements ContextItem {

    private long value;

    public LongItem() {

    }

    public LongItem(long value) {
        this.value = value;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    @Override
    public ContextItemType getType() {
        return ContextItemType.LONG;
    }

}
