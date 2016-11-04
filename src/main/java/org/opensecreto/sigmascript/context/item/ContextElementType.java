package org.opensecreto.sigmascript.context.item;

import org.opensecreto.sigmascript.exceptions.IllegalOperationException;

public enum ContextElementType {
    OBJECT, BYTE, SHORT, INTEGER, LONG, FLOAT, DOUBLE;

    public static ContextElementType normalize(ContextElementType type1, ContextElementType type2)
            throws IllegalOperationException {
        if ((type1 == ContextElementType.OBJECT)
                || (type2 == ContextElementType.OBJECT)) {
            throw new IllegalOperationException("Can not normalize objects.");
        }
        return fromOrd(Math.max(toOrd(type1), toOrd(type2)));
    }

    private static int toOrd(ContextElementType type) {
        switch (type) {
            case OBJECT:
                return -1;
            case BYTE:
                return 0;
            case SHORT:
                return 1;
            case INTEGER:
                return 2;
            case LONG:
                return 3;
            case FLOAT:
                return 4;
            case DOUBLE:
                return 5;
            default:
                if (type == null) {
                    throw new NullPointerException("type is null");
                }
                throw new RuntimeException("Unexpected type: " + type.toString());
        }
    }

    private static ContextElementType fromOrd(int ord) {
        switch (ord) {
            case -1:
                return OBJECT;
            case 0:
                return BYTE;
            case 1:
                return SHORT;
            case 2:
                return INTEGER;
            case 3:
                return LONG;
            case 4:
                return FLOAT;
            case 5:
                return DOUBLE;
            default:
                throw new IllegalArgumentException("Illegal ord: " + ord + ". " + "" +
                        "Expected from -1 (inclusive) to 5(inclusive).");
        }
    }
}
