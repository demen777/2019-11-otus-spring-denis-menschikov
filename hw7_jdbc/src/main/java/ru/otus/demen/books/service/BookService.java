package ru.otus.demen.books.service;

import ru.otus.demen.books.model.Book;

import java.util.Collection;

public interface BookService {
    Book addBook(String name, long authorId, String genreName) throws ServiceError;
    Collection<Book> findBooksBySurname(String surname) throws ServiceError;
}
