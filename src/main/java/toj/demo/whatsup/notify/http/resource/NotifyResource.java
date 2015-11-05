package toj.demo.whatsup.notify.http.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import toj.demo.whatsup.domain.Keyword;
import toj.demo.whatsup.domain.User;
import toj.demo.whatsup.http.filters.Authentication;
import toj.demo.whatsup.http.filters.Session;
import toj.demo.whatsup.notify.services.KeywordService;
import toj.demo.whatsup.user.services.UserService;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by mihai.popovici on 11/4/2015.
 */
@Component
@Path("/notify")
public class NotifyResource {
    private final UserService userService;
    private final KeywordService keywordService;

    @Autowired
    public NotifyResource(UserService userService,KeywordService keywordService) {
        this.userService = userService;
        this.keywordService = keywordService;
    }

    @PUT
    @Path("/addkeywords")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Session
    @Authentication
    public Response addKeywords(KeywordsSet keywordsSet,@Context SecurityContext context){
        User user=(User)context.getUserPrincipal();
        Set<String> keywordsStrings=keywordsSet.getKeywords();
        keywordsStrings.removeAll(keywordService.getKeywordsTextsByTexts(keywordsStrings));
        Set<Keyword> keywords=keywordsStrings.stream().map(p -> new Keyword(p)).collect(Collectors.toSet());
        keywordService.saveKeywords(keywords);
        return Response.status(Response.Status.OK).entity(keywordsSet).build();
    }
    @PUT
    @Path("/changeperiod")
    @Session
    @Authentication
    @Produces(MediaType.APPLICATION_JSON)
    public Response changeNotifyPeriod(@QueryParam("sessionId") String sessionId,@QueryParam("notifyperiod")Long notifyPeriod,@Context SecurityContext context){
        User user=(User)context.getUserPrincipal();
        userService.changeNotifyPeriod(user,notifyPeriod);
        NotificationResponse notificationResponse =new NotificationResponse(sessionId,user.getUsername(),notifyPeriod);
        return Response.status(Response.Status.OK).entity(notificationResponse).build();
    }


}
