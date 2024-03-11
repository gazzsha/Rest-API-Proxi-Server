package gazzsha.sprint.security.application.service;

import gazzsha.sprint.security.application.dto.CommentsDto;
import gazzsha.sprint.security.application.dto.PostDto;
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

import static gazzsha.sprint.security.application.controller.PostsController.*;


@Service
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class PostsServerProxy {

    WebClient webClient;

    public PostDto getPostById(final String id) {
        return webClient
                .get()
                .uri(POSTS_GET_DEFINED, id)
                .retrieve()
                .onStatus(httpStatus -> !httpStatus.is2xxSuccessful(),
                        ClientResponse::createException)
                .bodyToMono(PostDto.class)
                .retryWhen(Retry.fixedDelay(3, Duration.ofMillis(100)))
                .block();
    }

    public List<PostDto> getAllPosts(final String userId, final String id,
                                     final String title, final String body) {
        StringBuilder query = new StringBuilder(GET_POSTS)
                .append("?")
                .append(Objects.isNull(userId) ? "" : "userId=" + userId + "&")
                .append(Objects.isNull(id) ? "" : "id=" + id + "&")
                .append(Objects.isNull(title) ? "" : "title=" + title + "&")
                .append(Objects.isNull(body) ? "" : "body=" + body + "&");
        query.replace(query.length() - 1, query.length(), "");
        return Arrays.stream(Objects.requireNonNull(webClient
                        .get()
                        .uri(query.toString())
                        .retrieve()
                        .onStatus(httpStatus -> !httpStatus.is2xxSuccessful(),
                                ClientResponse::createException)
                        .bodyToMono(PostDto[].class)
                        .retryWhen(Retry.fixedDelay(3, Duration.ofMillis(100)))
                        .block()))
                .toList();
    }

    public List<CommentsDto> getAllComments(final String postId) {
        return Arrays.stream(Objects.requireNonNull(webClient
                        .get()
                        .uri(new StringBuilder(GET_COMMENTS).append(Objects.isNull(postId) ? "" : "?postId=" + postId).toString())
                        .retrieve()
                        .onStatus(httpStatus -> !httpStatus.is2xxSuccessful(),
                                ClientResponse::createException)
                        .bodyToMono(CommentsDto[].class)
                        .retryWhen(Retry.fixedDelay(3, Duration.ofMillis(100)))
                        .block()))
                .toList();
    }

    public List<CommentsDto> getCommentsFromPost(final String id) {
        return Arrays.stream(Objects.requireNonNull(webClient
                        .get()
                        .uri(GET_COMMENTS_FROM_POST, id)
                        .retrieve()
                        .onStatus(httpStatus -> !httpStatus.is2xxSuccessful(),
                                ClientResponse::createException)
                        .bodyToMono(CommentsDto[].class)
                        .retryWhen(Retry.fixedDelay(3, Duration.ofMillis(100)))
                        .block()))
                .toList();
    }

    public PostDto savePost(final PostDto postDto) {
        return webClient
                .post()
                .uri(POST_POSTS)
                .body(Mono.just(postDto), PostDto.class)
                .retrieve()
                .onStatus(httpStatus -> !httpStatus.is2xxSuccessful(),
                        ClientResponse::createException)
                .bodyToMono(PostDto.class)
                .retryWhen(Retry.fixedDelay(3, Duration.ofMillis(100)))
                .block();
    }

    public PostDto updatePost(final String postId, final PostDto postDto) {
        return webClient
                .put()
                .uri(PUT_POSTS, postId)
                .body(Mono.just(postDto), PostDto.class)
                .retrieve()
                .onStatus(httpStatus -> !httpStatus.is2xxSuccessful(),
                        ClientResponse::createException)
                .bodyToMono(PostDto.class)
                .retryWhen(Retry.fixedDelay(3, Duration.ofMillis(100)))
                .block();
    }

    public Mono<Void> deletePost(final String postId) {
        return webClient
                .delete()
                .uri(DELETE_POSTS, postId)
                .retrieve()
                .onStatus(httpStatus -> !httpStatus.is2xxSuccessful(),
                        ClientResponse::createException)
                .bodyToMono(Void.class)
                .retryWhen(Retry.fixedDelay(3, Duration.ofMillis(100)));
    }

}
