CREATE SCHEMA IF NOT EXISTS audit
    AUTHORIZATION postgres;

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS audit.users
(
    id        uuid,
    username  character varying(20)       NOT NULL,
    email     character varying(50)       NOT NULL,
    role      character varying(20)       NOT NULL,
    status    character varying(50)       NOT NULL,
    dt_update timestamp without time zone NOT NULL DEFAULT now(),
    dt_create timestamp without time zone NOT NULL DEFAULT now(),
    PRIMARY KEY (id)
);

ALTER TABLE IF EXISTS audit.users
    OWNER to postgres;

ALTER TABLE IF EXISTS audit.users
    ADD CONSTRAINT "users_UK" UNIQUE (email)
        INCLUDE (username);

CREATE TABLE audit.audit
(
    id        uuid,
    uuid      uuid                        NOT NULL,
    user_id   uuid                        NOT NULL REFERENCES audit.users (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
    text      character varying(200)      NOT NULL,
    type      character varying(100)      NOT NULL,
    dt_create timestamp without time zone NOT NULL DEFAULT now(),
    PRIMARY KEY (id)
);

ALTER TABLE IF EXISTS audit.audit
    OWNER to postgres;