import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.opensecreto.j2p.protocols.stun.*;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.*;

public class TestGettingIpFromStun {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestGettingIpFromStun.class);

    private InetSocketAddress getRandomServer() {
        return Servers.servers.get(new SecureRandom().nextInt(Servers.servers.size()));
    }

    public void run() throws InterruptedException, UnknownHostException {
        List<Future<StunMessage>> messages = new ArrayList<>(5);
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < 10; i++) {
            StunAgent agent = new StunAgent(getRandomServer(), new StunMessage(
                    MessageClass.REQUEST,
                    MessageMethod.BINDING,
                    new byte[12],
                    Collections.EMPTY_LIST
            ), 1);
            messages.add(executorService.submit(agent));
        }

        for (int i = 0; i < 5; i++) {
            TimeUnit.SECONDS.sleep(5);

            if (messages.size() == 0) {
                executorService.shutdownNow();
                return;
            } else {
                Iterator<Future<StunMessage>> iterator = messages.iterator();
                while (iterator.hasNext()) {
                    Future<StunMessage> item = iterator.next();
                    try {
                        if (item.isDone() && item.get() != null) {
                            StunMessage message = item.get();
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
                            iterator.remove();
                        }
                    } catch (Throwable e) {
                        LOGGER.error("Could not finish task", e);
                        iterator.remove();
                    }
                }
            }
        }
        executorService.shutdownNow();
    }

}
