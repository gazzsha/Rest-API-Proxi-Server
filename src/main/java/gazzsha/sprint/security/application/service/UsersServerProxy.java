package gazzsha.sprint.security.application.service;


import gazzsha.sprint.security.application.dto.UserDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static gazzsha.sprint.security.application.controller.UsersController.*;

@Service
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UsersServerProxy {


    WebClient webClient;

    public List<UserDto> getAllUsers(final String id, final String name,
                                     final String username, final String email,
                                     final String phone, final String website) {
        StringBuilder query = new StringBuilder(GET_USERS)
                .append("?")
                .append(Objects.isNull(id) ? "" : "id=" + id + "&")
                .append(Objects.isNull(name) ? "" : "name=" + name + "&")
                .append(Objects.isNull(username) ? "" : "username=" + username + "&")
                .append(Objects.isNull(email) ? "" : "email=" + email + "&")
                .append(Objects.isNull(phone) ? "" : "phone=" + phone + "&")
                .append(Objects.isNull(website) ? "" : "website=" + website + "&");
        query.replace(query.length() - 1, query.length(), "");
        return Arrays.stream(Objects.requireNonNull(webClient
                        .get()
                        .uri(query.toString())
                        .retrieve()
                        .onStatus(httpStatus -> !httpStatus.is2xxSuccessful(),
                                ClientResponse::createException)
                        .bodyToMono(UserDto[].class)
                        .retryWhen(Retry.fixedDelay(3, Duration.ofMillis(100)))
                        .block()))
                .toList();
    }


    public UserDto geUserById(final String id) {
        return webClient
                .get()
                .uri(GET_USER, id)
                .retrieve()
                .onStatus(httpStatus -> !httpStatus.is2xxSuccessful(),
                        ClientResponse::createException)
                .bodyToMono(UserDto.class)
                .retryWhen(Retry.fixedDelay(3, Duration.ofMillis(100)))
                .block();
    }

    public UserDto saveUser(final UserDto userDto) {
        return webClient
                .post()
                .uri(POST_USER)
                .body(Mono.just(userDto), UserDto.class)
                .retrieve()
                .onStatus(httpStatus -> !httpStatus.is2xxSuccessful(),
                        ClientResponse::createException)
                .bodyToMono(UserDto.class)
                .retryWhen(Retry.fixedDelay(3, Duration.ofMillis(100)))
                .block();
    }

    public UserDto updateUser(final String userId, final UserDto userDto) {
        return webClient
                .put()
                .uri(PUT_USER, userId)
                .body(Mono.just(userDto), UserDto.class)
                .retrieve()
                .onStatus(httpStatus -> !httpStatus.is2xxSuccessful(),
                        ClientResponse::createException)
                .bodyToMono(UserDto.class)
                .retryWhen(Retry.fixedDelay(3, Duration.ofMillis(100)))
                .block();
    }

    public Mono<Void> deleteUser(final String userId) {
        return webClient
                .delete()
                .uri(DELETE_USER, userId)
                .retrieve()
                .onStatus(httpStatus -> !httpStatus.is2xxSuccessful(),
                        ClientResponse::createException)
                .bodyToMono(Void.class)
                .retryWhen(Retry.fixedDelay(3, Duration.ofMillis(100)));
    }
}
