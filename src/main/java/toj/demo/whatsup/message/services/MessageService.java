package toj.demo.whatsup.message.services;

import toj.demo.whatsup.domain.Message;
import toj.demo.whatsup.domain.User;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface MessageService {
    void addNewMessage(Message var1);

    Optional<Message> getStatusMessage(User var1);

    void removeAll();

    List<Message> getUpdates(Date var1, User var2);

    List<Message> getMessages(User var1);

    List<Message> getLatestMessages(Set<User> var1);

    public void removeByDeletionTimestamp();
}
