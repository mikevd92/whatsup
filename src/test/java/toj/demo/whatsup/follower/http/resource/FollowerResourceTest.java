package toj.demo.whatsup.follower.http.resource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import toj.demo.whatsup.domain.Credentials;
import toj.demo.whatsup.domain.User;
import toj.demo.whatsup.test.jersey.SpringManagedResourceTest;
import toj.demo.whatsup.user.services.UserService;
import toj.demo.whatsup.user.services.UserSessionService;

import javax.ws.rs.client.Entity;
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
    private String followerSessionId;
    private String toBeFollowedSessionId;
    private User toBeFollowed;
    private Credentials credentialsAdi;
    private Credentials credentialsMihai;
    private static int count=0;

    @Before
    public void initialize(){
        credentialsMihai = new Credentials("Mihai"+count,"password","misuvd92"+count+"@yahoo.com");
        userService.signup(credentialsMihai);
        toBeFollowed = userService.get("Mihai"+count).get();
        toBeFollowedSessionId = userSessionService.createUserSession(toBeFollowed);
        credentialsAdi=new Credentials("Adi"+count,"password","adi"+count+"@yahoo.com");
        userService.signup(credentialsAdi);
        follower = userService.get("Adi"+count).get();
        followerSessionId = userSessionService.createUserSession(follower);
    }
    @After
    public void after(){
        count++;
    }


    @Test
    public void testFollowNoSessionReturnsBadRequest() {
        Response response = target("follower/follow").queryParam("username", toBeFollowed.getUsername()).request().put(Entity.text(""));
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
    public void testWrongSessionReturnsForbidden() {
        Response followResponse=target("follower/follow").queryParam("sessionId","wrong").request().put(Entity.text(""));
        assertEquals(followResponse.getStatusInfo(), Response.Status.FORBIDDEN);
        Response unfollowResponse=target("follower/unsubscribe").queryParam("sessionId","wrong").request().delete();
        assertEquals(unfollowResponse.getStatusInfo(), Response.Status.FORBIDDEN);

    }


    @Test
    public void testFollowBadUserNameReturnsInternalServerError() {
        Response response = target("follower/follow").queryParam("sessionId", followerSessionId).queryParam("username", "John").request().put(Entity.text(""));
        assertEquals(response.getStatusInfo(), Response.Status.INTERNAL_SERVER_ERROR);
    }

    @Test
    public void testUnFollowBadUserNameReturnsInternalServerError() {
        Response response = target("follower/unsubscribe").queryParam("sessionId", followerSessionId).queryParam("username", "John").request().delete();
        assertEquals(response.getStatusInfo(), Response.Status.INTERNAL_SERVER_ERROR);
    }

    @Test
    public void testFollowSucceeds() {
        Response response = target("follower/follow").queryParam("sessionId", followerSessionId).queryParam("username", toBeFollowed.getUsername()).request().put(Entity.text(""));
        assertEquals(response.getStatusInfo(), Response.Status.OK);
    }

    @Test
    public void testFollowEmptyUserReturnsBadRequest() {
        Response response = target("follower/follow").queryParam("sessionId", followerSessionId).request().put(Entity.text(""));
        assertEquals(response.getStatusInfo(), Response.Status.BAD_REQUEST);
    }
    @Test
    public void testFollowerFollowsFollower(){
        Response response = target("follower/follow").queryParam("sessionId", toBeFollowedSessionId).queryParam("username", toBeFollowed.getUsername()).request().put(Entity.text(""));
        assertEquals(response.getStatusInfo(), Response.Status.INTERNAL_SERVER_ERROR);
    }

    @Test
    public void testUnfollowSucceeds() {
        target("follower/follow").queryParam("sessionId", followerSessionId).queryParam("username", toBeFollowed.getUsername()).request().put(Entity.text(""));
        Response response = target("follower/unsubscribe").queryParam("sessionId", followerSessionId).queryParam("username", toBeFollowed.getUsername()).request().delete();
        assertEquals(response.getStatusInfo(), Response.Status.OK);
    }

    @Test
    public void testUnfollowEmptyUserReturnsBadRequest() {
        target("follower/follow").queryParam("sessionId", followerSessionId).queryParam("username", toBeFollowed.getUsername()).request().put(Entity.text(""));
        Response response = target("follower/unsubscribe").queryParam("sessionId", followerSessionId).request().delete();
        assertEquals(response.getStatusInfo(), Response.Status.BAD_REQUEST);
    }

}
