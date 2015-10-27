package toj.demo.whatsup.test.jersey;

import org.springframework.test.context.ContextConfiguration;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.util.List;

import static org.junit.Assert.assertEquals;

@ContextConfiguration
public abstract class BaseResourceTest<R> extends SpringManagedResourceTest<R> {

    public void testNoSessionGET(List<WebTarget> targets){
        for(WebTarget target : targets){
            Response response=target.request().get();
            assertEquals(response.getStatusInfo(), Response.Status.BAD_REQUEST);
        }
    }
    public void testNoSessionPUT(List<WebTarget> targets){
        for(WebTarget target : targets){
            Response response=target.request().put(Entity.text(""));
            assertEquals(response.getStatusInfo(), Response.Status.BAD_REQUEST);
        }
    }
}
