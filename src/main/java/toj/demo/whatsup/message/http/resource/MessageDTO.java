package toj.demo.whatsup.message.http.resource;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

@XmlRootElement
public class MessageDTO implements Serializable {

    private long msgId;

    private String message;

    private String userName;


    private Date creationTimestamp;


    private Date deletionTimestamp;

    public MessageDTO() {

    }

    public long getMsgId() {
        return msgId;
    }

    public Date getDeletionTimestamp() {
        return deletionTimestamp;
    }

    public void setDeletionTimestamp(Date deletionTimestamp) {
        this.deletionTimestamp = deletionTimestamp;
    }
    public void setCreationTimestamp(Date creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    public void setMsgId(long msgId) {
        this.msgId = msgId;
    }

    public Date getCreationTimestamp() {
        return creationTimestamp;
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
        return "MessageDTO{" +
                "msgId=" + msgId +
                ", message='" + message + '\'' +
                ", userName='" + userName + '\'' +
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
        if (creationTimestamp != null ? !creationTimestamp.equals(that.creationTimestamp) : that.creationTimestamp != null)
            return false;
        if (deletionTimestamp != null ? !deletionTimestamp.equals(that.deletionTimestamp) : that.deletionTimestamp != null)
            return false;
        if (message != null ? !message.equals(that.message) : that.message != null) return false;
        if (userName != null ? !userName.equals(that.userName) : that.userName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (msgId ^ (msgId >>> 32));
        result = 31 * result + (message != null ? message.hashCode() : 0);
        result = 31 * result + (userName != null ? userName.hashCode() : 0);
        result = 31 * result + (creationTimestamp != null ? creationTimestamp.hashCode() : 0);
        result = 31 * result + (deletionTimestamp != null ? deletionTimestamp.hashCode() : 0);
        return result;
    }
}
