package toj.demo.whatsup.user.http.resource;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

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

}
