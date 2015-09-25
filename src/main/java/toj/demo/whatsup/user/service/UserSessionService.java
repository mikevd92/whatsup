package toj.demo.whatsup.user.service;

import toj.demo.whatsup.user.model.User;

import java.util.Optional;

/**
 * Created by mihai.popovici on 9/25/2015.
 */
public interface UserSessionService {
    void addUserSession(String Session, User user);

    void removeUserSession(String Session);

    boolean userSessionExists(String Session);

    Optional<User> getUserBySession(String Session);

    boolean userExists(User user);
}
