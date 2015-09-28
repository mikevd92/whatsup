package toj.demo.whatsup.user.model;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by mihai.popovici on 9/28/2015.
 */
@XmlRootElement
public class UserResponse implements Serializable{

    private String username;

    private Date timestamp;

    public UserResponse(){

    }

    public UserResponse(String username, Date timestamp) {
        this.username = username;
        this.timestamp = timestamp;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "{\"username\":\"" + username + "\",\"timestamp\":\"" + timestamp + "\"}";
    }

}
