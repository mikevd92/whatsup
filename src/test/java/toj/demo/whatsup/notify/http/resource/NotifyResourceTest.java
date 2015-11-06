package toj.demo.whatsup.notify.http.resource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import toj.demo.whatsup.domain.Credentials;
import toj.demo.whatsup.domain.Keyword;
import toj.demo.whatsup.domain.User;
import toj.demo.whatsup.notify.services.KeywordService;
import toj.demo.whatsup.test.jersey.SpringManagedResourceTest;
import toj.demo.whatsup.user.services.UserService;
import toj.demo.whatsup.user.services.UserSessionService;

import static org.junit.Assert.*;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

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
    private KeywordService keywordService;
    @Autowired
    private UserSessionService userSessionService;

    private  static int count=0;

    @Before
    public void initialize(){
        credentialsMihai = new Credentials("Mihai"+count,"password","misuvd92+"+count+"@yahoo.com");
        userService.signup(credentialsMihai);
        user = userService.get("Mihai"+count).get();
        sessionId = userSessionService.createUserSession(user);
    }
    @After
    public void after(){
        count++;
    }
    @Test
    public void testaddKeywords(){
        Response response=target("notify/addkeywords").queryParam("sessionId",sessionId).request().put(Entity.json("{\"keywords\":[\"Ioana\",\"Maria\",\"Mihaela\"]}"));
        assertEquals(response.readEntity(KeywordsSet.class).getKeywords(), new LinkedHashSet<>(Arrays.asList("Ioana","Maria","Mihaela")));
    }
    @Test
    public void testChangePeriod(){
        Response response=target("notify/changeperiod").queryParam("sessionId",sessionId).queryParam("notifyperiod",3).request().put(Entity.text(""));
        NotificationResponse response1=response.readEntity(NotificationResponse.class);
        assertTrue(response1.equals(new NotificationResponse(sessionId,user.getUsername(),3)));
    }
    @Test
    public void testNotifyAndAdd()
    {
        target("notify/addkeywords").queryParam("sessionId",sessionId).request().put(Entity.json("{\"keywords\":[\"Ioana\",\"Maria\",\"Mihaela\"]}"));
        target("notify/addkeywords").queryParam("sessionId",sessionId).request().put(Entity.json("{\"keywords\":[\"Ioana1\",\"Maria1\",\"Mihaela1\"]}"));
        target("notify/changeperiod").queryParam("sessionId",sessionId).queryParam("notifyperiod",3).request().put(Entity.text(""));
        User user=userService.get("Mihai"+count).get();
        assertEquals(user.getNotificationPeriod(),new Integer(3));
        assertEquals(user.getKeywords().size(),6);
    }
}
