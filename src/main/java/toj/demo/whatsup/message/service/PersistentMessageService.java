package toj.demo.whatsup.message.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import toj.demo.whatsup.domain.Message;
import toj.demo.whatsup.domain.User;
import toj.demo.whatsup.message.dao.MessageDAO;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by mihai.popovici on 10/8/2015.
 */
@Transactional
public class PersistentMessageService implements MessageService {
    private MessageDAO messageDAO;

    @Autowired
    public PersistentMessageService(MessageDAO messageDAO) {
        this.messageDAO = messageDAO;
    }

    @Override
    public void addNewMessage(Message message, User user) {
        message.setUser(user);
        messageDAO.save(message);
    }

    @Override
    public void removeMessage(Message message) {
        messageDAO.delete(message);
    }

    @Override
    public Message getStatusMessage(User user) {
        return messageDAO.getMessageByUser(user);
    }

    @Override
    public Message getMessageByUserAndContent(User user, String content) {
        return messageDAO.getMessageByUserAndContent(user,content);
    }

    @Override
    public List<Message> getUpdates(Date timestamp, User user) {
        return messageDAO.getUpdates(timestamp, user);
    }

    @Override
    public List<Message> getMessages(User user) {
        return messageDAO.getMessagesByUser(user);
    }

    @Override
    public List<Message> getLatestMessages(Set<User> users) {
        return messageDAO.getMessagesByUsers(users);
    }
}
