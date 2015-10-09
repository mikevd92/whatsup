package toj.demo.whatsup.user.http.resource;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by mihai.popovici on 10/9/2015.
 */
public class UserDTO implements Serializable {


    private long Id;

    private String username;

    private String password;

    private Set<UserDTO> followers;

    public UserDTO() {
        followers = new LinkedHashSet<UserDTO>();
    }


    public UserDTO(String username, String password) {
        this.username = username;
        this.password = password;
        followers = new LinkedHashSet<UserDTO>();
    }

    public void addFollower(UserDTO user) {
        followers.add(user);
    }

    public void removeFollower(UserDTO user) {
        followers.remove(user);
    }

    public Set<UserDTO> getFollowers() {
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
        if (!(o instanceof UserDTO)) return false;

        UserDTO user = (UserDTO) o;

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