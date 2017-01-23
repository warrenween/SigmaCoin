package org.opensecreto.sigmascript;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;

public final class Opcodes {

    /**
     * Останавливает выполнение.
     */
    public static final byte OP_STOP = (byte) 0x00;

    /**
     * Перейти к выполнению данных из памяти.
     * <p>
     * Режим устанавливается в режим выполнение кода из памяти.
     * Указатель устнавливается в ноль.
     */
    public static final byte OP_JUMP_M = 0x01;

    /**
     * Переходит к выполнению данных из хранилища.
     * <p>
     * Режим устанавливается в режим выполнения данных из памяти.
     * Указатель устанавливается в ноль.
     */
    public static final byte OP_JUMP_S = 0x02;

    /**
     * Следующий байт после OP_PUSH заносится в вершину стеку.
     * Указатель смещается на два вперед.
     */
    public static final byte OP_PUSH = 0x10;

    /**
     * Удаляет один байт из вершины стека.
     * Указатель смещается на один вперед.
     */
    public static final byte OP_POP = 0x11;

    /**
     * Помещает байт из стека в память.
     * <p>
     * ^ Вершина стека<p>
     * | <- байт, который будет отправлен в память<p>
     * | <- старший байт адреса<p>
     * |<p>
     * | <- младший бит адреса<p>
     * Указатель смещается на 1 вперед.
     * <p>
     * <b>Внимание:</b> после этой операции байты из памяти <b>не удаляются</b>
     */
    public static final byte OP_MEM_PUT = 0x20;

    public static final BiMap<Byte, String> OPCODE_NAMES = new ImmutableBiMap.Builder<Byte, String>()
            .put(OP_JUMP_M, "OP_JUMP_M")
            .put(OP_JUMP_S, "OP_JUMP_S")
            .put(OP_PUSH, "OP_PUSH")
            .put(OP_POP, "OP_POP")
            .put(OP_MEM_PUT, "OP_MEM_PUT")
            .put(OP_STOP, "OP_STOP")
            .build();

}
