package toj.demo.whatsup.user.dao;

import toj.demo.whatsup.dao.DAO;
import toj.demo.whatsup.domain.User;

import java.util.Optional;


public interface UserDAO extends DAO<User,Long> {
    public boolean checkUser(String name, String password);

    public Optional<User> findUserByName(String name);
    public void removeAll();
    public void addFollower(User toBeFollowed,User follower);
    public void removeFollower(User toBeFollowed,User follower);

}
