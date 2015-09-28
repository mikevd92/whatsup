package toj.demo.whatsup.message.http.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import toj.demo.whatsup.message.model.Message;
import toj.demo.whatsup.message.service.MessageService;
import toj.demo.whatsup.user.model.User;
import toj.demo.whatsup.user.service.UserSessionService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by mihai.popovici on 9/25/2015.
 */
    @Component
    @Path("/message")
    public class MessageResource {


        private final MessageService messageService;
        private final UserSessionService userSessionService;

    @Autowired
    public MessageResource(final MessageService messageService,final UserSessionService userSessionService){
        this.messageService = messageService;
        this.userSessionService = userSessionService;
    }
        @GET
        @Path("/submit")
        @Produces(MediaType.APPLICATION_JSON)
        public Response submitMessage(@QueryParam("sessionId") String sessionId, @QueryParam("message") String msg) {
            if (sessionId == null || msg == null) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            } else {
                Optional<User> user = userSessionService.getUserBySession(sessionId);
                if (!user.isPresent()) {
                    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
                } else {
                    Message message = new Message(msg, user.get().getUsername(), new Date());
                    messageService.addNewMessage(message, user.get().getUsername());
                    return Response.status(Response.Status.OK).build();
                }
            }
        }

        @GET
        @Path("/status")
        @Produces(MediaType.APPLICATION_JSON)
        public Response getStatusCall(@QueryParam("sessionId") String sessionId) {
            if (sessionId == null) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            } else {
                Optional<User> user = userSessionService.getUserBySession(sessionId);
                if (!user.isPresent()) {
                    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
                } else {
                    Message message = messageService.getStatusMessage(user.get().getUsername());
                    if (message == null) {
                        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
                    } else {
                        String json = "{\"results\":[{\"message\":\"" + message.getMessage() + "\",\"creationTimestamp\":\"" + message.getCreationTimestamp() + "\"}]}";
                        return Response.status(Response.Status.OK).entity(json).build();
                    }
                }
            }


        }

        @GET
        @Path("/updates")
        @Produces(MediaType.APPLICATION_JSON)
        public Response getUpdates(@QueryParam("sessionId") String sessionId, @QueryParam("timestamp") String timestamp) {
            DateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
            Date date;
            try {
                date = format.parse(timestamp);
            } catch (ParseException e) {
                //e.printStackTrace();
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            }
            if (sessionId == null || timestamp == null) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            } else {
                Optional<User> user = userSessionService.getUserBySession(sessionId);
                if (!user.isPresent()) {
                    return Response.status(Response.Status.OK).entity("{\"errorCode\":500}").build();
                } else {
                    List<Message> messageList = messageService.getUpdates(date, user.get().getUsername());
                    return Response.status(Response.Status.OK).entity("\"results\":" + messageList + "}").build();
                }
            }
        }
    @GET
    @Path("/latestmessages")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLatestMessages(@QueryParam("sessionId") String sessionId) {
        if (sessionId == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } else {
            Optional<User> user = userSessionService.getUserBySession(sessionId);
            if (!user.isPresent()) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            } else {
                List<Message> latestMessages = new LinkedList<Message>();
                Set<User> followers = user.get().getFollowers();
                Iterator<User> iterator = followers.iterator();
                while (iterator.hasNext() && latestMessages.size() <= 10) {
                    List<Message> userMessages = messageService.getMessages(iterator.next().getUsername());
                    if (userMessages.size() > 0) {
                        latestMessages.add(userMessages.get(userMessages.size() - 1));
                    }
                    if (userMessages.size() > 1) {
                        latestMessages.add(userMessages.get(userMessages.size() - 2));
                    }
                }
                return Response.status(Response.Status.OK).entity("{\"results\":" + latestMessages + "}").build();
            }
        }
    }
    }

