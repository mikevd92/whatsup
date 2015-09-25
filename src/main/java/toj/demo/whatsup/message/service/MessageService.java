package toj.demo.whatsup.message.service;

import toj.demo.whatsup.message.model.Message;

import java.util.Date;
import java.util.List;

/**
 * Created by mihai.popovici on 9/25/2015.
 */
public interface MessageService {
    //private LinkedHashMap<String,List<Message>> messages;
    void addNewMessage(Message message, String userName);

    Message getStatusMessage(String userName);

    List<Message> getUpdates(Date timestamp, String userName);
    List<Message> getMessages(String userName);

}