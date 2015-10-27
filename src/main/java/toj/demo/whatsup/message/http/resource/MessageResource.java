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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.text.DateFormat;
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
    public MessageResource(final MessageService messageService, final Mapper mapper) {
        this.messageService = messageService;
        this.mapper = mapper;
    }

    @POST
    @Path("/submit")
    @Produces(MediaType.APPLICATION_JSON)
    public Response submitMessage(@QueryParam("message") String msg, @Context SecurityContext securityContext) {
        Preconditions.checkNotNull(msg);
        User user = (User) securityContext.getUserPrincipal();
        Message message = new Message(msg, user);
        messageService.addNewMessage(message);
        return Response.status(Response.Status.OK).build();
    }

    @GET
    @Path("/status")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStatusCall(@Context SecurityContext securityContext) {
        User user = (User) securityContext.getUserPrincipal();
        Optional<Message> optionalMessage=messageService.getStatusMessage(user);
        if(optionalMessage.isPresent()) {
            Message message = optionalMessage.get();
            MessageDTO messageDTO = mapper.map(message, MessageDTO.class);
            UserDTO userDTO = mapper.map(user, UserDTO.class);
            messageDTO.setUserDTO(userDTO);
            MessageResponse response = new MessageResponse(Collections.singletonList(messageDTO));
            return Response.status(Response.Status.OK).entity(response).build();
        }else{
            MessageResponse response = new MessageResponse(Collections.EMPTY_LIST);
            return Response.status(Response.Status.OK).entity(response).build();
        }
    }

    @GET
    @Path("/updates")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUpdates(@QueryParam("timestamp") String timestamp, @Context SecurityContext securityContext) {
        Preconditions.checkNotNull(timestamp);
        DateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
        Date date;
        try {
            date = format.parse(timestamp);
        } catch (ParseException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }


        User user = (User) securityContext.getUserPrincipal();

        List<Message> messageList = messageService.getUpdates(date, user);
        List<MessageDTO> messageDTOList = new LinkedList<>();
        UserDTO userDTO=mapper.map(user,UserDTO.class);
        for (Message message : messageList) {
            MessageDTO messageDTO=mapper.map(message, MessageDTO.class);
            messageDTO.setUserDTO(userDTO);
            messageDTOList.add(messageDTO);
        }
        MessageResponse response = new MessageResponse(messageDTOList);
        return Response.status(Response.Status.OK).entity(response).build();
    }

    @GET
    @Path("/latestmessages")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLatestMessages(@Context SecurityContext securityContext) {
        User user = (User) securityContext.getUserPrincipal();
        Set<User> followers = user.getFollowers();
        List<Message> latestMessages = messageService.getLatestMessages(followers);
        List<MessageDTO> messageDTOList = new LinkedList<>();
        for (Message message : latestMessages) {
            MessageDTO messageDTO=mapper.map(message, MessageDTO.class);
            UserDTO userDTO=mapper.map(message.getUser(),UserDTO.class);
            messageDTO.setUserDTO(userDTO);
            messageDTOList.add(messageDTO);
        }
        MessageResponse response = new MessageResponse(messageDTOList);
        return Response.status(Response.Status.OK).entity(response).build();
    }
}

