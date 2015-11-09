package quartz;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import toj.demo.whatsup.domain.User;
import toj.demo.whatsup.user.services.UserService;

import java.util.List;

/**
 * Created by mihai.popovici on 11/6/2015.
 */
public class RecoveryJob extends QuartzJobBean {


    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    private UserService userService;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        List<User> users=userService.findAllAssigned();
        if(users.size()!=0) {
            try {
                for (User user : users) {
                    userService.addJob(user);
                }
            } catch (SchedulerException e) {
                e.printStackTrace();
            }
        }
    }
}
