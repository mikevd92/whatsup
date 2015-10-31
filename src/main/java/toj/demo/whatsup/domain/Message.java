package toj.demo.whatsup.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * Created by mihai.popovici on 9/25/2015.
 */
@Entity(name="Messages")
public class Message implements Serializable {
    @Id
    @GeneratedValue
    private long msgId;

    private String message;

    @ManyToOne(fetch= FetchType.EAGER)
    @JoinColumn(name="Id")
    private User user;

    public void setCreationTimestamp(Date creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    private Date creationTimestamp;

    private Date deletionTimestamp;

    public Message() {

    }

    public Message(String message, User user) {
        this.message = message;
        this.user=user;
        this.creationTimestamp = new Date();
        this.deletionTimestamp = Date.from(creationTimestamp.toInstant().plus(10,ChronoUnit.MINUTES));
    }
    public Message(String message, User user,Date creationTimestamp, Date deletionTimestamp) {
        this.message = message;
        this.user=user;
        this.deletionTimestamp = deletionTimestamp;
        this.creationTimestamp = creationTimestamp;
    }

    public Date getDeletionTimestamp() {
        return deletionTimestamp;
    }

    public void setDeletionTimestamp(Date deletionTimestamp) {
        this.deletionTimestamp = deletionTimestamp;
    }

    public Date getCreationTimestamp() {
        return creationTimestamp;
    }

    public User getUser() {
        return user;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Message)) return false;

        Message message1 = (Message) o;

        if (msgId != message1.msgId) return false;
        if (getMessage() != null ? !getMessage().equals(message1.getMessage()) : message1.getMessage() != null)
            return false;
        if (getUser() != null ? !getUser().equals(message1.getUser()) : message1.getUser() != null) return false;
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

    @Override
    public String toString() {
        return "Message{" +
                "msgId=" + msgId +
                ", message='" + message + '\'' +
                ", user=" + user +
                ", creationTimestamp=" + creationTimestamp +
                ", deletionTimestamp=" + deletionTimestamp +
                '}';
    }
}