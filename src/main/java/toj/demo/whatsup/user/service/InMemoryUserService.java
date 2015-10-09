package toj.demo.whatsup.user.service;

import toj.demo.whatsup.domain.User;
import toj.demo.whatsup.user.http.resource.Credentials;

import java.util.HashMap;
import java.util.Optional;

/**
 * Created by mihai.popovici on 9/24/2015.
 */
public class InMemoryUserService implements UserService {

    private HashMap<String, User> users;

    public InMemoryUserService() {
        users = new HashMap<String, User>();
    }


    @Override
    public Optional<User> get(String name) {
        return Optional.ofNullable(users.get(name));
    }

    @Override
    public void signup(String name, String password) {
        User user = new User(name, password);
        users.put(user.getUsername(), user);
    }

    @Override
    public void signup(Credentials credentials) {
        User user = new User(credentials.getUsername(), credentials.getPassword());
        users.put(user.getUsername(), user);
    }

    @Override
    public boolean has(User user) {
        return users.containsValue(user);
    }

    @Override
    public boolean has(String name) {
        return users.containsKey(name);
    }

    @Override
    public boolean checkUser(String name, String password) {
        if (!users.containsKey(name)) {
            return false;
        } else {
            return users.get(name).getPassword().equals(password);
        }
    }

    @Override
    public boolean checkUser(Credentials credentials) {
        if (!users.containsKey(credentials.getUsername())) {
            return false;
        } else {
            return users.get(credentials.getUsername()).getPassword().equals(credentials.getPassword());
        }
    }

    @Override
    public void remove(String name) {
        users.remove(name);
    }
}
