package toj.demo.whatsup.notify.http.resource;

import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import toj.demo.whatsup.domain.Keyword;
import toj.demo.whatsup.domain.User;
import toj.demo.whatsup.http.filters.Authentication;
import toj.demo.whatsup.http.filters.Session;
import toj.demo.whatsup.notify.services.KeywordService;
import toj.demo.whatsup.user.services.UserService;
import toj.demo.whatsup.user.services.UserSessionService;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.LinkedHashSet;
import java.util.List;
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
    private final UserSessionService userSessionService;

    @Autowired
    public NotifyResource(UserService userService,KeywordService keywordService,UserSessionService userSessionService) {
        this.userService = userService;
        this.keywordService = keywordService;
        this.userSessionService=userSessionService;
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

        Set<Keyword> oldKeywords=keywordService.getExistingKeywords(keywordsStrings);
        user=userService.get(user.getName()).get();
        if(oldKeywords.size()!=0){
            Set<Keyword> userkeywords=user.getKeywords();
            if(userkeywords.size()!=0) {
                Set<Keyword> filteredKeywords = oldKeywords.stream().filter(p -> !userkeywords.contains(p)).collect(Collectors.toSet());
                oldKeywords.removeAll(filteredKeywords);
            }
            userService.addKeywordsToUser(user,oldKeywords);

        }
        keywordsStrings.removeAll(keywordService.checkExistingKeywordTexts(keywordsStrings));
        if(keywordsStrings.size()!=0) {
            Set<Keyword> newKeywords = keywordsStrings.stream().map(p -> new Keyword(p)).collect(Collectors.toSet());
            keywordService.saveKeywords(newKeywords);
            userService.addKeywordsToUser(user, newKeywords);
        }

        keywordsStrings.addAll(oldKeywords.stream().map(p -> p.getText()).collect(Collectors.toSet()));
        keywordsSet.setKeywords(keywordsStrings);
        return Response.status(Response.Status.OK).entity(keywordsSet).build();
    }
    @PUT
    @Path("/removekeywords")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Session
    @Authentication
    public Response removeKeywords(KeywordsSet keywordsSet,@Context SecurityContext context) {
        User user=(User)context.getUserPrincipal();
        user=userService.get(user.getName()).get();
        Set<String> keywordStrings=keywordsSet.getKeywords();
        Set<Keyword> keywords=user.getKeywords().stream().filter(p -> keywordStrings.contains(p.getText())).collect(Collectors.toSet());
        userService.removeKeywordsFromUser(user,keywords);
        keywordsSet.setKeywords(keywords.stream().map(p -> p.getText()).collect(Collectors.toSet()));
        return Response.status(Response.Status.OK).entity(keywordsSet).build();
    }

    @DELETE
    @Path("/removejob")
    @Session
    @Authentication
    public Response removeNotifyJob(@Context SecurityContext context) throws SchedulerException {
        User user=(User)context.getUserPrincipal();
        if(!userService.checkJobExists(user.getId())){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        userService.removeJob(user);
        return Response.status(Response.Status.OK).build();
    }

    @POST
    @Path("/requestjob")
    @Session
    @Authentication
    public Response requestNotifyJob(@Context SecurityContext context) throws SchedulerException {
        User user=(User)context.getUserPrincipal();
        user=userService.get(user.getName()).get();
        if(user.getNotificationPeriod()==null||user.getKeywords().size()==0){
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        if(userService.checkJobExists(user.getId())){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        userService.addJob(user);
        return Response.status(Response.Status.OK).build();
    }


    @PUT
    @Path("/changeperiod")
    @Session
    @Authentication
    @Produces(MediaType.APPLICATION_JSON)
    public Response changeNotifyPeriod(@QueryParam("sessionId") String sessionId,@QueryParam("notifyperiod") Integer notifyPeriod,@Context SecurityContext context){
        User user=(User)context.getUserPrincipal();
        userService.changeNotifyPeriod(user,notifyPeriod);
        NotificationResponse notificationResponse =new NotificationResponse(sessionId,user.getUsername(),notifyPeriod);
        return Response.status(Response.Status.OK).entity(notificationResponse).build();
    }


}
