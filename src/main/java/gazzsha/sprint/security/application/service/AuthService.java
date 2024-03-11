package gazzsha.sprint.security.application.service;

import gazzsha.sprint.security.application.dto.JwtRequest;
import gazzsha.sprint.security.application.dto.JwtResponse;
import gazzsha.sprint.security.application.dto.RegistrationUserDto;
import gazzsha.sprint.security.application.dto.UserWithRolesDto;
import gazzsha.sprint.security.application.entity.Role;
import gazzsha.sprint.security.application.entity.User;
import gazzsha.sprint.security.application.exception.ExceptionWeb;
import gazzsha.sprint.security.application.utils.JWTTokenUtils;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class AuthService {

    UserService userService;

    JWTTokenUtils jwtTokenUtils;
    AuthenticationManager authenticationManager;


    public ResponseEntity<?> createAuthToken(JwtRequest authRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.username(), authRequest.password())
            );
        } catch (BadCredentialsException exception) {
            throw new AuthenticationCredentialsNotFoundException("Incorrect login or password");
        }
        UserDetails userDetails = userService.loadUserByUsername(authRequest.username());
        String token = jwtTokenUtils.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(token));
    }

    public ResponseEntity<?> createNewUser(@RequestBody RegistrationUserDto registrationUserDto) {
        if (!registrationUserDto.password().equals(registrationUserDto.confirmPassword())) {
            return new ResponseEntity<>(new ExceptionWeb(HttpStatus.BAD_REQUEST.value(), "Password is not equals"), HttpStatus.BAD_REQUEST);
        }
        if (userService.findByUsername(registrationUserDto.username()).isPresent()) {
            return new ResponseEntity<>(new ExceptionWeb(HttpStatus.BAD_REQUEST.value(), "User already exist"), HttpStatus.BAD_REQUEST);
        }
        User user = userService.createNewUser(registrationUserDto);
        return ResponseEntity.ok(new UserWithRolesDto(user.getId(), user.getUsername(), user.getRoles().stream()
                .map(Role::getRoleName)
                .toList()));
    }
}
