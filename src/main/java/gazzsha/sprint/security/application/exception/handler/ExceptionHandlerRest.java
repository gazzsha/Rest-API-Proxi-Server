package gazzsha.sprint.security.application.exception.handler;


import gazzsha.sprint.security.application.exception.ExceptionWeb;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ValidationException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.reactive.function.client.WebClientException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;
import java.util.Objects;

@ControllerAdvice
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ExceptionHandlerRest {


    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleNotFoundException(MethodArgumentNotValidException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
                exception.getMessage());
        problemDetail.setTitle("Failed");
        problemDetail.setType(exception.getBody().getType());
        return new ResponseEntity<>(new ErrorResponseException(HttpStatus.BAD_REQUEST, problemDetail, null).getBody(), HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseBody
    public ResponseEntity<?> handleAuthenticationException(AuthenticationException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED,
                ex.getMessage());
        problemDetail.setTitle("Failed authentication");
        return new ResponseEntity<>(new ErrorResponseException(HttpStatus.UNAUTHORIZED, problemDetail, null).getBody(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(JwtException.class)
    @ResponseBody
    public ResponseEntity<?> handleJwtException(JwtException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN,
                exception.getMessage());
        problemDetail.setTitle("Failed authentication");
        return new ResponseEntity<>(new ErrorResponseException(HttpStatus.FORBIDDEN, problemDetail, null).getBody(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<?> handleWebClientException(WebClientResponseException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(exception.getStatusCode(),
                exception.getStatusText());
        problemDetail.setTitle("Failed");
        //problemDetail.setType(Objects.requireNonNull(exception.getHeaders().getLocation()));
        return new ResponseEntity<>(new ErrorResponseException(exception.getStatusCode(), problemDetail, null).getBody(),exception.getStatusCode());
    }


}
