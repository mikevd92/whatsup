package toj.demo.whatsup.user.services;

import toj.demo.whatsup.domain.User;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

public class InMemoryUserSessionService implements UserSessionService {
    private HashMap<String, User> userSessions;

    public InMemoryUserSessionService() {
        userSessions = new HashMap<>();
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

    @Override
    public boolean sessionIdExists(String sessionId) { return userSessions.containsKey(sessionId); }

    @Override
    public void updateSession(String sessionId, User user) {
        userSessions.put(sessionId,user);
    }

    @Override
    public void removeUserSession(String sessionId){
        userSessions.remove(sessionId);
    }

}
