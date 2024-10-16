CREATE SEQUENCE IF NOT EXISTS elibrary.seq_category_id
    START WITH 1
    CACHE 1000;

CREATE TABLE IF NOT EXISTS elibrary.tb_category (
    id BIGINT PRIMARY KEY NOT NULL DEFAULT nextval('elibrary.seq_category_id'),
    category_name VARCHAR(120) NOT NULL,

    constraint ukcategoryname UNIQUE(category_name)
);