package ru.otus.demen.books.dao;

import ru.otus.demen.books.model.Author;

import java.util.Optional;

public interface AuthorDao {
    Optional<Author> findById(long id);
}
