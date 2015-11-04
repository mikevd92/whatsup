package toj.demo.whatsup.user.services;


import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import toj.demo.whatsup.domain.Credentials;
import toj.demo.whatsup.domain.User;
import toj.demo.whatsup.user.dao.UserDAO;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

/**
 * Created by mihai.popovici on 10/29/2015.
 */

public class UserServiceTest {

    @Mock
    private UserDAO userDAO;

    @InjectMocks
    private UserService userService=new PersistentUserService(userDAO);

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGet(){
        Optional<User> userOptional=Optional.of(new User("Mihai","password","misuvd92@yahoo.com"));
        when(userDAO.findUserByName("Mihai")).thenReturn(userOptional);
        assertEquals(userService.get("Mihai"),userOptional);
    }

    @Test
    public void testSignupWithCredentials(){
        List<User> userList=new ArrayList<>();
        User user=new User("Mihai","password","misuvd92@yahoo.com");
        when(userDAO.save(user)).then(invocationOnMock -> {
            userList.add(user);
            return user;
        });
        Credentials credentials=new Credentials("Mihai","password","misuvd92@yahoo.com");
        userService.signup(credentials);
        assertEquals(userList.isEmpty(), false);
    }

    @Test
    public void testCheckUser(){
        when(userDAO.checkUser("Mihai","password")).thenReturn(true);
        Credentials credentials=new Credentials("Mihai","password","misuvd92@yahoo.com");
        assertEquals(userService.checkUser(credentials),true);
    }

    @Test
    public void testAddFollower(){
        User toBeFollowed=new User("Mihai","password","misuvd92@yahoo.com");
        User follower=new User("Adi","password","adi@yahoo.com");
        doAnswer(invocationOnMock -> {
            toBeFollowed.addFollower(follower);
            return null;
        }).when(userDAO).addFollower(toBeFollowed,follower);
        userService.addFollower(toBeFollowed,follower);
        assertEquals(toBeFollowed.getFollowers().isEmpty(),false);
    }
    @Test
    public void testRemoveFollower(){
        User toBeFollowed=new User("Mihai","password","misuvd92@yahoo.com");
        User follower=new User("Adi","password","adi@yahoo.com");
        toBeFollowed.addFollower(follower);
        doAnswer(invocationOnMock -> {
            toBeFollowed.removeFollower(follower);
            return null;
        }).when(userDAO).removeFollower(toBeFollowed,follower);
        userService.removeFollower(toBeFollowed,follower);
        assertEquals(toBeFollowed.getFollowers().isEmpty(),true);
    }
}
