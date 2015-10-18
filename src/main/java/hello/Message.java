package hello;

import lombok.Value;

/**
 * Created by kevin on 18/10/2015 for spring-websocket-disconnect
 */
@Value
public class Message {
    Integer id;
    String body;
}
