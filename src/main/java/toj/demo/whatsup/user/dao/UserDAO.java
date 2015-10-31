package toj.demo.whatsup.user.dao;

import toj.demo.whatsup.dao.DAO;
import toj.demo.whatsup.domain.User;

import java.util.Optional;


public interface UserDAO extends DAO<User,Long> {

    boolean checkUser(String name, String password);

    Optional<User> findUserByName(String name);

    void addFollower(User toBeFollowed,User follower);

    void removeFollower(User toBeFollowed,User follower);

}
