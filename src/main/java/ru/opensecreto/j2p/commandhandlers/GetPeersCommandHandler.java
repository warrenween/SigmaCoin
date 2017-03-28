package ru.opensecreto.j2p.commandhandlers;

import ru.opensecreto.j2p.CommandHandler;
import ru.opensecreto.j2p.Controller;

import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;

public class GetPeersCommandHandler implements CommandHandler {

    @Override
    public void handle(Socket socket, InputStream in, OutputStream out, Controller controller) throws IOException {
        DataOutputStream dataOut = new DataOutputStream(out);
        dataOut.writeInt(controller.getPeersDatabase().getPeers().size());
        dataOut.write(controller.getPeersDatabase().encodePeersList());
    }
}
