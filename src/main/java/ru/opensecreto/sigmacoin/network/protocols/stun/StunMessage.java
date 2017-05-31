package ru.opensecreto.sigmacoin.network.protocols.stun;

import ru.opensecreto.openutil.Util;

import java.util.Arrays;

public class StunMessage {

    public static final byte[] MAGIC_COOKIE = {0x21, 0x12, (byte) 0xA4, 0x42};

    private final byte[] stunMessage;

    public StunMessage(byte[] message) {
        this(message, 0, message.length);
    }

    public StunMessage(byte[] message, int offset, int length) {
        this.stunMessage = new byte[length];
        System.arraycopy(message, offset, stunMessage, 0, length);
    }

    public StunMessage(MessageClass messageClass, MessageMethod messageMethod, byte[] id, byte[] message) {
        this(MessageClass.convert(messageClass), MessageMethod.convert(messageMethod), id, message);
    }

    public StunMessage(MessageClass messageClass, MessageMethod messageMethod, byte[] id, Iterable<Attribute> attributes) {
        this(MessageClass.convert(messageClass), MessageMethod.convert(messageMethod), id, Attribute.encodeAll(attributes));
    }

    public StunMessage(byte[] messageClass, byte[] messageMethod, byte[] id, byte[] message) throws IllegalArgumentException {
        if (messageClass == null) throw new NullPointerException("messageClass is null");
        if (messageMethod == null) throw new NullPointerException("messageMethod is null");
        if (id == null) throw new NullPointerException("id is null");
        if (message == null) throw new NullPointerException("message is null");

        if (messageClass.length != 2) throw new IllegalArgumentException("wrong messageClass length");
        if (!(Arrays.equals(messageClass, MessageClass.REQUEST_BYTES)) ||
                Arrays.equals(messageClass, MessageClass.SUCCESS_BYTES) ||
                Arrays.equals(messageClass, MessageClass.FAILURE_BYTES) ||
                Arrays.equals(messageClass, MessageClass.INDICATION_BYTES))
            throw new IllegalArgumentException("bad messageClass");
        if (messageMethod.length != 2) throw new IllegalArgumentException("bad messageMethod");
        if (id.length != 12) throw new IllegalArgumentException("bad id length");

        stunMessage = new byte[20 + message.length];

        for (int i = 0; i < messageClass.length; i++) {
            stunMessage[i] |= messageClass[i];
        }
        for (int i = 0; i < messageMethod.length; i++) {
            stunMessage[i] |= messageMethod[i];
        }
        stunMessage[2] = (byte) ((message.length & 0xff00) >> 8);
        stunMessage[3] = (byte) (message.length & 0xff);
        System.arraycopy(MAGIC_COOKIE, 0, stunMessage, 4, MAGIC_COOKIE.length);
        System.arraycopy(id, 0, stunMessage, 8, id.length);
        System.arraycopy(message, 0, stunMessage, 20, message.length);
    }

    public byte[] getStunMessage() {
        return Util.cloneArray(stunMessage);
    }

    public MessageClass getMessageClass() {
        byte[] classData = new byte[2];
        classData[0] = (byte) (stunMessage[0] & MessageClass.MASK[0]);
        classData[1] = (byte) (stunMessage[1] & MessageClass.MASK[1]);
        return MessageClass.convert(classData);
    }

    public MessageMethod getMessageMethod() {
        byte[] methodData = new byte[2];
        methodData[0] = (byte) (stunMessage[0] & MessageMethod.MASK[0]);
        methodData[1] = (byte) (stunMessage[1] & MessageMethod.MASK[1]);
        return MessageMethod.convert(methodData);
    }

    public byte[] getData() {
        byte[] result = new byte[stunMessage.length - 20];
        System.arraycopy(stunMessage, 20, result, 0, result.length);
        return result;
    }

    public byte[] getID() {
        byte[] id = new byte[12];
        System.arraycopy(stunMessage, 8, id, 0, id.length);
        return id;
    }
}
