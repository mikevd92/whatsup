package toj.demo.whatsup.user.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
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
public class UserDAOImpl implements UserDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void delete(User deleted) {
        entityManager.remove(entityManager.contains(deleted) ? deleted : entityManager.merge(deleted));
    }

    @Override
    public List<User> findAll() {

        return entityManager.createQuery("select u from Users u", User.class).getResultList();
    }

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

    @Override
    public Optional<User> findOne(Long aLong) {
        return Optional.ofNullable(entityManager.find(User.class, aLong));
    }

    @Override
    public User save(User persisted) {
        entityManager.persist(persisted);
        return persisted;
    }
}
