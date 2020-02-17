package ru.otus.demen.books.service;

import ru.otus.demen.books.model.Author;

import java.util.Collection;
import java.util.Optional;

public interface AuthorService {
    Optional<Author> findById(String id);
    Author getById(String id);
    Optional<Author> findByNameAndSurname(String firstName, String surname);
    Author add(String firstName, String surname);
    Collection<Author> getAll();
}
