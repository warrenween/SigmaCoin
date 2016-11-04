package org.opensecreto.sigmascript.context.item;

public interface ContextElement {

    public static ContextElement getElementByType(ContextElementType type) {
        switch (type) {
            // TODO: 04.11.2016 add getting object
            case BYTE:
                return new ByteContextElement();
            case SHORT:
                return new ShortContextElement();
            case INTEGER:
                return new IntegerContextElement();
            case LONG:
                return new LongContextElement();
            case FLOAT:
                return new FloatContextElement();
            case DOUBLE:
                return new DoubleContextElement();
            case BOOLEAN:
                return new BooleanContextElement();
            default:
                if (type == null) {
                    throw new NullPointerException("Type is null");
                }
                throw new RuntimeException("Unexpected type: " + type.toString());
        }
    }
    public ContextElementType getType();

}
