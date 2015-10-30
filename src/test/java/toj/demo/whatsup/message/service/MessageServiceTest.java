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
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocationOnMock) throws Throwable {
                messages.add(message);
                return null;
            }
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
                                new Message("wow", user, Date.from(Instant.now().minus(2, ChronoUnit.DAYS)), Date.from(Instant.now().minus(1, ChronoUnit.DAYS))),
                                new Message("cool", user, Date.from(Instant.now().minus(4, ChronoUnit.DAYS)), Date.from(Instant.now().minus(3, ChronoUnit.DAYS))),
                                new Message("sweet", user, Date.from(Instant.now().minus(5, ChronoUnit.DAYS)), Date.from(Instant.now().minus(4, ChronoUnit.DAYS))),
                                new Message("marvel", user, Date.from(Instant.now().minus(3, ChronoUnit.DAYS)), Date.from(Instant.now().minus(2, ChronoUnit.DAYS)))
                        )
        );
        when(messageDAO.getMessageByUser(user)).then(new Answer<Optional<Message>>() {
            @Override
            public Optional<Message> answer(InvocationOnMock invocationOnMock) throws Throwable {
                messages.sort((a, b) -> a.getCreationTimestamp().after(b.getCreationTimestamp()) ? -1 : (a.getCreationTimestamp().equals(b.getCreationTimestamp()) ? 0 : 1));
                return Optional.of(messages.get(0));
            }
        });
        assertEquals(messageService.getStatusMessage(user).get(), messages.get(0));
    }

    @Test
    public void testRemoveAll() {
        List<Message> messages = new LinkedList<>();
        User user = new User("Mihai", "password");
        messages.addAll(Arrays.asList(
                new Message("wow", user),
                new Message("cool", user),
                new Message("sweet", user),
                new Message("marvel", user)
        ));
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocationOnMock) throws Throwable {
                messages.clear();
                return null;
            }
        }).when(messageDAO).removeAll();
        messageService.removeAll();
        assertEquals(messages.isEmpty(), true);
    }

    @Test
    public void testGetUpdates() {
        List<Message> messages = new LinkedList<>();
        User user = new User("Mihai", "password");
        messages.addAll(Arrays.asList(
                new Message("wow", user, Date.from(Instant.now().minus(2, ChronoUnit.DAYS)),Date.from(Instant.now().minus(1,ChronoUnit.DAYS))),
                new Message("cool", user, Date.from(Instant.now().minus(4, ChronoUnit.DAYS)),Date.from(Instant.now().minus(3, ChronoUnit.DAYS))),
                new Message("sweet", user, Date.from(Instant.now().minus(5, ChronoUnit.DAYS)),Date.from(Instant.now().minus(4, ChronoUnit.DAYS))),
                new Message("marvel", user, Date.from(Instant.now().minus(3, ChronoUnit.DAYS)),Date.from(Instant.now().minus(2, ChronoUnit.DAYS)))
        ));
        Date date = Date.from(Instant.now().minus(3, ChronoUnit.DAYS));
        when(messageDAO.getUpdates(date, user)).then(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                return messages.stream().filter(p -> !p.getCreationTimestamp().before(date)).collect(Collectors.toList());
            }
        });
        assertEquals(messageService.getUpdates(date, user).size(), 2);
    }

    @Test
    public void testGetMessages() {
        List<Message> messages = new LinkedList<>();
        User user = new User("Mihai", "password");
        messages.addAll(Arrays.asList(
                new Message("wow", user),
                new Message("cool", user),
                new Message("sweet", user),
                new Message("marvel", user)
        ));
        when(messageDAO.getMessagesByUser(user)).thenReturn(messages);
        assertEquals(messageService.getMessages(user), messages);
    }

    @Test
    public void testGetLatestMessages() {
        List<Message> messages = new LinkedList<>();
        User user = new User("Mihai", "password");
        User user1 = new User("Adi", "password");
        messages.addAll(Arrays.asList(
                new Message("wow", user, Date.from(Instant.now().minus(2, ChronoUnit.DAYS)),Date.from(Instant.now().minus(1,ChronoUnit.DAYS))),
                new Message("cool", user, Date.from(Instant.now().minus(4, ChronoUnit.DAYS)),Date.from(Instant.now().minus(3, ChronoUnit.DAYS))),
                new Message("sweet", user, Date.from(Instant.now().minus(5, ChronoUnit.DAYS)),Date.from(Instant.now().minus(4, ChronoUnit.DAYS))),
                new Message("marvel", user, Date.from(Instant.now().minus(3, ChronoUnit.DAYS)),Date.from(Instant.now().minus(2, ChronoUnit.DAYS)))
        ));
        User user2 = new User("Julia", "password");
        messages.addAll(Arrays.asList(
                new Message("wow", user, Date.from(Instant.now().minus(2, ChronoUnit.DAYS)),Date.from(Instant.now().minus(1,ChronoUnit.DAYS))),
                new Message("cool", user, Date.from(Instant.now().minus(4, ChronoUnit.DAYS)),Date.from(Instant.now().minus(3, ChronoUnit.DAYS))),
                new Message("sweet", user, Date.from(Instant.now().minus(5, ChronoUnit.DAYS)),Date.from(Instant.now().minus(4, ChronoUnit.DAYS))),
                new Message("marvel", user, Date.from(Instant.now().minus(3, ChronoUnit.DAYS)),Date.from(Instant.now().minus(2, ChronoUnit.DAYS)))
        ));
        user.addFollower(user1);
        user.addFollower(user2);

        when(messageDAO.getMessagesByUsers(user.getFollowers())).then(new Answer<List<Message>>() {
            @Override
            public List<Message> answer(InvocationOnMock invocationOnMock) throws Throwable {
                messages.sort((a, b) -> a.getCreationTimestamp().after(b.getCreationTimestamp()) ? -1 : (a.getCreationTimestamp().equals(b.getCreationTimestamp()) ? 0 : 1));
                return user.getFollowers()
                        .stream()
                        .flatMap(p -> messages.stream()
                                .filter(m -> m.getUser().equals(p))
                                .limit(2))
                        .collect(Collectors.toList());
            }
        });
        assertEquals(
                messageService.getLatestMessages(user.getFollowers()),
                user.getFollowers()
                        .stream()
                        .flatMap(p -> messages.stream()
                                .filter(m -> m.getUser().equals(p))
                                .limit(2))
                        .collect(Collectors.toList())
        );

    }
}
