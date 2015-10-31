package toj.demo.whatsup.user.http.resource;

import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import toj.demo.whatsup.test.jersey.SpringManagedResourceTest;
import toj.demo.whatsup.user.services.UserService;
import toj.demo.whatsup.user.services.UserSessionService;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;

@ContextConfiguration
public class UserResourceTest extends SpringManagedResourceTest<UserResource> {

    @Autowired
    private UserService userService;

    @Autowired
    private UserSessionService userSessionService;


    @Test
    public void testAcceptsOnlyJson() {
        final Response response = target("user/signup").request().put(Entity.text(""));
        assertEquals(Response.Status.UNSUPPORTED_MEDIA_TYPE, response.getStatusInfo());
    }

    @Test
    public void testReturnsCreatedOnCorrectRequest() {

        final Response response = target("user/signup").request().put(Entity.json("{\"username\":\"Mihai1\",\"password\":\"password\"}"));
        assertEquals(Response.Status.CREATED, response.getStatusInfo());

    }

    @Test
    public void testLoginsAfterUserCreatedSucceeds() {
        target("user/signup").request().put(Entity.json("{\"username\":\"Mihai2\",\"password\":\"password\"}"));
        final Response response=target("user/login").request().post(Entity.json("{\"username\":\"Mihai2\",\"password\":\"password\"}"));
        assertEquals(response.readEntity(SessionResponse.class).getResults().get(0).getUserName(),"Mihai2");
    }
    @Test
    public void testLoginInvalidUserNameFails(){
        final Response response=target("user/login").request().post(Entity.json("{\"username\":\"Mihai3\",\"password\":\"password\"}"));
        assertEquals(response.getStatusInfo(),Response.Status.INTERNAL_SERVER_ERROR);
    }
    @Test
    public void testLogOutSucceeds(){
        target("user/signup").request().put(Entity.json("{\"username\":\"Mihai4\",\"password\":\"password\"}"));
        Response loginResponse=target("user/login").request().post(Entity.json("{\"username\":\"Mihai4\",\"password\":\"password\"}"));
        String sessionId=loginResponse.readEntity(SessionResponse.class).getResults().get(0).getSessionId();
        Response logOutResponse=target("user/logout").queryParam("sessionId",sessionId).request().put(Entity.text(""));
        assertEquals(logOutResponse.getStatusInfo(),Response.Status.OK);
    }
}