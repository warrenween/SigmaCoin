package ru.opensecreto.j2p.commandhandlers;

import ru.opensecreto.j2p.CommandHandler;
import ru.opensecreto.j2p.Controller;

import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;

public class PeerInfoHandler implements CommandHandler {

    @Override
    public void handle(Socket socket, DataInputStream in, DataOutputStream out, Controller controller) throws IOException {
        out.write(controller.version);
        out.write(controller.software.getBytes().length);
        out.write(controller.software.getBytes());
    }
}
