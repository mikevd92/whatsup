package toj.demo.whatsup.test.jersey;

import org.springframework.test.context.ContextConfiguration;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by mihai.popovici on 10/16/2015.
 */
@ContextConfiguration
public abstract class BaseResourceTest<R> extends SpringManagedResourceTest<R> {

    public void testNoSession(List<WebTarget> targets){
        for(WebTarget target : targets){
            Response response=target.request().get();
            assertEquals(response.getStatusInfo(), Response.Status.BAD_REQUEST);
        }
    }
}
