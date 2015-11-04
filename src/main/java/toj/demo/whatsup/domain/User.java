package toj.demo.whatsup.domain;

import jersey.repackaged.com.google.common.collect.ImmutableSet;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.impl.matchers.GroupMatcher;

import javax.persistence.*;
import javax.security.auth.Subject;
import java.security.Principal;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by mihai.popovici on 9/24/2015.
 */
@Entity(name = "Users")
public class User implements Principal {

    @Id
    @GeneratedValue
    @Column(name="Id")
    private long Id;

    @Column(unique=true)
    private String username;

    @Column(unique = true)
    private String email;

    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="Followers",
            joinColumns = {@JoinColumn(name = "userId",referencedColumnName="ID")},
            inverseJoinColumns = {@JoinColumn(name = "followerId",referencedColumnName = "ID")} )
    private Set<User> followers;
    public User() {
        followers = new LinkedHashSet<User>();
    }

    public User(String username, String password,String email) {
        this.username = username;
        this.password = password;
        this.email = email;
        followers = new LinkedHashSet<User>();
    }

    public String getEmail() {
        return email;
    }

    public void addFollower(User user) {
        followers.add(user);
    }

    public void removeFollower(User user) {
        followers.remove(user);
    }
    public long getId() {
        return Id;
    }

    public Set<User> getFollowers() {
        return ImmutableSet.copyOf(followers);
    }

    public String getUsername() {
        return username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;

        User user = (User) o;

        if (getUsername() != null ? !getUsername().equals(user.getUsername()) : user.getUsername() != null)
            return false;
        return !(password != null ? !password.equals(user.password) : user.password != null);

    }

    @Override
    public int hashCode() {
        int result = getUsername() != null ? getUsername().hashCode() : 0;
        result = 31 * result + (password != null ? password.hashCode() : 0);
        return result;
    }

    @Override
    public String getName() {
        return username;
    }

    @Override
    public boolean implies(Subject subject) {
        return false;
    }

}
