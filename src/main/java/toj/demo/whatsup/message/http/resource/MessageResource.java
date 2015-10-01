package toj.demo.whatsup.message.http.resource;

import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import toj.demo.whatsup.http.filter.Authentication;
import toj.demo.whatsup.http.filter.Session;
import toj.demo.whatsup.message.dto.MessageDTO;
import toj.demo.whatsup.message.model.Message;
import toj.demo.whatsup.message.service.MessageService;
import toj.demo.whatsup.user.model.User;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by mihai.popovici on 9/25/2015.
 */
@Component
@Session
@Path("/message")
@Authentication
public final class  MessageResource {


    private final MessageService messageService;
    private final Mapper mapper;

    @Autowired
    public MessageResource(final MessageService messageService,final Mapper mapper) {
        this.messageService = messageService;
        this.mapper=mapper;
    }

    @GET
    @Path("/submit")
    @Produces(MediaType.APPLICATION_JSON)
    public Response submitMessage(@QueryParam("message") String msg,@Context SecurityContext securityContext) {
        if (msg == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        User user = (User)securityContext.getUserPrincipal();
        Message message = new Message(msg, user.getUsername());
        messageService.addNewMessage(message, user.getUsername());
        return Response.status(Response.Status.OK).build();
    }

    @GET
    @Path("/status")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStatusCall(@Context SecurityContext securityContext) {
        User user = (User)securityContext.getUserPrincipal();
        Message message = messageService.getStatusMessage(user.getUsername());
        MessageDTO messageDTO=mapper.map(message,MessageDTO.class);
        MessageResponse response = new MessageResponse(Collections.singletonList(messageDTO));
        //String json = "{\"results\":[{\"message\":\"" + message.getMessage() + "\",\"creationTimestamp\":\"" + message.getCreationTimestamp() + "\"}]}";
        return Response.status(Response.Status.OK).entity(response).build();
    }

    @GET
    @Path("/updates")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUpdates(@QueryParam("timestamp") String timestamp,@Context SecurityContext securityContext) {
        if (timestamp == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        DateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
        Date date;
        try {
            date = format.parse(timestamp);
        } catch (ParseException e) {
            //e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }


        User user = (User)securityContext.getUserPrincipal();

        List<Message> messageList = messageService.getUpdates(date, user.getUsername());
        List<MessageDTO> messageDTOList=new LinkedList<>();
        for(Message message : messageList){
            messageDTOList.add(mapper.map(message,MessageDTO.class));
        }
        MessageResponse response = new MessageResponse(messageDTOList);
        return Response.status(Response.Status.OK).entity(response).build();
    }

    @GET
    @Path("/latestmessages")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLatestMessages(@Context SecurityContext securityContext) {
        User user = (User)securityContext.getUserPrincipal();
        Set<User> followers = user.getFollowers();
        List<Message> latestMessages = messageService.getLatestMessages(followers);
        List<MessageDTO> messageDTOList=new LinkedList<>();
        for(Message message : latestMessages){
            messageDTOList.add(mapper.map(message,MessageDTO.class));
        }
        MessageResponse response = new MessageResponse(messageDTOList);
        return Response.status(Response.Status.OK).entity(response).build();
    }
}

