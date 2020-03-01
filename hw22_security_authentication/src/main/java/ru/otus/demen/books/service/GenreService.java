package ru.otus.demen.books.service;

import ru.otus.demen.books.model.Genre;

import java.util.Collection;
import java.util.Optional;

public interface GenreService {
    Optional<Genre> findByName(String name);
    Genre getByName(String name);
    Genre add(String name);
    Collection<Genre> getAll();
}
