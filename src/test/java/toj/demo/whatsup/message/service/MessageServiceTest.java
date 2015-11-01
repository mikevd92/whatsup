package toj.demo.whatsup.message.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import toj.demo.whatsup.domain.Message;
import toj.demo.whatsup.domain.User;
import toj.demo.whatsup.message.dao.MessageDAO;
import toj.demo.whatsup.message.services.MessageService;
import toj.demo.whatsup.message.services.PersistentMessageService;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

/**
 * Created by mihai.popovici on 10/29/2015.
 */
public class MessageServiceTest {

    @Mock
    private MessageDAO messageDAO;
    @InjectMocks
    private MessageService messageService = new PersistentMessageService(messageDAO);

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAddNewMessage() {
        User user = new User("Mihai", "password");
        Message message = new Message("wow", user);
        List<Message> messages = new ArrayList<>();
        doAnswer(invocationOnMock -> {
            messages.add(message);
            return null;
        }).when(messageDAO).save(message);
        messageService.addNewMessage(message);
        assertEquals(messages.isEmpty(), false);
    }

    @Test
    public void testGetStatusMessage() {
        List<Message> messages = new LinkedList<>();
        User user = new User("Mihai", "password");
        messages.addAll(Arrays
                        .asList(
                                new Message("wow", user, Date.from(Instant.now().minus(2, ChronoUnit.DAYS)), Date.from(Instant.now().plus(1, ChronoUnit.DAYS))),
                                new Message("cool", user, Date.from(Instant.now().minus(4, ChronoUnit.DAYS)), Date.from(Instant.now().plus(3, ChronoUnit.DAYS))),
                                new Message("sweet", user, Date.from(Instant.now().minus(5, ChronoUnit.DAYS)), Date.from(Instant.now().plus(4, ChronoUnit.DAYS))),
                                new Message("marvel", user, Date.from(Instant.now().minus(3, ChronoUnit.DAYS)), Date.from(Instant.now().plus(2, ChronoUnit.DAYS))),
                                new Message("marvelous", user, Date.from(Instant.now().minus(3, ChronoUnit.DAYS)), Date.from(Instant.now().minus(2, ChronoUnit.DAYS))),
                                new Message("cooler", user, Date.from(Instant.now().minus(4, ChronoUnit.DAYS)), Date.from(Instant.now().minus(3, ChronoUnit.DAYS)))
                        )
        );
        when(messageDAO.getMessageByUser(user)).then(invocationOnMock -> {
            messages.sort((a, b) -> a.getCreationTimestamp().after(b.getCreationTimestamp()) ? -1 : (a.getCreationTimestamp().equals(b.getCreationTimestamp()) ? 0 : 1));
            return Optional.of(messages.stream().filter(p -> !p.getDeletionTimestamp().before(new Date())).collect(Collectors.toList()).get(0));
        });
        assertEquals(messageService.getStatusMessage(user).get(), messages.get(0));
    }

    @Test
    public void testGetUpdates() {
        List<Message> messages = new LinkedList<>();
        User user = new User("Mihai", "password");
        messages.addAll(Arrays.asList(
                new Message("wow", user, Date.from(Instant.now().minus(2, ChronoUnit.DAYS)), Date.from(Instant.now().plus(1, ChronoUnit.DAYS))),
                new Message("cool", user, Date.from(Instant.now().minus(4, ChronoUnit.DAYS)), Date.from(Instant.now().plus(3, ChronoUnit.DAYS))),
                new Message("sweet", user, Date.from(Instant.now().minus(5, ChronoUnit.DAYS)), Date.from(Instant.now().plus(4, ChronoUnit.DAYS))),
                new Message("marvel", user, Date.from(Instant.now().minus(3, ChronoUnit.DAYS)), Date.from(Instant.now().plus(2, ChronoUnit.DAYS))),
                new Message("marvelous", user, Date.from(Instant.now().minus(3, ChronoUnit.DAYS)), Date.from(Instant.now().minus(2, ChronoUnit.DAYS))),
                new Message("cooler", user, Date.from(Instant.now().minus(4, ChronoUnit.DAYS)), Date.from(Instant.now().minus(3, ChronoUnit.DAYS)))
        ));
        Date date = Date.from(Instant.now().minus(3, ChronoUnit.DAYS));
        when(messageDAO.getUpdates(date, user)).then(invocationOnMock -> messages.stream().filter(p -> !p.getCreationTimestamp().before(date) && !p.getDeletionTimestamp().before(new Date())).collect(Collectors.toList()));
        assertEquals(messageService.getUpdates(date, user).size(), 2);
    }

    @Test
    public void testGetMessages() {
        List<Message> messages = new LinkedList<>();
        User user = new User("Mihai", "password");
        messages.addAll(Arrays.asList(
                new Message("wow1", user, Date.from(Instant.now().minus(2, ChronoUnit.DAYS)), Date.from(Instant.now().plus(1, ChronoUnit.DAYS))),
                new Message("cool1", user, Date.from(Instant.now().minus(4, ChronoUnit.DAYS)), Date.from(Instant.now().plus(3, ChronoUnit.DAYS))),
                new Message("sweet1", user, Date.from(Instant.now().minus(5, ChronoUnit.DAYS)), Date.from(Instant.now().plus(4, ChronoUnit.DAYS))),
                new Message("marvel1", user, Date.from(Instant.now().minus(3, ChronoUnit.DAYS)), Date.from(Instant.now().plus(2, ChronoUnit.DAYS))),
                new Message("marvelous1", user, Date.from(Instant.now().minus(3, ChronoUnit.DAYS)), Date.from(Instant.now().minus(2, ChronoUnit.DAYS))),
                new Message("cooler1", user, Date.from(Instant.now().minus(4, ChronoUnit.DAYS)), Date.from(Instant.now().minus(3, ChronoUnit.DAYS)))
        ));
        when(messageDAO.getMessagesByUser(user)).thenReturn(messages.stream().filter(p -> !p.getDeletionTimestamp().before(new Date())).collect(Collectors.toList()));
        assertEquals(messageService.getMessages(user).size(),4);
    }

