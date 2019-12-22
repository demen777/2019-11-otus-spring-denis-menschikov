package ru.otus.demen.books.dao;

import ru.otus.demen.books.model.Book;

import java.util.Collection;

public interface BookDao {
    Book save(Book book);
    Collection<Book> findBooksBySurname(String surname);
}
