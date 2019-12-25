package ru.otus.demen.books.service;

import ru.otus.demen.books.model.Book;

import java.util.Collection;

public interface BookService {
    Book add(String name, long authorId, String genreName) throws ServiceError;
    Collection<Book> findBySurname(String surname) throws ServiceError;
    Book getById(long id) throws ServiceError;
}
