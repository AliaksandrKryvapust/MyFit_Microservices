CREATE SCHEMA IF NOT EXISTS users
    AUTHORIZATION postgres;

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS users.users
(
    id        uuid,
    username  character varying(20)       NOT NULL,
    password  character varying(200)      NOT NULL,
    email     character varying(50)       NOT NULL,
    role      character varying(20)       NOT NULL,
    status    character varying(50)       NOT NULL,
    dt_update timestamp without time zone NOT NULL DEFAULT now(),
    dt_create timestamp without time zone NOT NULL DEFAULT now(),
    PRIMARY KEY (id)
);

ALTER TABLE IF EXISTS users.users
    OWNER to postgres;

ALTER TABLE IF EXISTS users.users
    ADD CONSTRAINT "users_UK" UNIQUE (email)
        INCLUDE (username);

CREATE TABLE IF NOT EXISTS users.token
(
    id        uuid,
    token     character varying(200)      NOT NULL,
    user_id   uuid                        NOT NULL REFERENCES users.users (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
    dt_create timestamp without time zone NOT NULL DEFAULT now(),
    dt_update timestamp without time zone NOT NULL DEFAULT now(),
    PRIMARY KEY (id)
);

ALTER TABLE IF EXISTS users.token
    OWNER to postgres;