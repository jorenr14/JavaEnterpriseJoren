package be.ucll.controller;

import be.ucll.entities.User;
import be.ucll.spring.JmsProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mail")
public class ProduceMessageController {
    @Autowired
    JmsProducer jmsProducer ;

    @PostMapping(value= "/api/user" )
    public String sendMessage(@RequestBody String to,@RequestBody String subject,@RequestBody String content){
        jmsProducer .sendMessage(to,subject,content);
        return "Bericht in que";
    }
}
