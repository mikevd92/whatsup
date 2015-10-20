package toj.demo.whatsup.message.services;

import toj.demo.whatsup.domain.Message;
import toj.demo.whatsup.domain.User;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface MessageService {

    void addNewMessage(Message message);

    Optional<Message> getStatusMessage(User user);

    void removeAll();

    List<Message> getUpdates(Date timestamp, User user);

    List<Message> getMessages(User user);

    List<Message> getLatestMessages(Set<User> users);

}