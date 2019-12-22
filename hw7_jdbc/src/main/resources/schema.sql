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

drop table if exists books;
create table books (
    id identity primary key,
    name varchar(4000) not null,
    author_id bigint,
    genre_id bigint
);

alter table books
    add foreign key(author_id)
    references authors(id);

alter table books
    add foreign key(genre_id)
    references genres(id);
