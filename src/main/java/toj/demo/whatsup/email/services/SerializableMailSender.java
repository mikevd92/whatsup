package toj.demo.whatsup.email.services;

/**
 * Created by mihai.popovici on 11/10/2015.
 */
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.io.Serializable;

public class SerializableMailSender extends JavaMailSenderImpl implements Serializable {
    private static final long serialVersionUID = 6L;
}
