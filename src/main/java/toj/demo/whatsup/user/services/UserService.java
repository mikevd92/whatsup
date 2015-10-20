package toj.demo.whatsup.user.services;

import toj.demo.whatsup.user.http.resource.CredentialsDTO;
import toj.demo.whatsup.domain.User;

import java.util.Optional;

public interface UserService {
    Optional<User> get(String name);

    void signup(String name, String password);

    void signup(CredentialsDTO credentialsDTO);

    boolean checkUser(CredentialsDTO credentialsDTO);

    void removeAll();

}
