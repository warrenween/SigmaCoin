package ru.opensecreto.sigmacoin.vm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.DatatypeConverter;

public class BytecodeExecutor {

    public static final Logger LOGGER = LoggerFactory.getLogger(BytecodeExecutor.class);

    private final VMConfiguration configuration;
    private final VirtualMachineController controller;

    private final Frame frame;
    private long pointer = 0;

    public BytecodeExecutor(VMConfiguration configuration, Frame frame, VirtualMachineController controller) {
        this.frame = frame;
        this.configuration = configuration;
        this.controller = controller;
    }

    public Stack run() {
        boolean run = true;
        boolean success = true;
        while (run) {
            switch (frame.memory.get(pointer)) {
                case Opcodes.STOP_BAD:
                    run = false;
                    success = false;
                    break;


                case Opcodes.STOP_GOOD:
                    run = false;
                    break;


                case Opcodes.INVOKE:
                    if (frame.stack.getSize() < (configuration.contractIdLength + Short.BYTES)) {
                        LOGGER.warn("Error while executing contract {} at {}. " +
                                        "Can not invoke - stack size is less than required minimum {} bytes.",
                                frame.contractID, pointer, configuration.contractIdLength + Short.BYTES);
                        run = false;
                        success = false;
                    }
                    int dataSize = frame.stack.popShort();
                    if (frame.stack.getSize() < (configuration.contractIdLength + Short.BYTES + dataSize)) {
                        LOGGER.warn("Error while executing contract {} at {}. " +
                                        "Can not invoke - stack size is less than {} bytes.",
                                frame.contractID, pointer, configuration.contractIdLength + Short.BYTES + dataSize);
                        run = false;
                        success = false;
                    }
                    ContractID contractId = new ContractID(frame.stack.popCustom(configuration.contractIdLength));
                    Stack stackInvoke = new Stack(configuration.stackSize);

                    Stack result = controller.invoke(stackInvoke, contractId);
                    frame.stack.pushCustom(result.getStack());
                    pointer++;
                    break;


                case Opcodes.PUSH:
                    frame.stack.push(frame.memory.get(pointer + 1));
                    pointer += 2;
                    break;


                case Opcodes.POP:
                    frame.stack.pop();
                    pointer++;
                    break;


                case Opcodes.DUP:
                    byte data = frame.stack.pop();
                    frame.stack.push(data);
                    frame.stack.push(data);
                    pointer++;
                    break;


                default:
                    run = false;
                    success = false;
                    LOGGER.warn("Error while executing {} - unexpected bytecode 0x{} at {}.",
                            frame.contractID, DatatypeConverter.printHexBinary(new byte[]{frame.memory.get(pointer)}), pointer);
            }
        }

        frame.stack.pushShort((short) frame.stack.getSize());

        if (success) {
            frame.stack.push((byte) 0x00);
        } else {
            frame.stack.push((byte) 0x01);
        }

        return frame.stack;
    }
}
