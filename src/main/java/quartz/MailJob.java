package quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.mail.SimpleMailMessage;
import toj.demo.whatsup.domain.Keyword;
import toj.demo.whatsup.domain.Message;
import toj.demo.whatsup.domain.User;
import toj.demo.whatsup.email.services.MailService;
import toj.demo.whatsup.email.services.SerializableMailSender;
import toj.demo.whatsup.message.dao.MessageDAO;
import toj.demo.whatsup.user.dao.UserDAO;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Created by mihai.popovici on 11/5/2015.
 */
public class MailJob implements Job {

    private MailService mailService;

    private MessageDAO messageDAO;
    private Long userId;
    private StringBuilder stringBuilder;
    private UserDAO userDAO;
    SimpleMailMessage mailMessage;
    List<Message> messages;
    private Optional<User> userOptional;
    private Set<Keyword> keywords;
    private SerializableMailSender mailSender;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        if(messageDAO==null)
            messageDAO=(MessageDAO)jobExecutionContext.getMergedJobDataMap().get("messageDAO");
        if(mailService==null) {
            mailService = (MailService) jobExecutionContext.getMergedJobDataMap().get("mailService");
            mailSender = mailService.getMailSender();
            mailSender.setHost("smtp.mail.yahoo.com");
            mailSender.setPort(465);
            mailSender.setProtocol("smtps");
            mailSender.setUsername("misuvd92@yahoo.com");
            mailSender.setPassword("Misuvd00");
            mailSender.getJavaMailProperties().setProperty("mail.smtps.auth", "true");
            mailSender.getJavaMailProperties().setProperty("mail.smtps.starttls.enable", "true");
            mailSender.getJavaMailProperties().setProperty("mail.debug", "true");
        }
        if(userDAO==null)
            userDAO=(UserDAO)jobExecutionContext.getMergedJobDataMap().get("userDAO");
        if(userId==null)
            userId=(Long)jobExecutionContext.getMergedJobDataMap().get("userId");
        userOptional=userDAO.findUserById(userId);
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
                messages = messageDAO.getMessagesByKeyWords(keywords);
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
