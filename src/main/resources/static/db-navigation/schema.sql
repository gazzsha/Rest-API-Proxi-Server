CREATE SCHEMA IF NOT EXISTS security_schema;

CREATE TABLE security_schema.users
(
    id       BIGSERIAL,
    username varchar(30) NOT NULL UNIQUE,
    password varchar(80) NOT NULL,
    PRIMARY KEY (id)
);
CREATE TABLE security_schema.roles
(
    id   serial,
    name varchar(50) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE security_schema.users_roles
(
    user_id bigint NOT NULL,
    role_id int    NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES security_schema.users (id),
    FOREIGN KEY (role_id) REFERENCES security_schema.roles (id)
);

INSERT INTO security_schema.roles (name)
VALUES ('ROLE_USER'),
       ('ROLE_ADMIN'),
       ('ROLE_POSTS'),
       ('ROLE_ALBUMS');


INSERT INTO security_schema.users (username, password)
VALUES ('user', '$2a$04$Fx/SX9.BAvtPlMyIIqqFx.hLY2Xp8nnhpzvEEVINvVpwIPbA3v/.i'),
       ('admin', '$2a$04$Fx/SX9.BAvtPlMyIIqqFx.hLY2Xp8nnhpzvEEVINvVpwIPbA3v/.i'),
       ('posts', '$2a$04$Fx/SX9.BAvtPlMyIIqqFx.hLY2Xp8nnhpzvEEVINvVpwIPbA3v/.i'),
       ('albums', '$2a$04$Fx/SX9.BAvtPlMyIIqqFx.hLY2Xp8nnhpzvEEVINvVpwIPbA3v/.i');

INSERT INTO security_schema.users_roles (user_id, role_id)
VALUES (1, 1),
       (2, 2),
       (3, 3),
       (4, 4);
