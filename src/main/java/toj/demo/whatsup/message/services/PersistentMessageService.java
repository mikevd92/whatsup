package toj.demo.whatsup.message.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import toj.demo.whatsup.domain.Keyword;
import toj.demo.whatsup.domain.Message;
import toj.demo.whatsup.domain.User;
import toj.demo.whatsup.message.dao.MessageDAO;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Transactional
public class PersistentMessageService implements MessageService {
    private MessageDAO messageDAO;

    public PersistentMessageService(){

    }
    @Autowired
    public PersistentMessageService(MessageDAO messageDAO) {
        this.messageDAO = messageDAO;
    }

    public void addNewMessage(Message message) {
        this.messageDAO.save(message);
    }

    public Optional<Message> getStatusMessage(User user) {
        return this.messageDAO.getMessageByUser(user);
    }

    public List<Message> getUpdates(Date timestamp, User user) {
        return this.messageDAO.getUpdates(timestamp, user);
    }

    public List<Message> getMessages(User user) {
        return this.messageDAO.getMessagesByUser(user);
    }

    public List<Message> getLatestMessages(Set<User> users) {
        return this.messageDAO.getMessagesByUsers(users);
    }

    @Override
    public List<Message> getMessagesByKeyWords(Set<Keyword> keywords) {
        return messageDAO.getMessagesByKeyWords(keywords);
    }

    public void removeByDeletionTimestamp(){
        messageDAO.removeByDeletionTimestamp();
    }
}