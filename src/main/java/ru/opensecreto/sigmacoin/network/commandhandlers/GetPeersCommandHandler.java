package ru.opensecreto.sigmacoin.network.commandhandlers;

import ru.opensecreto.sigmacoin.network.CommandHandler;
import ru.opensecreto.sigmacoin.network.Controller;

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
