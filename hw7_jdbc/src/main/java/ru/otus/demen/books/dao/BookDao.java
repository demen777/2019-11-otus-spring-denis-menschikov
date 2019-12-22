package ru.otus.demen.books.dao;

import ru.otus.demen.books.model.Book;

import java.util.Collection;
import java.util.Optional;

public interface BookDao {
    Book save(Book book);
    Collection<Book> findBySurname(String surname);
    Optional<Book> findById(long id);
}
