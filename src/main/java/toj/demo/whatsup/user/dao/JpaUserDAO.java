package toj.demo.whatsup.user.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import toj.demo.whatsup.dao.JpaDAO;
import toj.demo.whatsup.domain.User;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

/**
 * Created by mihai.popovici on 10/8/2015.
 */
@Repository
public class JpaUserDAO extends JpaDAO<User,Long> implements UserDAO {

    @Override
    public boolean checkUser(String name, String password) {
        try {
            return entityManager.createQuery("select u from Users u where u.username = :username and u.password =:password", User.class)
                    .setParameter("username", name)
                    .setParameter("password", password)
                    .getSingleResult() != null;
        } catch (NoResultException ex) {
            return false;
        }
    }

    @Override
    public User findUserByName(String name) {
        try {
            return entityManager.createQuery("select u from Users u where u.username = :username", User.class).setParameter("username", name).getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public void removeAll() {
        entityManager.createQuery("DELETE FROM Users u").executeUpdate();
    }

    @Override
    public boolean contains(String name) {
        try {
            User user = entityManager.createQuery("select u from Users u where u.username = :username", User.class).setParameter("username", name).getSingleResult();
            return entityManager.contains(user);
        } catch (NoResultException ex) {
            return false;
        }
    }

    @Override
    public boolean contains(User user) {
        try {
            return entityManager.contains(user);
        } catch (NoResultException ex) {
            return false;
        }
    }
}
