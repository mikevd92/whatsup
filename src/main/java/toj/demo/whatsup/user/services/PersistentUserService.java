package toj.demo.whatsup.user.services;

import org.quartz.*;
import org.quartz.impl.StdScheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import quartz.MailJob;
import toj.demo.whatsup.domain.AssignedStatus;
import toj.demo.whatsup.domain.Credentials;
import toj.demo.whatsup.domain.Keyword;
import toj.demo.whatsup.domain.User;
import toj.demo.whatsup.email.services.MailService;
import toj.demo.whatsup.message.services.MessageService;
import toj.demo.whatsup.user.dao.UserDAO;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Transactional
public class PersistentUserService implements UserService {

    private UserDAO userDAO;
    @Resource(name = "mailScheduler")
    private StdScheduler mailScheduler;

    @Autowired
    private MessageService messageService;

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
        setAssignedStatus(user,AssignedStatus.UNASSIGNED);
    }

    @Override
    public void addJob(User user) throws SchedulerException {
        JobDetail jobDetail = JobBuilder.newJob(MailJob.class).withIdentity(new StringBuilder("job-").append(user.getId()).toString(), "mailGroup").build();
        jobDetail.getJobDataMap().put("messageService", messageService);
        jobDetail.getJobDataMap().put("mailService", mailService);
        jobDetail.getJobDataMap().put("userService",this);
        jobDetail.getJobDataMap().put("userId", user.getId());
        Trigger trigger = TriggerBuilder
                .newTrigger()
                .withIdentity(new StringBuilder("trigger-").append(user.getId()).toString(), "mailTriggerGroup")
                .withSchedule(
                        SimpleScheduleBuilder.simpleSchedule()
                                .withIntervalInSeconds(user.getNotificationPeriod() * 3600).repeatForever())
                .build();
        setAssignedStatus(user, AssignedStatus.ASSIGNED);
        mailScheduler.scheduleJob(jobDetail, trigger);

    }

    @Override
    public boolean checkJobExists(Long id) throws SchedulerException {
        JobKey jobKey=new JobKey(new StringBuilder("job-").append(id).toString(),"mailGroup");
        return mailScheduler.checkExists(jobKey);
    }

    @Override
    public void changeNotifyPeriod(User user, int period) {
        userDAO.changeNotifyPeriod(user, period);
    }

    public void setAssignedStatus(User user, AssignedStatus assignedStatus) {
        userDAO.setAssignedStatus(user,assignedStatus);
    }

    @Override
    public List<User> findAllAssigned() {
        return userDAO.findAllAssigned();
    }


}
