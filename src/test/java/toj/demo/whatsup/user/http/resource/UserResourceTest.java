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

    private static int count=0;
    @After
    public void after(){
        count++;
    }
    @Test
    public void testAcceptsOnlyJson() {
        final Response response = target("user/signup").request().post(Entity.text(""));
        assertEquals(Response.Status.UNSUPPORTED_MEDIA_TYPE, response.getStatusInfo());
    }

    @Test
    public void testReturnsCreatedOnCorrectRequest() {

        final Response response = target("user/signup").request().post(Entity.json("{\"username\":\"Mihai"+count+"\",\"password\":\"password\"}"));
        assertEquals(Response.Status.CREATED, response.getStatusInfo());

    }

    @Test
    public void testLoginsAfterUserCreatedSucceeds() {
        target("user/signup").request().post(Entity.json("{\"username\":\"Mihai"+count+"\",\"password\":\"password\"}"));
        final Response response=target("user/login").request().put(Entity.json("{\"username\":\"Mihai"+count+"\",\"password\":\"password\"}"));
        assertEquals(response.readEntity(SessionResponse.class).getResults().get(0).getUserName(),"Mihai"+count);
    }
    @Test
    public void testLoginInvalidUserNameFails(){
        final Response response=target("user/login").request().put(Entity.json("{\"username\":\"Mihai"+count+"\",\"password\":\"password\"}"));
        assertEquals(response.getStatusInfo(),Response.Status.INTERNAL_SERVER_ERROR);
    }
    @Test
    public void testLogOutSucceeds(){
        target("user/signup").request().post(Entity.json("{\"username\":\"Mihai"+count+"\",\"password\":\"password\"}"));
        Response loginResponse=target("user/login").request().put(Entity.json("{\"username\":\"Mihai"+count+"\",\"password\":\"password\"}"));
        String sessionId=loginResponse.readEntity(SessionResponse.class).getResults().get(0).getSessionId();
        Response logOutResponse=target("user/logout").queryParam("sessionId",sessionId).request().put(Entity.text(""));
        assertEquals(logOutResponse.getStatusInfo(),Response.Status.OK);
    }
}