package toj.demo.whatsup.user.service;

import toj.demo.whatsup.domain.User;

import java.util.Optional;

/**
 * Created by mihai.popovici on 9/25/2015.
 */
public interface UserSessionService {
    String createUserSession(User user);

    void removeUserSession(String sessionId);

    boolean userSessionExists(String sessionId);

    Optional<User> getUserBySession(String sessionId);

    boolean userExists(User user);
}
