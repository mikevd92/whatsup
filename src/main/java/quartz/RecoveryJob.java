package quartz;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import toj.demo.whatsup.user.services.UserService;

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
        userService.resetHasJobAssigned();
    }
}
