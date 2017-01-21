package org.opensecreto.sigmascript;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

public final class Opcodes {

    /**
     * Push byte to top of stack.
     */
    public static final int OP_PUSH = 0x01;

    public static final Map<Integer, String> OPCODE_NAMES = new ImmutableMap.Builder<Integer, String>()
            .put(OP_PUSH, "OP_PUSH")
            .build();

}
