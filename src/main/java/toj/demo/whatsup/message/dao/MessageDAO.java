package toj.demo.whatsup.message.dao;

import toj.demo.whatsup.dao.DAO;
import toj.demo.whatsup.domain.Message;
import toj.demo.whatsup.domain.User;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface MessageDAO extends DAO<Message,Long> {
    Optional<Message> getMessageByUser(User user);
    List<Message> getMessagesByUser(User user);
    List<Message> getUpdates(Date date,User user);
    List<Message> getMessagesByUsers(Set<User> users);
    void removeByDeletionTimestamp();
}
