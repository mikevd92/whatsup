package quartz;

import org.quartz.*;
import org.quartz.impl.StdScheduler;
import org.springframework.scheduling.quartz.QuartzJobBean;
import toj.demo.whatsup.domain.AssignedStatus;
import toj.demo.whatsup.domain.User;
import toj.demo.whatsup.email.services.MailService;
import toj.demo.whatsup.message.services.MessageService;
import toj.demo.whatsup.user.services.UserService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mihai.popovici on 11/6/2015.
 */
public class SchedulingJob extends QuartzJobBean {

    private UserService userService;

    private MessageService messageService;

    private MailService mailService;

    private StdScheduler mailScheduler;

    private List<User> unassignedUsers=new ArrayList<>();

    public void setMailService(MailService mailService) {
        this.mailService = mailService;
    }

    public void setMailScheduler(StdScheduler mailScheduler) {
        this.mailScheduler = mailScheduler;
    }

    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }


    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        unassignedUsers = userService.findAllUnassigned();
        if(unassignedUsers.size()!=0) {
            for (User user : unassignedUsers) {
                    JobDetail jobDetail = JobBuilder.newJob(MailJob.class).withIdentity("job-" + user.getId(), "mailGroup").build();
                    jobDetail.getJobDataMap().put("messageService", messageService);
                    jobDetail.getJobDataMap().put("mailService", mailService);
                    jobDetail.getJobDataMap().put("userService",userService);
                    jobDetail.getJobDataMap().put("userId", user.getId());
                    Trigger trigger = TriggerBuilder
                            .newTrigger()
                            .withIdentity("trigger-" + user.getId(), "mailTriggerGroup")
                            .withSchedule(
                                    SimpleScheduleBuilder.simpleSchedule()
                                            .withIntervalInSeconds(user.getNotificationPeriod() * 3600).repeatForever())
                            .build();
                    userService.setAssignedStatus(user, AssignedStatus.ASSIGNED);
                    try {
                        mailScheduler.scheduleJob(jobDetail, trigger);
                    } catch (SchedulerException e) {
                        e.printStackTrace();
                    }
            }
        }
    }
}
