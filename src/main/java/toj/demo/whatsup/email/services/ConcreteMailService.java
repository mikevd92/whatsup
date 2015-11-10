package toj.demo.whatsup.email.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

/**
 * Created by mihai.popovici on 11/5/2015.
 */
public class ConcreteMailService implements MailService {

    public SerializableMailSender getMailSender() {
        return mailSender;
    }

    @Autowired
    private SerializableMailSender mailSender;

    @Override
    public void sendMail(SimpleMailMessage mailMessage) {
        mailSender.send(mailMessage);
    }
}
