package net.einself.opa.service;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Predicate;

@Service
public class UserService {

    public record User(String username) { }

    public final static List<User> USERS = List.of(
            new User("gordon"),
            new User("kate")
    );

    public Mono<User> findByUsername(String username) {
        return USERS.stream()
                .filter(isUserPresentByUsername(username))
                .map(Mono::just)
                .findFirst()
                .orElseGet(Mono::empty);
    }

    private Predicate<User> isUserPresentByUsername(String username) {
        return user -> username.equalsIgnoreCase(user.username());
    }

}
