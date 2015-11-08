package quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.mail.SimpleMailMessage;
import toj.demo.whatsup.domain.Keyword;
import toj.demo.whatsup.domain.Message;
import toj.demo.whatsup.domain.User;
import toj.demo.whatsup.email.services.MailService;
import toj.demo.whatsup.message.services.MessageService;
import toj.demo.whatsup.user.services.UserService;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Created by mihai.popovici on 11/5/2015.
 */
public class MailJob implements Job {

    private MailService mailService;

    private MessageService messageService;
    private Long userId;
    private StringBuilder stringBuilder;
    private UserService userService;
    SimpleMailMessage mailMessage;
    List<Message> messages;
    private Optional<User> userOptional;
    private Set<Keyword> keywords;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        if(messageService==null)
            messageService=(MessageService)jobExecutionContext.getMergedJobDataMap().get("messageService");
        if(mailService==null)
            mailService=(MailService)jobExecutionContext.getMergedJobDataMap().get("mailService");
        if(userService==null)
            userService=(UserService)jobExecutionContext.getMergedJobDataMap().get("userService");
        if(userId==null)
            userId=(Long)jobExecutionContext.getMergedJobDataMap().get("userId");
        userOptional=userService.findUserById(userId);
        if(userOptional.isPresent()) {
            keywords=userOptional.get().getKeywords();
            mailMessage = new SimpleMailMessage();
            mailMessage.setSubject("Messages with requested keywords");
            mailMessage.setTo(userOptional.get().getEmail());
            mailMessage.setFrom("misuvd92@yahoo.com");
            if(keywords.isEmpty()) {
                mailMessage.setText("Please add some keywords!");
                mailService.sendMail(mailMessage);
            }else{
                messages = messageService.getMessagesByKeyWords(keywords);
                if (messages.size() == 0) {
                    mailMessage.setText("No messages to display");
                    mailService.sendMail(mailMessage);
                } else {
                    stringBuilder = new StringBuilder("");
                    messages.forEach(p -> stringBuilder.append(p + "\n"));
                    mailMessage.setText(stringBuilder.toString());
                    mailService.sendMail(mailMessage);
                }
            }
        }
    }
}
