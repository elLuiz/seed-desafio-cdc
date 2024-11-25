-- Authors
INSERT INTO elibrary.tb_author(author_name, email, description)
VALUES('GEORGE ORWELL', 'orwell@bigbrother.bb', 'Just a great author');
INSERT INTO elibrary.tb_author(author_name, email, description)
VALUES('Saw Newman', 'newman@ms.com', 'Microservices!');

-- Category
INSERT INTO elibrary.tb_category(category_name)
VALUES('FICTION');
INSERT INTO elibrary.tb_category(category_name)
VALUES('I.T');
INSERT INTO elibrary.tb_category(category_name)
VALUES('SOFTWARE ENGINEERING');
INSERT INTO elibrary.tb_category(category_name)
VALUES('POLITICS');

-- Books
INSERT INTO elibrary.tb_book(title, summary, table_of_contents, price, num_of_pages, isbn, publish_at, category_id, author_id)
VALUES('1984', 'A great book', 'Chapter 1 \n Chapter 2 \n Chapter N', 21.0, 150, '209-394030', current_timestamp - interval '2 days', (SELECT id FROM elibrary.tb_category WHERE category_name='POLITICS'), (SELECT id FROM elibrary.tb_author WHERE email='orwell@bigbrother.bb'));

INSERT INTO elibrary.tb_book(title, summary, table_of_contents, price, num_of_pages, isbn, publish_at, category_id, author_id)
VALUES('Microservices', 'Want to develop microservices?', 'Chapter 1 \n Chapter 2 \n Chapter N', 121.50, 850, '219-394030', current_timestamp - interval '3 days', (SELECT id FROM elibrary.tb_category WHERE category_name='SOFTWARE ENGINEERING'), (SELECT id FROM elibrary.tb_author WHERE email='newman@ms.com'));

INSERT INTO elibrary.tb_book(title, summary, table_of_contents, price, num_of_pages, isbn, publish_at, category_id, author_id)
VALUES('Building Microservices', 'Want to develop high-throughput applications?', 'Chapter 1 \n Chapter 2 \n Chapter N', 121.50, 850, '109-394030', current_timestamp + interval '3 days', (SELECT id FROM elibrary.tb_category WHERE category_name='SOFTWARE ENGINEERING'), (SELECT id FROM elibrary.tb_author WHERE email='newman@ms.com'));