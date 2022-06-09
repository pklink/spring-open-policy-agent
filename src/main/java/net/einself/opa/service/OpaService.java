package net.einself.opa.service;

import net.einself.opa.exception.OpaAssertionError;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class OpaService {

    record Input(String method, String path, String user) { }
    record Request(Input input) { }
    record Result(boolean allow) { }
    record Response(Result result) { }

    private final WebClient webClient;

    public OpaService() {
        webClient = WebClient.builder()
                .baseUrl("http://localhost:8181/v1")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public Mono<Void> assertHello(HttpMethod method, String path, String user) {
        final var input = new Input(method.toString(), path, user);
        final var body = new Request(input);

        return webClient.post()
                .uri("/data/hello")
                .bodyValue(body)
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
