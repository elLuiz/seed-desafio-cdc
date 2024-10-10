CREATE SEQUENCE IF NOT EXISTS elibrary.seq_author_id
    START WITH 1
    INCREMENT BY 1
    CACHE 1000;

CREATE TABLE IF NOT EXISTS elibrary.tb_author (
    id bigint PRIMARY KEY DEFAULT nextval('elibrary.seq_author_id'),
    email varchar(255) NOT NULL,
    description varchar(400) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT current_timestamp,

    CONSTRAINT ukemail UNIQUE(email)
);