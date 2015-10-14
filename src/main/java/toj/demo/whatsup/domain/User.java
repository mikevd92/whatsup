package toj.demo.whatsup.domain;

import javax.persistence.*;
import javax.security.auth.Subject;
import java.io.Serializable;
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

    private String username;

    private String password;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="Followers",
            joinColumns = {@JoinColumn(name = "userId",referencedColumnName="ID")},
            inverseJoinColumns = {@JoinColumn(name = "followerId",referencedColumnName = "ID")} )
    private Set<User> followers;

    public User() {
        followers = new LinkedHashSet<User>();
    }


    public User(String username, String password) {
        this.username = username;
        this.password = password;
        followers = new LinkedHashSet<User>();
    }

    public void addFollower(User user) {
        followers.add(user);
    }

    public void removeFollower(User user) {
        followers.remove(user);
    }

    public Set<User> getFollowers() {
        return followers;
    }

    public String getPassword() {
        return password;
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
        return !(getPassword() != null ? !getPassword().equals(user.getPassword()) : user.getPassword() != null);

    }

    @Override
    public int hashCode() {
        int result = getUsername() != null ? getUsername().hashCode() : 0;
        result = 31 * result + (getPassword() != null ? getPassword().hashCode() : 0);
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
