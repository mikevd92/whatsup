package toj.demo.whatsup.follower.http.resource;

import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import toj.demo.whatsup.http.filters.Authentication;
import toj.demo.whatsup.http.filters.Session;
import toj.demo.whatsup.domain.User;
import toj.demo.whatsup.user.services.UserService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.Optional;

@Component
@Session
@Authentication
@Path("/follower")
public final class FollowerResource {


    private UserService userService;

    @Autowired
    public FollowerResource(final UserService userService) {
        this.userService = userService;
    }

    @GET
    @Path("/follow")
    @Produces(MediaType.APPLICATION_JSON)
    public Response follow(@QueryParam("userName") String userName, @Context SecurityContext securityContext) {
        Preconditions.checkNotNull(userName);
        Optional<User> toBeFollowed = userService.get(userName);
        User follower = (User) securityContext.getUserPrincipal();
        if (!toBeFollowed.isPresent()) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

        toBeFollowed.get().addFollower(follower);
        return Response.status(Response.Status.OK).build();
    }

    @GET
    @Path("/unsubscribe")
    @Produces(MediaType.APPLICATION_JSON)
    public Response unfollow(@QueryParam("userName") String userName, @Context SecurityContext securityContext) {
        Preconditions.checkNotNull(userName);
        Optional<User> toBeUnFollowed = userService.get(userName);
        User follower = (User) securityContext.getUserPrincipal();
        if (!toBeUnFollowed.isPresent()) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

        toBeUnFollowed.get().removeFollower(follower);
        return Response.status(Response.Status.OK).build();

    }
}