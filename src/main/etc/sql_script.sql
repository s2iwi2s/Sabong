# PostgreSQL: jdbc:postgresql://ubuntu.vm:5432/sabong_dev

drop table if exists sabong.users_authority;
drop table if exists sabong.authority;
drop table if exists sabong.users;

CREATE TABLE sabong.authority
(
    name        VARCHAR(50) NOT NULL PRIMARY KEY,
    description VARCHAR(250)
);

ALTER TABLE sabong.authority OWNER TO sabong_dev;

CREATE TABLE sabong.users
(
    LOGIN              VARCHAR(50) NOT NULL PRIMARY KEY,
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

ALTER TABLE sabong.users OWNER TO sabong_dev;

CREATE TABLE sabong.users_authority
(
    LOGIN          VARCHAR(50) NOT NULL
        CONSTRAINT fk_users_authority_login REFERENCES sabong.users,
    authority_name VARCHAR(50) NOT NULL
        CONSTRAINT fk_users_authority_authority_name REFERENCES sabong.authority,
    PRIMARY KEY (
                 LOGIN,
                 authority_name
        )
);

ALTER TABLE sabong.users_authority OWNER TO sabong_dev;

INSERT INTO sabong.authority (name, description)
VALUES ('ROLE_ADMIN', 'Administrator Role');

INSERT INTO sabong.authority (name, description)
VALUES ('ROLE_USER', 'User Role');

INSERT INTO sabong.users (LOGIN, password_hash, first_name, last_name, email, created_by, created_date,
                          last_modified_by, last_modified_date, activated)
VALUES ('wpidor', '$2a$10$gSAhZrxMllrbgj/kkK9UceBPpChGWJA7SYIb1Mqo.n5aNLq1/oRrC', 'Admin', 'Administrator',
        's2iwi2s@yahoo.com', 'wpidor', '2023-08-04 15:02:29.377000 +00:00', 'wpidor',
        '2023-08-04 15:02:36.433000 +00:00', true);

INSERT INTO sabong.users_authority (LOGIN, authority_name)
VALUES ('wpidor', 'ROLE_ADMIN');