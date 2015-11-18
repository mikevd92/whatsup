package toj.demo.whatsup.user.dao;

import org.springframework.stereotype.Repository;
import toj.demo.whatsup.dao.JpaDAO;
import toj.demo.whatsup.domain.Keyword;
import toj.demo.whatsup.domain.User;
import toj.demo.whatsup.domain.User_;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.Optional;
import java.util.Set;

@Repository
public class JpaUserDAO extends JpaDAO<User, Long> implements UserDAO {

    private static final long serialVersionUID = 3L;
    @Override
    public boolean checkUser(String name, String password) {
        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Boolean> cq = cb.createQuery(Boolean.class);
            Root<User> userRoot = cq.from(this.entityClass);
            cq.where(
                    cb.and(
                            cb.equal(userRoot.get(User_.username), name),
                            cb.equal(userRoot.get(User_.password), password)
                    )
            );
            cq.select(userRoot.isNotNull());
            TypedQuery<Boolean> query = entityManager.createQuery(cq);
            return query.getSingleResult();
        } catch (NoResultException ex) {
            return false;
        }
    }

    @Override
    public Optional<User> findUserByName(String name) {
        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<User> cq = cb.createQuery(this.entityClass);
            Root<User> userRoot = cq.from(this.entityClass);
            cq.where(
                    cb.equal(userRoot.get(User_.username), name)
            );
            cq.select(userRoot);
            TypedQuery<User> query = entityManager.createQuery(cq);
            return Optional.ofNullable(query.getSingleResult());
        } catch (NoResultException ex) {
            return Optional.ofNullable(null);
        }
    }

    @Override
    public void addFollower(User toBeFollowed, User follower) {
        toBeFollowed.addFollower(follower);
        entityManager.merge(toBeFollowed);
    }

    @Override
    public void removeFollower(User toBeUnFollowed, User follower) {
        toBeUnFollowed.removeFollower(follower);
        entityManager.merge(toBeUnFollowed);
    }

    @Override
    public void addKeyWordsToUser(User user, Set<Keyword> keywords) {
        user.addKeywords(keywords);
        entityManager.merge(user);
    }

    @Override
    public void changeNotifyPeriod(User user, int period) {
        user.setNotificationPeriod(period);
        entityManager.merge(user);
    }

    @Override
    public Optional<User> findUserById(Long id) {
        return Optional.ofNullable(entityManager.find(entityClass, id));
    }

    @Override
    public void removeKeywordsFromUser(User user, Set<Keyword> keywords) {
        user.removeKeywords(keywords);
        entityManager.merge(user);
    }

}
