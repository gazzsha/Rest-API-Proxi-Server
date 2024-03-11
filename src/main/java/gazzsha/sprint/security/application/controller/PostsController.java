package gazzsha.sprint.security.application.controller;


import gazzsha.sprint.security.application.dto.CommentsDto;
import gazzsha.sprint.security.application.dto.PostDto;
import gazzsha.sprint.security.application.service.PostsServerProxy;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class PostsController {

    static final public String POSTS_GET_DEFINED = "/posts/{post_id}";
    static final public String GET_POSTS = "/posts";

    static final public String GET_COMMENTS = "/comments";

    static final public String GET_COMMENTS_FROM_POST = "/posts/{post_id}/comments";

    static final public String POST_POSTS = "/posts";

    static final public String DELETE_POSTS = "/posts/{post_id}";

    static final public String PUT_POSTS = "/posts/{post_id}";


    PostsServerProxy postsServerProxy;

    @GetMapping(value = POSTS_GET_DEFINED)
    public ResponseEntity<?> getPost(@PathVariable(name = "post_id") String id) {
        PostDto postById = postsServerProxy.getPostById(id);
        return ResponseEntity.ok().body(postById);
    }


    @GetMapping(value = GET_POSTS)
    public ResponseEntity<?> getAllPost(@RequestParam(required = false, name = "userId") final String userId,
                                        @RequestParam(required = false, name = "id") final String id,
                                        @RequestParam(required = false, name = "title") final String title,
                                        @RequestParam(required = false, name = "body") final String body) {
        List<PostDto> posts = postsServerProxy.getAllPosts(userId, id, title, body);
        return ResponseEntity.ok().body(posts);
    }

    @GetMapping(value = GET_COMMENTS)
    public ResponseEntity<?> getAllComments(@RequestParam(required = false, name = "postId") String postId) {
        List<CommentsDto> comments = postsServerProxy.getAllComments(postId);
        return ResponseEntity.ok().body(comments);
    }


    @GetMapping(value = GET_COMMENTS_FROM_POST)
    public ResponseEntity<?> getCommentsFromPost(@PathVariable(name = "post_id") String id) {
        List<CommentsDto> comments = postsServerProxy.getCommentsFromPost(id);
        return ResponseEntity.ok().body(comments);
    }

    @PostMapping(value = POST_POSTS, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> savePost(@RequestBody PostDto post) {
        PostDto returnTypePostDto = postsServerProxy.savePost(post);
        return ResponseEntity.created(URI.create(POST_POSTS)).body(returnTypePostDto);
    }

    @PutMapping(value = PUT_POSTS, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updatePost(@PathVariable(name = "post_id") String postId,
                                        @RequestBody PostDto post) {
        PostDto returnTypePostDto = postsServerProxy.updatePost(postId, post);
        return ResponseEntity.ok().body(returnTypePostDto);
    }


    @DeleteMapping(value = DELETE_POSTS)
    public ResponseEntity<?> deletePost(@PathVariable(name = "post_id") String postId) {
        postsServerProxy.deletePost(postId);
        return ResponseEntity.ok().build();
    }
}
