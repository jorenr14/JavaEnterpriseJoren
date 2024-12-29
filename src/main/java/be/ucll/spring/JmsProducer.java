package be.ucll.spring;

import be.ucll.entities.User;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JmsProducer {
    @Autowired
    JmsTemplate jmsTemplate ;

    public JmsProducer(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }


    @Value("email-queue")
    private String queueName ;

    @Value("${active-mq.topic:default-topic}")
    private String topic ;

    public void sendMessage(String email, List<String> gridData) {
        String message = email + "::" + String.join(";", gridData);
        jmsTemplate.convertAndSend(queueName, message);
        System.out.println("Bericht verzonden naar queue: " + queueName);
    }

//    public void sendEmailRequest(String email, List<Long> productIds) {
//        // Verstuur bericht naar de opgegeven queue
//        jmsTemplate.send(queueName, new MessageCreator() {
//            @Override
//            public Message createMessage(Session session) throws JMSException {
//                String messageBody = email + ";" + productIds.toString();
//                return session.createTextMessage(messageBody);
//            }
//        });
//    }
}

