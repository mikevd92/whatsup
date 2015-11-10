package toj.demo.whatsup.user.services;

import org.quartz.SchedulerException;
import toj.demo.whatsup.domain.AssignedStatus;
import toj.demo.whatsup.domain.Credentials;
import toj.demo.whatsup.domain.Keyword;
import toj.demo.whatsup.domain.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserService {

    Optional<User> get(String name);

    void signup(Credentials credentials);

    boolean checkUser(Credentials credentials);

    void addFollower(User toBeFollowed,User follower);

    Optional<User> findUserById(Long userId);

    void removeFollower(User toBeFollowed,User follower);

    public void addKeywordsToUser(User user,Set<Keyword> keywords);

    public void removeKeywordsFromUser(User user,Set<Keyword> keywords);

    public void removeJob(User user) throws SchedulerException;

    public void addJob(User user) throws SchedulerException;

    public boolean checkJobExists(Long id) throws SchedulerException;

    public void changeNotifyPeriod(User user, int period) throws SchedulerException;

}
