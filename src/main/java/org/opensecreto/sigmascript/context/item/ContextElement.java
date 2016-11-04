package org.opensecreto.sigmascript.context.item;

import java.util.ArrayList;
import java.util.HashMap;

public class ContextElement {

    public final ContextElementType type;
    protected HashMap<String, Object> values;

    public ContextElement(ContextElementType type) {
        if (type == null) {
            throw new NullPointerException("Type can not be null");
        }
        values = new HashMap<>(8);
        this.type = type;

        switch (type) {
            case BYTE:
                setValue("value", new Byte((byte) 0));
                break;
            case BOOLEAN:
                setValue("value", new Boolean(false));
                break;
            case ARRAY:
                setValue("value", new ArrayList<ContextElement>());
                break;
            case DOUBLE:
                setValue("value", new Double(0d));
                break;
            case FLOAT:
                setValue("value", new Float(0f));
                break;
            case LONG:
                setValue("value", new Long(0L));
                break;
            case NULL:
                break;
            case INTEGER:
                setValue("value", new Integer(0));
                break;
            case SHORT:
                setValue("value", new Short((short) 0));
                break;
            case OBJECT:
                break;
            default:
                throw new IllegalArgumentException("Unexpected type " + type.toString());
        }
    }

    public Object getValue(String name) {
        return values.get(name);
    }

    public void setValue(String name, Object value) {
        values.put(name, value);
    }


}
