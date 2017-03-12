import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        switch (args[0]) {
            case "localAddress":
                new TestAddress().run();
                break;
            case "controllerTest":
                new ControllerTest().run();
                break;
            default:
                System.out.println("Unknown: " + args[0]);
        }
    }

}
