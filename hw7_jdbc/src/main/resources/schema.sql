drop table if exists genres;
create table genres (
    id identity primary key,
    name varchar(255) unique not null
);

drop table if exists authors;
create table authors (
    id identity primary key,
    first_name varchar(255) not null,
    surname varchar(255) not null
);

alter table authors
add constraint authors_unique
unique (first_name, surname);

drop table if exists books;
create table books (
    id identity primary key,
    name varchar(4000) not null,
    author_id bigint not null,
    genre_id bigint not null
);

alter table books
    add foreign key(author_id)
    references authors(id);

alter table books
    add foreign key(genre_id)
    references genres(id);

alter table books
add constraint books_unique
unique (name, author_id);