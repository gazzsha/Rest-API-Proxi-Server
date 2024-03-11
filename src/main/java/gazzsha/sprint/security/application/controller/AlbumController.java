package gazzsha.sprint.security.application.controller;


import gazzsha.sprint.security.application.dto.AlbumDto;
import gazzsha.sprint.security.application.dto.UserDto;
import gazzsha.sprint.security.application.service.AlbumsServerProxy;
import gazzsha.sprint.security.application.service.UsersServerProxy;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@AllArgsConstructor
@RestController
public class AlbumController {
    static final public String GET_ALBUM = "/albums/{album_id}";
    static final public String GET_ALBUMS = "/albums";
    static final public String POST_ALBUM = "/albums";

    static final public String DELETE_ALBUM = "/albums/{album_id}";

    static final public String PUT_ALBUM = "/albums/{album_id}";


    AlbumsServerProxy albumsServerProxy;

    @GetMapping(value = GET_ALBUM)
    public ResponseEntity<?> getAlbum(@PathVariable(name = "album_id") String id) {
        AlbumDto albumById = albumsServerProxy.getAlbumById(id);
        return ResponseEntity.ok().body(albumById);
    }


    @GetMapping(value = GET_ALBUMS)
    public ResponseEntity<?> getAllAlbums(@RequestParam(required = false, name = "userId") final String userId,
                                        @RequestParam(required = false, name = "id") final String id,
                                        @RequestParam(required = false, name = "title") final String title) {
        List<AlbumDto> albums = albumsServerProxy.getAllAlbums(userId, id, title);
        return ResponseEntity.ok().body(albums);
    }

    @PostMapping(value = POST_ALBUM, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveAlbum(@RequestBody AlbumDto album) {
        AlbumDto returnTypeAlbumDto = albumsServerProxy.saveAlbum(album);
        return ResponseEntity.created(URI.create(POST_ALBUM)).body(returnTypeAlbumDto);
    }

    @PutMapping(value = PUT_ALBUM, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updatePost(@PathVariable(name = "album_id") String albumId,
                                        @RequestBody AlbumDto albumDto) {
        AlbumDto returnTypeAlbumDto = albumsServerProxy.updateAlbum(albumId, albumDto);
        return ResponseEntity.ok().body(returnTypeAlbumDto);
    }


    @DeleteMapping(value = DELETE_ALBUM)
    public ResponseEntity<?> deletePost(@PathVariable(name = "album_id") String albumId) {
        albumsServerProxy.deleteAlbum(albumId);
        return ResponseEntity.ok().build();
    }
}
