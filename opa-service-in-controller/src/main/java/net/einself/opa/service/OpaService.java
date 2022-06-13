package net.einself.opa.service;

import net.einself.opa.exception.OpaAssertionError;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.nio.file.Path;

@Service
public class OpaService {

    record Input(String method, String path, String user) { }
    record Request(Input input) { }
    record Result(boolean allow) { }
    record Response(Result result) { }

    private final UserService userService;
    private final WebClient webClient;

    public OpaService(UserService userService) {
        this.userService = userService;
        webClient = WebClient.builder()
                .baseUrl("http://localhost:8181/v1")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public Mono<Void> assertHello(HttpMethod method, String path, String username) {
        return userService.findByUsername(username)
                .map(user -> new Input(method.toString(), path, user.username()))
                .switchIfEmpty(Mono.error(new OpaAssertionError()))
                .flatMap(input -> this.assertPolicy("hello", input));
    }

    public Mono<Void> assertPolicy(String policyId, Input input) {
        final var uri = String.format("/data/%s", policyId);

        return webClient.post()
                .uri(uri)
                .bodyValue(new Request(input))
                .retrieve()
                .bodyToMono(Response.class)
                .map(Response::result)
                .doOnNext(OpaService::assertResult)
                .then(Mono.empty());
    }

    private static void assertResult(Result result) {
        if (!result.allow()) {
            throw new OpaAssertionError();
        }
    }

}
