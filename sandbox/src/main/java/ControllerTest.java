import org.slf4j.LoggerFactory;
import ru.opensecreto.j2p.Controller;

import java.io.*;

public class ControllerTest {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(ControllerTest.class);

    public void run() throws IOException {
        new Controller(new File("peers.db"), (socket, controller) -> {
            Thread thread = new Thread(() -> {
                System.out.println(socket.getLocalAddress());
                try {
                    DataInputStream in = new DataInputStream(socket.getInputStream());
                    DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                    while (true) {
                        if (in.available()>0) {
                            out.write(in.read());
                        } else {
                            Thread.sleep(500);
                        }
                    }
                } catch (IOException e) {
                    LOGGER.error("exception", e);
                } catch (InterruptedException e) {
                    LOGGER.error("exception", e);
                }
            });
            thread.start();
        });
    }

}
