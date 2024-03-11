package gazzsha.sprint.security.application.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Post {
    Integer userId;
    Integer id;
    String title;
    String body;
}
