package hello;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class SessionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(SessionHandler.class);
    private final Map<String, WebSocketSession> sessionMap = new ConcurrentHashMap<>();

    AtomicInteger number = new AtomicInteger(0);

    @Autowired SessionHandler(SimpMessagingTemplate template) {

        /*Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(this::closeAll, 10, 10, TimeUnit.SECONDS);*/
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(sendMessage(template, number), 0, 2, TimeUnit.SECONDS);
    }

    private Runnable sendMessage(SimpMessagingTemplate template, AtomicInteger number) {
        return () -> {
            Integer id = number.addAndGet(1);
            template.convertAndSend("/topic/hello", new Message(id, "aMessage n°" + id));
        };
    }

    public void closeAll() {
        LOGGER.info("Closing all current session");
        sessionMap.keySet().forEach(this::close);
    }

    public void register(WebSocketSession session) {
        sessionMap.put(session.getId(), session);
    }

    private void close(String key) {
        try {
            sessionMap.get(key).close();
            sessionMap.remove(key);
        } catch (IOException e) {
            LOGGER.error("Error while closing websocket session: {}", e);
        }
    }
}
