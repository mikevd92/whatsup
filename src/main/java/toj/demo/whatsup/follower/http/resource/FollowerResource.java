package toj.demo.whatsup.follower.http.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import toj.demo.whatsup.user.model.User;
import toj.demo.whatsup.user.service.UserService;
import toj.demo.whatsup.user.service.UserSessionService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;

/**
 * Created by mihai.popovici on 9/25/2015.
 */
@Component
@Path("/follower")
public class FollowerResource {
    @Autowired
    private UserSessionService userSessionService;
    @Autowired
    private UserService userService;

    @GET
    @Path("/follow")
    @Produces(MediaType.APPLICATION_JSON)
    public Response follow(@QueryParam("sessionId") String sessionId, @QueryParam("userName") String userName) {
        if (sessionId != null && userName != null) {
            Optional<User> toBeFollowed = userService.get(userName);
            Optional<User> follower = userSessionService.getUserBySession(sessionId);
            if (toBeFollowed.isPresent() && follower.isPresent()) {
                toBeFollowed.get().addFollower(follower.get());
                return Response.status(Response.Status.OK).build();
            } else {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            }
        } else {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @GET
    @Path("/unsubscribe")
    @Produces(MediaType.APPLICATION_JSON)
    public Response unfollow(@QueryParam("sessionId") String sessionId, @QueryParam("userName") String userName) {
        if (sessionId != null && userName != null) {
            Optional<User> toBeUnFollowed = userService.get(userName);
            Optional<User> follower = userSessionService.getUserBySession(sessionId);
            if (toBeUnFollowed.isPresent() && follower.isPresent()) {
                toBeUnFollowed.get().removeFollower(follower.get());
                return Response.status(Response.Status.OK).build();
            } else {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            }
        } else {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }
}