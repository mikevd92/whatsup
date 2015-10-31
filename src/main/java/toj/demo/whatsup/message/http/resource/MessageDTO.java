package toj.demo.whatsup.message.http.resource;

import toj.demo.whatsup.user.http.resource.UserDTO;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

@XmlRootElement
public class MessageDTO implements Serializable {


    @Override
    public int hashCode() {
        int result = (int) (msgId ^ (msgId >>> 32));
        result = 31 * result + (message != null ? message.hashCode() : 0);
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + (creationTimestamp != null ? creationTimestamp.hashCode() : 0);
        result = 31 * result + (deletionTimestamp != null ? deletionTimestamp.hashCode() : 0);
        return result;
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
    public long getMsgId() {
        return msgId;
    }

    public void setMsgId(long msgId) {
        this.msgId = msgId;
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
    public String toString() {
        return "MessageDTO{" +
                "msgId=" + msgId +
                ", message='" + message + '\'' +
                ", user=" + user +
                ", creationTimestamp=" + creationTimestamp +
                ", deletionTimestamp=" + deletionTimestamp +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MessageDTO that = (MessageDTO) o;

        if (msgId != that.msgId) return false;
        if (message != null ? !message.equals(that.message) : that.message != null) return false;
        if (user != null ? !user.equals(that.user) : that.user != null) return false;
        if (creationTimestamp != null ? !creationTimestamp.equals(that.creationTimestamp) : that.creationTimestamp != null)
            return false;
        if (deletionTimestamp != null ? !deletionTimestamp.equals(that.deletionTimestamp) : that.deletionTimestamp != null)
            return false;

        return true;
    }

}
