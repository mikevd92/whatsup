package toj.demo.whatsup.user.http.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import toj.demo.whatsup.domain.User;
import toj.demo.whatsup.user.services.UserService;
import toj.demo.whatsup.user.services.UserSessionService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;

@Component
@Path("user")
public final class UserResource {


    private final UserService userService;
    private final UserSessionService userSessionService;

    @Autowired
    public UserResource(final UserService userService, final UserSessionService userSessionService) {
        this.userService = userService;
        this.userSessionService = userSessionService;
    }

    @PUT
    @Path("/signup")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response signup(CredentialsDTO credentialsDTO) {
        final Optional<User> user = userService.get(credentialsDTO.getUsername());
        if (user.isPresent()) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        userService.signup(credentialsDTO);
        return Response.status(Response.Status.CREATED).build();
    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(CredentialsDTO credentialsDTO) {

        if (!userService.checkUser(credentialsDTO)) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

        User user = userService.get(credentialsDTO.getUsername()).get();
        if (userSessionService.userExists(user)) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

        String sessionId = userSessionService.createUserSession(user);
        //String json="{\"results\":[{\"sessionId\":\""+sessionId+"\",\"userName\":\"" + user.getUsername() + "\"}]}";
        SessionResponse sessionResponse = new SessionResponse(new SessionDTO(sessionId, user.getUsername()));
        return Response.status(Response.Status.OK).entity(sessionResponse).build();
    }
}
