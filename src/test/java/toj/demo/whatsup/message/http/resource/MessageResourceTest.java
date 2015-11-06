package toj.demo.whatsup.message.http.resource;

import org.dozer.Mapper;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import toj.demo.whatsup.domain.Credentials;
import toj.demo.whatsup.domain.Message;
import toj.demo.whatsup.message.dao.MessageDAO;
import toj.demo.whatsup.message.services.MessageService;
import toj.demo.whatsup.test.jersey.BaseResourceTest;
import toj.demo.whatsup.domain.User;
import toj.demo.whatsup.user.services.UserService;
import toj.demo.whatsup.user.services.UserSessionService;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import static org.junit.Assert.assertEquals;

@ContextConfiguration
public class MessageResourceTest extends BaseResourceTest<MessageResource> {

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserSessionService userSessionService;

    @Autowired
    private UserService userService;

    @Autowired
    private Mapper mapper;
    private Function<Message,MessageDTO> mapFunction=p -> {
        MessageDTO messageDTO = mapper.map(p, MessageDTO.class);
        Message message = p;
        if (message.getUser() != null) {
            messageDTO.setUserName(message.getUser().getName());
        }
        return messageDTO;
    };

    private  String sessionId;
    private  User toBeFollowed;
    private  User user;
    private  String tobeFollowedSessionId;
    private  Credentials credentialsMihai;
    private  Credentials credentialsAdi;
    private  static int count=0;

    @Before
    public void initializes(){
        credentialsMihai = new Credentials("Mihai"+count,"password","misuvd92"+count+"@yahoo.com");
        userService.signup(credentialsMihai);
        user = userService.get("Mihai"+count).get();
        sessionId = userSessionService.createUserSession(user);
        credentialsAdi=new Credentials("Adi"+count,"password","adi"+count+"@yahoo.com");
        userService.signup(credentialsAdi);
        toBeFollowed = userService.get("Adi"+count).get();
        tobeFollowedSessionId = userSessionService.createUserSession(toBeFollowed);
        toBeFollowed.addFollower(user);
    }
    @After
    public void after(){
        count++;
    }

    @Test
    public void testSubmitCorrectMessageSucceeds() {
        Response messageResponse = target("message/submit").queryParam("sessionId", sessionId).queryParam("message", "awesome").request().post(Entity.text(""));
        assertEquals(messageResponse.getStatusInfo(), Response.Status.OK);
    }

    @Test
    public void testSubmitEmptyMessageReturnsBadRequest() {
        Response messageResponse = target("message/submit").queryParam("sessionId", sessionId).request().post(Entity.text(""));
        assertEquals(messageResponse.getStatusInfo(), Response.Status.BAD_REQUEST);
    }

    @Test
    public void testGetStatusCallSucceeds() {

        target("message/submit").queryParam("sessionId", sessionId).queryParam("message", "awesome").request().post(Entity.text(""));
        messageService.addNewMessage(new Message("awesome",user,Date.from(Instant.now().minus(3, ChronoUnit.DAYS)),Date.from(Instant.now().minus(2, ChronoUnit.DAYS))));
        messageService.addNewMessage(new Message("awesome",user,Date.from(Instant.now().minus(4, ChronoUnit.DAYS)),Date.from(Instant.now().minus(3, ChronoUnit.DAYS))));
        Response statusResponse = target("message/status").queryParam("sessionId", sessionId).request().get();
        MessageDTO message = statusResponse.readEntity(MessageResponse.class).getResults().get(0);
        assertEquals(message.getMessage(), "awesome");
        assertEquals(message.getUserName(), "Mihai"+count);

    }

    @Test
    public void testGetStatusCallReturnsEmptyList() {

        Response statusResponse = target("message/status").queryParam("sessionId", sessionId).request().get();
        assertEquals(Collections.EMPTY_LIST, statusResponse.readEntity(MessageResponse.class).getResults());
    }

    @Test

    public void testUpdatesSucceeds() {

        Date timestamp = Date.from(Instant.now().minus(2, ChronoUnit.DAYS));
        messageService.addNewMessage(new Message("awesome",user,Date.from(Instant.now().minus(3, ChronoUnit.DAYS)),Date.from(Instant.now().minus(2, ChronoUnit.DAYS))));
        messageService.addNewMessage(new Message("awesome",user,Date.from(Instant.now().minus(2, ChronoUnit.DAYS)),Date.from(Instant.now().plus(3, ChronoUnit.DAYS))));
        target("message/submit").queryParam("sessionId", sessionId).queryParam("message", "awesome").request().post(Entity.text(""));
        Response updatesResponse = target("message/updates").queryParam("sessionId", sessionId).queryParam("timestamp", timestamp.toString()).request().get();
        List<MessageDTO> results=updatesResponse.readEntity(MessageResponse.class).getResults();
        List<MessageDTO> updates = messageService.getUpdates(timestamp,user).stream()
                .map(mapFunction)
                .collect(Collectors.toList());

        assertEquals(results,updates);
        assertEquals(results.size(),2);
    }

