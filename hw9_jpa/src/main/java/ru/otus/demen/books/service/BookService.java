package ru.otus.demen.books.service;

import ru.otus.demen.books.model.Book;

import java.util.Collection;

public interface BookService {
    Book add(String name, long authorId, String genreName);
    Collection<Book> findBySurname(String surname);
    Book getById(long id);
}
