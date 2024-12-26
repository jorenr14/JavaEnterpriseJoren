package be.ucll.services;

import jakarta.mail.MessagingException;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MailService {

    private JavaMailSender mailSender;



    public void sendmail(String to, String subject, String body) throws MessagingException {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message); // E-mail verzenden
        System.out.println("E-mail verzonden naar: " + to);
    }
    public void sendProductIds(String to, List<Long> productIds) throws MessagingException {
        StringBuilder body = new StringBuilder();
        body.append("Beste Gebruiker,\n\n");
        body.append("Hieronder vindt u de ID's van de geselecteerde producten:\n\n");

        // Voeg ID's toe
        for (Long id : productIds) {
            body.append("- Product ID: ").append(id).append("\n");
        }

        body.append("\nMet vriendelijke groet,\nUw winkelteam");

        sendmail(to,"bestelling overzicht", body.toString());
    }



}
