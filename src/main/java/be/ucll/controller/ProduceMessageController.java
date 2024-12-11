package be.ucll.controller;

import be.ucll.entities.User;
import be.ucll.spring.JmsProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProduceMessageController {
    @Autowired
    JmsProducer jmsProducer ;

    @PostMapping(value= "/api/user" )
    public User sendMessage(@RequestBody User user){
        jmsProducer .sendMessage(user);
        return user;
    }
}
