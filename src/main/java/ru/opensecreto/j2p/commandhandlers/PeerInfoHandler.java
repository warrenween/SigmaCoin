package ru.opensecreto.j2p.commandhandlers;

import ru.opensecreto.j2p.CommandHandler;
import ru.opensecreto.j2p.Controller;

import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;

public class PeerInfoHandler implements CommandHandler {

    @Override
    public void handle(Socket socket, DataInputStream in, DataOutputStream out, Controller controller) throws IOException {
        //version + softwarebytes.length + softwarebytes
        ByteBuffer buf = ByteBuffer.allocate(Integer.BYTES + Integer.BYTES + controller.software.getBytes().length);
        buf.putInt(controller.version);
        buf.putInt(controller.software.getBytes().length);
        buf.put(controller.software.getBytes());
        out.write(buf.array());
    }
}
