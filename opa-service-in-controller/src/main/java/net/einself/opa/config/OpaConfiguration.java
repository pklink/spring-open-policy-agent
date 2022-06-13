package net.einself.opa.config;

import net.einself.opa.service.OpaService;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.ResourceUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;

@Configuration
public class OpaConfiguration implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        final var webClient = WebClient.builder()
                .baseUrl("http://localhost:8181/v1")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        try {
            final var resource = new ClassPathResource("policies/hello.rego");
            final var policy = Files.readString(resource.getFile().toPath());
            webClient.put()
                    .uri("/policies/hello")
                    .contentType(MediaType.TEXT_PLAIN)
                    .bodyValue(policy)
                    .retrieve()
                    .bodyToMono(String.class).subscribe();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }



    }
}
