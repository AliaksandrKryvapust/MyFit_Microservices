CREATE SCHEMA IF NOT EXISTS report
    AUTHORIZATION postgres;

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE report.report
(
    id uuid,
    dt_create timestamp without time zone NOT NULL DEFAULT now(),
    dt_update timestamp without time zone NOT NULL DEFAULT now(),
    status character varying(100) NOT NULL,
    type character varying(100) NOT NULL,
    description character varying(200) NOT NULL,
    "from" timestamp without time zone NOT NULL,
    "to" timestamp without time zone NOT NULL,
    username character varying(200) NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE IF EXISTS report.report
    OWNER to postgres;