    @Test
    public void testGetUpdatesReturnsEmptyList() {

        Date timestamp = new Date();
        Response updatesResponse = target("message/updates").queryParam("sessionId", sessionId).queryParam("timestamp", timestamp.toString()).request().get();
        assertEquals(Collections.EMPTY_LIST, updatesResponse.readEntity(MessageResponse.class).getResults());
    }

    @Test
    public void testLatestMessagesSucceeds() {


        target("message/submit").queryParam("sessionId", sessionId).queryParam("message", "awesome").request().post(Entity.text(""));
        target("message/submit").queryParam("sessionId", sessionId).queryParam("message", "bla").request().post(Entity.text(""));
        messageService.addNewMessage(new Message("awesome",user,Date.from(Instant.now().minus(3, ChronoUnit.DAYS)),Date.from(Instant.now().minus(2, ChronoUnit.DAYS))));
        messageService.addNewMessage(new Message("awesome",user,Date.from(Instant.now().minus(4, ChronoUnit.DAYS)),Date.from(Instant.now().minus(3, ChronoUnit.DAYS))));
        Response latestMessagesResponse = target("message/latestmessages").queryParam("sessionId", tobeFollowedSessionId).request().get();

        List<MessageDTO> latestMessages = toBeFollowed.getFollowers().stream().flatMap(p -> messageService.getMessages(p).stream().limit(2))
                .limit(10)
                .<MessageDTO>map(mapFunction)
                .collect(Collectors.toList());

        List<MessageDTO> results=latestMessagesResponse.readEntity(MessageResponse.class).getResults();
        assertEquals(results, latestMessages);
        assertEquals(results.size(),2);

    }

    @Test
    public void testRemoveByDeletionTimestamp(){

        target("message/submit").queryParam("sessionId", sessionId).queryParam("message", "awesome").request().post(Entity.text(""));
        target("message/submit").queryParam("sessionId", sessionId).queryParam("message", "bla").request().post(Entity.text(""));
        messageService.addNewMessage(new Message("awesome",user,Date.from(Instant.now().minus(3, ChronoUnit.DAYS)),Date.from(Instant.now().minus(2, ChronoUnit.DAYS))));
        messageService.addNewMessage(new Message("awesome",user,Date.from(Instant.now().minus(4, ChronoUnit.DAYS)),Date.from(Instant.now().minus(3, ChronoUnit.DAYS))));
        messageService.removeByDeletionTimestamp();
        assertEquals(messageService.getMessages(user).size(),2);
    }

    @Test
    public void testGetLatestMessagesReturnsEmptyList() {

        Response latestMessagesResponse = target("message/latestmessages").queryParam("sessionId", tobeFollowedSessionId).request().get();
        assertEquals(Collections.EMPTY_LIST, latestMessagesResponse.readEntity(MessageResponse.class).getResults());
    }

    @Test
    public void testNoSessionReturnsBadRequest() {
        Date timestamp = new Date();
        Response response=target("message/submit").request().post(Entity.text(""));
        assertEquals(response.getStatusInfo(),Response.Status.BAD_REQUEST);
        List<WebTarget> targets = Arrays.asList(
                target("message/latestmessages"),
                target("message/updates").queryParam("timestamp", timestamp.toString()),
                target("message/status")
        );
        testNoSessionGETBadRequest(targets);
    }

    @Test
    public void testWrongSessionReturnsForbidden() {
        Date timestamp = new Date();
        Response response=target("message/submit").queryParam("sessionId","wrong").request().post(Entity.text(""));
        assertEquals(response.getStatusInfo(),Response.Status.FORBIDDEN);
        List<WebTarget> targets = Arrays.asList(
                target("message/latestmessages").queryParam("sessionId","wrong"),
                target("message/updates").queryParam("sessionId","wrong").queryParam("timestamp", timestamp.toString()),
                target("message/status").queryParam("sessionId","wrong")
        );
        testWrongSessionGETForbidden(targets);
    }


}
