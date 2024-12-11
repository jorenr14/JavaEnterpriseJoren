package be.ucll.spring;

import be.ucll.entities.User;
import jakarta.jms.Message;
import jakarta.jms.MessageListener;
import jakarta.jms.ObjectMessage;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class JmsConsumer implements MessageListener {

    @Override
    @JmsListener(destination = "${active-mq.topic}")
    public void onMessage(Message message) {
        try{
            ObjectMessage objectMessage = (ObjectMessage)message;
            User employee = (User) objectMessage.getObject();
            //do additional processing
            //log.info("Received Message: "+ employee.toString());
        } catch(Exception e) {
            //log.error("Received Exception : "+ e);
        }

    }
}
