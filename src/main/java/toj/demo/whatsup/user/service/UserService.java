package toj.demo.whatsup.user.service;

import toj.demo.whatsup.user.http.resource.Credentials;
import toj.demo.whatsup.domain.User;

import java.util.Optional;

/**
 * Created by mihai.popovici on 9/24/2015.
 */
public interface UserService {
    Optional<User> get(String name);

    void signup(String name, String password);

    void signup(Credentials credentials);

    boolean has(User user);

    boolean has(String name);

    boolean checkUser(String name, String password);

    boolean checkUser(Credentials credentials);

    void remove(String name);

}
