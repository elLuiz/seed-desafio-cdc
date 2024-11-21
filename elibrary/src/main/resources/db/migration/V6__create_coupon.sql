CREATE SEQUENCE IF NOT EXISTS elibrary.seq_coupon_id START WITH 1;

CREATE TABLE IF NOT EXISTS elibrary.tb_coupon (
    id bigint PRIMARY KEY DEFAULT nextval('elibrary.seq_coupon_id'),
    code varchar(255) NOT NULL UNIQUE,
    discount integer NOT NULL CHECK(discount > 0 AND discount <= 100),
    expires_at TIMESTAMP WITH TIME ZONE NOT NULL
);