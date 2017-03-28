import ru.opensecreto.j2p.NetUtils;

import java.io.IOException;

public class TestAddress {

    public void run() throws IOException {
        System.out.println(NetUtils.getExternalIp());
    }

}
