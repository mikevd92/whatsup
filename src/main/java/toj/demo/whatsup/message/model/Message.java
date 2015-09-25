package toj.demo.whatsup.message.model;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by mihai.popovici on 9/25/2015.
 */
@XmlRootElement
public class Message implements Serializable{

    private String message;

    private String userName;

    private Date creationTimestamp;

    public Message(){

    }

    public Message(String message, String userName, Date creationTimestamp) {
        this.message = message;
        this.userName = userName;
        this.creationTimestamp = creationTimestamp;
    }

    public Date getCreationTimestamp() {
        return creationTimestamp;
    }

    public void setCreationTimestamp(Date creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "{\"message\":\"" + message + "\",\"userName\":" + "\"" + userName + "\",\"timestamp\":\"" + creationTimestamp + "\"}";
    }
}