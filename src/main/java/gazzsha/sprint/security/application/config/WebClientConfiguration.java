package gazzsha.sprint.security.application.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfiguration {
    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";

    @Bean
    public WebClient webClientWithTimeout() {

        return WebClient.builder()
                .baseUrl(BASE_URL)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}