package toj.demo.whatsup.message.service;

import toj.demo.whatsup.domain.Message;
import toj.demo.whatsup.domain.User;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by mihai.popovici on 9/25/2015.
 */
public interface MessageService {

    void addNewMessage(Message message);

    Message getStatusMessage(User user);

    void removeAll();

    List<Message> getUpdates(Date timestamp, User user);

    List<Message> getMessages(User user);

    List<Message> getLatestMessages(Set<User> users);

}