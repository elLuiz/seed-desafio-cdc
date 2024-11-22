INSERT INTO elibrary.tb_coupon(code, discount, expires_at)
VALUES ('JUNIT25', 25, now() AT TIME ZONE 'UTC' + interval '2 days'), ('FIRSTORDER20', 20, now() AT TIME ZONE 'UTC' - interval '2 days');