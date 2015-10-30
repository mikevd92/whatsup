
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
import toj.demo.whatsup.user.http.resource.UserDTO;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
@Session
@Authentication
@Path("/message")
public final class MessageResource {
    private final MessageService messageService;
    private final Mapper mapper;

    @Autowired
    public MessageResource(MessageService messageService, Mapper mapper) {
        this.messageService = messageService;
        this.mapper = mapper;
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
            SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
            try {
                date = format.parse(deletionTimestamp);
            } catch (ParseException var12) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            }
            Date now=new Date();
            if(date.before(now)){
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
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
        Optional optionalMessage = this.messageService.getStatusMessage(user);
        if(optionalMessage.isPresent()) {
            Message response2 = (Message)optionalMessage.get();
            MessageDTO messageDTO = (MessageDTO)this.mapper.map(response2, MessageDTO.class);
            UserDTO userDTO = (UserDTO)this.mapper.map(user, UserDTO.class);
            messageDTO.setUserDTO(userDTO);
            MessageResponse response1 = new MessageResponse(Collections.singletonList(messageDTO));
            return Response.status(Response.Status.OK).entity(response1).build();
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
        SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");

        Date date;
        try {
            date = format.parse(timestamp);
        } catch (ParseException var12) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

        User user = (User)securityContext.getUserPrincipal();
        List messageList = this.messageService.getUpdates(date, user);
        LinkedList messageDTOList = new LinkedList();
        UserDTO userDTO = (UserDTO)this.mapper.map(user, UserDTO.class);
        Iterator response = messageList.iterator();

        while(response.hasNext()) {
            Message message = (Message)response.next();
            MessageDTO messageDTO = (MessageDTO)this.mapper.map(message, MessageDTO.class);
            messageDTO.setUserDTO(userDTO);
            messageDTOList.add(messageDTO);
        }

        MessageResponse response1 = new MessageResponse(messageDTOList);
        return Response.status(Response.Status.OK).entity(response1).build();
    }

    @GET
    @Path("/latestmessages")
    @Produces({"application/json"})
    public Response getLatestMessages(@Context SecurityContext securityContext) {
        User user = (User)securityContext.getUserPrincipal();
        Set followers = user.getFollowers();
        List latestMessages = this.messageService.getLatestMessages(followers);
        LinkedList messageDTOList = new LinkedList();
        Iterator response = latestMessages.iterator();

        while(response.hasNext()) {
            Message message = (Message)response.next();
            MessageDTO messageDTO = (MessageDTO)this.mapper.map(message, MessageDTO.class);
            UserDTO userDTO = (UserDTO)this.mapper.map(message.getUser(), UserDTO.class);
            messageDTO.setUserDTO(userDTO);
            messageDTOList.add(messageDTO);
        }

        MessageResponse response1 = new MessageResponse(messageDTOList);
        return Response.status(Response.Status.OK).entity(response1).build();
    }
}
