package toj.demo.whatsup.user.dto;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by mihai.popovici on 10/1/2015.
 */
@XmlRootElement
public class CredentialsDTO implements Serializable {
    private String username;

    private String password;

    public CredentialsDTO(){

    }

    public CredentialsDTO(String password, String username) {
        this.password = password;
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
