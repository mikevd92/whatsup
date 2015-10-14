package toj.demo.whatsup.user.http.resource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.kubek2k.springockito.annotations.ReplaceWithMock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import toj.demo.whatsup.test.jersey.SpringManagedResourceTest;
import toj.demo.whatsup.user.service.UserService;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by mihai.popovici on 9/24/2015.
 */

@ContextConfiguration
public class UserResourceTest extends SpringManagedResourceTest<UserResource> {

    @Autowired
    private UserService userService;

    @After
    public void after(){
        userService.removeAll();
    }
    @Test
    public void testAcceptsOnlyJson() {
        final Response response = target("user/signup").request().put(Entity.text(""));
        assertEquals(Response.Status.UNSUPPORTED_MEDIA_TYPE, response.getStatusInfo());
    }

    @Test
    public void testReturnsCreatedOnCorrectRequest() {

        final Response response = target("user/signup").request().put(Entity.json("{\"username\":\"Mihai\",\"password\":\"password\"}"));
        assertEquals(Response.Status.CREATED, response.getStatusInfo());

    }

    @Test
    public void testReturnsUserDetailsWhenUserCreated() {
        final Response response=target("user/signup").request().put(Entity.json("{\"username\":\"Mihai\",\"password\":\"password\"}"));
        assertEquals(response.getStatusInfo(),Response.Status.CREATED);
    }

    @Test
    public void testLoginsAfterUserCreated() {
        target("user/signup").request().put(Entity.json("{\"username\":\"Mihai\",\"password\":\"password\"}"));
        final Response response=target("user/login").request().post(Entity.json("{\"username\":\"Mihai\",\"password\":\"password\"}"));
        assertEquals(response.readEntity(SessionResponse.class).getResults().get(0).getUserName(),"Mihai");
    }


}