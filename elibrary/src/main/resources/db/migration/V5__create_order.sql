CREATE SEQUENCE IF NOT EXISTS elibrary.seq_order_id START WITH 1
    INCREMENT BY 1
    CACHE 100;

CREATE TABLE IF NOT EXISTS elibrary.tb_order (
    id bigint PRIMARY KEY DEFAULT nextval('elibrary.seq_order_id'),
    email varchar(255) NOT NULL,
    customer_first_name varchar(255) NOT NULL,
    customer_last_name varchar(255) NOT NULL,
    document_value varchar(255) NOT NULL,
    type varchar(5),
    address varchar(255) NOT NULL,
    complement varchar(255) NOT NULL,
    city varchar(255) NOT NULL,
    zip_code varchar(255) NOT NULL,
    fk_country_id bigint NOT NULL,
    state varchar(255),
    phone_area_code smallint NOT NULL,
    phone_number varchar(255) NOT NULL,
    order_status varchar(20) NOT NULL DEFAULT 'PENDING',

    constraint fkCountryId FOREIGN KEY(fk_country_id)
        REFERENCES elibrary.tb_country(id),
    constraint state FOREIGN KEY(fk_country_id, state)
        REFERENCES elibrary.tb_state(fk_country_id, name)
);

CREATE TABLE IF NOT EXISTS elibrary.tb_order_item (
    fk_book_id bigint NOT NULL,
    quantity integer NOT NULL,
    price double precision NOT NULL,
    fk_order_id bigint NOT NULL,

    constraint fkBookId FOREIGN KEY(fk_book_id)
        REFERENCES elibrary.tb_book(id),
    constraint fkOrderId FOREIGN KEY(fk_order_id)
        REFERENCES elibrary.tb_order(id)
);

CREATE INDEX IF NOT EXISTS orderEmailIdx ON elibrary.tb_order(email);