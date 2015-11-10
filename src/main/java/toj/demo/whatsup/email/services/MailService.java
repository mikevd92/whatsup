package toj.demo.whatsup.email.services;

import org.springframework.mail.SimpleMailMessage;

import java.io.Serializable;

/**
 * Created by mihai.popovici on 11/5/2015.
 */
public interface MailService extends Serializable {
    public void sendMail(SimpleMailMessage mailMessage);
    SerializableMailSender getMailSender();
}
