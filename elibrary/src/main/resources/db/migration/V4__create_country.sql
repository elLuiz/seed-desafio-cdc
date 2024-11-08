CREATE SEQUENCE IF NOT EXISTS elibrary.seq_country_id START WITH 1;

CREATE TABLE IF NOT EXISTS elibrary.tb_country (
    id bigint PRIMARY KEY DEFAULT nextval('elibrary.seq_country_id'),
    name varchar(120) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS elibrary.tb_state (
    name varchar(120) NOT NULL,
    fk_country_id bigint NOT NULL,

    constraint fkCountry FOREIGN KEY(fk_country_id) REFERENCES elibrary.tb_country(id),
    constraint pkState PRIMARY KEY(name, fk_country_id)
);