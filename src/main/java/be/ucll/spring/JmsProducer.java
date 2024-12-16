package be.ucll.spring;

import be.ucll.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class JmsProducer {
    @Autowired
    JmsTemplate jmsTemplate ;

    @Value("${active-mq.topic:default-topic}")
    private String topic ;

    public void sendMessage(User message){
       jmsTemplate .convertAndSend( topic , message);
    }
}

