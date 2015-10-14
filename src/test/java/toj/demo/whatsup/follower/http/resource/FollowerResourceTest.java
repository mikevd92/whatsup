package toj.demo.whatsup.follower.http.resource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.kubek2k.springockito.annotations.ReplaceWithMock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import toj.demo.whatsup.test.jersey.SpringManagedResourceTest;
import toj.demo.whatsup.domain.User;
import toj.demo.whatsup.user.service.UserService;
import toj.demo.whatsup.user.service.UserSessionService;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import static org.junit.Assert.assertEquals;
/**
 * Created by mihai.popovici on 9/28/2015.
 */
@ContextConfiguration
public class FollowerResourceTest extends SpringManagedResourceTest<FollowerResource> {

    @Autowired
    private UserSessionService userSessionService;

    @Autowired
    private UserService userService;

    @Context
    private SecurityContext securityContext;

    private User follower;
    private String sessionId;
    private User toBeFollowed;

    @Before
    public void initialize(){
        userService.signup("Mihai","password");
        follower=userService.get("Mihai").get();
        sessionId=userSessionService.createUserSession(follower);
        userService.signup("Adi","password");
        toBeFollowed=userService.get("Adi").get();
        userSessionService.createUserSession(toBeFollowed);
    }
    @After
    public void after(){
        userService.removeAll();
    }

    @Test
    public void testFollow(){
        Response response=target("follower/follow").queryParam("sessionId",sessionId).queryParam("userName",toBeFollowed.getUsername()).request().get();
        assertEquals(response.getStatusInfo(), Response.Status.OK);
    }

    @Test
    public void testUnfollow(){
        target("follower/follow").queryParam("sessionId",sessionId).queryParam("userName",toBeFollowed.getUsername()).request().get();
        Response response=target("follower/unsubscribe").queryParam("sessionId",sessionId).queryParam("userName",toBeFollowed.getUsername()).request().get();
        assertEquals(response.getStatusInfo(),Response.Status.OK);
    }
}
