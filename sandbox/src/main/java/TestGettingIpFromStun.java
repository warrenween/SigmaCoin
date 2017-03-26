import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.opensecreto.j2p.protocols.stun.*;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

public class TestGettingIpFromStun {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestGettingIpFromStun.class);

    private InetSocketAddress getRandomServer() {
        return Servers.servers.get(new Random().nextInt(Servers.servers.size()));
    }

    public void run() throws InterruptedException {
        List<Future<StunMessage>> messages = new ArrayList<>(5);
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 5; i++) {
            StunAgent agent = new StunAgent(getRandomServer(), new StunMessage(
                    MessageClass.REQUEST,
                    MessageMethod.BINDING,
                    new byte[12],
                    Collections.EMPTY_LIST
            ));
            messages.add(executorService.submit(agent));
        }

        for (int i = 0; i < 5; i++) {
            TimeUnit.SECONDS.sleep(5);

            if (messages.size() == 0) {
                executorService.shutdownNow();
                return;
            } else {
                messages.forEach(stunMessageFuture -> {
                    if (stunMessageFuture.isDone()) {
                        try {
                            StunMessage message = stunMessageFuture.get();
                            List<Attribute> attributes = Attribute.decode(message.getData());
                            attributes.forEach(attribute -> {
                                try {
                                    if (attribute.type == Attribute.MAPPED_ADDRESS) {
                                        LOGGER.info("IP: {}", AttributeParser.parseMAPPED_ADDRESS(attribute));
                                    } else if (attribute.type == Attribute.XOR_MAPPED_ADDRESS) {
                                        LOGGER.info("IP: {}", AttributeParser.parseXOR_MAPPED_ADDRESS(attribute, new byte[12]));
                                    }
                                } catch (UnknownHostException e) {
                                    LOGGER.error("Could not parse ip", e);
                                }
                            });
                        } catch (Throwable e) {
                            LOGGER.error("Could not finish task", e);
                            messages.remove(stunMessageFuture);
                        }
                    }
                });
            }
        }
        executorService.shutdownNow();
    }

}
