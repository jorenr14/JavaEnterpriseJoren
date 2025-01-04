package be.ucll.services;

import jakarta.mail.MessagingException;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

     public void sendmail(String email, List<String> productIds)  {


            SimpleMailMessage message = new SimpleMailMessage();

            message.setFrom("reniers.joren@gmail.com");

            message.setTo(email);
            message.setSubject("Uw productbestelling");
            message.setText("Beste Klant,\n\nHieronder vindt u de details van uw bestelling:\n\n"
                 + String.join("\n", productIds) + "\n\nMet vriendelijke groeten,\nUw Javastore");


            mailSender.send(message);
            System.out.println("E-mail verzonden naar: " + email);


            System.out.println("E-mail verzonden naar: " + email);
    }

}
