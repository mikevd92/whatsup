package toj.demo.whatsup.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import toj.demo.whatsup.domain.User;
import toj.demo.whatsup.user.dao.UserDAO;
import toj.demo.whatsup.user.http.resource.Credentials;

import java.util.Optional;

/**
 * Created by mihai.popovici on 10/8/2015.
 */
@Transactional
public class PersistentUserService implements UserService {

    private UserDAO userDAO;
    @Autowired
    public PersistentUserService(UserDAO userDAO){
        this.userDAO=userDAO;
    }

    @Override
    public Optional<User> get(String name) {
        return Optional.ofNullable(userDAO.findUserByName(name));
    }

    @Override
    public void signup(String name, String password) {
        User user=new User(name,password);
        userDAO.save(user);
    }

    @Override
    public void signup(Credentials credentials) {
        User user=new User(credentials.getUsername(),credentials.getPassword());
        userDAO.save(user);
    }

    @Override
    public boolean has(User user) {
        return userDAO.contains(user);
    }

    @Override
    public boolean has(String name) {
        return userDAO.contains(name);
    }

    @Override
    public boolean checkUser(String name, String password) {
        return userDAO.checkUser(name,password);
    }

    @Override
    public boolean checkUser(Credentials credentials) {
        return userDAO.checkUser(credentials.getUsername(),credentials.getPassword());
    }

    @Override
    public void remove(String name) {
        User user=userDAO.findUserByName(name);
        userDAO.delete(user);
    }
}
