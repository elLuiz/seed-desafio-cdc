ALTER TABLE IF EXISTS elibrary.tb_order ADD COLUMN IF NOT EXISTS fk_coupon_id bigint;

ALTER TABLE IF EXISTS elibrary.tb_order ADD CONSTRAINT FKCouponId FOREIGN KEY (fk_coupon_id)
    REFERENCES elibrary.tb_coupon(id);