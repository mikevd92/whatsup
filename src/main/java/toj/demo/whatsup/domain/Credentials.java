package toj.demo.whatsup.domain;

/**
 * Created by mihai.popovici on 10/29/2015.
 */
public class Credentials {
    public Credentials(String username, String password) {
        this.username = username;
        this.password = password;
    }

    private String username;

    private String password;

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