    @Test
    public void testRemoveByDeletionTimestamp() {
        List<Message> messages = new LinkedList<>();
        User user = new User("Mihai", "password");
        messages.addAll(Arrays.asList(
                new Message("wow1", user, Date.from(Instant.now().minus(2, ChronoUnit.DAYS)), Date.from(Instant.now().plus(1, ChronoUnit.DAYS))),
                new Message("cool1", user, Date.from(Instant.now().minus(4, ChronoUnit.DAYS)), Date.from(Instant.now().plus(3, ChronoUnit.DAYS))),
                new Message("sweet1", user, Date.from(Instant.now().minus(5, ChronoUnit.DAYS)), Date.from(Instant.now().plus(4, ChronoUnit.DAYS))),
                new Message("marvel1", user, Date.from(Instant.now().minus(3, ChronoUnit.DAYS)), Date.from(Instant.now().plus(2, ChronoUnit.DAYS))),
                new Message("marvelous1", user, Date.from(Instant.now().minus(3, ChronoUnit.DAYS)), Date.from(Instant.now().minus(2, ChronoUnit.DAYS))),
                new Message("cooler1", user, Date.from(Instant.now().minus(4, ChronoUnit.DAYS)), Date.from(Instant.now().minus(3, ChronoUnit.DAYS)))
        ));
        doAnswer(invocationOnMock -> {
            messages.removeIf(p -> p.getDeletionTimestamp().before(new Date()));
            return null;
        }).when(messageDAO).removeByDeletionTimestamp();
        messageService.removeByDeletionTimestamp();
        when(messageDAO.getMessagesByUser(user)).thenReturn(messages.stream().filter(p -> !p.getDeletionTimestamp().before(new Date())).collect(Collectors.toList()));
        assertEquals(messageService.getMessages(user).size(),4);
    }

    @Test
    public void testGetLatestMessages() {
        List<Message> messages = new LinkedList<>();
        User user = new User("Mihai", "password");
        User user1 = new User("Adi", "password");
        messages.addAll(Arrays.asList(
                new Message("wow1", user1, Date.from(Instant.now().minus(2, ChronoUnit.DAYS)), Date.from(Instant.now().plus(1, ChronoUnit.DAYS))),
                new Message("cool1", user1, Date.from(Instant.now().minus(4, ChronoUnit.DAYS)), Date.from(Instant.now().plus(3, ChronoUnit.DAYS))),
                new Message("sweet1", user1, Date.from(Instant.now().minus(5, ChronoUnit.DAYS)), Date.from(Instant.now().plus(4, ChronoUnit.DAYS))),
                new Message("marvel1", user1, Date.from(Instant.now().minus(3, ChronoUnit.DAYS)), Date.from(Instant.now().plus(2, ChronoUnit.DAYS))),
                new Message("marvelous1", user1, Date.from(Instant.now().minus(3, ChronoUnit.DAYS)), Date.from(Instant.now().minus(2, ChronoUnit.DAYS))),
                new Message("cooler1", user1, Date.from(Instant.now().minus(4, ChronoUnit.DAYS)), Date.from(Instant.now().minus(3, ChronoUnit.DAYS)))
        ));
        User user2 = new User("Julia", "password");
        messages.addAll(Arrays.asList(
                new Message("wow2", user2, Date.from(Instant.now().minus(2, ChronoUnit.DAYS)), Date.from(Instant.now().plus(1, ChronoUnit.DAYS))),
                new Message("cool2", user2, Date.from(Instant.now().minus(4, ChronoUnit.DAYS)), Date.from(Instant.now().plus(3, ChronoUnit.DAYS))),
                new Message("sweet2", user2, Date.from(Instant.now().minus(5, ChronoUnit.DAYS)), Date.from(Instant.now().plus(4, ChronoUnit.DAYS))),
                new Message("marvel2", user2, Date.from(Instant.now().minus(3, ChronoUnit.DAYS)), Date.from(Instant.now().plus(2, ChronoUnit.DAYS))),
                new Message("marvelous2", user2, Date.from(Instant.now().minus(3, ChronoUnit.DAYS)), Date.from(Instant.now().minus(2, ChronoUnit.DAYS))),
                new Message("cooler2", user2, Date.from(Instant.now().minus(4, ChronoUnit.DAYS)), Date.from(Instant.now().minus(3, ChronoUnit.DAYS)))
        ));
        user.addFollower(user1);
        user.addFollower(user2);

        when(messageDAO.getMessagesByUsers(user.getFollowers())).then(invocationOnMock -> {
            messages.sort((a, b) -> a.getCreationTimestamp().after(b.getCreationTimestamp()) ? -1 : (a.getCreationTimestamp().equals(b.getCreationTimestamp()) ? 0 : 1));
            return user.getFollowers()
                    .stream()
                    .flatMap(p -> messages.stream()
                            .filter(m -> m.getUser().equals(p) && !m.getDeletionTimestamp().before(new Date()))
                            .limit(2))
                            .limit(10)
                    .collect(Collectors.toList());
        });
        assertEquals(messageService.getLatestMessages(user.getFollowers()).size(), 4);

    }
}
