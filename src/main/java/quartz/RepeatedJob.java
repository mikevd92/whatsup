package quartz;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.quartz.QuartzJobBean;
import toj.demo.whatsup.email.services.MailService;
import toj.demo.whatsup.message.services.MessageService;

public class RepeatedJob extends QuartzJobBean {

    private MessageService messageService;

    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    public RepeatedJob() {
    }

    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        this.messageService.removeByDeletionTimestamp();

    }
}