package io.github.alexfx1.domain.utils;

import io.github.alexfx1.domain.dto.EmailDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.util.Date;
import javax.mail.Message;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@Slf4j
@Component
public class EmailUtils {

    private String errorMsg;

    /**
     Outgoing Mail (SMTP) Server
     requires TLS or SSL: smtp.gmail.com (use authentication)
     Use Authentication: Yes
     Port for TLS/STARTTLS: 587
     */
    public EmailDTO sendEmail(EmailDTO emailDTO) {
        // https://www.digitalocean.com/community/tutorials/javamail-example-send-mail-in-java-smtp
        // App Manager - https://support.google.com/accounts/answer/185833?hl=en&dark=1&sjid=6697788424356366176-SA

        log.info("TLSEmail Start");
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
        props.put("mail.smtp.port", "587"); //TLS Port
        props.put("mail.smtp.auth", "true"); //enable authentication
        props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS

        Authenticator auth = new Authenticator() {
            //override the getPasswordAuthentication method
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailDTO.getFromEmail(), emailDTO.getPassword());
            }
        };
        Session session = Session.getInstance(props, auth);

        boolean sent = sendEmail(session, emailDTO.getToEmail(), emailDTO.getSubject(), emailDTO.getBody());
        emailDTO.setSent(sent);
        emailDTO.setMsgError(errorMsg);

        return emailDTO;
    }

    private boolean sendEmail(Session session, String toEmail, String subject, String body){
        try
        {
            MimeMessage msg = new MimeMessage(session);
            //set message headers
            msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
            msg.addHeader("format", "flowed");
            msg.addHeader("Content-Transfer-Encoding", "8bit");

            msg.setFrom(new InternetAddress("no_reply@example.com", "NoReply-JD"));

            msg.setReplyTo(InternetAddress.parse("no_reply@example.com", false));

            msg.setSubject(subject, "UTF-8");

            msg.setText(body, "UTF-8");

            msg.setSentDate(new Date());

            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
            log.info("Message is ready");
            Transport.send(msg);

            log.info("EMail Sent Successfully!!");

            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            errorMsg = e.getMessage();
            return false;
        }
    }
}
