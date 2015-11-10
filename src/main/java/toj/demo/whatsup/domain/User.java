package toj.demo.whatsup.domain;

import jersey.repackaged.com.google.common.collect.ImmutableSet;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="Id")
    private Long Id;

    @Column(unique=true)
    private String username;

    @Column(unique = true)
    private String email;

    private String password;

    private Integer notificationPeriod;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name="UsersKeywords",
            joinColumns = {@JoinColumn(name = "userId",referencedColumnName="Id")},
            inverseJoinColumns = {@JoinColumn(name = "wordId",referencedColumnName = "wordId")} )
    private Set<Keyword> keywords;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name="Followers",
            joinColumns = {@JoinColumn(name = "userId",referencedColumnName="Id")},
            inverseJoinColumns = {@JoinColumn(name = "followerId",referencedColumnName = "Id")} )
    private Set<User> followers;

    public User() {
        followers = new LinkedHashSet<User>();
        keywords = new LinkedHashSet<Keyword>();
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

    public Long getId() {
        return Id;
    }

    public Set<User> getFollowers() {
        return ImmutableSet.copyOf(followers);
    }

    public Set<Keyword> getKeywords() {
        return ImmutableSet.copyOf(keywords);
    }

    public void addKeywords(Set<Keyword> keywords) {
        this.keywords.addAll(keywords);
    }

    public void removeKeywords(Set<Keyword> keywords) { this.keywords.removeAll(keywords);}

    public String getUsername() {
        return username;
    }

    @Override
    public String toString() {
        return "User{" +
                "notificationPeriod=" + notificationPeriod +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", Id=" + Id +
                ", username='" + username + '\'' +
                '}';
    }

    public Integer getNotificationPeriod() {
        return notificationPeriod;
    }

    public void setNotificationPeriod(Integer notificationPeriod) {
        this.notificationPeriod = notificationPeriod;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (email != null ? !email.equals(user.email) : user.email != null) return false;
        if (username != null ? !username.equals(user.username) : user.username != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = username != null ? username.hashCode() : 0;
        result = 31 * result + (email != null ? email.hashCode() : 0);
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
