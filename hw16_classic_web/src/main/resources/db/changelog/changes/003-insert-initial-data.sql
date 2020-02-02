insert into authors(id, first_name, surname) values (1, 'Лев', 'Толстой');
insert into authors(id, first_name, surname) values (2, 'Федор', 'Достоевский');
insert into genres(id, name) values (1, 'Роман');
insert into genres(id, name) values (2, 'Повесть');
insert into books(id, name, author_id, genre_id) values (1, 'Война и мир', 1, 1);