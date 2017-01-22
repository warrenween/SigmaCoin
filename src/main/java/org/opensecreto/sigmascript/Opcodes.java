package org.opensecreto.sigmascript;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

public final class Opcodes {

    /**
     * Перейти к выполнению данных из памяти.
     * <p>
     * Режим устанавливается в режим выполнение кода из памяти.
     * Указатель устнавливается в ноль.
     */
    public static final byte OP_JUMP_M = 0x00;

    /**
     * Переходит к выполнению данных из хранилища.
     * <p>
     * Режим устанавливается в режим выполнения данных из памяти.
     * Указатель устанавливается в ноль.
     */
    public static final byte OP_JUMP_S = 0x01;

    /**
     * Следующий байт после OP_PUSH заносится в вершину стеку.
     * Указатель смещается на два вперед.
     */
    public static final byte OP_PUSH = 0x10;

    /**
     * Удаляет один байт из вешины стека.
     * Указатель смещается на один вперед.
     */
    public static final byte OP_POP = 0x11;

    /**
     * Останавливает выполнение со статусом.
     * <p>
     * Если стек пустой, то статусом будет 0.
     * Если стек непустой, то статусом будет первый байт из стека.
     */
    public static final byte OP_RETURN = (byte) 0xf0;

    public static final Map<Byte, String> OPCODE_NAMES = new ImmutableMap.Builder<Byte, String>()
            .put(OP_JUMP_M, "OP_JUMP_M")
            .put(OP_JUMP_S, "OP_JUMP_S")
            .put(OP_PUSH, "OP_PUSH")
            .put(OP_POP, "OP_POP")
            .put(OP_RETURN, "OP_RETURN")
            .build();

}
