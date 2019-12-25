package ru.otus.demen.books.dao;

import ru.otus.demen.books.model.Author;

import java.util.Collection;
import java.util.Optional;

public interface AuthorDao {
    Optional<Author> findById(long id);
    Author save(Author author);
    Collection<Author> getAll();
    Optional<Author> findByNameAndSurname(String firstName, String surname);
}
