package org.opensecreto.sigmascript.context.operations;

import org.apache.commons.lang3.text.WordUtils;
import org.opensecreto.sigmascript.context.item.ContextElement;
import org.opensecreto.sigmascript.context.item.ContextElementType;
import org.opensecreto.sigmascript.exceptions.CastException;

import static org.opensecreto.sigmascript.context.item.ContextElementType.*;

public class TypeCaster {

    /**
     * Casts element.
     * Only {@link ContextElementType#BYTE}, {@link ContextElementType#SHORT},
     * {@link ContextElementType#INTEGER}, {@link ContextElementType#LONG},
     * {@link ContextElementType#FLOAT}, {@link ContextElementType#DOUBLE},
     * {@link ContextElementType#BOOLEAN} can be casted.
     *
     * @param element    element to be casted
     * @param targetType type of result element
     * @return element with targetType type
     * @throws CastException if element can not be casted into targetType.
     */
    public static ContextElement cast(ContextElement element, ContextElementType targetType)
            throws CastException {
        if (element == null) {
            throw new NullPointerException("Element is null.");
        }
        if (targetType == null) {
            throw new NullPointerException("Target type is null");
        }
        if (!isCastable(element.type, targetType)) {
            throw new CastException(WordUtils.capitalize(element.type.name()) + " " +
                    "can not be casted to " + targetType.name());
        }
        ContextElement resultElement = new ContextElement(targetType);

        Object value = element.getValue("value");

        switch (element.type) {
            case BYTE:
                Byte byteValue = (Byte) value;
                switch (targetType) {
                    case BYTE:
                        return element;
                    case SHORT:
                        resultElement.setValue("value", new Short(byteValue));
                        break;
                    case INTEGER:
                        resultElement.setValue("value", new Integer(byteValue));
                        break;
                    case LONG:
                        resultElement.setValue("value", new Long(byteValue));
                        break;
                    case FLOAT:
                        resultElement.setValue("value", new Float(byteValue));
                        break;
                    case DOUBLE:
                        resultElement.setValue("value", new Double(byteValue));
                        break;
                    case BOOLEAN:
                        if (byteValue.shortValue() > 0) {
                            resultElement.setValue("value", true);
                        } else {
                            resultElement.setValue("value", false);
                        }
                        break;
                    default:
                        throw new IllegalArgumentException("Unexpected type of target element " + targetType.name());
                }
                break;
            case SHORT:
                Short shortValue = (Short) value;
                switch (targetType) {
                    case BYTE:
                        resultElement.setValue("value", new Byte(shortValue.byteValue()));
                        break;
                    case SHORT:
                        return element;
                    case INTEGER:
                        resultElement.setValue("value", new Integer(shortValue));
                        break;
                    case LONG:
                        resultElement.setValue("value", new Long(shortValue));
                        break;
                    case FLOAT:
                        resultElement.setValue("value", new Float(shortValue));
                        break;
                    case DOUBLE:
                        resultElement.setValue("value", new Double(shortValue));
                        break;
                    case BOOLEAN:
                        if (shortValue.shortValue() > 0) {
                            resultElement.setValue("value", new Boolean(true));
                        } else {
                            resultElement.setValue("value", new Boolean(false));
                        }
                        break;
                    default:
                        throw new IllegalArgumentException("Unexpected type of target element " + targetType.name());
                }
                break;
            case INTEGER:
                Integer intValue = (Integer) value;
                switch (targetType) {
                    case BYTE:
                        resultElement.setValue("value", new Byte(intValue.byteValue()));
                        break;
                    case SHORT:
                        resultElement.setValue("value", new Short(intValue.shortValue()));
                        break;
                    case INTEGER:
                        return element;
                    case LONG:
                        resultElement.setValue("value", new Long(intValue));
                        break;
                    case FLOAT:
                        resultElement.setValue("value", new Float(intValue));
                        break;
                    case DOUBLE:
                        resultElement.setValue("value", new Double(intValue));
                        break;
                    case BOOLEAN:
                        if (intValue.byteValue() > 0) {
                            resultElement.setValue("value", new Boolean(true));
                        } else {
                            resultElement.setValue("value", new Boolean(false));
                        }
                        break;
                    default:
                        throw new IllegalArgumentException("Unexpected type of target element " + targetType.name());
                }
                break;
            case LONG:
                Long longValue = (Long) value;
                switch (targetType) {
                    case BYTE:
                        resultElement.setValue("value", new Byte(longValue.byteValue()));
                        break;
                    case SHORT:
                        resultElement.setValue("value", new Short(longValue.shortValue()));
                        break;
                    case INTEGER:
                        resultElement.setValue("value", new Integer(longValue.intValue()));
                        break;
                    case LONG:
                        return element;
                    case FLOAT:
                        resultElement.setValue("value", new Float(longValue));
                        break;
                    case DOUBLE:
                        resultElement.setValue("value", new Double(longValue));
                        break;
                    case BOOLEAN:
                        if (longValue.byteValue() > 0) {
                            resultElement.setValue("value", new Boolean(true));
                        } else {
                            resultElement.setValue("value", new Boolean(false));
                        }
                        break;
                    default:
                        throw new IllegalArgumentException("Unexpected type of target element " + targetType.name());
                }
                break;
            case FLOAT:
                Float floatValue = (Float) value;
                switch (targetType) {
                    case BYTE:
                        resultElement.setValue("value", new Byte(floatValue.byteValue()));
                        break;
                    case SHORT:
                        resultElement.setValue("value", new Short(floatValue.shortValue()));
                        break;
                    case INTEGER:
                        resultElement.setValue("value", new Integer(floatValue.intValue()));
                        break;
                    case LONG:
                        resultElement.setValue("value", new Long(floatValue.longValue()));
                        break;
                    case FLOAT:
                        return element;
                    case DOUBLE:
                        resultElement.setValue("value", new Double(floatValue));
                        break;
                    case BOOLEAN:
                        if (floatValue.byteValue() > 0) {
                            resultElement.setValue("value", new Boolean(true));
                        } else {
                            resultElement.setValue("value", new Boolean(false));
                        }
                        break;
                    default:
                        throw new IllegalArgumentException("Unexpected type of target element " + targetType.name());
                }
                break;
            case DOUBLE:
                Double doubleValue = (Double) value;
                switch (targetType) {
                    case BYTE:
                        resultElement.setValue("value", new Byte(doubleValue.byteValue()));
                        break;
                    case SHORT:
                        resultElement.setValue("value", new Short(doubleValue.shortValue()));
                        break;
                    case INTEGER:
                        resultElement.setValue("value", new Integer(doubleValue.intValue()));
                        break;
                    case LONG:
                        resultElement.setValue("value", new Long(doubleValue.longValue()));
                        break;
                    case FLOAT:
                        resultElement.setValue("value", new Float(doubleValue.doubleValue()));
                        break;
                    case DOUBLE:
                        return element;
                    case BOOLEAN:
                        if (doubleValue.byteValue() > 0) {
                            resultElement.setValue("value", new Boolean(true));
                        } else {
                            resultElement.setValue("value", new Boolean(false));
                        }
                        break;
                    default:
                        throw new IllegalArgumentException("Unexpected type of target element " + targetType.name());
                }
                break;
            case BOOLEAN:
                boolean booleanValue = (Boolean) value;
                switch (targetType) {
                    case BYTE:
                        if (booleanValue) {
                            resultElement.setValue("value", new Byte((byte) 1));
                        } else {
                            resultElement.setValue("value", new Byte((byte) 0));
                        }
                        break;
                    case SHORT:
                        if (booleanValue) {
                            resultElement.setValue("value", new Short((short) 1));
                        } else {
                            resultElement.setValue("value", new Short((short) 0));
                        }
                        break;
                    case INTEGER:
                        if (booleanValue) {
                            resultElement.setValue("value", new Integer(1));
                        } else {
                            resultElement.setValue("value", new Integer(0));
                        }
                        break;
                    case LONG:
                        if (booleanValue) {
                            resultElement.setValue("value", new Long(1L));
                        } else {
                            resultElement.setValue("value", new Long(0L));
                        }
                        break;
                    case FLOAT:
                        if (booleanValue) {
                            resultElement.setValue("value", new Float(1f));
                        } else {
                            resultElement.setValue("value", new Float(0f));
                        }
                        break;
                    case DOUBLE:
                        if (booleanValue) {
                            resultElement.setValue("value", new Double(1d));
                        } else {
                            resultElement.setValue("value", new Double(0d));
                        }
                        break;
                    case BOOLEAN:
                        return element;
                    default:
                        throw new IllegalArgumentException("Unexpected type of target element " + targetType.name());
                }
                break;
            default:
                throw new IllegalArgumentException("Unexpected type of casted element " + element.type.name());
        }
        return resultElement;
    }

    /**
     * Checks if element a can be casted to b and vice-versa.
     *
     * @param a first element to be checked.
     * @param b second element to be checked.
     * @return if a can be casted to b and vice-versa.
     */
    public static boolean isCastable(ContextElementType a, ContextElementType b) {
        return (a == BYTE || a == SHORT || a == INTEGER || a == LONG || a == FLOAT || a == DOUBLE || a == BOOLEAN) &&
                (b == BYTE || b == SHORT || b == INTEGER || b == LONG || b == FLOAT || b == DOUBLE || b == BOOLEAN);
    }

}
