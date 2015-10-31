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
    private Credentials credentialsAdi;
    private Credentials credentialsMihai;


    @Test
    public void testFollowNoSessionReturnsBadRequest() {
        initFollowerSetup("Mihai1","Adi1");
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
        initFollowerSetup("Mihai2","Adi2");
        Response response = target("follower/follow").queryParam("sessionId", sessionId).queryParam("username", "John").request().put(Entity.text(""));
        assertEquals(response.getStatusInfo(), Response.Status.INTERNAL_SERVER_ERROR);
    }

    @Test
    public void testUnFollowBadUserNameReturnsInternalServerError() {
        initFollowerSetup("Mihai3","Adi3");
        Response response = target("follower/unsubscribe").queryParam("sessionId", sessionId).queryParam("username", "John").request().delete();
        assertEquals(response.getStatusInfo(), Response.Status.INTERNAL_SERVER_ERROR);
    }

    @Test
    public void testFollowSucceeds() {
        initFollowerSetup("Mihai4","Adi4");
        Response response = target("follower/follow").queryParam("sessionId", sessionId).queryParam("username", toBeFollowed.getUsername()).request().put(Entity.text(""));
        assertEquals(response.getStatusInfo(), Response.Status.OK);
    }

    @Test
    public void testFollowEmptyUserReturnsBadRequest() {
        initFollowerSetup("Mihai5","Adi5");
        Response response = target("follower/follow").queryParam("sessionId", sessionId).request().put(Entity.text(""));
        assertEquals(response.getStatusInfo(), Response.Status.BAD_REQUEST);
    }

    @Test
    public void testUnfollowSucceeds() {
        initFollowerSetup("Mihai6","Adi6");
        target("follower/follow").queryParam("sessionId", sessionId).queryParam("username", toBeFollowed.getUsername()).request().put(Entity.text(""));
        Response response = target("follower/unsubscribe").queryParam("sessionId", sessionId).queryParam("username", toBeFollowed.getUsername()).request().delete();
        assertEquals(response.getStatusInfo(), Response.Status.OK);
    }

    @Test
    public void testUnfollowEmptyUserReturnsBadRequest() {
        initFollowerSetup("Mihai7","Adi7");
        target("follower/follow").queryParam("sessionId", sessionId).queryParam("username", toBeFollowed.getUsername()).request().put(Entity.text(""));
        Response response = target("follower/unsubscribe").queryParam("sessionId", sessionId).request().delete();
        assertEquals(response.getStatusInfo(), Response.Status.BAD_REQUEST);
    }
    private void initFollowerSetup(String beFollowedName,String followerName){
        credentialsMihai=new Credentials(beFollowedName,"password");
        userService.signup(credentialsMihai);
        follower = userService.get(beFollowedName).get();
        sessionId = userSessionService.createUserSession(follower);
        credentialsAdi=new Credentials(followerName,"password");
        userService.signup(credentialsAdi);
        toBeFollowed = userService.get(followerName).get();
        userSessionService.createUserSession(toBeFollowed);
    }
}
