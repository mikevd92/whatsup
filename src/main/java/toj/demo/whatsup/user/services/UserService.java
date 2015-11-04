package toj.demo.whatsup.user.services;

import toj.demo.whatsup.domain.Credentials;
import toj.demo.whatsup.domain.Keyword;
import toj.demo.whatsup.domain.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserService {

    Optional<User> get(String name);

    void signup(Credentials credentials);

    boolean checkUser(Credentials credentials);

    void addFollower(User toBeFollowed,User follower);

    void removeFollower(User toBeFollowed,User follower);

    public void addKeywordsToUser(User user,Set<Keyword> keywords);

}
