package toj.demo.whatsup.message.dao;

import toj.demo.whatsup.dao.DAO;
import toj.demo.whatsup.domain.Message;
import toj.demo.whatsup.domain.User;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Created by mihai.popovici on 10/7/2015.
 */

public interface MessageDAO extends DAO<Message,Long> {
    public Optional<Message> getMessageByUser(User user);
    public void removeAll();
    public List<Message> getMessagesByUser(User user);
    public List<Message> getUpdates(Date date,User user);
    public List<Message> getMessagesByUsers(Set<User> users);

}
