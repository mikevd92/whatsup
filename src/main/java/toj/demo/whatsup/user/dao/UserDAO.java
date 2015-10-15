package toj.demo.whatsup.user.dao;

import toj.demo.whatsup.dao.DAO;
import toj.demo.whatsup.domain.User;

/**
 * Created by mihai.popovici on 10/8/2015.
 */
public interface UserDAO extends DAO<User,Long> {
    public boolean checkUser(String name, String password);

    public User findUserByName(String name);
    public void removeAll();

    public boolean contains(String name);

    public boolean contains(User user);
}
