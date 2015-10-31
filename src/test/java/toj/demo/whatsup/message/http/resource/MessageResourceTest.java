package toj.demo.whatsup.message.http.resource;

import org.dozer.Mapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import toj.demo.whatsup.domain.Credentials;
import toj.demo.whatsup.domain.Message;
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
import java.util.stream.Collectors;

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


    private String sessionId;
    private User toBeFollowed;
    private User user;
    private String tobeFollowedSessionId;
    private Credentials credentialsMihai;
    private Credentials credentialsAdi;

    @Test
    public void testSubmitCorrectMessageSucceeds() {
        initUserSetup("Mihai1");
        Response messageResponse = target("message/submit").queryParam("sessionId", sessionId).queryParam("message", "awesome").request().post(Entity.text(""));
        assertEquals(messageResponse.getStatusInfo(), Response.Status.OK);
    }

    @Test
    public void testSubmitEmptyMessageReturnsBadRequest() {
        initUserSetup("Mihai2");
        Response messageResponse = target("message/submit").queryParam("sessionId", sessionId).request().post(Entity.text(""));
        assertEquals(messageResponse.getStatusInfo(), Response.Status.BAD_REQUEST);
    }

    @Test
    public void testGetStatusCallSucceeds() {
        initUserSetup("Mihai3");
        target("message/submit").queryParam("sessionId", sessionId).queryParam("message", "awesome").request().post(Entity.text(""));
        messageService.addNewMessage(new Message("awesome",user,Date.from(Instant.now().minus(3, ChronoUnit.DAYS)),Date.from(Instant.now().minus(2, ChronoUnit.DAYS))));
        messageService.addNewMessage(new Message("awesome",user,Date.from(Instant.now().minus(4, ChronoUnit.DAYS)),Date.from(Instant.now().minus(3, ChronoUnit.DAYS))));
        Response statusResponse = target("message/status").queryParam("sessionId", sessionId).request().get();
        MessageDTO message = statusResponse.readEntity(MessageResponse.class).getResults().get(0);
        assertEquals(message.getMessage(), "awesome");
        assertEquals(message.getUser().getUsername(), "Mihai3");

    }

    @Test
    public void testGetStatusCallReturnsEmptyList() {
        initUserSetup("Mihai4");
        Response statusResponse = target("message/status").queryParam("sessionId", sessionId).request().get();
        assertEquals(Collections.EMPTY_LIST, statusResponse.readEntity(MessageResponse.class).getResults());
    }

    @Test
    public void testUpdatesSucceeds() {
        initUserSetup("Mihai5");
        Date timestamp = Date.from(Instant.now().minus(2, ChronoUnit.DAYS));
        messageService.addNewMessage(new Message("awesome",user,Date.from(Instant.now().minus(3, ChronoUnit.DAYS)),Date.from(Instant.now().minus(2, ChronoUnit.DAYS))));
        messageService.addNewMessage(new Message("awesome",user,Date.from(Instant.now().minus(2, ChronoUnit.DAYS)),Date.from(Instant.now().plus(3, ChronoUnit.DAYS))));
        target("message/submit").queryParam("sessionId", sessionId).queryParam("message", "awesome").request().post(Entity.text(""));
        Response updatesResponse = target("message/updates").queryParam("sessionId", sessionId).queryParam("timestamp", timestamp.toString()).request().get();
        List<MessageDTO> results=updatesResponse.readEntity(MessageResponse.class).getResults();
        List<MessageDTO> updates = messageService.getUpdates(timestamp,user).stream().map(p -> mapper.map(p,MessageDTO.class)).collect(Collectors.toList());

        assertEquals(results,updates);
        assertEquals(results.size(),2);
    }

    @Test
    public void testGetUpdatesReturnsEmptyList() {
        initUserSetup("Mihai6");
        Date timestamp = new Date();
        Response updatesResponse = target("message/updates").queryParam("sessionId", sessionId).queryParam("timestamp", timestamp.toString()).request().get();
        assertEquals(Collections.EMPTY_LIST, updatesResponse.readEntity(MessageResponse.class).getResults());
    }

    @Test
    public void testLatestMessagesSucceeds() {
        initFollowerSetup("Mihai7","Adi");

        target("message/submit").queryParam("sessionId", sessionId).queryParam("message", "awesome").request().post(Entity.text(""));
        target("message/submit").queryParam("sessionId", sessionId).queryParam("message", "bla").request().post(Entity.text(""));
        messageService.addNewMessage(new Message("awesome",user,Date.from(Instant.now().minus(3, ChronoUnit.DAYS)),Date.from(Instant.now().minus(2, ChronoUnit.DAYS))));
        messageService.addNewMessage(new Message("awesome",user,Date.from(Instant.now().minus(4, ChronoUnit.DAYS)),Date.from(Instant.now().minus(3, ChronoUnit.DAYS))));
        Response latestMessagesResponse = target("message/latestmessages").queryParam("sessionId", tobeFollowedSessionId).request().get();

        List<MessageDTO> latestMessages = toBeFollowed.getFollowers().stream().flatMap(p -> messageService.getMessages(p).stream().limit(2))
                .limit(10).<MessageDTO>map(p -> mapper.map(p,MessageDTO.class)).collect(Collectors.toList());

        List<MessageDTO> results=latestMessagesResponse.readEntity(MessageResponse.class).getResults();
        assertEquals(results, latestMessages);
        assertEquals(results.size(),2);

    }

    @Test
    public void testRemoveByDeletionTimestamp(){
        initUserSetup("Mihai8");
        target("message/submit").queryParam("sessionId", sessionId).queryParam("message", "awesome").request().post(Entity.text(""));
        target("message/submit").queryParam("sessionId", sessionId).queryParam("message", "bla").request().post(Entity.text(""));
        messageService.addNewMessage(new Message("awesome",user,Date.from(Instant.now().minus(3, ChronoUnit.DAYS)),Date.from(Instant.now().minus(2, ChronoUnit.DAYS))));
        messageService.addNewMessage(new Message("awesome",user,Date.from(Instant.now().minus(4, ChronoUnit.DAYS)),Date.from(Instant.now().minus(3, ChronoUnit.DAYS))));
        messageService.removeByDeletionTimestamp();
        assertEquals(messageService.getMessages(user).size(),2);
    }

    @Test
    public void testGetLatestMessagesReturnsEmptyList() {
        initFollowerSetup("Mihai9","Adi1");
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
        testNoSessionGET(targets);
    }

    private void initUserSetup(String userName) {
        credentialsMihai=new Credentials(userName,"password");
        userService.signup(credentialsMihai);
        user = userService.get(userName).get();
        sessionId = userSessionService.createUserSession(user);
    }

    private void initFollowerSetup(String beFollowedName,String followerName) {
        credentialsMihai = new Credentials(beFollowedName,"password");
        userService.signup(credentialsMihai);
        user = userService.get(beFollowedName).get();
        sessionId = userSessionService.createUserSession(user);
        credentialsAdi=new Credentials(followerName,"password");
        userService.signup(credentialsMihai);
        userService.signup(credentialsAdi);
        toBeFollowed = userService.get(followerName).get();
        tobeFollowedSessionId = userSessionService.createUserSession(toBeFollowed);
        toBeFollowed.addFollower(user);
    }

}
