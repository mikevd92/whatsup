package toj.demo.whatsup.user.services;

import org.quartz.*;
import org.quartz.impl.StdScheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import quartz.MailJob;
import toj.demo.whatsup.domain.Credentials;
import toj.demo.whatsup.domain.Keyword;
import toj.demo.whatsup.domain.User;
import toj.demo.whatsup.email.services.MailService;
import toj.demo.whatsup.message.dao.MessageDAO;
import toj.demo.whatsup.user.dao.UserDAO;

import javax.annotation.Resource;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;
import java.util.Set;


@Transactional
public class PersistentUserService implements UserService {

    private UserDAO userDAO;
    @Resource(name = "mailScheduler")
    private StdScheduler mailScheduler;

    @Autowired
    private MessageDAO messageDAO;

    @Autowired
    private MailService mailService;

    public PersistentUserService(){

    }
    @Autowired
    public PersistentUserService(UserDAO userDAO){
        this.userDAO = userDAO;
    }

    @Override
    public Optional<User> get(String name) {
        return userDAO.findUserByName(name);
    }

    @Override
    public void signup(Credentials credentials) {
        User user=new User(credentials.getUsername(), credentials.getPassword(),credentials.getEmail());
        userDAO.save(user);
    }

    @Override
    public boolean checkUser(Credentials credentials) {
        return userDAO.checkUser(credentials.getUsername(), credentials.getPassword());
    }

    @Override
    public void addFollower(User toBeFollowed, User follower) {
         userDAO.addFollower(toBeFollowed, follower);
    }

    @Override
    public Optional<User> findUserById(Long userId) {
        return userDAO.findUserById(userId);
    }

    @Override
    public void removeFollower(User toBeFollowed, User follower) {
         userDAO.removeFollower(toBeFollowed, follower);
    }

    @Override
    public void addKeywordsToUser(User user, Set<Keyword> keywords) {
        userDAO.addKeyWordsToUser(user, keywords);

    }

    @Override
    public void removeKeywordsFromUser(User user, Set<Keyword> keywords) {
        userDAO.removeKeywordsFromUser(user,keywords);
    }

    @Override
    public void removeJob(User user) throws SchedulerException {
        String keyString=new StringBuilder("job-").append(user.getId()).toString();
        JobKey key=new JobKey(keyString,"mailGroup");
        mailScheduler.pauseJob(key);
        mailScheduler.deleteJob(key);
    }

    @Override
    public void addJob(User user) throws SchedulerException {
        JobDetail jobDetail = JobBuilder.newJob(MailJob.class).withIdentity(new StringBuilder("job-").append(user.getId()).toString(), "mailGroup").build();
        jobDetail.getJobDataMap().put("messageDAO", messageDAO);
        jobDetail.getJobDataMap().put("mailService", mailService);
        jobDetail.getJobDataMap().put("userDAO",userDAO);
        jobDetail.getJobDataMap().put("userId", user.getId());
        Trigger trigger = TriggerBuilder
                .newTrigger()
                .withIdentity(new StringBuilder("trigger-").append(user.getId()).toString(), "mailTriggerGroup")
                .withSchedule(
                        SimpleScheduleBuilder.simpleSchedule()
                                .withIntervalInHours(user.getNotificationPeriod()).repeatForever())
                               // .withIntervalInSeconds(user.getNotificationPeriod()).repeatForever())
                .startAt(Date.from(Instant.now().plus(user.getNotificationPeriod(), ChronoUnit.HOURS)))
                .build();
        mailScheduler.scheduleJob(jobDetail, trigger);

    }

    @Override
    public boolean checkJobExists(Long id) throws SchedulerException {
        JobKey jobKey=new JobKey(new StringBuilder("job-").append(id).toString(),"mailGroup");
        return mailScheduler.checkExists(jobKey);
    }

    @Override
    public void changeNotifyPeriod(User user, int period) throws SchedulerException {
        TriggerKey triggerKey=new TriggerKey(new StringBuilder("trigger-").append(user.getId()).toString(), "mailTriggerGroup");
        if(mailScheduler.checkExists(triggerKey)) {
            Date oldFireTime = mailScheduler.getTrigger(triggerKey).getNextFireTime();
            Trigger newTrigger = TriggerBuilder
                    .newTrigger()
                    .withIdentity(new StringBuilder("trigger-").append(user.getId()).toString(), "mailTriggerGroup")
                    .withSchedule(
                            SimpleScheduleBuilder.simpleSchedule()
                                    .withIntervalInHours(period).repeatForever())
                                    //.withIntervalInSeconds(period).repeatForever())
                    .startAt(oldFireTime)
                    .build();
            JobKey jobKey=new JobKey(new StringBuilder("job-").append(user.getId()).toString(),"mailGroup");
            mailScheduler.pauseJob(jobKey);
            mailScheduler.rescheduleJob(newTrigger.getKey(), newTrigger);
            mailScheduler.resumeJob(jobKey);
        }
        userDAO.changeNotifyPeriod(user, period);
    }


}
