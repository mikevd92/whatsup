package toj.demo.whatsup.notify.http.resource;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import toj.demo.whatsup.domain.Credentials;
import toj.demo.whatsup.domain.User;
import toj.demo.whatsup.test.jersey.SpringManagedResourceTest;
import toj.demo.whatsup.user.services.UserService;
import toj.demo.whatsup.user.services.UserSessionService;

import static org.junit.Assert.*;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.LinkedHashSet;

/**
 * Created by mihai.popovici on 11/4/2015.
 */
@ContextConfiguration
public class NotifyResourceTest extends SpringManagedResourceTest<NotifyResource> {

    private  String sessionId;

    private User toBeFollowed;

    private  User user;

    private  Credentials credentialsMihai;

    @Autowired
    private UserService userService;
    @Autowired
    private UserSessionService userSessionService;

    private  static int count=0;

    @Before
    public void initialize(){
        credentialsMihai = new Credentials("Mihai"+count,"password","misuvd92"+count+"@yahoo.com");
        userService.signup(credentialsMihai);
        user = userService.get("Mihai"+count).get();
        sessionId = userSessionService.createUserSession(user);
    }
    @Test
    public void testaddKeywords(){
        Response response=target("notify/addkeywords").queryParam("sessionId",sessionId).request().put(Entity.json("{\"keywords\":[\"Ioana\",\"Maria\",\"Mihaela\"]}"));
        assertEquals(response.readEntity(KeywordsSet.class).getKeywords(), new LinkedHashSet<>(Arrays.asList("Ioana","Maria","Mihaela")));
    }
    @Test
    public void testChangePeriod(){
        Response response=target("notify/changeperiod").queryParam("sessionId",sessionId).queryParam("notifyperiod",3L).request().put(Entity.text(""));
        NotificationResponse response1=response.readEntity(NotificationResponse.class);
        assertTrue(response1.equals(new NotificationResponse(sessionId,user.getUsername(),3L)));
    }
}
