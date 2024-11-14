INSERT INTO elibrary.tb_country(name)
VALUES ('SWITZERLAND'), ('BOSNIA'), ('ECUADOR'), ('ARGENTINA'), ('DEUTSCHLAND');

INSERT INTO elibrary.tb_state(name, fk_country_id)
VALUES ('Baden-Württemberg', (SELECT id FROM elibrary.tb_country WHERE name='DEUTSCHLAND'));

INSERT INTO elibrary.tb_state(name, fk_country_id)
VALUES ('Bavaria', (SELECT id FROM elibrary.tb_country WHERE name='DEUTSCHLAND'));

INSERT INTO elibrary.tb_state(name, fk_country_id)
VALUES ('Berlin', (SELECT id FROM elibrary.tb_country WHERE name='DEUTSCHLAND'));

