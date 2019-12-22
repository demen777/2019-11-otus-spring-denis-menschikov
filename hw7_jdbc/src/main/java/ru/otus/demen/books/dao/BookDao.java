package ru.otus.demen.books.dao;

import ru.otus.demen.books.model.Book;

public interface BookDao {
    Book save(Book book);
}
