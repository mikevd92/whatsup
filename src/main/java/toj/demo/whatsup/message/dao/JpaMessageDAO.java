package toj.demo.whatsup.message.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import toj.demo.whatsup.dao.JpaDAO;
import toj.demo.whatsup.domain.Message;
import toj.demo.whatsup.domain.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TemporalType;
import java.util.*;

/**
 * Created by mihai.popovici on 10/8/2015.
 */
@Repository
public class JpaMessageDAO extends JpaDAO<Message,Long> implements MessageDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Message getMessageByUser(User user) {
        return entityManager.createQuery("select m from Messages m where m.user = :user ORDER BY m.creationTimestamp desc",Message.class).setParameter("user",user).getResultList().get(0);
    }

    @Override
    public void removeAll() {
        entityManager.createQuery("DELETE FROM Messages m").executeUpdate();
    }

    @Override
    public List<Message> getMessagesByUser(User user) {
        return entityManager.createQuery("select m from Messages m where m.user = :user ORDER BY m.creationTimestamp desc",Message.class).setParameter("user",user).getResultList();
    }

    @Override
    public List<Message> getUpdates(Date date, User user) {
        return entityManager.createQuery("select m from Messages m where m.user = :user and m.creationTimestamp >= :date ORDER BY m.creationTimestamp desc",Message.class).setParameter("user",user).setParameter("date",date, TemporalType.DATE).getResultList();
    }

    @Override
    public List<Message> getMessagesByUsers(Set<User> users) {
        List<Message> list=new ArrayList<>();
        Iterator<User> iterator=users.iterator();
        while(iterator.hasNext()&&list.size()<10){
            List<Message> results=entityManager.createQuery("select m from Messages m where m.user = :user", Message.class).setParameter("user", iterator.next()).getResultList();
            if(results.size()>0)
            list.add(results.get(0));
            if(results.size()>1)
            list.add(results.get(1));
        }
        return list;
    }

}
