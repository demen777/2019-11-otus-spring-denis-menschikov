package ru.otus.demen.books.dao;

import ru.otus.demen.books.model.Genre;

import java.util.Collection;
import java.util.Optional;


public interface GenreDao {
    Optional<Genre> findByName(String name);
    Genre save(Genre genre);
    Collection<Genre> getAll();
}
