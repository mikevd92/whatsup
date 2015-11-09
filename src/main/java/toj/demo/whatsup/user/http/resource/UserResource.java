package toj.demo.whatsup.user.http.resource;

import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import toj.demo.whatsup.domain.Credentials;
import toj.demo.whatsup.domain.User;
import toj.demo.whatsup.http.filters.Session;
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
    private final Mapper mapper;

    @Autowired
    public UserResource(final UserService userService, final UserSessionService userSessionService,final Mapper mapper) {
        this.userService = userService;
        this.userSessionService = userSessionService;
        this.mapper=mapper;
    }

    @POST
    @Path("/signup")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response signup(CredentialsDTO credentialsDTO) {
        final Optional<User> user = userService.get(credentialsDTO.getUsername());
        if (user.isPresent()) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        Credentials credentials=mapper.map(credentialsDTO,Credentials.class);
        userService.signup(credentials);
        return Response.status(Response.Status.CREATED).build();
    }

    @PUT
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(CredentialsDTO credentialsDTO) {
        Credentials credentials=mapper.map(credentialsDTO,Credentials.class);
        if (!userService.checkUser(credentials)) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        User user = userService.get(credentialsDTO.getUsername()).get();
        if (userSessionService.userExists(user)) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        String sessionId = userSessionService.createUserSession(user);
        SessionResponse sessionResponse = new SessionResponse(new SessionDTO(sessionId, user.getUsername()));
        return Response.status(Response.Status.OK).entity(sessionResponse).build();
    }
    @PUT
    @Session
    @Path("/logout")
    public Response logout(@QueryParam("sessionId") String sessionId){
        if(!userSessionService.sessionIdExists(sessionId)){
            return Response.status(Response.Status.FORBIDDEN).build();
        }else{
            userSessionService.removeUserSession(sessionId);
            return Response.status(Response.Status.OK).build();
        }

    }
}
