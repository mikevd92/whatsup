package toj.demo.whatsup.user.services;

import toj.demo.whatsup.domain.User;

import java.util.Optional;

public interface UserSessionService {
    String createUserSession(User user);

    Optional<User> getUserBySession(String sessionId);

    boolean userExists(User user);

    boolean sessionIdExists(String sessionId);

    void removeUserSession(String sessionId);

}
