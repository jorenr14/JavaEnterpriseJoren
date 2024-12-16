package be.ucll.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    private MailSender mailSender;


    public void setMailSender(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendSummaryMail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText("Beste Gebruiker, hieronder\n" +
                "het gevraagde overzicht van de bestellingen via email"+ text);

        mailSender.send(message);
    }


}
