package toj.demo.whatsup.message.dao;

import toj.demo.whatsup.dao.ObjectDAO;
import toj.demo.whatsup.domain.Message;
import toj.demo.whatsup.domain.User;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by mihai.popovici on 10/7/2015.
 */

public interface MessageDAO extends ObjectDAO<Message,Long> {
    public Message getMessageByUser(User user);
    public Message getMessageByUserAndContent(User user,String content);
    public List<Message> getMessagesByUser(User user);
    public List<Message> getUpdates(Date date,User user);
    public List<Message> getMessagesByUsers(Set<User> users);

}
