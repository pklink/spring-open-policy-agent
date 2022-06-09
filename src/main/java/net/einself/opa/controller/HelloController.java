package net.einself.opa.controller;

import net.einself.opa.service.OpaService;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/hello")
public class HelloController {

    private final OpaService opaService;

    public HelloController(OpaService opaService) {
        this.opaService = opaService;
    }

    @GetMapping
    public Mono<String> hello(ServerHttpRequest request) {
        final var method = request.getMethod();
        final var path = request.getPath().value();
        final var username = request.getHeaders().getFirst("X-Username");

        return opaService.assertHello(method, path, username)
                .thenReturn("Hi!");
    }

}
