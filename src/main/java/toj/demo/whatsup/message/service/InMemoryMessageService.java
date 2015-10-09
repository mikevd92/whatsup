package toj.demo.whatsup.message.service;

import org.springframework.beans.factory.annotation.Autowired;
import toj.demo.whatsup.domain.Message;
import toj.demo.whatsup.domain.User;
import toj.demo.whatsup.message.dao.MessageDAO;

import java.util.*;

/**
 * Created by mihai.popovici on 9/25/2015.
 */
public class InMemoryMessageService implements MessageService {

    private HashMap<User, List<Message>> messages;

    public InMemoryMessageService() {
        messages = new HashMap<User, List<Message>>();

    }

    @Override
    public void addNewMessage(Message message, User user) {
        if (messages.get(user) == null)
            messages.put(user, new ArrayList<Message>(Collections.singletonList(message)));
        else
            messages.get(user).add(message);
    }

    @Override
    public void removeMessage(Message message) {
        messages.remove(message.getUser());
    }

    @Override
    public Message getStatusMessage(User user) {
        return messages.get(user).get(messages.values().size() - 1);
    }

    @Override
    public Message getMessageByUserAndContent(User user, String content) {
        return messages.get(user).get(0);
    }

    @Override
    public List<Message> getUpdates(Date timestamp, User user) {
        List<Message> messageList = messages.get(user);
        List<Message> updates = new ArrayList<Message>();
        for (Message message : messageList) {
            if (!message.getCreationTimestamp().before(timestamp))
                updates.add(message);
        }
        return updates;
    }

    @Override
    public List<Message> getMessages(User user) {
        return messages.get(user);
    }

    @Override
    public List<Message> getLatestMessages(Set<User> users) {
        Iterator<User> iterator = users.iterator();
        List<Message> latestMessages = new LinkedList<Message>();
        while (iterator.hasNext() && latestMessages.size() <= 10) {
            List<Message> userMessages = getMessages(iterator.next());
            if (userMessages.size() > 0) {
                latestMessages.add(userMessages.get(userMessages.size() - 1));
            }
            if (userMessages.size() > 1) {
                latestMessages.add(userMessages.get(userMessages.size() - 2));
            }
        }
        return latestMessages;
    }
}