package ru.otus.demen.books.dao;

import ru.otus.demen.books.model.Genre;

import java.util.Optional;


public interface GenreDao {
    Optional<Genre> findByName(String name);
}
