package ru.otus.demen.books.dao;

import ru.otus.demen.books.model.Book;

import java.util.List;


public interface BookRepositoryCustom {
    List<Book> findByAuthorSurname(String surname);
}
