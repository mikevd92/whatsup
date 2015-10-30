package quartz;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import toj.demo.whatsup.message.services.MessageService;

public class RepeatedJob extends QuartzJobBean {
    private MessageService messageService;

    public RepeatedJob() {
    }

    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        this.messageService.removeByDeletionTimestamp();
    }
}