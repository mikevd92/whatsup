package toj.demo.whatsup.message.http.resource;

import toj.demo.whatsup.user.http.resource.UserDTO;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

@XmlRootElement
public class MessageDTO implements Serializable {

    @Override
    public String toString() {
        return "MessageDTO{" +
                "msgId=" + msgId +
                ", message='" + message + '\'' +
                ", user=" + user +
                ", creationTimestamp=" + creationTimestamp +
                ", deletionTimestamp=" + deletionTimestamp +
                '}';
    }

    private long msgId;

    private String message;

    private UserDTO user;

    public void setDeletionTimestamp(Date deletionTimestamp) {
        this.deletionTimestamp = deletionTimestamp;
    }

    public void setCreationTimestamp(Date creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    private Date creationTimestamp;

    public Date getDeletionTimestamp() {
        return deletionTimestamp;
    }

    private Date deletionTimestamp;

    public MessageDTO() {

    }

    public Date getCreationTimestamp() {
        return creationTimestamp;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MessageDTO)) return false;

        MessageDTO message1 = (MessageDTO) o;

        if (msgId != message1.msgId) return false;
        if (getMessage() != null ? !getMessage().equals(message1.getMessage()) : message1.getMessage() != null)
            return false;
        if (getUser() != null ? !getUser().equals(message1.getUser()) : message1.getUser() != null)
            return false;
        return !(getCreationTimestamp() != null ? !getCreationTimestamp().equals(message1.getCreationTimestamp()) : message1.getCreationTimestamp() != null);

    }

    @Override
    public int hashCode() {
        int result = (int) (msgId ^ (msgId >>> 32));
        result = 31 * result + (getMessage() != null ? getMessage().hashCode() : 0);
        result = 31 * result + (getUser() != null ? getUser().hashCode() : 0);
        result = 31 * result + (getCreationTimestamp() != null ? getCreationTimestamp().hashCode() : 0);
        return result;
    }
}
