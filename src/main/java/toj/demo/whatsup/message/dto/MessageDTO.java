package toj.demo.whatsup.message.dto;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by mihai.popovici on 10/1/2015.
 */
@XmlRootElement
public class MessageDTO implements Serializable {
    private String message;

    private String userName;

    private Date creationTimestamp;

    public MessageDTO(){

    }

    public MessageDTO(String message, String userName) {
        this.message = message;
        this.userName = userName;
        this.creationTimestamp = new Date();
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MessageDTO)) return false;

        MessageDTO message1 = (MessageDTO) o;

        if (getMessage() != null ? !getMessage().equals(message1.getMessage()) : message1.getMessage() != null)
            return false;
        if (getUserName() != null ? !getUserName().equals(message1.getUserName()) : message1.getUserName() != null)
            return false;
        return !(getCreationTimestamp() != null ? !getCreationTimestamp().equals(message1.getCreationTimestamp()) : message1.getCreationTimestamp() != null);

    }

    @Override
    public int hashCode() {
        int result = getMessage() != null ? getMessage().hashCode() : 0;
        result = 31 * result + (getUserName() != null ? getUserName().hashCode() : 0);
        result = 31 * result + (getCreationTimestamp() != null ? getCreationTimestamp().hashCode() : 0);
        return result;
    }
}
