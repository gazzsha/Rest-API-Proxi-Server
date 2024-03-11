package gazzsha.sprint.security.application.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatusCode;

import java.util.Date;

@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@Data
public class ExceptionWeb {
    Integer status;
    String message;
    Date timestamp;

    public ExceptionWeb(Integer status, String message) {
        this.status = status;
        this.message = message;
        this.timestamp = new Date();
    }
}
