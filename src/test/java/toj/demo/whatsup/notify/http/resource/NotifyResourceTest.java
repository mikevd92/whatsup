package toj.demo.whatsup.notify.http.resource;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import toj.demo.whatsup.test.jersey.SpringManagedResourceTest;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

/**
 * Created by mihai.popovici on 11/4/2015.
 */
@ContextConfiguration
public class NotifyResourceTest extends SpringManagedResourceTest<NotifyResource> {
    @Test
    public void testSetKeywords(){
        Response response=target("notify/addkeywords").request().put(Entity.json("{\"keywords\":[\"Ioana\",\"Maria\",\"Mihaela\"]}"));
        System.out.println(response.readEntity(KeywordsSet.class).getKeywords());
    }
}
