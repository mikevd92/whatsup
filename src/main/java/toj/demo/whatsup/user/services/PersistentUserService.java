package toj.demo.whatsup.user.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
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
    public void signup(String name, String password) {
        User user=new User(name,password);
        userDAO.save(user);
    }

    @Override
    public void signup(CredentialsDTO credentialsDTO) {
        User user=new User(credentialsDTO.getUsername(), credentialsDTO.getPassword());
        userDAO.save(user);
    }

    @Override
    public boolean checkUser(CredentialsDTO credentialsDTO) {
        return userDAO.checkUser(credentialsDTO.getUsername(), credentialsDTO.getPassword());
    }

    @Override
    public void removeAll() {
        userDAO.removeAll();
    }
}
