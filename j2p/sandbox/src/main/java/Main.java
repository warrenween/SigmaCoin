import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Main {

    private static final Logger LOGGER;

    static {
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "trace");
        System.setProperty("org.slf4j.simpleLogger.warnLevelString", "trace");
        LOGGER = LoggerFactory.getLogger(Main.class);
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        if (args.length == 0) {
            LOGGER.warn("no args");
        }
        LOGGER.info("executing {}", args[0]);
        switch (args[0]) {
            case "localAddress":
                new TestAddress().run();
                break;
            case "controllerTest":
                new ControllerTest().run();
                break;
            case "stunIP":
                new TestGettingIpFromStun().run();
                break;
            default:
                System.out.println("Unknown: " + args[0]);
        }
    }

}
