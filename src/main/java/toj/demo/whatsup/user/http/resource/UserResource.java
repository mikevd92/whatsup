package toj.demo.whatsup.user.http.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import toj.demo.whatsup.user.model.PotentialUser;
import toj.demo.whatsup.user.model.User;
import toj.demo.whatsup.user.service.UserService;
import toj.demo.whatsup.user.service.UserSessionService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;
import java.util.UUID;

@Component
@Path("user")
public final class UserResource {


    private final UserService userService;
    private final UserSessionService userSessionService;
    @Autowired
    public UserResource(final UserService userService,final UserSessionService userSessionService){
        this.userService=userService;
        this.userSessionService=userSessionService;
    }
    @PUT
    @Path("/signup")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response signup(PotentialUser potentialUser) {
        final Optional<User> user = userService.get(potentialUser.getUsername());
        if (user.isPresent()) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        userService.signup(potentialUser.getUsername(), potentialUser.getPassword());
        return Response.status(Response.Status.CREATED).build();
    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(User user){
        if(userService.has(user)){
          String sessionId=UUID.randomUUID().toString();
            String json="{\"results\":[{\"sessionId\":\""+sessionId+"\",\"userName\":\"" + user.getUsername() + "\"}]}";
            if(!userSessionService.userExists(user)){
                userSessionService.addUserSession(sessionId,userService.get(user.getUsername()).get());
                return Response.status(Response.Status.OK).entity(json).build();
            }else{
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            }
        }else{
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}
