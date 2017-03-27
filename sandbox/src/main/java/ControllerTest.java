import org.slf4j.LoggerFactory;
import ru.opensecreto.j2p.Controller;
import ru.opensecreto.j2p.WelcomeRunnable;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class ControllerTest {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(ControllerTest.class);

    public void run() throws IOException {
        Controller controller = new Controller(new File("peers.db"), "j2p sandbox", 1);
        Scanner scanner = new Scanner(System.in);
        scanner.next();
        controller.stop();
    }

}
