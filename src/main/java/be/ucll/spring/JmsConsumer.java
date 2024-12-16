package be.ucll.spring;

import be.ucll.entities.User;
import be.ucll.services.MailService;
import jakarta.jms.Message;
import jakarta.jms.MessageListener;
import jakarta.jms.ObjectMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

@Component
public class JmsConsumer implements MessageListener {
    @Autowired
    private MailService mailService;

    @Override
    @JmsListener(destination = "${active-mq.topic}")
    public void onMessage(Message message) {
        try{
            ObjectMessage objectMessage = (ObjectMessage)message;
            User employee = (User) objectMessage.getObject();
            System.out.println("Ontvangen bericht voor gebruiker: ");


        } catch(Exception e) {
            System.out.println("Received Exception : "+ e);
        }

    }
}
