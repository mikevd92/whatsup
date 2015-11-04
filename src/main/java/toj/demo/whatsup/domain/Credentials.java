package toj.demo.whatsup.domain;

/**
 * Created by mihai.popovici on 10/29/2015.
 */
public class Credentials {
    private String username;

    private String password;

    private String email;

    public Credentials(String username, String password,String email) {
        this.username = username;
        this.password = password;
        this.email=email;
    }

    public Credentials(){}

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
