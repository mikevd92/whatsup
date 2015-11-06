package quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.mail.SimpleMailMessage;
import toj.demo.whatsup.domain.Message;
import toj.demo.whatsup.domain.User;
import toj.demo.whatsup.email.services.MailService;
import toj.demo.whatsup.message.services.MessageService;

import java.util.List;

/**
 * Created by mihai.popovici on 11/5/2015.
 */
public class MailJob implements Job {

    private MailService mailService;

    private MessageService messageService;
    private User user;
    private StringBuilder stringBuilder;
    SimpleMailMessage mailMessage;
    List<Message> messages;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        if(messageService==null)
            messageService=(MessageService)jobExecutionContext.getMergedJobDataMap().get("messageService");
        if(mailService==null)
            mailService=(MailService)jobExecutionContext.getMergedJobDataMap().get("mailService");
        if(user==null)
            user=(User)jobExecutionContext.getMergedJobDataMap().get("user");
        messages=messageService.getMessagesByKeyWords(user.getKeywords());
        mailMessage=new SimpleMailMessage();
        if(messages.size()!=0) {
            stringBuilder=new StringBuilder("");
            messages.forEach(p -> stringBuilder.append(p + "\n"));
            mailMessage.setText(stringBuilder.toString());
            mailMessage.setSubject("Messages with requested keywords");
            mailMessage.setTo(user.getEmail());
            mailMessage.setFrom("misuvd92@yahoo.com");
            mailService.sendMail(mailMessage);
        }else{
            mailMessage.setText("No messages to display");
            mailMessage.setSubject("Messages with requested keywords");
            mailMessage.setTo(user.getEmail());
            mailMessage.setFrom("misuvd92@yahoo.com");
            mailService.sendMail(mailMessage);
        }
    }
}
