package gazzsha.sprint.security.application.controller;


import gazzsha.sprint.security.application.dto.JwtRequest;
import gazzsha.sprint.security.application.dto.RegistrationUserDto;
import gazzsha.sprint.security.application.service.AuthService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class AuthController {
    AuthService authService;

    @PostMapping("/auth")
    public ResponseEntity<?> createAuthToken(@RequestBody @Valid JwtRequest authRequest) {
       return authService.createAuthToken(authRequest);
    }

    @PostMapping("/registration")
    public ResponseEntity<?> createNewUser(@RequestBody @Valid RegistrationUserDto registrationUserDto) {
           return authService.createNewUser(registrationUserDto);
    }

}
