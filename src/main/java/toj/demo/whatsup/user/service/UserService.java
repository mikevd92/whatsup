package toj.demo.whatsup.user.service;

import toj.demo.whatsup.user.model.User;

import java.util.Optional;

/**
 * Created by mihai.popovici on 9/24/2015.
 */
public interface UserService {
    Optional<User> get(String name);
    void signup(String name,String password);
    boolean has(User user);
    boolean has(String name);
    void remove(String name);

}
