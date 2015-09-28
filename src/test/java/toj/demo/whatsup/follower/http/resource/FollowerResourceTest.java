package toj.demo.whatsup.follower.http.resource;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import toj.demo.whatsup.message.service.MessageService;
import toj.demo.whatsup.test.jersey.SpringManagedResourceTest;
import toj.demo.whatsup.user.model.User;
import toj.demo.whatsup.user.service.UserService;
import toj.demo.whatsup.user.service.UserSessionService;

import javax.ws.rs.core.Response;
import static org.junit.Assert.assertEquals;
/**
 * Created by mihai.popovici on 9/28/2015.
 */
public class FollowerResourceTest extends SpringManagedResourceTest<FollowerResource> {

    @Autowired
    private UserSessionService userSessionService;

    @Autowired
    private UserService userService;

    @Test
    public void testFollow(){
        userService.signup("Mihai","password");
        User follower=userService.get("Mihai").get();
        String sessionId=userSessionService.createUserSession(follower);
        userService.signup("Adi","password");
        User toBeFollowed=userService.get("Adi").get();
        userSessionService.createUserSession(toBeFollowed);
        Response response=target("follower/follow").queryParam("sessionId",sessionId).queryParam("userName",toBeFollowed.getUsername()).request().get();
        assertEquals(response.getStatusInfo(), Response.Status.OK);
    }

    @Test
    public void testUnfollow(){
        userService.signup("Mihai","password");
        User follower=userService.get("Mihai").get();
        String sessionId=userSessionService.createUserSession(follower);
        userService.signup("Adi", "password");
        User toBeFollowed=userService.get("Adi").get();
        userSessionService.createUserSession(toBeFollowed);
        target("follower/follow").queryParam("sessionId",sessionId).queryParam("userName",toBeFollowed.getUsername()).request().get();
        Response response=target("follower/unsubscribe").queryParam("sessionId",sessionId).queryParam("userName",toBeFollowed.getUsername()).request().get();
        assertEquals(response.getStatusInfo(),Response.Status.OK);
    }

}
