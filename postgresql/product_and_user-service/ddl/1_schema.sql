CREATE SCHEMA IF NOT EXISTS app
    AUTHORIZATION postgres;

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS app.products
(
    id            uuid,
    title         character varying(100)      NOT NULL,
    calories      bigint                      NOT NULL,
    proteins      double precision            NOT NULL,
    fats          double precision            NOT NULL,
    carbohydrates double precision            NOT NULL,
    weight        bigint                      NOT NULL,
    dt_create     timestamp without time zone NOT NULL DEFAULT now(),
    dt_update     timestamp without time zone NOT NULL DEFAULT now(),
    PRIMARY KEY (id)
);

ALTER TABLE IF EXISTS app.products
    OWNER to postgres;

CREATE TABLE IF NOT EXISTS app.meal
(
    id        uuid,
    title     character varying(100)      NOT NULL,
    dt_create timestamp without time zone NOT NULL DEFAULT now(),
    dt_update timestamp without time zone NOT NULL DEFAULT now(),
    PRIMARY KEY (id)
);

ALTER TABLE IF EXISTS app.meal
    OWNER to postgres;

CREATE TABLE IF NOT EXISTS app.ingredients
(
    id         uuid,
    meal_id    uuid REFERENCES app.meal (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
    product_id uuid REFERENCES app.products (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
    weight     bigint                      NOT NULL,
    dt_create  timestamp without time zone NOT NULL DEFAULT now(),
    dt_update  timestamp without time zone NOT NULL DEFAULT now(),
    PRIMARY KEY (id)
);

ALTER TABLE IF EXISTS app.ingredients
    OWNER to postgres;

CREATE TABLE IF NOT EXISTS app.records
(
    id         uuid,
    meal_id    uuid REFERENCES app.meal (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
    product_id uuid REFERENCES app.products (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
    weight     bigint                      NOT NULL,
    dt_create  timestamp without time zone NOT NULL DEFAULT now(),
    dt_supply  timestamp without time zone NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE IF EXISTS app.records
    OWNER to postgres;

CREATE TABLE IF NOT EXISTS app.users
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

ALTER TABLE IF EXISTS app.users
    OWNER to postgres;

ALTER TABLE IF EXISTS app.users
    ADD CONSTRAINT "users_UK" UNIQUE (email)
        INCLUDE (username);