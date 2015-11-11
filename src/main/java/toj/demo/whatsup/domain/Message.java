package toj.demo.whatsup.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * Created by mihai.popovici on 9/25/2015.
 */
@Entity(name="Message")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long msgId;

    private String message;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="Id")
    private User user;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Message message1 = (Message) o;

        if (creationTimestamp != null ? !creationTimestamp.equals(message1.creationTimestamp) : message1.creationTimestamp != null)
            return false;
        if (deletionTimestamp != null ? !deletionTimestamp.equals(message1.deletionTimestamp) : message1.deletionTimestamp != null)
            return false;
        if (message != null ? !message.equals(message1.message) : message1.message != null) return false;
        if (msgId != null ? !msgId.equals(message1.msgId) : message1.msgId != null) return false;
        if (user != null ? !user.equals(message1.user) : message1.user != null) return false;

        return true;
    }

    @Override
    public String toString() {
        return "Message{" +
                "msgId=" + msgId +
                ", message='" + message + '\'' +
                ", user=" + user.getUsername() +
                ", creationTimestamp=" + creationTimestamp +
                '}';
    }

    @Override
    public int hashCode() {
        int result = msgId != null ? msgId.hashCode() : 0;
        result = 31 * result + (message != null ? message.hashCode() : 0);
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + (creationTimestamp != null ? creationTimestamp.hashCode() : 0);
        result = 31 * result + (deletionTimestamp != null ? deletionTimestamp.hashCode() : 0);
        return result;
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

    public Long getMsgId() {
        return msgId;
    }

    public Date getDeletionTimestamp() {
        return deletionTimestamp;
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

}