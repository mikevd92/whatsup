package toj.demo.whatsup.user.model;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by mihai.popovici on 9/24/2015.
 */
@XmlRootElement
public class User implements Serializable{

    private String username;

    private String password;


    public User(String username, String password) {
        this.username = username;
        this.password = password;
        followers=new LinkedHashSet<User>();
    }
    private Set<User> followers;
    public User() {
        followers=new LinkedHashSet<User>();
    }

    public void addFollower(User user){
        followers.add(user);
    }

    public void removeFollower(User user){
        followers.remove(user);
    }
    public Set<User> getFollowers() {
        return followers;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
    public String toString() {
        return "{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}";
    }

}
