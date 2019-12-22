package ru.otus.demen.books.service;

import ru.otus.demen.books.model.Author;

import java.util.Optional;

public interface AuthorService {
    Optional<Author> findById(long id) throws ServiceError;
    Author getById(long id) throws ServiceError;
}
