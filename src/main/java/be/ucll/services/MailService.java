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

           // MimeMessage message = mailSender.createMimeMessage();
            SimpleMailMessage message = new SimpleMailMessage();
            //MimeMessageHelper helper = new MimeMessageHelper(message, true);

            message.setFrom("reniers.joren@gmail.com");

            message.setTo(email);
            message.setSubject("Uw productbestelling");
            message.setText("Beste gebruiker, hieronder de product-ID's van uw bestelling: " + productIds);

            mailSender.send(message);
            System.out.println("E-mail verzonden naar: " + email);


            System.out.println("E-mail verzonden naar: " + email);
    }

    public String buildHtmlTable(List<String> gridData) {
        StringBuilder html = new StringBuilder("<h1>Overzicht Bestellingen</h1><table border='1'><tr><th>ID</th><th>Klant</th><th>Totaal</th><th>Afgeleverd</th></tr>");
        for (String row : gridData) {
            html.append("<tr>").append(row).append("</tr>");
        }
        html.append("</table>");
        return html.toString();
    }
//    public void sendProductIds(String to, List<Long> productIds) throws MessagingException {
//        StringBuilder body = new StringBuilder();
//        body.append("Beste Gebruiker,\n\n");
//        body.append("Hieronder vindt u de ID's van de geselecteerde producten:\n\n");
//
//        // Voeg ID's toe
//        for (Long id : productIds) {
//            body.append("- Product ID: ").append(id).append("\n");
//        }
//
//        body.append("\nMet vriendelijke groet,\nUw winkelteam");
//
//        sendmail("bestelling overzicht", body.toString());
//    }



}
