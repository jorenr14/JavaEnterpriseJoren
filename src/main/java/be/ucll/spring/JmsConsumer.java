package be.ucll.spring;

import be.ucll.entities.User;
import be.ucll.services.MailService;
import jakarta.jms.*;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JmsConsumer {

    @Autowired
    private MailService mailService;

    @Value("${jms.queue-name}")
    private String queueName ;

    public JmsConsumer(MailService mailService) {
        this.mailService = mailService;
    }

    @JmsListener(destination = "${jms.queue-name}")
    public void receiveMessage(String message) {
        System.out.println("Bericht ontvangen: " + message);

        // Split het bericht
        String[] parts = message.split("::");
        String email = parts[0];
        List<String> gridData = Arrays.asList(parts[1].split(";"));

        // Toon e-mailinhoud in de console
        System.out.println("E-mail naar: " + email);
        System.out.println("Inhoud:");
        gridData.forEach(System.out::println);

        // Later hier de daadwerkelijke mail verzendlogica toevoegen!
    }

//    @JmsListener(destination = "${jms.queue-name}")
//    public void receiveMessage(String message) throws JMSException, MessagingException {
//        try {
//            // Split de email en de grid-data
//            String[] parts = message.split("::");
//            String email = parts[0];
//            List<String> gridData = Arrays.asList(parts[1].split(","));
//
//            // Bouw HTML-tabel en verstuur de e-mail
//            String htmlContent = mailService.buildHtmlTable(gridData);
//            mailService.sendmail(email, htmlContent);
//
//            System.out.println("E-mail verzonden naar: " + email);
//        } catch (MessagingException e) {
//            e.printStackTrace();
//        }
    }



