package toj.demo.whatsup.message.dao;

import org.springframework.stereotype.Repository;
import toj.demo.whatsup.dao.JpaDAO;
import toj.demo.whatsup.domain.Message;
import toj.demo.whatsup.domain.Message_;
import toj.demo.whatsup.domain.User;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.*;

/**
 * Created by mihai.popovici on 10/8/2015.
 */
@Repository
public class JpaMessageDAO extends JpaDAO<Message, Long> implements MessageDAO {

    @Override
    public Optional<Message> getMessageByUser(User user) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Message> cq = cb.createQuery(this.entityClass);
        Root<Message> messageRoot = cq.from(this.entityClass);
        cq.where(
                cb.equal(messageRoot.get(Message_.user), user)
        );
        cq.orderBy(
                cb.desc(messageRoot.get(Message_.creationTimestamp))
        );
        cq.select(messageRoot);
        TypedQuery<Message> query = entityManager.createQuery(cq);
        List<Message> messages = query.getResultList();
        if (messages.size() > 0)
            return Optional.ofNullable(messages.get(0));
        else
            return Optional.ofNullable(null);
    }

    @Override
    public List<Message> getMessagesByUser(User user) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Message> cq = cb.createQuery(this.entityClass);
        Root<Message> messageRoot = cq.from(this.entityClass);
        cq.where(
                cb.equal(messageRoot.get(Message_.user), user)
        );
        cq.orderBy(
                cb.desc(messageRoot.get(Message_.creationTimestamp))
        );
        cq.select(messageRoot);
        TypedQuery<Message> query = entityManager.createQuery(cq);
        return query.getResultList();
    }

    @Override
    public List<Message> getUpdates(Date date, User user) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Message> cq = cb.createQuery(this.entityClass);
        Root<Message> messageRoot = cq.from(this.entityClass);
        cq.where(cb.and(
                        cb.equal(messageRoot.get(Message_.user), user),
                        cb.greaterThanOrEqualTo(messageRoot.get(Message_.creationTimestamp), date)
                )
        );
        cq.orderBy(
                cb.desc(messageRoot.get(Message_.creationTimestamp))
        );
        cq.select(messageRoot);
        TypedQuery<Message> query = entityManager.createQuery(cq);
        return query.getResultList();
    }

    @Override
    public List<Message> getMessagesByUsers(Set<User> users) {
        List<Message> list = new ArrayList<>();
        Iterator<User> iterator = users.iterator();
        while (iterator.hasNext() && list.size() < 10) {
            List<Message> results = getUserMessages(iterator.next());
            if (results.size() > 0)
                list.add(results.get(0));
            if (results.size() > 1)
                list.add(results.get(1));
        }
        return list;
    }

    public List<Message> getUserMessages(User user) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Message> cq = cb.createQuery(this.entityClass);
        Root<Message> messageRoot = cq.from(this.entityClass);
        cq.where(
                cb.equal(messageRoot.get(Message_.user), user)
        );

        cq.select(messageRoot);
        TypedQuery<Message> query = entityManager.createQuery(cq);
        return query.getResultList();
    }

}
