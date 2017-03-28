package ru.opensecreto.sigmacoin.j2p.commandhandlers;

import ru.opensecreto.sigmacoin.j2p.CommandHandler;
import ru.opensecreto.sigmacoin.j2p.Controller;

import java.io.*;
import java.net.Socket;

public class GetPeersCommandHandler implements CommandHandler {

    @Override
    public void handle(Socket socket, DataInputStream in, DataOutputStream out, Controller controller) throws IOException {
        DataOutputStream dataOut = new DataOutputStream(out);
        dataOut.writeInt(controller.getPeersDatabase().getPeers().size());
        dataOut.write(controller.getPeersDatabase().encodePeersList());
    }
}
