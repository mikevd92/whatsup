package toj.demo.whatsup.follower.http.resource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import toj.demo.whatsup.domain.Credentials;
import toj.demo.whatsup.domain.User;
import toj.demo.whatsup.test.jersey.BaseResourceTest;
import toj.demo.whatsup.test.jersey.SpringManagedResourceTest;
import toj.demo.whatsup.user.services.UserService;
import toj.demo.whatsup.user.services.UserSessionService;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.Arrays;
import java.util.List;

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
    public void initialize() {
        Credentials credentialsMihai=new Credentials("Mihai","password");
        userService.signup(credentialsMihai);
        follower = userService.get("Mihai").get();
        sessionId = userSessionService.createUserSession(follower);
        Credentials credentialsAdi=new Credentials("Adi","password");
        userService.signup(credentialsAdi);
        toBeFollowed = userService.get("Adi").get();
        userSessionService.createUserSession(toBeFollowed);
    }

    @After
    public void after() {
        userService.removeAll();
    }

    @Test
    public void testFollowNoSessionReturnsBadRequest() {
        Response response = target("follower/follow").queryParam("userName", toBeFollowed.getUsername()).request().put(Entity.text(""));
        assertEquals(response.getStatusInfo(), Response.Status.BAD_REQUEST);
    }

    @Test
    public void testNoSessionReturnsBadRequest() {
        Response followResponse=target("follower/follow").request().put(Entity.text(""));
        assertEquals(followResponse.getStatusInfo(), Response.Status.BAD_REQUEST);
        Response unfollowResponse=target("follower/unsubscribe").request().delete();
        assertEquals(unfollowResponse.getStatusInfo(), Response.Status.BAD_REQUEST);

    }


    @Test
    public void testFollowBadUserNameReturnsInternalServerError() {
        Response response = target("follower/follow").queryParam("sessionId", sessionId).queryParam("userName", "John").request().put(Entity.text(""));
        assertEquals(response.getStatusInfo(), Response.Status.INTERNAL_SERVER_ERROR);
    }

    @Test
    public void testUnFollowBadUserNameReturnsInternalServerError() {
        Response response = target("follower/unsubscribe").queryParam("sessionId", sessionId).queryParam("userName", "John").request().delete();
        assertEquals(response.getStatusInfo(), Response.Status.INTERNAL_SERVER_ERROR);
    }

    @Test
    public void testFollowSucceeds() {
        Response response = target("follower/follow").queryParam("sessionId", sessionId).queryParam("userName", toBeFollowed.getUsername()).request().put(Entity.text(""));
        assertEquals(response.getStatusInfo(), Response.Status.OK);
    }

    @Test
    public void testFollowEmptyUserReturnsBadRequest() {
        Response response = target("follower/follow").queryParam("sessionId", sessionId).request().put(Entity.text(""));
        assertEquals(response.getStatusInfo(), Response.Status.BAD_REQUEST);
    }

    @Test
    public void testUnfollowSucceeds() {
        target("follower/follow").queryParam("sessionId", sessionId).queryParam("userName", toBeFollowed.getUsername()).request().put(Entity.text(""));
        Response response = target("follower/unsubscribe").queryParam("sessionId", sessionId).queryParam("userName", toBeFollowed.getUsername()).request().delete();
        assertEquals(response.getStatusInfo(), Response.Status.OK);
    }

    @Test
    public void testUnfollowEmptyUserReturnsBadRequest() {
        target("follower/follow").queryParam("sessionId", sessionId).queryParam("userName", toBeFollowed.getUsername()).request().put(Entity.text(""));
        Response response = target("follower/unsubscribe").queryParam("sessionId", sessionId).request().delete();
        assertEquals(response.getStatusInfo(), Response.Status.BAD_REQUEST);
    }
}
