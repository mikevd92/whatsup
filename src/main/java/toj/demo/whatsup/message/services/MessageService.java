package toj.demo.whatsup.message.services;

import toj.demo.whatsup.domain.Keyword;
import toj.demo.whatsup.domain.Message;
import toj.demo.whatsup.domain.User;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface MessageService {
    void addNewMessage(Message var1);

    Optional<Message> getStatusMessage(User user);

    List<Message> getUpdates(Date date, User user);

    List<Message> getMessages(User user);

    List<Message> getLatestMessages(Set<User> users);

    void removeByDeletionTimestamp();
}
