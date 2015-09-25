package toj.demo.whatsup.user.model;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by mihai.popovici on 9/24/2015.
 */
@XmlRootElement
public class PotentialUser implements Serializable{
    private String username;

    private String password;

    public PotentialUser(){

    }

    public PotentialUser(String password, String username) {
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
