CREATE SCHEMA IF NOT EXISTS report
    AUTHORIZATION app;

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS report.report
(
    id           uuid,
    dt_create    timestamp without time zone NOT NULL DEFAULT now(),
    dt_update    timestamp without time zone NOT NULL DEFAULT now(),
    status       character varying(100)      NOT NULL,
    type         character varying(100)      NOT NULL,
    description  character varying(200)      NOT NULL,
    start        date                        NOT NULL,
    finish       date                        NOT NULL,
    user_id      uuid                        NOT NULL,
    username     character varying(200)      NOT NULL,
    role         character varying(100)      NOT NULL,
    file_type    character varying,
    content_type character varying,
    file_name    character varying,
    url          character varying,
    file_key     character varying,
    PRIMARY KEY (id)
);

ALTER TABLE IF EXISTS report.report
    OWNER to app;

