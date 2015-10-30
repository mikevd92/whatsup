package toj.demo.whatsup.user.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import toj.demo.whatsup.domain.Credentials;
import toj.demo.whatsup.domain.User;
import toj.demo.whatsup.user.dao.UserDAO;
import toj.demo.whatsup.user.http.resource.CredentialsDTO;

import java.util.Optional;

@Transactional
public class PersistentUserService implements UserService {

    private UserDAO userDAO;
    @Autowired
    public PersistentUserService(UserDAO userDAO){
        this.userDAO=userDAO;
    }

    @Override
    public Optional<User> get(String name) {
        return userDAO.findUserByName(name);
    }

    @Override
    public void signup(Credentials credentials) {
        User user=new User(credentials.getUsername(), credentials.getPassword());
        userDAO.save(user);
    }

    @Override
    public boolean checkUser(Credentials credentials) {
        return userDAO.checkUser(credentials.getUsername(), credentials.getPassword());
    }

    @Override
    public void removeAll() {
        userDAO.removeAll();
    }

    @Override
    public void addFollower(User toBeFollowed, User follower) {
         userDAO.addFollower(toBeFollowed,follower);
    }

    @Override
    public void removeFollower(User toBeFollowed, User follower) {
         userDAO.removeFollower(toBeFollowed,follower);
    }


}
