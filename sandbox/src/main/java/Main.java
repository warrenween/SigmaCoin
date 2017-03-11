import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        System.out.println(args[0]);
        switch (args[0]) {
            case "localAddress":
                new TestAddress().run();
                break;
            default:
                System.out.println("Unknown: " + args[0]);
        }
    }

}
