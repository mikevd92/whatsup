package toj.demo.whatsup.notify.http.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import toj.demo.whatsup.user.services.UserService;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by mihai.popovici on 11/4/2015.
 */
@Component
@Path("/notify")
public class NotifyResource {
    private UserService userService;

    @Autowired
    public NotifyResource(final UserService userService) {
        this.userService = userService;
    }

    @PUT
    @Path("/addkeywords")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response setKeywords(KeywordsSet keywords){


        return Response.status(Response.Status.OK).entity(keywords).build();
    }

}
