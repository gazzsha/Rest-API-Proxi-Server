package gazzsha.sprint.security.application.controller;


import gazzsha.sprint.security.application.dto.UserDto;
import gazzsha.sprint.security.application.service.UsersServerProxy;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;


@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@AllArgsConstructor
@RestController
public class UsersController {
    static final public String GET_USER = "/users/{user_id}";
    static final public String GET_USERS = "/users";
    static final public String POST_USER = "/users";

    static final public String DELETE_USER = "/users/{user_id}";

    static final public String PUT_USER = "/users/{user_id}";


    UsersServerProxy usersServerProxy;

    @GetMapping(value = GET_USER)
    public ResponseEntity<?> getUser(@PathVariable(name = "user_id") String id) {
        UserDto userById = usersServerProxy.geUserById(id);
        return ResponseEntity.ok().body(userById);
    }


    @GetMapping(value = GET_USERS)
    public ResponseEntity<?> getAllPost(@RequestParam(required = false, name = "id") final String id,
                                        @RequestParam(required = false, name = "name") final String name,
                                        @RequestParam(required = false, name = "username") final String username,
                                        @RequestParam(required = false, name = "email") final String email,
                                        @RequestParam(required = false, name = "phone") final String phone,
                                        @RequestParam(required = false, name = "website") final String website) {
        List<UserDto> users = usersServerProxy.getAllUsers(id, name, username, email, phone, website);
        return ResponseEntity.ok().body(users);
    }

    @PostMapping(value = POST_USER, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> savePost(@RequestBody UserDto user) {
        UserDto returnTypeUserDto = usersServerProxy.saveUser(user);
        return ResponseEntity.created(URI.create(POST_USER)).body(returnTypeUserDto);
    }

    @PutMapping(value = PUT_USER, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updatePost(@PathVariable(name = "user_id") String userId,
                                        @RequestBody UserDto user) {
        UserDto returnTypeUserDto = usersServerProxy.updateUser(userId, user);
        return ResponseEntity.ok().body(returnTypeUserDto);
    }


    @DeleteMapping(value = DELETE_USER)
    public ResponseEntity<?> deletePost(@PathVariable(name = "user_id") String userId) {
        usersServerProxy.deleteUser(userId);
        return ResponseEntity.ok().build();
    }
}
