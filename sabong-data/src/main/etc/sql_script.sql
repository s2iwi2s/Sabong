-- PostgreSQL: jdbc:postgresql://ubuntu.vm:5432/sabong_dev

drop table sabong.authority cascade;
drop table sabong.users cascade;



CREATE TABLE sabong.authority
(
    id          bigserial PRIMARY KEY,
    name        VARCHAR(50) NOT NULL
        CONSTRAINT uk_authority_name UNIQUE,
    description VARCHAR(250)
);

ALTER TABLE sabong.authority
    OWNER TO sabong_dev;

CREATE TABLE sabong.users
(
    id                 bigserial PRIMARY KEY,
    LOGIN              VARCHAR(50) NOT NULL
        CONSTRAINT uk_users_login UNIQUE,
    created_by         VARCHAR(50) NOT NULL,
    created_date       TIMESTAMP(6) WITH TIME zone,
    last_modified_by   VARCHAR(50),
    last_modified_date TIMESTAMP(6) WITH TIME zone,
    activated          boolean     NOT NULL,
    email              VARCHAR(254)
        CONSTRAINT uk_users_email UNIQUE,
    first_name         VARCHAR(50),
    last_name          VARCHAR(50),
    password_hash      VARCHAR(60) NOT NULL
);

ALTER TABLE sabong.users
    OWNER TO sabong_dev;

CREATE TABLE sabong.users_authority
(
    user_id      BIGINT NOT NULL
        CONSTRAINT fk_users_authority_user_id REFERENCES sabong.users,
    authority_id BIGINT NOT NULL
        CONSTRAINT fk_users_authority_authority_id REFERENCES sabong.authority,
    PRIMARY KEY (
                 user_id,
                 authority_id
        )
);

ALTER TABLE sabong.users_authority
    OWNER TO sabong_dev;

INSERT INTO sabong.authority (name, description)
VALUES ('ROLE_ADMIN', 'Administrator Role');

INSERT INTO sabong.authority (name, description)
VALUES ('ROLE_USER', 'User Role');

INSERT INTO sabong.users (LOGIN, password_hash, first_name, last_name, email, created_by, created_date,
                          last_modified_by, last_modified_date, activated)
VALUES ('wpidor', '$2a$10$gSAhZrxMllrbgj/kkK9UceBPpChGWJA7SYIb1Mqo.n5aNLq1/oRrC', 'Admin', 'Administrator',
        's2iwi2s@yahoo.com', 'wpidor', '2023-08-04 15:02:29.377000 +00:00', 'wpidor',
        '2023-08-04 15:02:36.433000 +00:00', true);

INSERT INTO sabong.users_authority VALUES (1, 1);