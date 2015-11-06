package toj.demo.whatsup.email.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kubek2k.springockito.annotations.SpringockitoContextLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by mihai.popovici on 11/5/2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        loader = SpringockitoContextLoader.class,
        locations = {
                "classpath:jersey-spring-applicationContext.xml",
                "classpath:/toj/demo/whatsup/http/resource/resources-test.xml"
        }
)
public class MailServiceTest  {

    @Autowired
    private MailService mailService;

    @Test
    public void testSendEmail(){
        SimpleMailMessage mailMessage=new SimpleMailMessage();
        mailMessage.setFrom("misuvd92@yahoo.com");
        mailMessage.setTo("mihai.popovici@softvision.ro");
        mailMessage.setSubject("Testing Dude");
        mailMessage.setText("I love you baby!");
        mailService.sendMail(mailMessage);
    }

}
