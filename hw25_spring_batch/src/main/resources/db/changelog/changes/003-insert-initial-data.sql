insert into authors(id, first_name, surname) values (1, 'Лев', 'Толстой');
insert into authors(id, first_name, surname) values (2, 'Федор', 'Достоевский');
insert into authors(id, first_name, surname) values (3, 'Антон', 'Чехов');
insert into authors(id, first_name, surname) values (4, 'Михаил', 'Лермонтов');

insert into genres(id, name) values (1, 'Роман');
insert into genres(id, name) values (2, 'Рассказ');
insert into genres(id, name) values (3, 'Драма');

insert into books(id, name, author_id, genre_id) values (1, 'Война и мир', 1, 1);
insert into books(id, name, author_id, genre_id) values (2, 'Анна Каренина', 1, 1);
insert into books(id, name, author_id, genre_id) values (3, 'Преступление и наказание', 2, 1);
insert into books(id, name, author_id, genre_id) values (4, 'Идиот', 2, 1);
insert into books(id, name, author_id, genre_id) values (5, 'Бедные люди', 2, 1);
insert into books(id, name, author_id, genre_id) values (6, 'Вишневый сад', 3, 3);
insert into books(id, name, author_id, genre_id) values (7, 'Дама с собачкой', 3, 2);



insert into book_comments(id, book_id, text) values (1, 1, 'Большая книга');
insert into book_comments(id, book_id, text) values (2, 1, 'Толстой рулит');
insert into book_comments(id, book_id, text) values (3, 7, 'Ни одна собачка не постарадала');