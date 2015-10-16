package toj.demo.whatsup.message.http.resource;

import org.dozer.Mapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import toj.demo.whatsup.domain.Message;
import toj.demo.whatsup.message.services.MessageService;
import toj.demo.whatsup.test.jersey.BaseResourceTest;
import toj.demo.whatsup.test.jersey.SpringManagedResourceTest;
import toj.demo.whatsup.domain.User;
import toj.demo.whatsup.user.http.resource.UserDTO;
import toj.demo.whatsup.user.services.UserService;
import toj.demo.whatsup.user.services.UserSessionService;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.util.*;

import static org.junit.Assert.assertEquals;
/**
 * Created by mihai.popovici on 9/28/2015.
 */
@ContextConfiguration
public class MessageResourceTest extends BaseResourceTest<MessageResource> {

    @Autowired
    private  MessageService messageService;

    @Autowired
    private  UserSessionService userSessionService;

    @Autowired
    private UserService userService;

    @Autowired
    private Mapper mapper;

    private User user;
    private String sessionId;
    private User toBeFollowed;
    private String tobeFollowedSessionId;

    @Before
    public void initialize(){
        userService.signup("Mihai", "password");
        user=userService.get("Mihai").get();
        sessionId=userSessionService.createUserSession(user);
        userService.signup("Adi", "password");
        toBeFollowed=userService.get("Adi").get();
        tobeFollowedSessionId=userSessionService.createUserSession(toBeFollowed);
        toBeFollowed.addFollower(user);
    }
    @After
    public void after(){
        messageService.removeAll();
        userService.removeAll();
    }

    @Test
    public void testSubmitCorrectMessageSucceeds(){
        Response messageResponse=target("message/submit").queryParam("sessionId",sessionId).queryParam("message","awesome").request().get();
        assertEquals(messageResponse.getStatusInfo(), Response.Status.OK);
    }
    @Test
    public void testSubmitEmptyMessageReturnsBadRequest(){
        Response messageResponse=target("message/submit").queryParam("sessionId",sessionId).request().get();
        assertEquals(messageResponse.getStatusInfo(), Response.Status.BAD_REQUEST);
    }
    @Test
    public void testGetStatusCallSucceeds(){
        target("message/submit").queryParam("sessionId",sessionId).queryParam("message","awesome").request().get();
        Response statusResponse=target("message/status").queryParam("sessionId", sessionId).request().get();
        MessageDTO message = statusResponse.readEntity(MessageResponse.class).getResults().get(0);
        assertEquals(message.getMessage(), "awesome");
        assertEquals(message.getUserDTO().getUsername(), "Mihai");

    }
    @Test
    public void testGetStatusCallReturnsEmptyList(){
        Response statusResponse=target("message/status").queryParam("sessionId", sessionId).request().get();
        assertEquals(Collections.EMPTY_LIST,statusResponse.readEntity(MessageResponse.class).getResults());
    }
    @Test
    public void testUpdatesSucceeds(){
        Date timestamp=new Date();
        Response submitResponse=target("message/submit").queryParam("sessionId",sessionId).queryParam("message","awesome").request().get();
        Response updatesResponse=target("message/updates").queryParam("sessionId", sessionId).queryParam("timestamp", timestamp.toString()).request().get();
        MessageDTO message=updatesResponse.readEntity(MessageResponse.class).getResults().get(0);
        assertEquals(message.getUserDTO().getUsername(),"Mihai");
        assertEquals(message.getMessage(), "awesome");
    }
    @Test
    public void testGetUpdatesReturnsEmptyList(){
        Date timestamp=new Date();
        Response updatesResponse=target("message/updates").queryParam("sessionId", sessionId).queryParam("timestamp", timestamp.toString()).request().get();
        assertEquals(Collections.EMPTY_LIST,updatesResponse.readEntity(MessageResponse.class).getResults());
    }
    @Test
    public void testLatestMessagesSucceeds(){
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
    }
    @Test
    public void testGetLatestMessagesReturnsEmptyList(){
        Response latestMessagesResponse=target("message/latestmessages").queryParam("sessionId",tobeFollowedSessionId).request().get();
        assertEquals(Collections.EMPTY_LIST,latestMessagesResponse.readEntity(MessageResponse.class).getResults());
    }
    @Test
    public void NoSessionReturnsBadRequest(){
        Date timestamp=new Date();
        List<WebTarget> targets=Arrays.asList(
                target("message/latestmessages"),
                target("message/updates").queryParam("timestamp", timestamp.toString()),
                target("message/status"),
                target("message/submit")

        );
        testNoSession(targets);
    }
}
