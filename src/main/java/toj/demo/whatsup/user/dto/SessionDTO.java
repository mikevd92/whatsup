package toj.demo.whatsup.user.dto;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by mihai.popovici on 9/28/2015.
 */
@XmlRootElement
public class SessionDTO implements Serializable {

    private String sessionId;

    private String userName;

    public SessionDTO(){

    }

    public SessionDTO(String sessionId, String userName) {
        this.sessionId = sessionId;
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public String toString() {
        return "{\"sessionId\":\""+sessionId+"\",\"username\":\""+userName+"\"}";
    }

}
