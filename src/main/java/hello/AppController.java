package hello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by kevin on 18/10/2015 for spring-websocket-disconnect
 */
@RestController
public class AppController {

    private final SessionHandler sessionHandler;

    @Autowired AppController(SessionHandler sessionHandler) {
        this.sessionHandler = sessionHandler;
    }

    @RequestMapping(value = "closeAll", method = RequestMethod.GET)
    public String closeAll(){
        sessionHandler.closeAll();
        return "OK";
    }
}
