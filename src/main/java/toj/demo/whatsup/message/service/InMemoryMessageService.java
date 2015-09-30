package toj.demo.whatsup.message.service;

import toj.demo.whatsup.message.model.Message;
import toj.demo.whatsup.user.model.User;

import java.util.*;

/**
 * Created by mihai.popovici on 9/25/2015.
 */
public class InMemoryMessageService implements MessageService {

    private HashMap<String, List<Message>> messages;

    public InMemoryMessageService() {
        messages = new HashMap<String, List<Message>>();
    }

    @Override
    public void addNewMessage(Message message, String userName) {
        if (messages.get(userName) == null)
            messages.put(userName, new ArrayList<Message>(Collections.singletonList(message)));
        else
            messages.get(userName).add(message);
    }

    @Override
    public Message getStatusMessage(String userName) {
        return messages.get(userName).get(messages.values().size() - 1);
    }

    @Override
    public List<Message> getUpdates(Date timestamp, String userName) {
        List<Message> messageList = messages.get(userName);
        List<Message> updates = new ArrayList<Message>();
        for (Message message : messageList) {
            if (!message.getCreationTimestamp().before(timestamp))
                updates.add(message);
        }
        return updates;
    }

    @Override
    public List<Message> getMessages(String userName) {
        return messages.get(userName);
    }

    @Override
    public List<Message> getLatestMessages(Set<User> users) {
        Iterator<User> iterator = users.iterator();
        List<Message> latestMessages = new LinkedList<Message>();
        while (iterator.hasNext() && latestMessages.size() <= 10) {
            List<Message> userMessages = getMessages(iterator.next().getUsername());
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