package org.opensecreto.sigmascript.bytecode;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public class Memory {

    private final Map<Long, Byte> memory = new HashMap<>();

    public static Memory assemble(byte[] in) {
        Memory memory = new Memory();
        if (in.length % 9 != 0) {
            throw new IllegalArgumentException("Input has wrong length.");
        }
        ByteBuffer buffer = ByteBuffer.wrap(in);
        for (int i = 0; i < buffer.remaining() / 9; i++) {
            memory.put(buffer.getLong(), buffer.get());
        }
        return memory;
    }

    public void put(long index, byte value) {
        if (index < 0) {
            throw new IllegalArgumentException("Index can not be negative");
        }
        memory.put(index, value);
    }

    public byte get(long index) {
        if (index < 0) {
            throw new IllegalArgumentException("Index can not be negative");
        }
        try {
            return memory.get(index);
        } catch (NullPointerException e) {
            return 0;
        }
    }

    public byte[] disassemble() {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES * memory.size() + memory.size());

        memory.forEach((aLong, aByte) -> {
            buffer.putLong(aLong);
            buffer.put(aByte);
        });

        return buffer.array();
    }

}
