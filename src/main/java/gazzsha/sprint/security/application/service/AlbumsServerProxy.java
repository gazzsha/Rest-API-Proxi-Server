package gazzsha.sprint.security.application.service;

import gazzsha.sprint.security.application.dto.AlbumDto;
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

import static gazzsha.sprint.security.application.controller.AlbumController.*;


@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class AlbumsServerProxy {

    WebClient webClient;

    public List<AlbumDto> getAllAlbums(final String userId, final String id,
                                       final String title) {
        StringBuilder query = new StringBuilder(GET_ALBUMS)
                .append("?")
                .append(Objects.isNull(userId) ? "" : "userId=" + userId + "&")
                .append(Objects.isNull(id) ? "" : "id=" + id + "&")
                .append(Objects.isNull(title) ? "" : "title=" + title + "&");
        query.replace(query.length() - 1, query.length(), "");
        return Arrays.stream(Objects.requireNonNull(webClient
                        .get()
                        .uri(query.toString())
                        .retrieve()
                        .onStatus(httpStatus -> !httpStatus.is2xxSuccessful(),
                                ClientResponse::createException)
                        .bodyToMono(AlbumDto[].class)
                        .retryWhen(Retry.fixedDelay(3, Duration.ofMillis(100)))
                        .block()))
                .toList();
    }


    public AlbumDto getAlbumById(final String id) {
        return webClient
                .get()
                .uri(GET_ALBUM, id)
                .retrieve()
                .onStatus(httpStatus -> !httpStatus.is2xxSuccessful(),
                        ClientResponse::createException)
                .bodyToMono(AlbumDto.class)
                .retryWhen(Retry.fixedDelay(3, Duration.ofMillis(100)))
                .block();
    }

    public AlbumDto saveAlbum(final AlbumDto albumDto) {
        return webClient
                .post()
                .uri(POST_ALBUM)
                .body(Mono.just(albumDto), AlbumDto.class)
                .retrieve()
                .onStatus(httpStatus -> !httpStatus.is2xxSuccessful(),
                        ClientResponse::createException)
                .bodyToMono(AlbumDto.class)
                .retryWhen(Retry.fixedDelay(3, Duration.ofMillis(100)))
                .block();
    }

    public AlbumDto updateAlbum(final String albumId, final AlbumDto albumDto) {
        return webClient
                .put()
                .uri(PUT_ALBUM, albumId)
                .body(Mono.just(albumDto), AlbumDto.class)
                .retrieve()
                .onStatus(httpStatus -> !httpStatus.is2xxSuccessful(),
                        ClientResponse::createException)
                .bodyToMono(AlbumDto.class)
                .retryWhen(Retry.fixedDelay(3, Duration.ofMillis(100)))
                .block();
    }

    public Mono<Void> deleteAlbum(final String albumId) {
        return webClient
                .delete()
                .uri(DELETE_ALBUM, albumId)
                .retrieve()
                .onStatus(httpStatus -> !httpStatus.is2xxSuccessful(),
                        ClientResponse::createException)
                .bodyToMono(Void.class)
                .retryWhen(Retry.fixedDelay(3, Duration.ofMillis(100)));
    }
}
