package toj.demo.whatsup.message.http.resource;

import org.dozer.Mapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import toj.demo.whatsup.domain.Message;
import toj.demo.whatsup.message.service.MessageService;
import toj.demo.whatsup.test.jersey.SpringManagedResourceTest;
import toj.demo.whatsup.domain.User;
import toj.demo.whatsup.user.http.resource.UserDTO;
import toj.demo.whatsup.user.service.UserService;
import toj.demo.whatsup.user.service.UserSessionService;

import javax.ws.rs.core.Response;
import java.util.*;

import static org.junit.Assert.assertEquals;
/**
 * Created by mihai.popovici on 9/28/2015.
 */
public class MessageResourceTest extends SpringManagedResourceTest<MessageResource> {

    @Autowired
    private  MessageService messageService;

    @Autowired
    private  UserSessionService userSessionService;

    @Autowired
    private UserService userService;
    @Autowired
    private Mapper mapper;

    @Test
    public void testSubmitMessage(){
        userService.signup("Mihai","password");
        User user=userService.get("Mihai").get();
        String sessionId=userSessionService.createUserSession(user);
        Response messageResponse=target("message/submit").queryParam("sessionId",sessionId).queryParam("message","awesome").request().get();
        assertEquals(messageResponse.getStatusInfo(), Response.Status.OK);
        messageService.removeMessage(messageService.getMessageByUserAndContent(user, "awesome"));
        userService.remove("Mihai");
    }
    @Test
    public void testGetStatusCall(){
        userService.signup("Mihai", "password");
        User user=userService.get("Mihai").get();
        String sessionId=userSessionService.createUserSession(user);
        target("message/submit").queryParam("sessionId",sessionId).queryParam("message","awesome").request().get();
        Response statusResponse=target("message/status").queryParam("sessionId",sessionId).request().get();
        MessageDTO message=statusResponse.readEntity(MessageResponse.class).getResults().get(0);
        assertEquals(message.getMessage(), "awesome");
        assertEquals(message.getUserDTO().getUsername(), "Mihai");
        messageService.removeMessage(messageService.getMessageByUserAndContent(user, "awesome"));
        userService.remove("Mihai");
    }
    @Test
    public void testUpdates(){
        userService.signup("Mihai", "password");
        User user=userService.get("Mihai").get();
        String sessionId=userSessionService.createUserSession(user);
        Date timestamp=new Date();
        Response submitResponse=target("message/submit").queryParam("sessionId",sessionId).queryParam("message","awesome").request().get();
        Response updatesResponse=target("message/updates").queryParam("sessionId", sessionId).queryParam("timestamp", timestamp.toString()).request().get();
        MessageDTO message=updatesResponse.readEntity(MessageResponse.class).getResults().get(0);
        assertEquals(message.getUserDTO().getUsername(),"Mihai");
        assertEquals(message.getMessage(), "awesome");
        messageService.removeMessage(messageService.getMessageByUserAndContent(user, "awesome"));
        userService.remove("Mihai");
    }
    @Test
    public void testLatestMessages(){
        userService.signup("Mihai","password");
        User follower=userService.get("Mihai").get();
        String sessionId=userSessionService.createUserSession(follower);
        userService.signup("Adi", "password");
        User toBeFollowed=userService.get("Adi").get();
        String tobeFollowedSessionId=userSessionService.createUserSession(toBeFollowed);
        toBeFollowed.addFollower(follower);
        target("message/submit").queryParam("sessionId", sessionId).queryParam("message","awesome").request().get();
        target("message/submit").queryParam("sessionId",sessionId).queryParam("message","bla").request().get();
        Response latestMessagesResponse=target("message/latestmessages").queryParam("sessionId",tobeFollowedSessionId).request().get();
        List<MessageDTO> latestMessages = new ArrayList<MessageDTO>();
        Set<User> followers = toBeFollowed.getFollowers();
        Iterator<User> iterator = followers.iterator();
        while (iterator.hasNext() && latestMessages.size() <= 10) {
            List<Message> userMessages = messageService.getMessages(iterator.next());
            if (userMessages.size() > 0) {
                Message message=userMessages.get(userMessages.size() - 1);
                MessageDTO messageDTO=mapper.map(message, MessageDTO.class);
                UserDTO userDTO=mapper.map(message.getUser(), UserDTO.class);
                messageDTO.setUserDTO(userDTO);
                latestMessages.add(messageDTO);
            }
            if (userMessages.size() > 1) {
                Message message=userMessages.get(userMessages.size() - 2);
                MessageDTO messageDTO=mapper.map(message, MessageDTO.class);
                UserDTO userDTO=mapper.map(message.getUser(), UserDTO.class);
                messageDTO.setUserDTO(userDTO);
                latestMessages.add(messageDTO);
            }
        }

        assertEquals(latestMessagesResponse.readEntity(MessageResponse.class).getResults(), latestMessages);
        messageService.removeMessage(messageService.getMessageByUserAndContent(follower, "awesome"));
        messageService.removeMessage(messageService.getMessageByUserAndContent(follower, "bla"));
        userService.remove("Mihai");
        userService.remove("Adi");
    }
}
