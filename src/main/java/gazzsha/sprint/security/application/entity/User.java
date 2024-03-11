package gazzsha.sprint.security.application.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Collection;

@Entity
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "users",schema = "security_schema")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;

    @Column(name = "username")
    String username;

    @Column(name = "password")
    String password;


    @ManyToMany
    @JoinTable(
            name = "users_roles",
            schema = "security_schema",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    Collection<Role> roles;
}
