package toj.demo.whatsup.user.service;

import toj.demo.whatsup.domain.User;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by mihai.popovici on 9/25/2015.
 */
public class InMemoryUserSessionService implements UserSessionService {
    private HashMap<String, User> userSessions;

    public InMemoryUserSessionService() {
        userSessions = new HashMap<String, User>();
    }

    @Override
    public String createUserSession(User user) {
        String sessionId = UUID.randomUUID().toString();
        userSessions.put(sessionId, user);
        return sessionId;
    }

    @Override
    public Optional<User> getUserBySession(String idSession) {
        return Optional.ofNullable(userSessions.get(idSession));
    }

    @Override
    public boolean userExists(User user) {
        return userSessions.containsValue(user);
    }

}
