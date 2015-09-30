package toj.demo.whatsup.message.service;

import toj.demo.whatsup.message.model.Message;
import toj.demo.whatsup.user.model.User;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by mihai.popovici on 9/25/2015.
 */
public interface MessageService {
    //private LinkedHashMap<String,List<Message>> messages;
    void addNewMessage(Message message, String userName);

    Message getStatusMessage(String userName);

    List<Message> getUpdates(Date timestamp, String userName);
    List<Message> getMessages(String userName);
    List<Message> getLatestMessages(Set<User> users);

}