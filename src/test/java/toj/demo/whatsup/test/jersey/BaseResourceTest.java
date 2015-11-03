package toj.demo.whatsup.test.jersey;

import org.springframework.test.context.ContextConfiguration;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.util.List;

import static org.junit.Assert.assertEquals;

@ContextConfiguration
public abstract class BaseResourceTest<R> extends SpringManagedResourceTest<R> {

    public void testNoSessionGETBadRequest(List<WebTarget> targets){
        targets.forEach(p -> assertEquals(p.request().get().getStatusInfo(),Response.Status.BAD_REQUEST));
    }
    public void testWrongSessionGETForbidden(List<WebTarget> targets){
        targets.forEach(p -> assertEquals(p.queryParam("sessionId","wrong").request().get().getStatusInfo(),Response.Status.FORBIDDEN));
    }

}
