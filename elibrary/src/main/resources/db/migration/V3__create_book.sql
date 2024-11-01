CREATE SEQUENCE IF NOT EXISTS elibrary.seq_book_id START WITH 1
    CACHE 100;

CREATE TABLE IF NOT EXISTS elibrary.tb_book (
    id bigint PRIMARY KEY DEFAULT nextval('elibrary.seq_book_id'),
    title varchar(200) NOT NULL,
    summary varchar(500) NOT NULL,
    table_of_contents text NOT NULL,
    price double precision NOT NULL,
    num_of_pages smallint NOT NULL,
    isbn varchar(50) NOT NULL,
    publish_at timestamp with time zone NOT NULL,
    category_id bigint NOT NULL,
    author_id bigint NOT NULL,

    constraint categoryFK FOREIGN KEY(category_id) REFERENCES elibrary.tb_category(id)
        ON UPDATE CASCADE,
    constraint authorFK FOREIGN KEY(author_id) REFERENCES elibrary.tb_author(id)
        ON UPDATE CASCADE,
    constraint isbnUK UNIQUE(isbn)
)