package toj.demo.whatsup.http.filters;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

/**
 * Created by mihai.popovici on 9/29/2015.
 */
@Provider
@Session
@Priority(Priorities.AUTHENTICATION)
public class SessionFilter implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        String sessionId = containerRequestContext.getUriInfo().getQueryParameters().getFirst("sessionId");
        if (sessionId == null) {
            containerRequestContext.abortWith(Response.status(Response.Status.BAD_REQUEST).build());
        }
    }
}
