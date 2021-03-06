
package toj.demo.whatsup.message.http.resource;

import com.google.common.base.Preconditions;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import toj.demo.whatsup.domain.Message;
import toj.demo.whatsup.domain.User;
import toj.demo.whatsup.http.filters.Authentication;
import toj.demo.whatsup.http.filters.Session;
import toj.demo.whatsup.message.services.MessageService;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@Session
@Authentication
@Path("/message")
public final class MessageResource {

    private final MessageService messageService;

    private final Mapper mapper;

    private Function<Message,MessageDTO> mapFunction;

    @Autowired
    public MessageResource(MessageService messageService, Mapper mapper) {
        this.messageService = messageService;
        this.mapper = mapper;
        mapFunction=p -> {
            MessageDTO messageDTO = mapper.map(p, MessageDTO.class);
            Message message = p;
            if (message.getUser() != null) {
                messageDTO.setUserName(message.getUser().getName());
            }
            return messageDTO;
        };
    }

    @POST
    @Path("/submit")
    @Produces({"application/json"})
    public Response submitMessage(@QueryParam("message") String msg,@QueryParam("deleteOn") String deletionTimestamp,
                                  @Context SecurityContext securityContext) {
        Preconditions.checkNotNull(msg);
        User user = (User)securityContext.getUserPrincipal();
        Date date;
        if(deletionTimestamp!=null) {
            SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy",Locale.ENGLISH);
            try {
                date = format.parse(deletionTimestamp);
            } catch (ParseException ex) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
            Date now=new Date();
            if(date.before(now)){
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
            Message message = new Message(msg, user,now,date);
            this.messageService.addNewMessage(message);
            return Response.status(Response.Status.OK).build();
        }else{
            Message message = new Message(msg, user);
            this.messageService.addNewMessage(message);
            return Response.status(Response.Status.OK).build();
        }
    }

    @GET
    @Path("/status")
    @Produces({"application/json"})
    public Response getStatusCall(@Context SecurityContext securityContext) {
        User user = (User)securityContext.getUserPrincipal();
        Optional<Message> optionalMessage = this.messageService.getStatusMessage(user);
        if(optionalMessage.isPresent()) {
            Message message = optionalMessage.get();
            MessageDTO messageDTO = this.mapper.map(message, MessageDTO.class);
            if(message.getUser()!=null) {
                messageDTO.setUserName(message.getUser().getName());
            }
            MessageResponse response = new MessageResponse(Collections.singletonList(messageDTO));
            return Response.status(Response.Status.OK).entity(response).build();
        } else {
            MessageResponse response = new MessageResponse(Collections.EMPTY_LIST);
            return Response.status(Response.Status.OK).entity(response).build();
        }
    }

    @GET
    @Path("/updates")
    @Produces({"application/json"})
    public Response getUpdates(@QueryParam("timestamp") String timestamp, @Context SecurityContext securityContext) {
        Preconditions.checkNotNull(timestamp);
        SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy",Locale.ENGLISH);

        Date date;
        try {
            date = format.parse(timestamp);
        } catch (ParseException ex) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        User user = (User)securityContext.getUserPrincipal();
        List<MessageDTO> messageDTOList = this.messageService.getUpdates(date, user).stream()
                .map(mapFunction)
                .collect(Collectors.toList());

        MessageResponse response1 = new MessageResponse(messageDTOList);
        return Response.status(Response.Status.OK).entity(response1).build();
    }

    @GET
    @Path("/latestmessages")
    @Produces({"application/json"})
    public Response getLatestMessages(@Context SecurityContext securityContext) {
        User user = (User)securityContext.getUserPrincipal();
        Set<User> followers = user.getFollowers();

        Stream<Message> stream=this.messageService.getLatestMessages(followers).stream();
        List<MessageDTO> messageDTOList=stream.map(mapFunction)
        .collect(Collectors.toList());
        MessageResponse response = new MessageResponse(messageDTOList);
        return Response.status(Response.Status.OK).entity(response).build();
    }
}
