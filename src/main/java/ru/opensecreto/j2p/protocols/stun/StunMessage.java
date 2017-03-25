package ru.opensecreto.j2p.protocols.stun;

import ru.opensecreto.openutil.Util;

import java.util.Arrays;

public class StunMessage {

    public static final byte[] MAGIC_COOKIE = {0x21, 0x12, (byte) 0xA4, 0x42};

    private final byte[] stunMessage;

    public StunMessage(byte[] message) {
        this.stunMessage = Util.cloneArray(message);
    }

    public StunMessage(MessageClass messageClass, MessageMethod messageMethod, byte[] id, byte[] message) {
        this(MessageClass.convert(messageClass), MessageMethod.convert(messageMethod), id, message);
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

}
