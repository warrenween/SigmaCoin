package ru.opensecreto.sigmacoin.j2p.commandhandlers;

import ru.opensecreto.sigmacoin.j2p.CommandHandler;
import ru.opensecreto.sigmacoin.j2p.Controller;

import java.io.*;
import java.net.Socket;

public class PeerInfoHandler implements CommandHandler {

    @Override
    public void handle(Socket socket, DataInputStream in, DataOutputStream out, Controller controller) throws IOException {
        out.write(controller.version);
        out.write(controller.software.getBytes().length);
        out.write(controller.software.getBytes());
    }
}
