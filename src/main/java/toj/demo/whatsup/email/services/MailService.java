package toj.demo.whatsup.email.services;

import org.springframework.mail.SimpleMailMessage;

/**
 * Created by mihai.popovici on 11/5/2015.
 */
public interface MailService {
    public void sendMail(SimpleMailMessage mailMessage);
}
