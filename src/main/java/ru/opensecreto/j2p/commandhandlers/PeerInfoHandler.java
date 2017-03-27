package ru.opensecreto.j2p.commandhandlers;

import ru.opensecreto.j2p.CommandHandler;
import ru.opensecreto.j2p.Controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;

public class PeerInfoHandler implements CommandHandler {

    @Override
    public void handle(Socket socket, InputStream in, OutputStream out, Controller controller) throws IOException {
        //version + softwarebytes.length + softwarebytes
        ByteBuffer buf = ByteBuffer.allocate(Integer.BYTES + Integer.BYTES + controller.software.getBytes().length);
        buf.putInt(controller.version);
        buf.putInt(controller.software.getBytes().length);
        buf.put(controller.software.getBytes());
        out.write(buf.array());
    }
}
