package toj.demo.whatsup.user.dao;

import org.springframework.stereotype.Repository;
import toj.demo.whatsup.dao.JpaDAO;
import toj.demo.whatsup.domain.Keyword;
import toj.demo.whatsup.domain.User;
import toj.demo.whatsup.domain.User_;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public class JpaUserDAO extends JpaDAO<User, Long> implements UserDAO {

    @Override
    public boolean checkUser(String name, String password) {
        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<User> cq = cb.createQuery(this.entityClass);
            Root<User> userRoot = cq.from(this.entityClass);
            cq.where(
                    cb.and(
                            cb.equal(userRoot.get(User_.username), name),
                            cb.equal(userRoot.get(User_.password), password)
                    )
            );
            cq.select(userRoot);
            TypedQuery<User> query = entityManager.createQuery(cq);
            return query.getSingleResult() != null;
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
        user.getKeywords().addAll(keywords);
        entityManager.merge(user);
    }
}
