package toj.demo.whatsup.http.filters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import toj.demo.whatsup.domain.User;
import toj.demo.whatsup.user.services.UserSessionService;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.nio.file.attribute.UserPrincipal;
import java.security.Principal;
import java.util.Optional;

/**
 * Created by mihai.popovici on 9/30/2015.
 */
@Provider
@Component
@Authentication
@Priority(Priorities.HEADER_DECORATOR)
public class AuthenticationFilter implements ContainerRequestFilter {

    private final UserSessionService userSessionService;

    @Autowired
    public AuthenticationFilter(UserSessionService userSessionService) {
        this.userSessionService = userSessionService;

    }

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        String sessionId = containerRequestContext.getUriInfo().getQueryParameters().getFirst("sessionId");
        Optional<User> optionalUser = userSessionService.getUserBySession(sessionId);

        if (optionalUser.isPresent()) {
            containerRequestContext.setSecurityContext(new SecurityContext() {
                @Override
                public Principal getUserPrincipal() {
                    return userSessionService.getUserBySession(sessionId).get();
                }

                @Override
                public boolean isUserInRole(String s) {
                    return false;
                }

                @Override
                public boolean isSecure() {
                    return false;
                }

                @Override
                public String getAuthenticationScheme() {
                    return "custom";
                }
            });
        } else {
            containerRequestContext.abortWith(Response.status(Response.Status.FORBIDDEN).build());
        }
    }
}
