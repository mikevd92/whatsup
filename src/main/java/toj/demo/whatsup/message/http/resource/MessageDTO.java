package toj.demo.whatsup.message.http.resource;

import toj.demo.whatsup.user.http.resource.UserDTO;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by mihai.popovici on 10/1/2015.
 */
@XmlRootElement
public class MessageDTO implements Serializable {

    private long msgId;

    private String message;

    private UserDTO user;

    private Date creationTimestamp;

    public MessageDTO() {

    }

    public Date getCreationTimestamp() {
        return creationTimestamp;
    }

    public UserDTO getUserDTO() {
        return user;
    }

    public void setUserDTO(UserDTO user) {
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
        if (getUserDTO() != null ? !getUserDTO().equals(message1.getUserDTO()) : message1.getUserDTO() != null)
            return false;
        return !(getCreationTimestamp() != null ? !getCreationTimestamp().equals(message1.getCreationTimestamp()) : message1.getCreationTimestamp() != null);

    }

    @Override
    public int hashCode() {
        int result = (int) (msgId ^ (msgId >>> 32));
        result = 31 * result + (getMessage() != null ? getMessage().hashCode() : 0);
        result = 31 * result + (getUserDTO() != null ? getUserDTO().hashCode() : 0);
        result = 31 * result + (getCreationTimestamp() != null ? getCreationTimestamp().hashCode() : 0);
        return result;
    }
}
